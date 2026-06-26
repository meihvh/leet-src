/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.other.pages;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIPage;
import im.leet.api.ui.widgets.impl.MacrosWidget;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MacrosPage
extends UIPage {
    MacrosWidget macrosWidget = new MacrosWidget();

    @Override
    public void render(int mouseX, int mouseY) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        system.hatch(0.143f);
        Client.RENDERER.outline(this.x + this.width / 2.0f - 32.5f, this.y + this.height / 2.0f - 17.5f, 65.0f, 35.0f, 1.0f, new Vector4f(6.0f), new Vector2f(1.0f, 1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        system.hatch(0.0f);
        Client.RENDERER.textCentered("+", this.x + this.width / 2.0f - 2.0f, this.y + this.height / 2.0f - 13.0f, TextureUse.SFMEDIUM, 20.0f, ClientColors.DARK_GRAY_COLOR);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY)) {
            this.macrosWidget.bound(this.x, this.y, 250.0f, 260.0f);
            this.macrosWidget.animation.setDirection(Direction.BACKWARDS);
            Client.CLICKGUI.WIDGETS.register(this.macrosWidget);
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }
}

