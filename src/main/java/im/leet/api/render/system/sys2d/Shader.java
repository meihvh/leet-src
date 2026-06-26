/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.opengl.GlStateManager
 *  org.joml.Matrix4f
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 */
package im.leet.api.render.system.sys2d;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Shader {
    private final int id;

    public Shader() {
        try {
            this.id = GL30.glCreateProgram();
            int vertex = this.create(35633, " #version 330 core\n\n layout (location = 0) in vec3 position;\n layout (location = 1) in vec2 VtexCoords;\n layout (location = 2) in vec4 Vcolor;\n layout (location = 3) in vec2 Vsize;\n layout (location = 4) in vec4 Vround;\n layout (location = 5) in float Vshader;\n layout (location = 6) in vec2 Vsmooth;\n layout (location = 7) in float Vthickness;\n layout (location = 8) in float VmsdfRange;\n layout (location = 9) in float VblurRadius;\n layout (location = 10) in vec4 Vscissor;\n layout (location = 11) in vec2 VfragCoord;\n layout (location = 12) in vec2 Vuv;\n layout (location = 13) in float Vhatch;\n layout (location = 14) in float VuseCircle;\n\n uniform mat4 ProjMat, ModelViewMat;\n\n out vec3 pos;\n out vec2 texCoords;\n out vec4 vertexColor;\n out vec2 size;\n out vec4 u_round;\n out float u_shaderId;\n out vec2 smoothness;\n out float thickness;\n out float texid;\n out float msdfRange;\n out float blurRadius;\n out vec4 scissor;\n out vec2 fragCoord;\n out vec2 uv;\n out float hatch;\n out float useCircle;\n\n void main() {\n     gl_Position = ProjMat * ModelViewMat * vec4(position, 1.0);\n     pos = position;\n     texCoords = VtexCoords;\n     vertexColor = Vcolor;\n     size = Vsize;\n     u_round = Vround;\n     u_shaderId = Vshader;\n     smoothness = Vsmooth;\n     thickness = Vthickness;\n     texid = 0;\n     msdfRange = VmsdfRange;\n     blurRadius = VblurRadius;\n     scissor = Vscissor;\n     fragCoord = VfragCoord;\n     uv = Vuv;\n     hatch = Vhatch;\n     useCircle = VuseCircle;\n }\n\n");
            int fragment = this.create(35632, "#version 150\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    return length(max(abs(p) - b, 0.0)) - r;\n}\nfloat test(vec2 vec_1, vec2 vec_2, vec4 vec_4) {\n    vec_4.xy = (vec_1.x > 0.0) ? vec_4.xy : vec_4.zw;\n    vec_4.x = (vec_1.y > 0.0) ? vec_4.x : vec_4.y;\n    vec2 coords = abs(vec_1) - vec_2 + vec_4.x;\n    return min(max(coords.x, coords.y), 0.0) + length(max(coords, vec2(0.0f))) - vec_4.x;\n}\nfloat rdist(vec2 pos, vec2 size, vec4 radius) {\n    radius.xy = (pos.x > 0.0) ? radius.xy : radius.wz;\n    radius.x  = (pos.y > 0.0) ? radius.x : radius.y;\n\n    vec2 v = abs(pos) - size + radius.x;\n    return min(max(v.x, v.y), 0.0) + length(max(v, 0.0)) - radius.x;\n}\n\nfloat ralpha(vec2 size, vec2 coord, vec4 radius, float smoothness) {\n    vec2 center = size * 0.5;\n    float dist = rdist(center - (coord * size), center - 1.0, radius);\n    return 1.0 - smoothstep(1.0 - smoothness, 1.0, dist);\n}\nfloat median(vec3 color) {\n    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));\n}\nvec3 sampleLinearMSDF(sampler2D tex, vec2 uv) {\n    vec2 texSize = vec2(textureSize(tex, 0));\n    vec2 pixel = uv * texSize;\n\n    vec2 base = floor(pixel - 0.5) + 0.5;\n    vec2 f = fract(pixel - 0.5);\n\n    vec3 c00 = texture(tex, (base + vec2(0.0, 0.0)) / texSize).rgb;\n    vec3 c10 = texture(tex, (base + vec2(1.0, 0.0)) / texSize).rgb;\n    vec3 c01 = texture(tex, (base + vec2(0.0, 1.0)) / texSize).rgb;\n    vec3 c11 = texture(tex, (base + vec2(1.0, 1.0)) / texSize).rgb;\n\n    vec3 cx0 = mix(c00, c10, f.x);\n    vec3 cx1 = mix(c01, c11, f.x);\n\n    return mix(cx0, cx1, f.y);\n}\nvec3 hsv2rgb(vec3 c)\n{\n    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\n\n\n\nuniform sampler2D tex;\n\nuniform vec2 iResolution;\n\nin vec3 pos;\nin vec2 texCoords;\nin vec4 vertexColor;\nin vec2 size;\nin vec4 u_round;\nin float u_shaderId;\nin vec2 smoothness;\nin float thickness;\nin float texid;\nin float msdfRange;\nin float blurRadius;\nin vec4 scissor;\nin vec2 fragCoord;\nin vec2 uv;\nin float hatch;\nin float useCircle;\n\nout vec4 outColor;\n\nconst float blurAmount = 1.0;\nconst float edgeWarpIntensity = 1.0;\nconst vec3 tintColor = vec3(0.0, 0.0, 0.0);\nconst float DPI = 6.28318530718;\nconst float STEP = DPI / 16.0;\n\nvoid main() {\n    // $$$igma circles\n    if (useCircle != 0.0) {\n        float d1 = distance(texCoords, vec2(-0.3 + useCircle, 0.2)) - useCircle;\n        float d2 = distance(texCoords, vec2(1.2, 0.9)) - useCircle;\n        if (d1 < 0.1 || d2 < 0.05)\n          discard;\n    }\n    if (gl_FragCoord.x < scissor.x || gl_FragCoord.x > scissor.x + scissor.z || gl_FragCoord.y < scissor.y || gl_FragCoord.y > scissor.y + scissor.w) {\n        discard;\n    }\n    int shaderId = int(round(u_shaderId));\n\n\n    if (shaderId == 0) { // RECTANGLE\n        float alpha = ralpha(size, texCoords, u_round, smoothness.x);\n        outColor = vec4(vertexColor.rgb, vertexColor.a * alpha);\n\n    } else if (shaderId == 1) { // OUTLINE\n        vec2 center = size * 0.5;\n        float dist = rdist(center - (texCoords.xy * size), center - 1.0, u_round);\n        float alpha = smoothstep(1.0 - thickness - smoothness.x - smoothness.y,\n        1.0 - thickness - smoothness.y, dist);\n        alpha *= 1.0 - smoothstep(1.0 - smoothness.y, 1.0, dist);\n        outColor = vec4(vertexColor.rgb, vertexColor.a * alpha);\n\n    } else if (shaderId == 2) { // BLUR\n        vec2 resolution = textureSize(tex, 0);\n        vec2 multiplier = 10 / resolution;\n\n        float f = resolution.x / resolution.y;\n\n        float dist = distance(uv, vec2(0.5));\n\n        vec2 refractedCoord = texCoords/* + vec2((uv.x - (dist * 0.5) - 0.5) / (size.x * dist)), 0.0)*/;\n\n        vec3 average = texture(tex, refractedCoord).rgb;\n        for (float d = 0.0; d < 32; d += 1) {\n            for (float i = 0.2; i <= 1.0; i += 0.2) {\n                average += texture(tex, refractedCoord + vec2(cos(d), sin(d)) * multiplier * i).rgb;\n            }\n        }\n        average /= 160.0;\n\n        vec4 color = vec4(average, 1.0) * vertexColor;\n        color.a *= ralpha(size, uv, u_round, smoothness.x);\n\n        if (color.a == 0.0) { // alpha test\n            discard;\n        }\n        outColor = color;\n    } else if (shaderId == 3) { // TEXTURE ( \u044f \u043d\u0435 \u0435\u0431\u0443 \u043f\u043e\u0447\u0435\u043c\u0443 \u043d\u043e \u043e\u043d\u043e \u043b\u043e\u043c\u0430\u0435\u0442\u0441\u044f )\n        vec4 t = texture(tex, texCoords);\n        t.a *= ralpha(size, uv, u_round, smoothness.x);\n        if (t.a == 0.0) discard;\n        outColor = vec4(vertexColor.rgb * t.rgb, t.a * vertexColor.a);\n    } else if (shaderId == 4) { // MSDF\n        vec3 msdfSample = sampleLinearMSDF(tex, texCoords);\n        float dist = median(msdfSample) - 0.48;\n\n        vec2 h = vec2(dFdx(texCoords.x), dFdy(texCoords.y)) * textureSize(tex, 0);\n        float pixels = msdfRange * inversesqrt(h.x * h.x + h.y * h.y);\n\n        float alpha = smoothstep(-smoothness.x, smoothness.x, dist * pixels);\n        vec4 color = vec4(vertexColor.rgb, vertexColor.a * alpha);\n\n        outColor = color;\n    } else if (shaderId == 5) { // GLOW\n        vec2 center = vec2(0.5);\n        float dist = clamp(0.5 - distance(center, texCoords), 0, 1);\n        outColor = vec4(vertexColor.rgb, vertexColor.a * pow(dist, 2) * smoothness.x);\n    } else if (shaderId == 6) { // HUE\n        float alpha = ralpha(size, texCoords, u_round, smoothness.x);\n        outColor = vec4(hsv2rgb(vec3(texCoords.x, 1.0, 1.0)), alpha * vertexColor.a);\n    } else if (shaderId == 7) { // COLOR_PICKER (\u0434\u0430 \u0434\u0430, \u0435\u0442\u043e \u043a\u043e\u0441\u0442\u044b\u043b\u044c \u043d\u0443 \u0430 \u0445\u0443\u043b\u0435 \u043c\u043d\u0435 \u0434\u0435\u043b\u0430\u0442\u044c \u043a\u043e\u043b\u0438 \u0442\u0443\u0442 \u043e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u0435 16 \u0430\u0442\u0440\u0438\u0431\u0443\u0442\u043e\u0432\u2620\ufe0f)\n        vec4 cl = vec4(vertexColor.rgb, 1.0);\n        vec4 c = mix(mix(vec4(1.0), cl, uv.x), vec4(0.0), uv.y);\n        float alpha = ralpha(size, texCoords, u_round, smoothness.x);\n        outColor = vec4(c.rgb, vertexColor.a * alpha);\n    }\n\n    // hatching\n    if (hatch > 0.0) {\n        float x = mod(texCoords.x * (size.x / size.y) - 0.05, hatch);\n        if (x < 0.05) x = 0.0;\n        else x = 1.0;\n\n        float y = mod(texCoords.y - 0.05, hatch);\n        if (y < 0.05) y = 0.0;\n        else y = 1.0;\n        outColor.a *= x * y;\n    }\n}\n\n");
            GL30.glAttachShader((int)this.id, (int)vertex);
            GL30.glAttachShader((int)this.id, (int)fragment);
            GL20.glLinkProgram((int)this.id);
            int linked = GL20.glGetProgrami((int)this.id, (int)35714);
            if (linked == 0) {
                String log = GL20.glGetProgramInfoLog((int)this.id);
                throw new RuntimeException("Shader link failed:\n" + log);
            }
            GL30.glDeleteShader((int)vertex);
            GL30.glDeleteShader((int)fragment);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int create(int type, String source) {
        int shaderId = GL30.glCreateShader((int)type);
        GL30.glShaderSource((int)shaderId, (CharSequence)source);
        GL20.glCompileShader((int)shaderId);
        int compiled = GL20.glGetShaderi((int)shaderId, (int)35713);
        if (compiled == 0) {
            String log = GL20.glGetShaderInfoLog((int)shaderId);
            throw new RuntimeException("Shader compile failed:\n" + log + "\nSource:\n" + source);
        }
        return shaderId;
    }

    public void bind() {
        GlStateManager._glUseProgram((int)this.id);
    }

    public void uploadMatrix(Matrix4f matrix4f, Matrix4f matrix4f2) {
        float[] proj = new float[16];
        matrix4f.get(proj);
        GL30.glUniformMatrix4fv((int)GL20.glGetUniformLocation((int)this.id, (CharSequence)"ProjMat"), (boolean)false, (float[])proj);
        float[] modelView = new float[16];
        matrix4f2.get(modelView);
        GL30.glUniformMatrix4fv((int)GL20.glGetUniformLocation((int)this.id, (CharSequence)"ModelViewMat"), (boolean)false, (float[])modelView);
    }

    public void setUniform1i(String name, int value) {
        GL30.glUniform1i((int)GL30.glGetUniformLocation((int)this.id, (CharSequence)name), (int)value);
    }

    public int getId() {
        return this.id;
    }
}

