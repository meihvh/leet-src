/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui.parts.impl;

import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.parts.UIPart;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientSettings;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UICheckbox
extends UIPart {
    private final Supplier<Boolean> get;
    private final Consumer<Boolean> set;
    private final SmoothStepAnimation checkAnimation = new SmoothStepAnimation(300, 1.0);

    public UICheckbox(Supplier<Boolean> get, Consumer<Boolean> set) {
        this.get = get;
        this.set = set;
        this.checkAnimation.setDirection(get.get() != false ? Direction.BACKWARDS : Direction.FORWARDS);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = 15.0f;
        this.checkAnimation.setDirection(this.get.get() != false ? Direction.BACKWARDS : Direction.FORWARDS);
        float anim = 1.0f - this.checkAnimation.getOutput();
        Client.RENDERER.drawModuleRect(this.x, this.y, 12.0f, 12.0f);
        float prevAlpha = Client.RENDERER.getCrenderSystem().alpha();
        Client.RENDERER.getCrenderSystem().alpha(anim);
        Color themeColor = ClientSettings.INSTANCE.getColor(0);
        Client.RENDERER.text(IconUse.CHECK, this.x, this.y, TextureUse.ICONS, 8.0f, themeColor);
        Client.RENDERER.getCrenderSystem().alpha(prevAlpha);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY) && button == 0) {
            this.set.accept(this.get.get() == false);
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }
}

