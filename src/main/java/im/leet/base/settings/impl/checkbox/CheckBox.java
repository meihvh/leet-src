/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.checkbox;

import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.checkbox.CheckBoxRenderer;

public class CheckBox
extends Setting.Basic<Boolean, CheckBox> {
    public CheckBox(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    public boolean get() {
        return (Boolean)this.value;
    }

    public CheckBox set(boolean value) {
        this.value = value;
        this.onChange();
        return this;
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new CheckBoxRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":checkbox");
        json.addProperty(name, (Boolean)this.value);
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":checkbox");
        if (json.has(name)) {
            this.value = json.get(name).getAsBoolean();
        }
    }
}

