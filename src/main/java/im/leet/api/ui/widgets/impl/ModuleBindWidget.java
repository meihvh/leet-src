/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.base.modules.Module;
import im.leet.base.screens.ingame.objects.ModuleRenderer;
import im.leet.base.settings.impl.keybind.KeybindRenderer;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleBindWidget
extends UIWidget
implements MinecraftHolder {
    private ModuleRenderer parent;
    Rectangle pressBox = new Rectangle();
    Rectangle holdBox = new Rectangle();
    float anim1;
    float anim2;
    KeybindSetting bind;
    KeybindRenderer bindRenderer;

    public ModuleBindWidget(ModuleRenderer parent) {
        this.parent = parent;
        this.animation.setDirection(Direction.BACKWARDS);
        this.animation.reset();
        this.bind = (KeybindSetting)new KeybindSetting("Hotkey", parent.module.getKey()).onChanged(parent.module::setKey);
        this.bindRenderer = new KeybindRenderer(this.bind);
    }

    @Override
    public void opened() {
        this.bound((float)ModuleBindWidget.mc.field_1729.method_68879(mc.method_22683()), (float)ModuleBindWidget.mc.field_1729.method_68883(mc.method_22683()), 100.0f, 48.0f);
        this.animation.setDirection(Direction.FORWARDS);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        MatrixStack stack = Client.RENDERER.getStack();
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        stack.method_22903();
        MathUtility.scale(stack, this.x + this.width / 2.0f, this.y + this.height / 2.0f, 0.5f + this.animation.getOutput() * 0.5f);
        Client.RENDERER.outlined(this.x, this.y, this.width, this.height);
        this.bindRenderer.bound(this.x + 8.0f, this.y + 6.0f, this.width - 16.0f, 20.0f).render(mouseX, mouseY);
        if (!this.bindRenderer.isBinding()) {
            this.bind.bind(this.parent.module.getKey());
        }
        Module.BindType type = this.parent.module.getBindType();
        float baseWidth = this.width / 2.0f - 8.0f;
        int ord = type.ordinal();
        boolean l = type == Module.BindType.PRESS;
        float ANIM = 0.8f;
        if (l || this.anim2 > 0.8f || (double)this.anim1 > 0.05) {
            this.anim1 = MathUtility.linearFps(this.anim1, type == Module.BindType.HOLD ? 1.0f : 0.0f, 10.0f);
        }
        if (!l || (double)this.anim1 < 0.2) {
            this.anim2 = MathUtility.linearFps(this.anim2, type == Module.BindType.HOLD ? 1.0f : 0.0f, 10.0f);
        }
        Client.RENDERER.drawModuleRect(this.x + 8.0f + baseWidth * this.anim1, this.y + 28.0f, baseWidth + (this.anim2 - this.anim1) * baseWidth, 15.0f);
        float alpha = system.alpha();
        this.pressBox.bound(this.x + 8.0f, this.y + 28.0f, baseWidth, 15.0f);
        system.alpha(alpha * (1.0f - this.anim1 + 0.5f));
        Client.RENDERER.textCenteredStrict("Toggle", this.pressBox.getX() + this.pressBox.getWidth() / 2.0f, this.pressBox.getY() + this.pressBox.getHeight() / 2.0f, TextureUse.SFMEDIUM, 7.0f, ClientColors.FORE_COLOR);
        system.alpha(alpha);
        this.holdBox.bound(this.x + 8.0f + baseWidth, this.y + 28.0f, baseWidth, 15.0f);
        system.alpha(alpha * (this.anim1 + 0.5f));
        Client.RENDERER.textCenteredStrict("Hold", this.holdBox.getX() + this.holdBox.getWidth() / 2.0f, this.holdBox.getY() + this.holdBox.getHeight() / 2.0f, TextureUse.SFMEDIUM, 7.0f, ClientColors.FORE_COLOR);
        system.alpha(alpha);
        stack.method_22909();
        if ((double)this.animation.getOutput() < 0.1 && this.animation.getDirection() == Direction.BACKWARDS) {
            this.shouldRemove = true;
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.bindRenderer.click(mouseX, mouseY, button)) {
            return true;
        }
        if (this.holdBox.hovered(mouseX, mouseY)) {
            this.parent.module.setBindType(Module.BindType.HOLD);
            return true;
        }
        if (this.pressBox.hovered(mouseX, mouseY)) {
            this.parent.module.setBindType(Module.BindType.PRESS);
            return true;
        }
        if (!this.hover(mouseX, mouseY)) {
            this.animation.setDirection(Direction.BACKWARDS);
        }
        return true;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.bindRenderer.isBinding() && keyCode == 256) {
            this.animation.setDirection(Direction.BACKWARDS);
        }
        this.bindRenderer.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return this.hover((int)mouseX, (int)mouseY);
    }
}

