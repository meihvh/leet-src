/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.multienum;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.widgets.impl.MultiEnumRendererWidget;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.lang.LangUtility;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import java.util.ArrayList;

public class MultiEnumRenderer<E extends Enum<E>>
extends SettingRenderer<MultiEnumSetting<E>> {
    final ArrayList<MultiObject> objects = new ArrayList();
    MultiEnumRendererWidget widget = new MultiEnumRendererWidget(this);

    public MultiEnumRenderer(MultiEnumSetting<E> setting) {
        super(setting);
        for (Enum entry : setting.getEnumEntries()) {
            this.objects.add(new MultiObject(this, entry));
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = Math.max(20.0f, 20.0f + this.drawDesc(Client.RENDERER.textLined(((MultiEnumSetting)this.setting).getName(), 25, this.x + 2.0f, this.y + 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR)));
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
        return false;
    }

    public ArrayList<MultiObject> getObjects() {
        return this.objects;
    }

    public static class MultiObject
    extends RendererObject {
        public E entry;
        SmoothStepAnimation animation = new SmoothStepAnimation(300, 1.0);
        final /* synthetic */ MultiEnumRenderer this$0;

        public MultiObject(E entry) {
            this.this$0 = this$0;
            this.entry = entry;
        }

        @Override
        public void render(int mouseX, int mouseY) {
            boolean value = ((MultiEnumSetting)this.this$0.setting).get(this.entry);
            this.animation.setDirection(value ? Direction.BACKWARDS : Direction.FORWARDS);
            float anim = 1.0f - this.animation.getOutput();
            Color color = ColorUtility.linear(ClientColors.DARK_GRAY_COLOR, ClientColors.FORE_COLOR, anim);
            Color bgColor = ClientColors.GUI_BACKGROUND;
            Color contrast = Color.WHITE;
            Color thumbBackColor = ClientColors.GUI_STROKE;
            float prevAlpha = Client.RENDERER.getCrenderSystem().alpha();
            String name = LangUtility.getEnumChoiceName(this.entry);
            float defW = Client.RENDERER.textWidth(name, TextureUse.SFMEDIUM, 7.0f) + 12.0f;
            Client.RENDERER.getCrenderSystem().alpha(anim * prevAlpha);
            Client.RENDERER.textCenteredStrict(IconUse.CHECK.glyph, this.x + Math.max(this.width - 12.0f, defW), this.y + this.height / 2.0f, TextureUse.ICONS, 8.0f, color);
            Client.RENDERER.getCrenderSystem().alpha(prevAlpha);
            Client.RENDERER.textCenteredStrict(name, this.x + defW / 2.0f, this.y + this.height / 2.0f, TextureUse.SFMEDIUM, 7.0f, color);
        }
    }
}

