/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Quaternionfc
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.base.settings.impl.group;

import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.group.Group;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class GroupRenderer
extends SettingRenderer<Group> {
    private final List<SettingRenderer<?>> renderers;
    float renderHeight = -1.0f;

    public GroupRenderer(Group setting) {
        super(setting);
        this.renderers = setting.getSettingRenderers();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        Color bgColor = ClientColors.GUI_BACKGROUND;
        Color thumbBackColor = ClientColors.GUI_STROKE;
        MatrixStack stack = Client.RENDERER.getStack();
        float expand = ((Group)this.getSetting()).isExpandable() ? ((Group)this.setting).expandAnim.getOutput() : 1.0f;
        Client.RENDERER.rect(this.x, this.y + 2.0f, this.width, this.renderHeight, new Vector4f(8.0f), 1.0f, bgColor, bgColor, bgColor, bgColor);
        Client.RENDERER.outline(this.x, this.y + 2.0f, this.width, this.renderHeight, 0.0f, new Vector4f(8.0f), new Vector2f(1.0f), thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
        Color c = ClientColors.DARK_GRAY_COLOR;
        Client.RENDERER.text(((Group)this.setting).getName(), this.x + 20.0f, this.y + 10.0f, TextureUse.SFMEDIUM, 8.0f, c);
        Client.RENDERER.text(IconUse.GROUP, this.x + 5.0f, this.y + 10.0f, TextureUse.ICONS, 8.0f, c);
        if (((Group)this.getSetting()).isExpandable()) {
            stack.method_22903();
            float sx = MathUtility.scaledX(this.x + 30.0f + Client.RENDERER.textWidth(((Group)this.setting).getName(), TextureUse.SFMEDIUM, 8.0f));
            float sy = MathUtility.scaledY(this.y + 15.0f);
            stack.method_46416(sx, sy, 0.0f);
            stack.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees(-(1.0f - expand) * 90.5f));
            Client.RENDERER.textCenteredStrict(IconUse.DOWN.glyph, 1.0f, 0.0f, TextureUse.ICONS, 8.0f, c);
            stack.method_22909();
        }
        if (((Group)this.setting).enabledRenderer != null) {
            ((Group)this.setting).enabledRenderer.noName = true;
            ((Group)this.setting).enabledRenderer.bound(this.x + 20.0f, this.y + 5.0f, this.width - 24.0f, 20.0f).render(mouseX, mouseY);
        }
        stack.method_22903();
        stack.method_46416(MathUtility.scaledX(this.x), MathUtility.scaledY(this.y + 25.0f), 0.0f);
        stack.method_22905(1.0f, expand, 1.0f);
        stack.method_46416(-MathUtility.scaledX(this.x), -MathUtility.scaledY(this.y + 25.0f), 0.0f);
        float offsetY = 14.0f;
        if ((double)expand > 0.1) {
            for (SettingRenderer<?> renderer : this.renderers) {
                if (renderer.getSetting() == ((Group)this.setting).enabled) continue;
                float visible = renderer.visible.getOutput();
                renderer.visible.setDirection(((Setting)renderer.getSetting()).getVisible().get() != false ? Direction.FORWARDS : Direction.BACKWARDS);
                if (!((double)visible > 0.1)) continue;
                renderer.bound(this.x + 4.0f, this.y + 8.0f + offsetY, this.width - 8.0f, 15.0f);
                stack.method_22903();
                MathUtility.scale(stack, renderer.getX() + renderer.getWidth() / 2.0f, renderer.getY() + renderer.getHeight() / 2.0f, 1.0f, visible);
                renderer.render(mouseX, mouseY);
                stack.method_22909();
                offsetY += renderer.getHeight() * visible;
            }
        }
        this.height = 14.0f + (offsetY - 14.0f) * expand + 16.0f;
        this.renderHeight = this.height - 6.0f;
        stack.method_22909();
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (button == 1 && MathUtility.mouseIn(this.x, this.y, this.width, 20.0f, mouseX, mouseY)) {
            ((Group)this.setting).expanded(!((Group)this.setting).isExpanded());
            return true;
        }
        if (((Group)this.setting).isExpanded()) {
            for (SettingRenderer<?> renderer : this.renderers) {
                if (!((Setting)renderer.getSetting()).getVisible().get().booleanValue() || renderer.getSetting() == ((Group)this.setting).enabled || !renderer.click(mouseX, mouseY, button)) continue;
                return true;
            }
        }
        if (((Group)this.setting).enabledRenderer != null && ((Group)this.setting).enabledRenderer.click(mouseX, mouseY, button)) {
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SettingRenderer<?> renderer : this.renderers) {
            if (!((Setting)renderer.getSetting()).getVisible().get().booleanValue()) continue;
            renderer.keyPressed(keyCode, scanCode, modifiers);
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (SettingRenderer<?> renderer : this.renderers) {
            if (!((Setting)renderer.getSetting()).getVisible().get().booleanValue()) continue;
            renderer.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }

    @Override
    public void release(int button) {
        for (SettingRenderer<?> renderer : this.renderers) {
            if (!((Setting)renderer.getSetting()).getVisible().get().booleanValue() || renderer.getSetting() == ((Group)this.setting).enabled) continue;
            renderer.release(button);
        }
        super.release(button);
    }
}

