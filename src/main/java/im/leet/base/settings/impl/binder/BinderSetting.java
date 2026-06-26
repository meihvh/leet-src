/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.item.Item
 *  net.minecraft.registry.Registries
 *  net.minecraft.util.Identifier
 */
package im.leet.base.settings.impl.binder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.binder.BinderRenderer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class BinderSetting
extends Setting.Basic<Map<Identifier, Integer>, BinderSetting> {
    public BinderSetting(String name, Map<Identifier, Integer> defaultValue) {
        super(name, new HashMap<Identifier, Integer>(defaultValue));
    }

    public BinderSetting bind(Item item, int value) {
        return this.bind(Registries.field_41178.method_10221((Object)item), value);
    }

    public BinderSetting bind(Identifier id, int value) {
        ((Map)this.value).put(id, value);
        this.onChange();
        return this;
    }

    public int get(Item item) {
        return this.get(Registries.field_41178.method_10221((Object)item));
    }

    public int get(Identifier id) {
        return ((Map)this.value).getOrDefault(id, -2);
    }

    public Set<Map.Entry<Identifier, Integer>> getEntry() {
        return ((Map)this.value).entrySet();
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new BinderRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        JsonObject j = new JsonObject();
        String name = this.name.concat(":binder");
        json.add(name, (JsonElement)j);
        for (Map.Entry entry : ((Map)this.value).entrySet()) {
            j.addProperty(((Identifier)entry.getKey()).toString(), (Number)entry.getValue());
        }
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":binder");
        if (json.has(name)) {
            JsonObject j = json.getAsJsonObject(name);
            for (Map.Entry entry : j.entrySet()) {
                try {
                    ((Map)this.value).put(Identifier.method_12829((String)((String)entry.getKey())), ((JsonElement)entry.getValue()).getAsInt());
                }
                catch (Exception exception) {}
            }
        }
    }
}

