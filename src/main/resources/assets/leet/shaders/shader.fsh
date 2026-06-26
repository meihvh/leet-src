#version 150

float roundSDF(vec2 p, vec2 b, float r) {
    return length(max(abs(p) - b, 0.0)) - r;
}
float test(vec2 vec_1, vec2 vec_2, vec4 vec_4) {
    vec_4.xy = (vec_1.x > 0.0) ? vec_4.xy : vec_4.zw;
    vec_4.x = (vec_1.y > 0.0) ? vec_4.x : vec_4.y;
    vec2 coords = abs(vec_1) - vec_2 + vec_4.x;
    return min(max(coords.x, coords.y), 0.0) + length(max(coords, vec2(0.0f))) - vec_4.x;
}
float rdist(vec2 pos, vec2 size, vec4 radius) {
    radius.xy = (pos.x > 0.0) ? radius.xy : radius.wz;
    radius.x  = (pos.y > 0.0) ? radius.x : radius.y;

    vec2 v = abs(pos) - size + radius.x;
    return min(max(v.x, v.y), 0.0) + length(max(v, 0.0)) - radius.x;
}

float ralpha(vec2 size, vec2 coord, vec4 radius, float smoothness) {
    vec2 center = size * 0.5;
    float dist = rdist(center - (coord * size), center - 1.0, radius);
    return 1.0 - smoothstep(1.0 - smoothness, 1.0, dist);
}
float median(vec3 color) {
    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));
}
vec3 sampleLinearMSDF(sampler2D tex, vec2 uv) {
    vec2 texSize = vec2(textureSize(tex, 0));
    vec2 pixel = uv * texSize;

    vec2 base = floor(pixel - 0.5) + 0.5;
    vec2 f = fract(pixel - 0.5);

    vec3 c00 = texture(tex, (base + vec2(0.0, 0.0)) / texSize).rgb;
    vec3 c10 = texture(tex, (base + vec2(1.0, 0.0)) / texSize).rgb;
    vec3 c01 = texture(tex, (base + vec2(0.0, 1.0)) / texSize).rgb;
    vec3 c11 = texture(tex, (base + vec2(1.0, 1.0)) / texSize).rgb;

    vec3 cx0 = mix(c00, c10, f.x);
    vec3 cx1 = mix(c01, c11, f.x);

    return mix(cx0, cx1, f.y);
}
float signedDistanceField(vec2 p, vec2 b, vec4 r) {
    r.xy = (p.x > 0.0) ? r.xy : r.zw;
    r.x = (p.y > 0.0) ? r.x : r.y;

    vec2 q = abs(p) - b + r.x;

    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r.x;
}
vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}



uniform sampler2D tex;

uniform vec2 iResolution;

in vec3 pos;
in vec2 texCoords;
in vec4 vertexColor;
in vec2 size;
in vec4 u_round;
in float u_shaderId;
in vec2 smoothness;
in float thickness;
in float texid;
in float msdfRange;
in float blurRadius;
in vec4 scissor;
in vec2 fragCoord;
in vec2 uv;
in float hatch;

out vec4 outColor;

const float blurAmount = 1.0;
const float edgeWarpIntensity = 1.0;
const vec3 tintColor = vec3(0.0, 0.0, 0.0);
const float DPI = 6.28318530718;
const float STEP = DPI / 16.0;

void main() {
    if (gl_FragCoord.x < scissor.x || gl_FragCoord.x > scissor.x + scissor.z || gl_FragCoord.y < scissor.y || gl_FragCoord.y > scissor.y + scissor.w) {
        discard;
    }
    int shaderId = int(round(u_shaderId));


    if (shaderId == 0) { // RECTANGLE
        float alpha = ralpha(size, texCoords, u_round, smoothness.x);
        outColor = vec4(vertexColor.rgb, vertexColor.a * alpha);

    } else if (shaderId == 1) { // OUTLINE
        vec2 center = size * 0.5;
        float dist = rdist(center - (texCoords.xy * size), center - 1.0, u_round);
        float alpha = smoothstep(1.0 - thickness - smoothness.x - smoothness.y,
        1.0 - thickness - smoothness.y, dist);
        alpha *= 1.0 - smoothstep(1.0 - smoothness.y, 1.0, dist);
        outColor = vec4(vertexColor.rgb, vertexColor.a * alpha);

    } else if (shaderId == 2) { // BLUR
        vec2 resolution = textureSize(tex, 0);
        vec2 multiplier = 10 / resolution;

        float dist = distance(uv, vec2(0.5));

        vec2 refractedCoord = texCoords + vec2(pow(dist, 2) * 0.06 + uv.x / resolution.x - (1.0 - uv.x) / resolution.x, 0.0);

        vec2 rectHalf = size * 0.5;

        vec3 blurredColor = mix(texture(tex, refractedCoord).rgb, vertexColor.rgb, vertexColor.a);
        vec4 average = vec4(blurredColor, 1.0);

        vec4 color = vec4(average, 1.0) * vertexColor;
        color.a *= ralpha(size, uv, u_round, smoothness.x);

        if (color.a == 0.0) { // alpha test
            discard;
        }
        outColor = color;
    } else if (shaderId == 3) { // TEXTURE ( я не ебу почему но оно ломается )
        vec4 t = texture(tex, texCoords);
        outColor = vec4(vertexColor.rgb * t.rgb, t.a * vertexColor.a);
    } else if (shaderId == 4) { // MSDF
        vec3 msdfSample = sampleLinearMSDF(tex, texCoords);
        float dist = median(msdfSample) - 0.48;

        vec2 h = vec2(dFdx(texCoords.x), dFdy(texCoords.y)) * textureSize(tex, 0);
        float pixels = msdfRange * inversesqrt(h.x * h.x + h.y * h.y);

        float alpha = smoothstep(-smoothness.x, smoothness.x, dist * pixels);
        vec4 color = vec4(vertexColor.rgb, vertexColor.a * alpha);

        outColor = color;
    } else if (shaderId == 5) { // GLOW
        vec2 center = vec2(0.5);
        float dist = clamp(0.5 - distance(center, texCoords), 0, 1);
        outColor = vec4(vertexColor.rgb, vertexColor.a * pow(dist, 2) * smoothness.x);
    } else if (shaderId == 6) { // HUE
        float alpha = ralpha(size, texCoords, u_round, smoothness.x);
        outColor = vec4(hsv2rgb(vec3(texCoords.x, 1.0, 1.0)), alpha * vertexColor.a);
    } else if (shaderId == 7) { // COLOR_PICKER (да да, ето костыль ну а хуле мне делать коли тут ограничение 16 атрибутов☠️)
        vec4 cl = vec4(vertexColor.rgb, 1.0);
        vec4 c = mix(mix(vec4(1.0), cl, uv.x), vec4(0.0), uv.y);
        float alpha = ralpha(size, texCoords, u_round, smoothness.x);
        outColor = vec4(c.rgb, vertexColor.a * alpha);
    }

    // hatching
    if (hatch > 0.0) {
        float x = mod(texCoords.x * (size.x / size.y) - 0.05, hatch);
        if (x < 0.05) x = 0.0;
        else x = 1.0;

        float y = mod(texCoords.y - 0.05, hatch);
        if (y < 0.05) y = 0.0;
        else y = 1.0;
        outColor.a *= x * y;
    }
}