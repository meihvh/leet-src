/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.multitext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.multitext.MultiTextRenderer;
import java.util.ArrayList;
import java.util.List;

public class MultiTextSetting
extends Setting.Basic<List<String>, MultiTextSetting> {
    public MultiTextSetting(String name, String ... defaults) {
        super(name, new ArrayList<String>(List.of(defaults)));
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new MultiTextRenderer(this);
    }

    public List<String> get() {
        return (List)this.value;
    }

    public void add() {
        ((List)this.value).add("");
    }

    public void remove(int index) {
        if (index < ((List)this.value).size()) {
            ((List)this.value).remove(index);
        }
    }

    @Override
    public void save(JsonObject json) {
        JsonArray array = new JsonArray();
        json.add(this.name, (JsonElement)array);
        for (String element : (List)this.value) {
            array.add(element);
        }
    }

    @Override
    public void load(JsonObject json) {
        Object object;
        if (json.has(this.name) && (object = json.get(this.name)) instanceof JsonArray) {
            JsonArray array = (JsonArray)object;
            for (JsonElement element : array) {
                ((List)this.value).add(element.getAsString());
            }
        }
    }
}

