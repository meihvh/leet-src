/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.point;

import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.widgets.impl.PointSettingWidget;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.point.PointSetting;
import im.leet.utils.client.ClientColors;

public class PointRenderer
extends SettingRenderer<PointSetting> {
    PointSettingWidget widget = new PointSettingWidget(this);

    public PointRenderer(PointSetting setting) {
        super(setting);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = Math.max(20.0f, 20.0f + this.drawDesc(16.0f));
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float prevAlpha = system.alpha();
        Client.RENDERER.text(((PointSetting)this.setting).getName(), this.x + 2.0f, this.y + 3.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        float off = Math.max(50.0f, Client.RENDERER.textWidth(((PointSetting)this.setting).getName(), TextureUse.SFMEDIUM, 8.0f) + 10.0f);
        Client.RENDERER.drawModuleRect(this.x + off, this.y + 2.0f, 12.0f, 12.0f);
        Client.RENDERER.text(IconUse.GEAR, this.x + off + 1.5f, this.y + 4.0f, TextureUse.ICONS, 7.0f, ClientColors.DARK_GRAY_COLOR);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY)) {
            Client.CLICKGUI.WIDGETS.register(this.widget);
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        super.release(button);
    }
}

