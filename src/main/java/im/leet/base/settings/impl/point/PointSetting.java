/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.joml.Vector2f
 */
package im.leet.base.settings.impl.point;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.point.PointRenderer;
import org.joml.Vector2f;

public class PointSetting
extends Setting.Basic<Vector2f, PointSetting> {
    public PointSetting(String name, Vector2f defaultValue) {
        super(name, defaultValue);
    }

    public PointSetting set(Vector2f vec) {
        this.value = vec;
        this.onChange();
        return this;
    }

    public Vector2f get() {
        return (Vector2f)this.value;
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new PointRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":point");
        JsonObject obj = new JsonObject();
        obj.addProperty("x", (Number)Float.valueOf(((Vector2f)this.value).x));
        obj.addProperty("y", (Number)Float.valueOf(((Vector2f)this.value).y));
        json.add(name, (JsonElement)obj);
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":point");
        if (json.has(name)) {
            JsonObject obj = json.getAsJsonObject(name);
            this.value = new Vector2f(obj.get("x").getAsFloat(), obj.get("y").getAsFloat());
        }
    }
}

