/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui.parts.impl;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.parts.UIPart;
import im.leet.utils.client.ClientColors;
import java.util.function.Consumer;

public class ButtonPart
extends UIPart {
    private final String label;
    private final Consumer<ButtonPart> onClick;

    public ButtonPart(String label, Consumer<ButtonPart> onClick) {
        this.label = label;
        this.onClick = onClick;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        Client.RENDERER.drawModuleRect(this.x, this.y, this.width, this.height);
        float tw = Client.RENDERER.textWidth(this.label, TextureUse.SFMEDIUM, 8.0f);
        Client.RENDERER.text(this.label, this.x + (this.width - tw) / 2.0f, this.y + 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY) && button == 0) {
            if (this.onClick != null) {
                this.onClick.accept(this);
            }
            return true;
        }
        return false;
    }

    @Override
    public float getPreferredHeight(float availableWidth) {
        return 16.0f;
    }
}

