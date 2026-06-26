/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.text;

import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.text.TextSettingRenderer;

public class TextSetting
extends Setting.Basic<String, TextSetting> {
    private String hideMask = null;

    public TextSetting(String name, String defaultValue) {
        super(name, defaultValue);
    }

    public String getText() {
        return (String)this.value;
    }

    public TextSetting setText(String value) {
        this.value = value;
        if (this.onChanged != null) {
            this.onChanged.accept(value);
        }
        return this;
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new TextSettingRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":text");
        json.addProperty(name, (String)this.value);
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":text");
        if (json.has(name)) {
            this.value = json.get(name).getAsString();
        }
    }

    public String getHideMask() {
        return this.hideMask;
    }

    public TextSetting setHideMask(String hideMask) {
        this.hideMask = hideMask;
        return this;
    }
}

