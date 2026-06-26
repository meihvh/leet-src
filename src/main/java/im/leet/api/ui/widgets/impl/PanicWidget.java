/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import im.leet.utils.math.TimeUtility;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class PanicWidget
extends UIWidget {
    public boolean open = false;
    float anim1 = 0.0f;
    float anim2;
    float anim3;
    public int timer = 0;
    TimeUtility time = new TimeUtility();
    Rectangle cancel = new Rectangle();
    EventBus<Event> events = event -> {};

    public PanicWidget() {
        Client.EVENTS.register(this);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float origin = system.alpha();
        this.anim1 = MathUtility.linearFps(this.anim1, this.open ? 1.0f : 0.0f, 10.0f);
        system.alpha(origin * this.anim1 * 0.75f);
        Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f(7.0f), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
        system.alpha(origin * this.anim1);
        this.cancel.bound(this.x + this.width / 2.0f - this.cancel.getWidth() / 2.0f, this.y + this.height / 2.0f - this.cancel.getHeight() / 2.0f + 20.0f, 60.0f, 18.0f);
        Client.RENDERER.rect(this.cancel, new Vector4f(7.0f), 1.0f, ClientColors.GUI_BACKGROUND, ClientColors.GUI_BACKGROUND, ClientColors.GUI_BACKGROUND, ClientColors.GUI_BACKGROUND);
        Client.RENDERER.outline(this.cancel.getX(), this.cancel.getY(), this.cancel.getWidth(), this.cancel.getHeight(), 0.0f, new Vector4f(7.0f), new Vector2f(1.0f), ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.textCentered("Cancel", this.cancel.getX() + this.cancel.getWidth() / 2.0f, this.cancel.getY() + this.cancel.getHeight() / 2.0f - 5.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.textCentered(String.format("Detaching in %s sec...", this.timer), this.x + this.width / 2.0f - 1.0f, this.y + this.height / 2.0f - 10.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        system.alpha(origin);
        if ((double)this.anim1 < 0.15 && !this.open) {
            this.shouldRemove = true;
        }
        if (this.time.reached(1000L, true) && this.timer > 0) {
            --this.timer;
        }
        if (this.timer == 0) {
            this.anim1 = 0.0f;
            Client.startPanic();
            this.open = false;
            this.shouldRemove = true;
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.cancel.hovered(mouseX, mouseY) && button == 0) {
            this.open = false;
            return true;
        }
        Drag drag = Client.CLICKGUI.drag;
        drag.dX = (float)mouseX - drag.x;
        drag.dY = (float)mouseY - drag.y;
        drag.dragging = true;
        return true;
    }
}

