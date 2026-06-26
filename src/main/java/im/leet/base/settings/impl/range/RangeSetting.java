/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.range;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.range.FloatRange;
import im.leet.base.settings.impl.range.RangeRenderer;
import im.leet.utils.math.MathUtility;

public class RangeSetting
extends Setting.Basic<FloatRange, RangeSetting> {
    public final float minimumValue;
    public final float maximumValue;
    public final float increment;

    public RangeSetting(String name, float defaultMin, float defaultMax, float min, float max, float increment) {
        super(name, new FloatRange(defaultMin, defaultMax));
        this.minimumValue = min;
        this.maximumValue = max;
        this.increment = increment;
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new RangeRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        JsonArray j = new JsonArray();
        json.add(this.name, (JsonElement)j);
        j.add((Number)Float.valueOf(this.getMin()));
        j.add((Number)Float.valueOf(this.getMax()));
    }

    @Override
    public void load(JsonObject json) {
        if (json.has(this.name) && json.get(this.name).isJsonArray()) {
            JsonArray j = json.getAsJsonArray(this.name);
            this.set(j.get(0).getAsFloat(), j.get(1).getAsFloat());
        }
    }

    public float getMin() {
        return ((FloatRange)this.value).min;
    }

    public float getMax() {
        return ((FloatRange)this.value).max;
    }

    public FloatRange get() {
        return (FloatRange)this.value;
    }

    public void set(FloatRange range) {
        this.value = range;
    }

    public void set(float min, float max) {
        ((FloatRange)this.value).set(min, max);
    }

    public void setMin(float min) {
        this.set(min, this.getMax());
    }

    public void setMax(float max) {
        this.set(this.getMin(), max);
    }

    public float random() {
        return MathUtility.random(((FloatRange)this.value).min, ((FloatRange)this.value).max);
    }
}

