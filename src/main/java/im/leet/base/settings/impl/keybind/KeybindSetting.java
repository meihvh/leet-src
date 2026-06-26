/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.keybind;

import com.google.gson.JsonObject;
import im.leet.api.events.list.EventKey;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.keybind.KeybindRenderer;

public class KeybindSetting
extends Setting.Basic<Integer, KeybindSetting> {
    public KeybindSetting(String name, int defaultValue) {
        super(name, defaultValue);
    }

    public KeybindSetting bind(int key) {
        this.value = key;
        this.onChange();
        return this;
    }

    public int getBind() {
        return (Integer)this.value;
    }

    public boolean matches(EventKey e) {
        return e.key == (Integer)this.value;
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new KeybindRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":keybind");
        json.addProperty(name, (Number)this.value);
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":keybind");
        if (json.has(name)) {
            this.value = json.get(name).getAsInt();
        }
    }
}

