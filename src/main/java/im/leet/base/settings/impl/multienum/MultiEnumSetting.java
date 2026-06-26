/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.multienum;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.multienum.MultiEnumRenderer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MultiEnumSetting<E extends Enum<E>>
extends Setting.Basic<SortedSet<E>, MultiEnumSetting<E>> {
    private final HashMap<String, E> constants = new HashMap();
    private final ArrayList<E> enumEntries = new ArrayList();
    private MultiEnumCallback<E> callback;

    public MultiEnumSetting(String name, Class<E> clazz) {
        super(name, new TreeSet<Enum>(Comparator.comparingInt(Enum::ordinal)));
        this.callback = (a, b) -> {};
        this.initConstants(clazz);
    }

    @SafeVarargs
    public MultiEnumSetting(String name, E ... defaults) {
        super(name, new TreeSet<Enum>(Comparator.comparingInt(Enum::ordinal)));
        this.callback = (a, b) -> {};
        this.initConstants(((Enum)defaults[0]).getDeclaringClass());
        ((SortedSet)this.value).addAll(List.of(defaults));
    }

    private void initConstants(Class<E> clazz) {
        for (Enum c : (Enum[])clazz.getEnumConstants()) {
            this.constants.put(c.name().toLowerCase(Locale.ROOT), c);
            if (c instanceof EnumChoice) {
                EnumChoice enumChoice = (EnumChoice)((Object)c);
                this.constants.put(enumChoice.getRenderName().toLowerCase(Locale.ROOT), c);
                if (enumChoice.isDefaultEnabled()) {
                    ((SortedSet)this.value).add(c);
                }
            }
            this.enumEntries.add(c);
        }
    }

    public MultiEnumSetting<E> onChange(MultiEnumCallback<E> callback) {
        this.callback = callback;
        return this;
    }

    public Set<E> get() {
        return (Set)this.value;
    }

    public boolean isEmpty() {
        return ((SortedSet)this.value).isEmpty();
    }

    public void toggle(E entry) {
        if (!((SortedSet)this.value).remove(entry)) {
            this.callback.callback(entry, true);
            ((SortedSet)this.value).add(entry);
        } else {
            this.callback.callback(entry, false);
        }
    }

    public void toggle(int entry) {
        this.toggle((Enum)this.enumEntries.get(entry));
    }

    public boolean get(E entry) {
        return ((SortedSet)this.value).contains(entry);
    }

    @SafeVarargs
    public final MultiEnumSetting<E> select(E ... entries) {
        ((SortedSet)this.value).addAll(Arrays.asList(entries));
        return this;
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new MultiEnumRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":multi");
        JsonArray array = new JsonArray();
        for (Enum value : (SortedSet)this.value) {
            array.add(value.name());
        }
        json.add(name, (JsonElement)array);
    }

    @Override
    public void load(JsonObject json) {
        String name1 = this.name.concat(":multi");
        if (json.has(name1)) {
            HashSet<String> values = new HashSet<String>();
            JsonArray array = json.get(name1).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                String name = jsonElement.getAsString();
                values.add(name);
            }
            ((SortedSet)this.value).clear();
            for (String string : values) {
                ((SortedSet)this.value).add((Enum)this.constants.get(string.toLowerCase(Locale.ROOT)));
            }
        }
    }

    public ArrayList<E> getEnumEntries() {
        return this.enumEntries;
    }

    public MultiEnumCallback<E> getCallback() {
        return this.callback;
    }

    public static interface MultiEnumCallback<E extends Enum<E>> {
        public void callback(E var1, boolean var2);
    }
}

