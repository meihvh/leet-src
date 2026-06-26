/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.enumsetting;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.widgets.impl.EnumRendererWidget;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.lang.LangUtility;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import java.util.Arrays;

public class EnumRenderer<E extends Enum<E>>
extends SettingRenderer<EnumSetting<E>> {
    private final EnumObject<E>[] objects;
    EnumRendererWidget widget = new EnumRendererWidget(this);

    public EnumRenderer(EnumSetting<E> setting) {
        super(setting);
        this.objects = (EnumObject[])Arrays.stream((Enum[])((Enum)setting.get()).getDeclaringClass().getEnumConstants()).map(EnumObject::new).toArray(EnumObject[]::new);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        String name = ((EnumSetting)this.setting).getName();
        this.height = Math.max(16.0f, 16.0f + this.drawDesc(Client.RENDERER.textLined(((EnumSetting)this.setting).getName(), 20, this.x + 2.0f, this.y + 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR)));
        this.widget.bound(this.x + this.width / 2.0f, this.y + this.height / 2.0f - this.widget.getHeight() / 2.0f, this.width / 2.0f, this.widget.getHeight());
        if (!this.widget.canRender()) {
            this.widget.render(mouseX, mouseY);
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.widget.hover(mouseX, mouseY)) {
            Client.CLICKGUI.WIDGETS.register(this.widget);
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }

    public EnumObject<E>[] getObjects() {
        return this.objects;
    }

    public static class EnumObject<E extends Enum<E>>
    extends RendererObject {
        public final E value;
        Color color;
        boolean selected = false;
        SmoothStepAnimation animation = new SmoothStepAnimation(300, 1.0);

        public EnumObject(E value) {
            this.value = value;
        }

        public EnumObject<E> param(Color color, boolean selected) {
            this.color = color;
            this.selected = selected;
            return this;
        }

        @Override
        public void render(int mouseX, int mouseY) {
            this.animation.setDirection(this.selected ? Direction.BACKWARDS : Direction.FORWARDS);
            float anim = 1.0f - this.animation.getOutput();
            Color color = ColorUtility.linear(ClientColors.DARK_GRAY_COLOR, ClientColors.FORE_COLOR, anim);
            Color bgColor = ClientColors.GUI_BACKGROUND;
            Color contrast = Color.WHITE;
            Color thumbBackColor = ClientColors.GUI_STROKE;
            float prevAlpha = Client.RENDERER.getCrenderSystem().alpha();
            Client.RENDERER.getCrenderSystem().alpha(anim * prevAlpha);
            Client.RENDERER.textCenteredStrict(IconUse.CHECK.glyph, this.x + this.width - 12.0f, this.y + this.height / 2.0f, TextureUse.ICONS, 8.0f, color);
            Client.RENDERER.getCrenderSystem().alpha(prevAlpha);
            String name = LangUtility.getEnumChoiceName(this.value);
            Client.RENDERER.textCenteredStrict(name, this.x + (Client.RENDERER.textWidth(name, TextureUse.SFMEDIUM, 7.0f) + 12.0f) / 2.0f, this.y + this.height / 2.0f, TextureUse.SFMEDIUM, 7.0f, color);
        }
    }
}

