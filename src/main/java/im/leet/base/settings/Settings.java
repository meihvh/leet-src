/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  org.jetbrains.annotations.NotNull
 */
package im.leet.base.settings;

import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.group.Group;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class Settings
implements Iterable<Setting<?>> {
    private final List<Setting<?>> settings = new ObjectArrayList();
    private final List<SettingRenderer<?>> settingRenderers = new ObjectArrayList();

    public <S extends Setting<?>> S add(S setting) {
        this.settings.add(setting);
        this.settingRenderers.add(setting.wrap());
        return setting;
    }

    public void addAll(Setting<?> ... settings) {
        this.settings.addAll(Arrays.asList(settings));
        for (Setting<?> s : settings) {
            this.settingRenderers.add(s.wrap());
        }
    }

    public boolean isEmpty() {
        return this.settings.isEmpty();
    }

    public void addAll(Collection<Setting<?>> settings) {
        this.settings.addAll(settings);
        for (Setting<?> s : settings) {
            this.settingRenderers.add(s.wrap());
        }
    }

    public Group group(String name) {
        return this.add(new Group(name));
    }

    @Override
    @NotNull
    public Iterator<Setting<?>> iterator() {
        return this.settings.iterator();
    }

    public void save(JsonObject json) {
        for (Setting<?> setting : this.settings) {
            setting.save(json);
        }
    }

    public void load(JsonObject json) {
        for (Setting<?> setting : this.settings) {
            setting.load(json);
        }
    }

    public List<Setting<?>> getSettings() {
        return this.settings;
    }

    public List<SettingRenderer<?>> getSettingRenderers() {
        return this.settingRenderers;
    }
}

