/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.ShaderUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ColorPickerWidget
extends UIWidget {
    ColorSetting colorSetting;
    Drag drag = new Drag("ColorPicker", () -> true);
    public static ArrayList<Color> lastColors = new ArrayList();
    public boolean expanded = false;
    Rectangle color = new Rectangle();
    Rectangle hue = new Rectangle();
    Rectangle saveColor = new Rectangle();
    boolean draggingHue = false;
    boolean draggingColor = false;
    public float[] hsb = new float[3];

    public ColorPickerWidget(ColorSetting setting) {
        this.colorSetting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.drag.dragging) {
            this.x = (float)mouseX + this.drag.dX;
            this.y = (float)mouseY + this.drag.dY;
        }
        this.animation.setDirection(this.expanded ? Direction.BACKWARDS : Direction.FORWARDS);
        float pAnim = 1.0f - this.animation.getOutput();
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        MatrixStack stack = Client.RENDERER.getStack();
        float alpha = system.alpha();
        stack.method_22903();
        MathUtility.scale(stack, this.x + this.width / 2.0f, this.y + this.height / 2.0f, 0.8f + pAnim * 0.2f);
        system.alpha(alpha * pAnim);
        Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f(6.0f), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
        Client.RENDERER.outline(this.x, this.y, this.width, this.height, 1.0f, new Vector4f(6.0f), new Vector2f(1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Client.RENDERER.text(this.colorSetting.getName(), this.x + 6.0f, this.y + 6.0f, TextureUse.SFMEDIUM, 9.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.text(IconUse.CROSS, this.x + this.width - 16.0f, this.y + 6.0f, TextureUse.ICONS, 9.0f, ClientColors.FORE_COLOR);
        this.color.bound(this.x + this.width / 2.0f - 60.0f, this.y + 30.0f, 120.0f, 85.0f);
        this.hue.bound(this.color.getX(), this.color.getY() + this.color.getHeight() + 4.0f, this.color.getWidth(), 5.0f);
        this.saveColor.bound(this.color.getX(), this.hue.getY() + 16.0f, 14.0f, 14.0f);
        Client.RENDERER.shader(this.color, new Vector4f(4.0f), 1.0f, ShaderUse.COLOR_PICKER, Color.getHSBColor(this.hsb[0], 1.0f, 1.0f));
        Client.RENDERER.shader(this.hue, new Vector4f(4.0f), 1.0f, ShaderUse.HUE);
        Client.RENDERER.drawModuleRect(this.saveColor);
        Client.RENDERER.text(IconUse.ADD, this.saveColor.getX() + 2.2f, this.saveColor.getY() + 2.5f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.rect(this.hue.getX() + this.hsb[0] * this.hue.getWidth() - 0.5f, this.hue.getY(), 1.0f, this.hue.getHeight(), new Vector4f(0.0f), 0.0f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        Color d = ColorUtility.contrast(this.colorSetting.get());
        Client.RENDERER.rect(this.color.getX() + this.hsb[1] * this.color.getWidth() - 2.0f, this.color.getY() + (1.0f - this.hsb[2]) * this.color.getHeight() - 2.0f, 4.0f, 4.0f, new Vector4f(3.0f), 2.0f, d, d, d, d);
        if (this.draggingHue) {
            this.hsb[0] = Math.clamp(((float)mouseX - this.hue.getX()) / this.hue.getWidth(), 0.0f, 1.0f);
            this.colorSetting.color(Color.getHSBColor(this.hsb[0], this.hsb[1], this.hsb[2]));
        } else if (this.draggingColor) {
            this.hsb[1] = Math.clamp(((float)mouseX - this.color.getX()) / this.color.getWidth(), 0.0f, 1.0f);
            this.hsb[2] = 1.0f - Math.clamp(((float)mouseY - this.color.getY()) / this.color.getHeight(), 0.0f, 1.0f);
            this.colorSetting.color(Color.getHSBColor(this.hsb[0], this.hsb[1], this.hsb[2]));
        } else {
            Color c = this.colorSetting.get();
            this.hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), this.hsb);
        }
        float off = 4.0f;
        for (Color co : lastColors) {
            Client.RENDERER.rect(this.saveColor.getX() + this.saveColor.getWidth() + off, this.saveColor.getY(), this.saveColor.getWidth(), this.saveColor.getHeight(), new Vector4f((this.saveColor.getWidth() + this.saveColor.getHeight()) / 2.0f - 1.0f), 1.0f, co, co, co, co);
            off += this.saveColor.getWidth() + 4.0f;
        }
        system.alpha(alpha);
        stack.method_22909();
        if (this.animation.finished(Direction.FORWARDS) && this.expanded && !this.shouldRemove) {
            this.shouldRemove = true;
            this.expanded = false;
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (!this.expanded) {
            return false;
        }
        if (!this.hover(mouseX, mouseY) || MathUtility.mouseIn(this.x + this.width - 16.0f, this.y + 6.0f, 9.0f, 9.0f, mouseX, mouseY)) {
            this.expanded = false;
            this.animation.setDirection(Direction.FORWARDS);
            return true;
        }
        if (this.hue.hovered(mouseX, mouseY)) {
            this.draggingHue = true;
            return true;
        }
        if (this.color.hovered(mouseX, mouseY)) {
            this.draggingColor = true;
            return true;
        }
        if (this.saveColor.hovered(mouseX, mouseY)) {
            lastColors.add(this.colorSetting.get());
            if (lastColors.size() > 6) {
                lastColors.removeFirst();
            }
            return true;
        }
        float off = 4.0f;
        for (Color co : lastColors) {
            if (MathUtility.mouseIn(this.saveColor.getX() + off + this.saveColor.getWidth(), this.saveColor.getY(), this.saveColor.getWidth(), this.saveColor.getHeight(), mouseX, mouseY)) {
                this.colorSetting.color(co);
            }
            off += this.saveColor.getWidth() + 4.0f;
        }
        if (this.hover(mouseX, mouseY)) {
            this.drag.dragging = true;
            this.drag.dX = this.x - (float)mouseX;
            this.drag.dY = this.y - (float)mouseY;
        }
        return true;
    }

    @Override
    public void release(int button) {
        super.release(button);
        this.drag.dragging = false;
        this.draggingHue = false;
        this.draggingColor = false;
    }

    public static class ColorObject
    extends RendererObject {
        Color color;
        ColorSetting setting;
        ColorPickerWidget parent;

        public ColorObject(Color color, ColorSetting setting, ColorPickerWidget parent) {
            this.color = color;
            this.setting = setting;
            this.parent = parent;
        }

        @Override
        public void render(int mouseX, int mouseY) {
            if (this.hover(mouseX, mouseY)) {
                return;
            }
            Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f((this.width + this.height) / 2.0f - 1.0f), 1.0f, this.color, this.color, this.color, this.color);
        }

        @Override
        public boolean click(int mouseX, int mouseY, int button) {
            if (this.hover(mouseX, mouseY)) {
                System.out.println(String.valueOf(this.color) + " " + String.valueOf(this.setting.get()));
                return true;
            }
            System.out.println(this.hover(mouseX, mouseY));
            return super.click(mouseX, mouseY, button);
        }
    }
}

