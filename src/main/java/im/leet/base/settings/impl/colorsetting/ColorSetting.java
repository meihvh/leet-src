/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.colorsetting;

import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.colorsetting.ColorRenderer;
import java.awt.Color;

public class ColorSetting
extends Setting.Basic<Color, ColorSetting> {
    public ColorSetting(String name, Color defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public void resetSetting() {
        this.color((Color)this.defaultValue);
    }

    public ColorSetting color(Color color) {
        this.value = color;
        this.onChange();
        return this;
    }

    public Color get() {
        return (Color)this.value;
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new ColorRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":color");
        json.addProperty(name, (Number)((Color)this.value).getRGB());
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":color");
        if (json.has(name)) {
            this.color(new Color(json.get(name).getAsInt(), true));
        }
    }
}

