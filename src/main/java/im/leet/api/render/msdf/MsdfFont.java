/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.textures.GpuTextureView
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.texture.AbstractTexture
 *  net.minecraft.client.texture.GlTextureView
 *  net.minecraft.util.Identifier
 *  org.joml.Matrix4f
 *  org.joml.Vector4f
 */
package im.leet.api.render.msdf;

import com.mojang.blaze3d.textures.GpuTextureView;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.render.msdf.FontData;
import im.leet.api.render.msdf.MsdfGlyph;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.render.system.sys2d.TextureAtlas;
import im.leet.api.render.util.ResourceProvider;
import im.leet.utils.client.ServerPrefix;
import im.leet.utils.client.TextUtility;
import im.leet.utils.font.EmojiUtility;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.GlTextureView;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import ru.aloweeed.perfect.Ignored;

@Ignored
public final class MsdfFont
implements MinecraftHolder {
    private final String name;
    private final AbstractTexture texture;
    private final FontData.AtlasData atlas;
    private final FontData.MetricsData metrics;
    public final Map<Integer, MsdfGlyph> glyphs;
    public final Map<Integer, Map<Integer, Float>> kernings;
    private final TextureUse textureUse;

    private MsdfFont(String name, AbstractTexture texture, FontData.AtlasData atlas, FontData.MetricsData metrics, Map<Integer, MsdfGlyph> glyphs, Map<Integer, Map<Integer, Float>> kernings, TextureUse textureUse) {
        this.name = name;
        this.texture = texture;
        this.atlas = atlas;
        this.metrics = metrics;
        this.glyphs = glyphs;
        this.kernings = kernings;
        this.textureUse = textureUse;
    }

    public GpuTextureView getTextureId() {
        return this.texture.method_71659();
    }

    public void applyGlyphs(Matrix4f matrix, String text, float size, float thickness, float spacing, float x, float y, float z, Color color1, Color color2, Color color3, Color color4, TextureUse tex) {
        this.applyGlyphs(matrix, text, size, thickness, spacing, x, y, z, color1, color2, color3, color4, tex, 1.0f);
    }

    public void applyGlyphs(Matrix4f matrix, String text, float size, float thickness, float spacing, float x, float y, float z, Color color, TextureUse tex, float smooth) {
        this.applyGlyphs(matrix, text, size, thickness, spacing, x, y, z, color, color, color, color, tex, smooth);
    }

    public void applyGlyphs(Matrix4f matrix, String text, float size, float thickness, float spacing, float x, float y, float z, Color color1, Color color2, Color color3, Color color4, TextureUse tex, float smooth) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        system.msdfRange(25.0f).smoothness(smooth, smooth);
        float prevAlpha = system.alpha();
        float width = this.getWidth(text, size);
        float factor = 20.0f;
        float lastAlpha = -1.0f;
        int prevChar = -1;
        text = TextUtility.smallToNormal(text);
        MsdfFont emoji = Client.FONTS.get(TextureUse.EMOJIS);
        for (int i = 0; i < text.length(); ++i) {
            boolean isEmoji;
            MsdfGlyph glyph;
            char ch = text.charAt(i);
            int[] rw = ServerPrefix.rwFrom(String.valueOf(ch));
            if (rw[0] != -1 && rw[1] != -1) {
                Identifier id = Identifier.method_60655((String)"leet", (String)"images/ui/rw_prefix.png");
                TextureAtlas.UV uv = Client.RENDERER.getCrenderSystem().getAtlas().getOrCreate(id.toString(), id);
                if (uv == null) continue;
                TextureAtlas atlas = Client.RENDERER.getCrenderSystem().getAtlas();
                float width_rw = uv.width / 4.0f;
                float height_rw = uv.height / 16.0f;
                float aspect = width_rw / height_rw;
                float size_rw = size + 1.0f;
                Client.RENDERER.textureRaw(id, x, y - size_rw / 2.0f - 2.0f, size_rw * aspect, size_rw, 0.0f, new Vector4f(0.0f), new Vector4f(width_rw * (float)rw[0] / (float)atlas.atlasWidth, height_rw * (float)rw[1] / (float)atlas.atlasHeight, width_rw / (float)atlas.atlasWidth, height_rw / (float)atlas.atlasHeight), Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
                continue;
            }
            int _char = TextUtility.charToNormal(ch);
            if (Character.isHighSurrogate(text.charAt(i)) && i + 1 < text.length()) {
                char low = text.charAt(i + 1);
                _char = Character.toCodePoint(ch, low);
            }
            MsdfGlyph msdfGlyph = glyph = (isEmoji = EmojiUtility.isEmoji(_char)) ? emoji.glyphs.get(_char) : this.glyphs.get(_char);
            if (glyph == null) continue;
            Map<Integer, Float> kerning = this.kernings.get(prevChar);
            if (kerning != null) {
                x += kerning.getOrDefault(_char, Float.valueOf(0.0f)).floatValue() * size;
            }
            float alpha = 1.0f;
            x += glyph.apply(matrix, size, x, y, z, color1, color2, color3, color4, isEmoji ? TextureUse.EMOJIS : tex, emoji) + thickness + spacing;
            prevChar = _char;
            lastAlpha = alpha;
        }
        system.alpha(prevAlpha);
    }

    public float getHeight(float size) {
        return this.metrics.lineHeight() * size;
    }

    public float getWidth(String text, float size) {
        MsdfFont emoji = Client.FONTS.get(TextureUse.EMOJIS);
        text = TextUtility.smallToNormal(text);
        int prevChar = -1;
        float width = 0.0f;
        for (int i = 0; i < text.length(); ++i) {
            boolean isEmoji;
            MsdfGlyph glyph;
            int ch = text.charAt(i);
            int[] rw = ServerPrefix.rwFrom(String.valueOf((char)ch));
            if (rw[0] != -1 && rw[1] != -1) {
                Identifier id = Identifier.method_60655((String)"leet", (String)"images/ui/rw_prefix.png");
                TextureAtlas.UV uv = Client.RENDERER.getCrenderSystem().getAtlas().getOrCreate(id.toString(), id);
                if (uv == null) continue;
                float aspect = uv.width / uv.height;
                float scale = 1.0f / size;
                width += size * 4.5f;
                continue;
            }
            int _char = ch;
            if (Character.isHighSurrogate(text.charAt(i)) && i + 1 < text.length()) {
                char low = text.charAt(i + 1);
                _char = Character.toCodePoint((char)ch, low);
            }
            MsdfGlyph msdfGlyph = glyph = (isEmoji = EmojiUtility.isEmoji(_char)) ? emoji.glyphs.get(_char) : this.glyphs.get(_char);
            if (glyph == null) continue;
            Map<Integer, Float> kerning = this.kernings.get(prevChar);
            if (kerning != null) {
                width += kerning.getOrDefault(_char, Float.valueOf(0.0f)).floatValue() * size;
            }
            width += glyph.getWidth(size) + 0.3f;
            prevChar = _char;
        }
        return width;
    }

    public String getName() {
        return this.name;
    }

    public FontData.AtlasData getAtlas() {
        return this.atlas;
    }

    public FontData.MetricsData getMetrics() {
        return this.metrics;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name = "?";
        private Identifier dataIdentifer;
        private Identifier atlasIdentifier;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder data(String dataFileName) {
            this.dataIdentifer = Identifier.method_60655((String)"leet", (String)("fonts/" + dataFileName + ".json"));
            return this;
        }

        public Builder atlas(String atlasFileName) {
            this.atlasIdentifier = Identifier.method_60655((String)"leet", (String)("fonts/" + atlasFileName + ".png"));
            return this;
        }

        public MsdfFont build(TextureUse textureUse) {
            FontData data = ResourceProvider.fromJsonToInstance(this.dataIdentifer, FontData.class);
            AbstractTexture texture = MinecraftClient.method_1551().method_1531().method_4619(this.atlasIdentifier);
            System.out.println(this.dataIdentifer.toString() + " " + this.atlasIdentifier.toString());
            if (data == null) {
                throw new RuntimeException("Failed to read font data file: " + this.dataIdentifer.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax.");
            }
            texture.method_4527(false, true);
            float aWidth = data.atlas().width();
            float aHeight = data.atlas().height();
            Map<Integer, MsdfGlyph> glyphs = data.glyphs().stream().collect(Collectors.toMap(glyphData -> glyphData.unicode(), glyphData -> new MsdfGlyph((FontData.GlyphData)glyphData, aWidth, aHeight)));
            HashMap<Integer, Map<Integer, Float>> kernings = new HashMap<Integer, Map<Integer, Float>>();
            data.kernings().forEach(kerning -> {
                HashMap<Integer, Float> map = (HashMap<Integer, Float>)kernings.get(kerning.leftChar());
                if (map == null) {
                    map = new HashMap<Integer, Float>();
                    kernings.put(kerning.leftChar(), map);
                }
                map.put(kerning.rightChar(), Float.valueOf(kerning.advance()));
            });
            Client.RENDERER.getCrenderSystem().putTex(textureUse, ((GlTextureView)texture.method_71659()).method_71638().method_68427());
            return new MsdfFont(this.name, texture, data.atlas(), data.metrics(), glyphs, kernings, textureUse);
        }
    }
}

