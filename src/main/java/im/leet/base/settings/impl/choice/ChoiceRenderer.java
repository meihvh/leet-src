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
package im.leet.base.settings.impl.choice;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ChoiceRenderer<C extends Choice>
extends SettingRenderer<ChoiceSetting<C>> {
    private final List<ChoiceObject<C>> objects;

    public ChoiceRenderer(ChoiceSetting<C> setting) {
        super(setting);
        this.objects = setting.getChoices().stream().map(ChoiceObject::new).toList();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        Color bgColor = ClientColors.GUI_BACKGROUND;
        Color thumbBackColor = ClientColors.GUI_STROKE;
        MatrixStack stack = Client.RENDERER.getStack();
        float expand = ((ChoiceSetting)this.setting).expandAnim.getOutput();
        Object active = ((ChoiceSetting)this.setting).get();
        List<SettingRenderer<?>> renderers = ((Group)active).getSettingRenderers();
        float offsetX = 0.0f;
        float enumOffsetY = 0.0f;
        for (ChoiceObject<C> object : this.objects) {
            if (offsetX + object.getWidth() > this.width) {
                offsetX = 0.0f;
                enumOffsetY += object.getHeight();
            }
            object.setActive(active == object.choice).bound(this.x + 5.0f + offsetX, this.y + 23.0f + enumOffsetY, object.getWidth(), 15.0f);
            offsetX += object.getWidth();
        }
        Client.RENDERER.rect(this.x, this.y + 2.0f, this.width, enumOffsetY + 40.0f, new Vector4f(8.0f), 1.0f, bgColor, bgColor, bgColor, bgColor);
        Client.RENDERER.outline(this.x, this.y + 2.0f, this.width, enumOffsetY + 40.0f, 0.0f, new Vector4f(8.0f), new Vector2f(1.0f), thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
        if (!((ChoiceSetting)this.setting).name.isEmpty()) {
            Client.RENDERER.text(((ChoiceSetting)this.setting).getName(), this.x + 20.0f, this.y + 10.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
            if (((Group)active).isExpandable()) {
                stack.method_22903();
                float sx = MathUtility.scaledX(this.x + 30.0f + Client.RENDERER.textWidth(((ChoiceSetting)this.setting).getName(), TextureUse.SFMEDIUM, 8.0f));
                float sy = MathUtility.scaledY(this.y + 15.0f);
                stack.method_46416(sx, sy, 0.0f);
                stack.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees(-(1.0f - expand) * 90.5f));
                Client.RENDERER.textCenteredStrict(IconUse.DOWN.glyph, 1.0f, 0.0f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
                stack.method_22909();
            }
        }
        Client.RENDERER.text(IconUse.GROUP, this.x + 5.0f, this.y + 10.0f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
        for (ChoiceObject<C> object : this.objects) {
            object.render(mouseX, mouseY);
        }
        enumOffsetY += 40.0f;
        if (!renderers.isEmpty()) {
            stack.method_22903();
            MathUtility.scale(stack, this.x, this.y + enumOffsetY, 1.0f, expand);
            float offsetY = 0.0f;
            if ((double)expand > 0.1) {
                float visible;
                for (SettingRenderer<?> renderer : renderers) {
                    visible = renderer.visible.getOutput();
                    renderer.visible.setDirection(((Setting)renderer.getSetting()).getVisible().get() != false ? Direction.FORWARDS : Direction.BACKWARDS);
                    if (!((double)visible > 0.1)) continue;
                    renderer.bound(this.x + 4.0f, this.y + 4.0f + enumOffsetY + offsetY, this.width - 8.0f, -1.0f);
                    offsetY += renderer.getHeight() * visible;
                }
                Client.RENDERER.rect(this.x, this.y + enumOffsetY + 2.0f, this.width, offsetY + 5.0f, new Vector4f(8.0f), 1.0f, bgColor, bgColor, bgColor, bgColor);
                Client.RENDERER.outline(this.x, this.y + enumOffsetY + 2.0f, this.width, offsetY + 5.0f, 0.0f, new Vector4f(8.0f), new Vector2f(1.0f), thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
                for (SettingRenderer<?> renderer : renderers) {
                    visible = renderer.visible.getOutput();
                    stack.method_22903();
                    MathUtility.scale(stack, renderer.getX() + renderer.getWidth() / 2.0f, renderer.getY() + renderer.getHeight() / 2.0f, 1.0f, visible);
                    renderer.render(mouseX, mouseY);
                    stack.method_22909();
                }
            }
            this.height = enumOffsetY + offsetY * expand + 10.0f;
            stack.method_22909();
        } else {
            this.height = enumOffsetY + 5.0f;
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (button == 1 && MathUtility.mouseIn(this.x, this.y, this.width, 20.0f, mouseX, mouseY)) {
            ((ChoiceSetting)this.setting).expanded(!((ChoiceSetting)this.setting).isExpanded());
            return true;
        }
        for (ChoiceObject<C> choiceObject : this.objects) {
            if (!choiceObject.hover(mouseX, mouseY) || ((ChoiceSetting)this.setting).get() == choiceObject.choice) continue;
            ((ChoiceSetting)this.setting).select(choiceObject.choice);
            return true;
        }
        for (SettingRenderer settingRenderer : ((Group)((ChoiceSetting)this.setting).get()).getSettingRenderers()) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue() || !settingRenderer.click(mouseX, mouseY, button)) continue;
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SettingRenderer<?> renderer : ((Group)((ChoiceSetting)this.setting).get()).getSettingRenderers()) {
            if (!((Setting)renderer.getSetting()).getVisible().get().booleanValue()) continue;
            renderer.keyPressed(keyCode, scanCode, modifiers);
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (SettingRenderer<?> renderer : ((Group)((ChoiceSetting)this.setting).get()).getSettingRenderers()) {
            if (!((Setting)renderer.getSetting()).getVisible().get().booleanValue()) continue;
            renderer.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }

    @Override
    public void release(int button) {
        for (SettingRenderer<?> renderer : ((Group)((ChoiceSetting)this.setting).get()).getSettingRenderers()) {
            if (!((Setting)renderer.getSetting()).getVisible().get().booleanValue()) continue;
            renderer.release(button);
        }
        super.release(button);
    }

    private static class ChoiceObject<C extends Choice>
    extends RendererObject {
        private final C choice;
        private boolean active = false;
        final SmoothStepAnimation animation = new SmoothStepAnimation(300, 1.0);

        @Override
        public void render(int mouseX, int mouseY) {
            this.width = Client.RENDERER.textWidth(((Choice)this.choice).name, TextureUse.SFMEDIUM, 7.0f) + 12.0f;
            this.animation.setDirection(this.active ? Direction.BACKWARDS : Direction.FORWARDS);
            float anim = 1.0f - this.animation.getOutput();
            Color color = ColorUtility.linear(ClientColors.DARK_GRAY_COLOR, ClientColors.FORE_COLOR, anim);
            float prevAlpha = Client.RENDERER.getCrenderSystem().alpha();
            Client.RENDERER.getCrenderSystem().alpha(anim * prevAlpha * 0.5f);
            Client.RENDERER.drawModuleRect(this.x, this.y - 1.0f, this.width, this.height);
            Client.RENDERER.getCrenderSystem().alpha(anim * prevAlpha);
            Client.RENDERER.outline(this.x, this.y - 1.0f, this.width, this.height, 0.0f, new Vector4f(7.0f), new Vector2f(1.0f), ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
            Client.RENDERER.getCrenderSystem().alpha(prevAlpha);
            Client.RENDERER.textCentered(((Choice)this.choice).name, this.x + this.width / 2.0f - 1.0f, this.y + 2.0f, TextureUse.SFMEDIUM, 7.0f, color);
        }

        public ChoiceObject(C choice) {
            this.choice = choice;
        }

        public ChoiceObject<C> setActive(boolean active) {
            this.active = active;
            return this;
        }
    }
}

