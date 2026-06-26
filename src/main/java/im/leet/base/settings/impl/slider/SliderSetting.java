/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.settings.impl.slider;

import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.slider.SliderRenderer;
import java.util.function.Function;
import net.minecraft.util.math.MathHelper;

public class SliderSetting
extends Setting.Basic<Float, SliderSetting> {
    private float min = -999.0f;
    private float max = -999.0f;
    private float increment = 1.0f;
    private Function<Float, Float> onChange;

    public SliderSetting(String name, float defaultValue, float min, float max) {
        super(name, Float.valueOf(defaultValue));
        this.setValue(defaultValue);
        this.min = min;
        this.max = max;
    }

    public void setValue(float value) {
        if (this.onChange != null) {
            value = this.onChange.apply(Float.valueOf(value)).floatValue();
        }
        this.value = this.min == -999.0f && this.max == -999.0f ? Float.valueOf(value) : Float.valueOf(MathHelper.method_15363((float)value, (float)this.min, (float)this.max));
        this.onChange();
    }

    public SliderSetting increment(float increment) {
        this.increment = increment;
        return this;
    }

    public SliderSetting onChange(Function<Float, Float> onChange) {
        this.onChange = onChange;
        return this;
    }

    public SliderSetting set(float value) {
        this.setValue(value);
        return this;
    }

    public float get() {
        return ((Float)this.value).floatValue();
    }

    public int getInt() {
        return ((Float)this.value).intValue();
    }

    public long getLong() {
        return ((Float)this.value).longValue();
    }

    public float getSquared() {
        return ((Float)this.value).floatValue() * ((Float)this.value).floatValue();
    }

    public float[] getWithMax(SliderSetting max) {
        float vMin = Math.min(this.get(), max.get());
        float vMax = Math.max(this.get(), max.get());
        this.setValue(vMin);
        max.setValue(vMax);
        return new float[]{vMin, vMax};
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new SliderRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":slider");
        json.addProperty(name, (Number)this.value);
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":slider");
        if (json.has(name)) {
            this.setValue(json.get(name).getAsFloat());
        }
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float getIncrement() {
        return this.increment;
    }
}

