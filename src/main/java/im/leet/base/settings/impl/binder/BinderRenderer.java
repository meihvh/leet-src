/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.binder;

import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.widgets.impl.BinderWidget;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.binder.BinderSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;

public class BinderRenderer
extends SettingRenderer<BinderSetting> {
    private final BinderWidget widget = new BinderWidget(this);

    public BinderRenderer(BinderSetting setting) {
        super(setting);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = 15.0f;
        float off = Client.RENDERER.textWidth(((BinderSetting)this.setting).getName(), TextureUse.SFMEDIUM, 8.0f) + 12.0f;
        Client.RENDERER.drawModuleRect(this.x + off, this.y + 2.0f, 12.0f, 12.0f);
        Client.RENDERER.text(IconUse.GEAR, this.x + off + 1.5f, this.y + 4.5f, TextureUse.ICONS, 7.0f, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.text(((BinderSetting)this.setting).getName(), this.x + 2.0f, this.y + 3.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY) && button < 2 && button >= 0) {
            this.widget.bound(this.x + this.width, this.y, 160.0f, 200.0f);
            this.widget.getAnimation().setDirection(Direction.BACKWARDS);
            Client.CLICKGUI.WIDGETS.register(this.widget);
        }
        return super.click(mouseX, mouseY, button);
    }
}

