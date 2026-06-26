/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 */
package im.leet.api.render.msdf;

import im.leet.Client;
import im.leet.api.render.msdf.FontData;
import im.leet.api.render.msdf.MsdfFont;
import im.leet.api.render.system.ShaderUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.TextureAtlas;
import java.awt.Color;
import org.joml.Matrix4f;

public final class MsdfGlyph {
    private final int code;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    private float advance;
    private float topPosition;
    private float width;
    private float height;
    private boolean emojiPatch = false;

    public MsdfGlyph(FontData.GlyphData data, float atlasWidth, float atlasHeight) {
        this.code = data.unicode();
        this.advance = data.advance();
        FontData.BoundsData atlasBounds = data.atlasBounds();
        if (atlasBounds != null) {
            this.minU = atlasBounds.left() / atlasWidth;
            this.maxU = atlasBounds.right() / atlasWidth;
            this.minV = 1.0f - atlasBounds.top() / atlasHeight;
            this.maxV = 1.0f - atlasBounds.bottom() / atlasHeight;
        } else {
            this.maxV = 0.0f;
            this.minV = 0.0f;
            this.maxU = 0.0f;
            this.minU = 0.0f;
        }
        FontData.BoundsData planeBounds = data.planeBounds();
        if (planeBounds != null) {
            this.width = planeBounds.right() - planeBounds.left();
            this.height = planeBounds.top() - planeBounds.bottom();
            this.topPosition = planeBounds.top();
        } else {
            this.topPosition = 0.0f;
            this.height = 0.0f;
            this.width = 0.0f;
        }
    }

    public float apply(Matrix4f matrix, float size, float x, float y, float z, Color color1, Color color2, Color color3, Color color4, TextureUse textureUse, MsdfFont emoji) {
        float width = this.width * size;
        float height = this.height * size;
        TextureAtlas.UV uv = Client.RENDERER.getCrenderSystem().getAtlas().getUV(textureUse.name().toLowerCase());
        Client.RENDERER.getCrenderSystem().shader(ShaderUse.MSDF).texture(0).rect(x, y -= this.topPosition * size, width, height).uv(uv.u0 + this.minU * uv.u1, uv.v0 + this.minV * uv.v1, (this.maxU - this.minU) * uv.u1, (this.maxV - this.minV) * uv.v1).color(color1, color2, color3, color4).build();
        return this.advance * size;
    }

    public float getWidth(float size) {
        return this.advance * size;
    }

    public int getCharCode() {
        return this.code;
    }
}

