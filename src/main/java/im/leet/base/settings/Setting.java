/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings;

import com.google.gson.JsonObject;
import im.leet.MinecraftHolder;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.group.Group;
import im.leet.utils.MultiBoolSupplier;
import im.leet.utils.lang.LangUtility;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Setting<T>
implements MinecraftHolder {
    protected T value;
    public String name;
    protected final T defaultValue;
    public Group base;
    public String _desc = "";
    protected Supplier<Boolean> visible = () -> true;
    protected Consumer<T> onChanged = ignored -> {};

    public String getDesc() {
        return LangUtility.get(this.getLangKey() + "._description", "");
    }

    public String getName() {
        return LangUtility.get(this.getLangKey() + "._name", this.name);
    }

    public String getLangKey() {
        if (this.base == null) {
            return this.name;
        }
        return this.base.getLangKey() + "." + this.name;
    }

    protected Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.onChange();
    }

    protected void onChange() {
        this.onChanged.accept(this.value);
    }

    public void resetSetting() {
    }

    public abstract SettingRenderer<?> wrap();

    public abstract void save(JsonObject var1);

    public abstract void load(JsonObject var1);

    public Supplier<Boolean> getVisible() {
        return this.visible;
    }

    public static abstract class Basic<T, Self extends Basic<T, Self>>
    extends Setting<T> {
        protected Basic(String name, T defaultValue) {
            super(name, defaultValue);
        }

        protected Self self() {
            return (Self)this;
        }

        @Deprecated
        public Self desc(String desc) {
            this._desc = desc;
            return this.self();
        }

        @SafeVarargs
        public final Self visible(Supplier<Boolean> ... visible) {
            this.visible = new MultiBoolSupplier(visible);
            return this.self();
        }

        public Self onChanged(Consumer<T> onChanged) {
            this.onChanged = onChanged;
            return this.self();
        }
    }
}

