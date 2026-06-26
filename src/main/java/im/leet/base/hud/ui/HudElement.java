/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.client.gui.screen.ChatScreen
 */
package im.leet.base.hud.ui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.widgets.HudSettingWidget;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.group.Group;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import java.util.List;
import net.minecraft.client.gui.screen.ChatScreen;

public abstract class HudElement
extends RendererObject {
    protected final Drag drag;
    protected Rectangle origin;
    protected boolean enabled = true;
    private final String name;
    protected final Group settings;
    private float f_animation = 0.0f;
    public boolean expanded = false;
    public SmoothStepAnimation expandAnim = new SmoothStepAnimation(300, 1.0);
    protected SmoothStepAnimation animation = new SmoothStepAnimation(300, 1.0);
    private final HudSettingWidget widget;

    public HudElement(String name, Drag drag) {
        this.name = name;
        this.settings = new Group(name);
        this.widget = new HudSettingWidget(this);
        this.drag = drag;
        if (drag != null) {
            this.origin = new Rectangle(drag.x, drag.y, drag.width, drag.height);
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY)) {
            switch (button) {
                case 0: {
                    if (!this.drag.canDrag.get().booleanValue()) break;
                    this.drag.dX = (float)mouseX - this.drag.x;
                    this.drag.dY = (float)mouseY - this.drag.y;
                    this.drag.dragging = true;
                    break;
                }
                case 1: {
                    this.expanded = true;
                    this.widget.bound(this.x + this.width / 2.0f, this.y + this.height / 2.0f, 140.0f, 200.0f);
                }
            }
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        if (this.drag != null) {
            this.drag.dragging = false;
        }
        super.release(button);
    }

    public void _render(int mouseX, int mouseY) {
        if (this.drag != null) {
            if (this.drag.dragging) {
                this.drag.x = (float)mouseX - this.drag.dX;
                this.drag.y = (float)mouseY - this.drag.dY;
            }
            this.x = this.drag.x;
            this.y = this.drag.y;
            this.width = this.drag.width;
            this.height = this.drag.height;
        }
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        this.f_animation = MathUtility.linearFps(this.f_animation, this.hover(mouseX, mouseY) && !this.settings.isEmpty() && !this.expanded && !this.drag.dragging && HudElement.mc.field_1755 instanceof ChatScreen ? 1.0f : 0.0f, 10.0f);
        float alpha = system.alpha();
        system.alpha(alpha * this.f_animation);
        Client.RENDERER.textCentered("right click - open settings", this.x + this.width / 2.0f, this.y - 10.0f, TextureUse.SFMEDIUM, 6.0f, ClientColors.FORE_COLOR);
        system.alpha(alpha);
        if ((double)(1.0f - this.expandAnim.getOutput()) > 0.1) {
            Client.HUD.registerOverlay(this.widget);
        } else {
            Client.HUD.unregisterOverlay(this.widget);
        }
        this.expandAnim.setDirection(this.expanded && HudElement.mc.field_1755 instanceof ChatScreen ? Direction.BACKWARDS : Direction.FORWARDS);
    }

    public List<SettingRenderer<?>> getSettingRenderers() {
        return this.settings.getSettingRenderers();
    }

    public void save(JsonObject json) {
        json.addProperty("_enabled", Boolean.valueOf(this.enabled));
        this.settings.save(json);
        if (this.drag != null) {
            JsonObject drag = new JsonObject();
            json.add("_drag", (JsonElement)drag);
            this.drag.save(drag);
        }
    }

    public void load(JsonObject json) {
        if (json.has("_enabled")) {
            this.enabled = json.get("_enabled").getAsBoolean();
        }
        this.settings.load(json);
        if (this.drag != null && json.has("_drag")) {
            JsonObject drag = json.getAsJsonObject("_drag");
            this.drag.load(drag);
        }
    }

    protected void drawBase(IconUse icon, String title, float iconOfX, float iconOfY) {
        float offset = this.animation.getOutput() * 20.0f;
        Client.RENDERER.drawHudRect(this.x, this.y, this.width, this.height);
        Client.RENDERER.backIcon(icon, this.x, this.y, 12.0f, 4.5f - offset + iconOfX, 5.5f + iconOfY, 8.0f, 0, 90);
        Client.RENDERER.text(title, this.x + 20.0f, this.y + 5.5f - offset, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return this.name;
    }

    public SmoothStepAnimation getAnimation() {
        return this.animation;
    }
}

