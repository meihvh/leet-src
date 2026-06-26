/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector4f
 */
package im.leet.base.settings.impl.colorsetting;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.widgets.impl.ColorPickerWidget;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.utils.client.ClientColors;
import org.joml.Vector4f;

public class ColorRenderer
extends SettingRenderer<ColorSetting> {
    ColorSetting setting;
    ColorPickerWidget picker;

    public ColorRenderer(ColorSetting setting) {
        super(setting);
        this.setting = setting;
        this.picker = new ColorPickerWidget(setting);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = Math.max(15.0f, 15.0f + this.drawDesc(Client.RENDERER.textLined(this.setting.getName(), 20, this.x + 2.0f, this.y + 3.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR)));
        float tWidth = Math.max(30.0f, Client.RENDERER.textWidth(this.setting.getName(), TextureUse.SFMEDIUM, 8.0f));
        Client.RENDERER.rect(this.x + 8.0f + tWidth, this.y + 5.0f, 6.0f, 6.0f, new Vector4f(5.0f), 2.0f, this.setting.get(), this.setting.get(), this.setting.get(), this.setting.get());
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY)) {
            this.picker.getAnimation().timerUtil.setTime(this.picker.getAnimation().timerUtil.getTime() + 50L);
            Client.CLICKGUI.WIDGETS.register(this.picker);
            this.picker.bound(this.x + this.width, this.y + this.height, 140.0f, 160.0f);
            this.picker.expanded = true;
            this.picker.shouldRemove = false;
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }
}

