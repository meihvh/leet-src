/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.render.msdf;

import im.leet.api.render.msdf.MsdfFont;
import im.leet.api.render.system.TextureUse;
import java.util.HashMap;
import java.util.Map;

public class Fonts {
    private final Map<String, MsdfFont> fonts = new HashMap<String, MsdfFont>();

    public MsdfFont get(TextureUse textureUse) {
        return this.fonts.computeIfAbsent(textureUse.name(), name -> MsdfFont.builder().atlas(name.toLowerCase()).data(name.toLowerCase()).build(textureUse));
    }

    public void clearFonts() {
        this.fonts.clear();
    }

    public Map<String, MsdfFont> getFonts() {
        return this.fonts;
    }
}

