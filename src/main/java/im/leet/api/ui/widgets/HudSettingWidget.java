/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.settings.SettingRenderer;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class HudSettingWidget
extends UIWidget {
    private final HudElement parent;
    private final Drag drag = new Drag("HudSetting", () -> true);
    private List<SettingRenderer<?>> renderers;

    public HudSettingWidget(HudElement parent) {
        this.parent = parent;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.renderers == null) {
            this.renderers = this.parent.getSettingRenderers();
        }
        if (this.drag.dragging) {
            this.x = (float)mouseX - this.drag.dX;
            this.y = (float)mouseY - this.drag.dY;
        }
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float pAnim = 1.0f - this.parent.expandAnim.getOutput();
        float alpha = system.alpha();
        system.alpha(alpha * pAnim);
        MatrixStack stack = Client.RENDERER.getStack();
        stack.method_22903();
        MathUtility.scale(stack, this.x + this.width / 2.0f, this.y + this.height / 2.0f, 0.9f + pAnim * 0.1f);
        Color bgColor = ClientColors.GUI_BACKGROUND;
        Color thumbBackColor = ClientColors.DARK_GRAY_COLOR;
        Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f(8.0f), 1.0f, bgColor, bgColor, bgColor, bgColor);
        Client.RENDERER.outline(this.x, this.y, this.width, this.height, 0.0f, new Vector4f(8.0f), new Vector2f(1.0f), thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
        Client.RENDERER.text("Settings", this.x + 6.0f, this.y + 6.0f, TextureUse.SFMEDIUM, 9.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.text(IconUse.CROSS, this.x + this.width - 16.0f, this.y + 6.0f, TextureUse.ICONS, 9.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.rect(this.x + 5.0f, this.y + 23.0f, this.width - 10.0f, 1.0f, new Vector4f(0.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        float offy = 0.0f;
        for (SettingRenderer<?> renderer : this.renderers) {
            renderer.bound(this.x + 4.0f, this.y + offy + 28.0f, this.width - 5.0f, this.height).render(mouseX, mouseY);
            offy += renderer.getHeight();
        }
        this.height = Math.max(offy + 35.0f, 50.0f);
        stack.method_22909();
        system.alpha(alpha);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        for (SettingRenderer<?> renderer : this.renderers) {
            if (!renderer.click(mouseX, mouseY, button)) continue;
            return true;
        }
        if (!this.hover(mouseX, mouseY) || MathUtility.mouseIn(this.x + this.width - 16.0f, this.y + 6.0f, 9.0f, 9.0f, mouseX, mouseY)) {
            this.parent.expanded = false;
            return true;
        }
        if (this.hover(mouseX, mouseY)) {
            this.drag.dragging = true;
            this.drag.dX = (float)mouseX - this.x;
            this.drag.dY = (float)mouseY - this.y;
        }
        return true;
    }

    @Override
    public void release(int button) {
        for (SettingRenderer<?> renderer : this.renderers) {
            renderer.release(button);
        }
        this.drag.dragging = false;
        super.release(button);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (SettingRenderer<?> renderer : this.renderers) {
            renderer.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SettingRenderer<?> renderer : this.renderers) {
            renderer.keyPressed(keyCode, scanCode, modifiers);
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }
}

