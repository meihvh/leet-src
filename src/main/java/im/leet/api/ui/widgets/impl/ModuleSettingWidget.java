/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.widgets.UIModuleWidget;
import im.leet.base.screens.ingame.objects.ModuleRenderer;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;

public class ModuleSettingWidget
extends UIModuleWidget {
    private final ModuleRenderer parent;
    private float scroll = 0.0f;
    private float scrollAnim;
    Drag drag;
    private boolean positioned = false;

    public ModuleSettingWidget(ModuleRenderer parent) {
        this.parent = parent;
        this.drag = new Drag("ModuleSettingWidget", () -> true);
    }

    public void openAt(float x, float y, float width) {
        if (!this.positioned) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.positioned = true;
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float pExpandAnim = 1.0f - Client.CLICKGUI.getSettingAnim().getOutput();
        Client.RENDERER.getStack().method_22903();
        Client.RENDERER.getStack().method_46416((1.0f - pExpandAnim) * this.width * 2.0f, 0.0f, 0.0f);
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float prevAlpha = system.alpha();
        system.push(this.x, this.y, this.width, this.height);
        Color back = ColorUtility.injectAlpha(ClientColors.GUI_BACKGROUND, 255.0f);
        Color out = ClientColors.GUI_STROKE;
        Client.RENDERER.text(String.format("%s", this.parent.module.getName()), this.x + 18.0f, this.y + 7.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.text(IconUse.BACK, this.x + 6.0f, this.y + 7.0f, TextureUse.ICONS, 9.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.rect(this.x + 5.0f, this.y + 23.0f, this.width - 10.0f, 1.0f, new Vector4f(0.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        system.push(this.x, this.y + 27.0f, this.width, this.height - 27.0f);
        float offsetY1 = 0.0f;
        float offsetY2 = 0.0f;
        float offsetX = 0.0f;
        for (SettingRenderer<?> renderer : this.parent.settingRenderers) {
            float visible = renderer.visible.getOutput();
            renderer.visible.setDirection(((Setting)renderer.getSetting()).getVisible().get() != false ? Direction.FORWARDS : Direction.BACKWARDS);
            if (!((double)visible > 0.1)) continue;
            boolean isRight = offsetX > 0.0f;
            renderer.bound(this.x + 8.0f + offsetX, this.y + 28.0f + (isRight ? offsetY2 : offsetY1) + this.scrollAnim, this.width / 2.0f - 16.0f, renderer.getHeight());
            Client.RENDERER.getStack().method_22903();
            MathUtility.scale(Client.RENDERER.getStack(), renderer.getX() + renderer.getWidth() / 2.0f, renderer.getY() + renderer.getHeight() / 2.0f, visible);
            renderer.render(mouseX, mouseY);
            Client.RENDERER.getStack().method_22909();
            if (!isRight) {
                offsetY1 += renderer.getHeight() * visible;
            } else {
                offsetY2 += renderer.getHeight() * visible;
            }
            if (!((offsetX += renderer.getWidth() + 12.0f) > this.width / 2.0f)) continue;
            offsetX = 0.0f;
        }
        system.pop();
        system.pop();
        Client.RENDERER.getStack().method_22909();
        this.height = Math.min(60.0f + offsetY1 + offsetY2, 300.0f);
        this.scroll = MathHelper.method_15363((float)this.scroll, (float)(-(offsetY1 + offsetY2) + 18.0f), (float)0.0f);
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 10.0f);
        if ((double)pExpandAnim > 0.2 && (double)pExpandAnim < 0.9) {
            this.scroll = 0.0f;
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (MathUtility.mouseIn(this.x + 6.0f, this.y + 6.0f, 9.0f, 9.0f, mouseX, mouseY)) {
            Client.CLICKGUI.getSettingAnim().setDirection(Direction.FORWARDS);
            return true;
        }
        for (SettingRenderer<?> settingRenderer : this.parent.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue() || !settingRenderer.click(mouseX, mouseY, button)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void release(int button) {
        if (button == 0) {
            this.drag.dragging = false;
        }
        for (SettingRenderer<?> settingRenderer : this.parent.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.release(button);
        }
        super.release(button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SettingRenderer<?> settingRenderer : this.parent.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (SettingRenderer<?> settingRenderer : this.parent.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }

    @Override
    public void mouseDragged(int mouseX, int mouseY) {
        if (this.drag.dragging) {
            this.x = (float)mouseX - this.drag.dX;
            this.y = (float)mouseY - this.drag.dY;
        }
        for (SettingRenderer<?> settingRenderer : this.parent.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.mouseDragged(mouseX, mouseY);
        }
        super.mouseDragged(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.hover((int)mouseX, (int)mouseY)) {
            this.scroll += (float)verticalAmount * 10.0f;
        }
        return true;
    }
}

