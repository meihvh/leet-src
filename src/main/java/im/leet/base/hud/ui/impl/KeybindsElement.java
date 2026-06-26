/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.modules.Module;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.screen.ChatScreen;

public class KeybindsElement
extends HudElement {
    float widthAnim = -999.0f;

    public KeybindsElement(Drag drag) {
        super("Keybinds", drag);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float anim = Math.abs(1.0f - this.animation.getOutput());
        float size = 0.0f;
        if (this.widthAnim == -999.0f) {
            this.widthAnim = this.width;
        }
        ArrayList<Module> modules = new ArrayList<Module>();
        for (Module module : Client.MODULES.getModules()) {
            module.keybindAnimation.setDirection(module.isEnabled() && module.getKey() != -1 ? Direction.BACKWARDS : Direction.FORWARDS);
            size += 1.0f - module.keybindAnimation.getOutput();
            modules.add(module);
        }
        this.animation.setDirection(size == 0.0f && !(KeybindsElement.mc.field_1755 instanceof ChatScreen) ? Direction.FORWARDS : Direction.BACKWARDS);
        Client.RENDERER.getCrenderSystem().alpha(anim);
        float offset = 0.0f;
        float maxWidth = 0.0f;
        this.widthAnim = MathUtility.linearFps(this.widthAnim, this.width, 10.0f);
        if (Float.isNaN(this.widthAnim) || MathUtility.delta(this.widthAnim, this.width) > 50.0f) {
            this.widthAnim = this.width;
        }
        Client.RENDERER.getCrenderSystem().push(this.x, this.y + 0.5f, this.widthAnim, this.height);
        this.drawBase(IconUse.KEYBOARD, "Hotkeys", 0.0f, 0.0f);
        for (Module module : modules) {
            float kOrigin = module.keybindAnimation.getOutput();
            float kAnim = 1.0f - kOrigin;
            offset += 16.0f * kAnim;
            if ((double)kAnim < 0.1) continue;
            Color color = ColorUtility.injectAlpha(ClientColors.FORE_COLOR, kAnim * 255.0f);
            Client.RENDERER.text(module.getKeyText(), this.x + this.widthAnim - (Client.RENDERER.textWidth(module.getKeyText(), TextureUse.SFMEDIUM, 8.0f) + 6.0f) * kAnim, this.y + 6.0f + offset, TextureUse.SFMEDIUM, 8.0f, color);
            Client.RENDERER.text(module.getName(), this.x + 4.0f - 20.0f * kOrigin, this.y + 6.0f + offset, TextureUse.SFMEDIUM, 8.0f, color);
            maxWidth = Math.max(maxWidth, Client.RENDERER.textWidth(module.getName(), TextureUse.SFMEDIUM, 8.0f) + Client.RENDERER.textWidth(module.getKeyText(), TextureUse.SFMEDIUM, 8.0f));
        }
        Client.RENDERER.getCrenderSystem().pop();
        Client.RENDERER.getCrenderSystem().alpha(1.0f);
        this.drag.height = 9.0f + offset + 12.0f;
        this.drag.width = Math.max(this.origin.getWidth(), maxWidth + 20.0f);
    }
}

