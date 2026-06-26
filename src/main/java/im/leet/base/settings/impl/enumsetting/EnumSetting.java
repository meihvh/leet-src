/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.enumsetting;

import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.enumsetting.EnumRenderer;
import java.util.function.Function;

public class EnumSetting<E extends Enum<E>>
extends Setting.Basic<E, EnumSetting<E>> {
    private Function<E, E> onChange = e -> e;

    public EnumSetting(String name, E value) {
        super(name, value);
        this.select(value);
    }

    public E get() {
        return (E)((Enum)this.value);
    }

    public EnumSetting<E> select(E value) {
        value = (Enum)this.onChange.apply(value);
        this.value = value;
        this.onChange();
        return this;
    }

    public EnumSetting<E> select(int ord) {
        Enum value = ((Enum[])((Enum)this.get()).getDeclaringClass().getEnumConstants())[ord];
        value = (Enum)this.onChange.apply(value);
        this.value = value;
        this.onChange();
        return this;
    }

    public boolean is(E is) {
        return this.value == is;
    }

    public boolean isAny(E ... is) {
        for (E iz : is) {
            if (iz != this.value) continue;
            return true;
        }
        return false;
    }

    public boolean is(int is) {
        return this.is(((Enum[])((Enum)this.value).getDeclaringClass().getEnumConstants())[is]);
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new EnumRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":enum");
        json.addProperty(name, ((Enum)this.value).name());
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":enum");
        if (json.has(name)) {
            String v = json.get(name).getAsString();
            for (Enum c : (Enum[])((Enum)this.value).getDeclaringClass().getEnumConstants()) {
                if (!c.name().equalsIgnoreCase(v)) continue;
                this.select(c);
            }
        }
    }

    public EnumSetting<E> onChange(Function<E, E> onChange) {
        this.onChange = onChange;
        return this;
    }
}

