/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui.parts.impl;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.parts.UIPart;
import im.leet.utils.client.ClientColors;

public class TextPart
extends UIPart {
    private final String text;
    private final int size;

    public TextPart(String text) {
        this(text, 8);
    }

    public TextPart(String text, int size) {
        this.text = text;
        this.size = size;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        Client.RENDERER.text(this.text, this.x, this.y, TextureUse.SFMEDIUM, (float)this.size, ClientColors.FORE_COLOR);
    }

    @Override
    public float getPreferredHeight(float availableWidth) {
        return this.size + 4;
    }
}

