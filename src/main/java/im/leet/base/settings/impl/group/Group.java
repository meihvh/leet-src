/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.item.Item
 *  net.minecraft.registry.Registries
 *  org.joml.Vector2f
 */
package im.leet.base.settings.impl.group;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.binder.BinderSetting;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.checkbox.CheckBoxRenderer;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;
import im.leet.base.settings.impl.choice.MultiChoice;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.GroupRenderer;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.multitext.MultiTextSetting;
import im.leet.base.settings.impl.point.PointSetting;
import im.leet.base.settings.impl.range.RangeSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.base.settings.impl.text.TextSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.joml.Vector2f;

public class Group
extends Setting.Basic<List<Setting<?>>, Group> {
    private final List<SettingRenderer<?>> settingRenderers = new ArrayList();
    public CheckBox enabled;
    CheckBoxRenderer enabledRenderer;
    private boolean expanded = true;
    public SmoothStepAnimation expandAnim = new SmoothStepAnimation(150, 1.0);
    static final String EXPANDED = "_expanded";

    public Group(String name) {
        super(name, new ArrayList());
    }

    protected Group(String name, boolean defaultToggle) {
        super(name, new ArrayList());
        this.toggleable(defaultToggle);
    }

    public Group(String name, List<Setting<?>> defaultValue) {
        super(name, new ArrayList(defaultValue));
    }

    public Group expanded(boolean expanded) {
        this.expanded = expanded;
        this.expandAnim.setDirection(expanded ? Direction.FORWARDS : Direction.BACKWARDS);
        return this;
    }

    public boolean isExpandable() {
        return !this.isEmpty();
    }

    public Group toggleable(boolean enabled) {
        if (this.enabled == null) {
            this.enabled = this.checkbox("Enabled", enabled);
            this.enabledRenderer = new CheckBoxRenderer(this.enabled);
        }
        return this;
    }

    public CheckBox checkbox(String name, boolean defaultValue) {
        return this.add(new CheckBox(name, defaultValue));
    }

    public TextSetting text(String name, String text) {
        return this.add(new TextSetting(name, text));
    }

    public <S extends Setting<?>> S add(S setting) {
        if (this.value == null) {
            this.value = new ArrayList();
        }
        setting.base = this;
        ((List)this.value).add(setting);
        this.settingRenderers.add(setting.wrap());
        return setting;
    }

    @Override
    public void resetSetting() {
        for (Setting setting : (List)this.value) {
            setting.resetSetting();
        }
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new GroupRenderer(this);
    }

    public void addAll(Setting<?> ... settings) {
        if (this.value == null) {
            this.value = new ArrayList();
        }
        ((List)this.value).addAll(List.of(settings));
        for (Setting<?> setting : settings) {
            this.settingRenderers.add(setting.wrap());
        }
    }

    @Override
    public void save(JsonObject json) {
        String name = this.name.concat(":group");
        JsonObject groupJson = new JsonObject();
        json.add(name, (JsonElement)groupJson);
        groupJson.addProperty(EXPANDED, Boolean.valueOf(this.expanded));
        for (Setting setting : (List)this.value) {
            setting.save(groupJson);
        }
    }

    @Override
    public void load(JsonObject json) {
        String name = this.name.concat(":group");
        if (json.has(name) && json.get(name).isJsonObject()) {
            JsonObject groupJson = json.getAsJsonObject(name);
            if (groupJson.has(EXPANDED) && groupJson.get(EXPANDED).isJsonPrimitive()) {
                this.expanded(groupJson.get(EXPANDED).getAsBoolean());
            }
            for (Setting setting : (List)this.value) {
                setting.load(groupJson);
            }
        }
    }

    public final List<Setting<?>> getSettings() {
        return (List)this.value;
    }

    public boolean isEnabled() {
        return this.enabled == null || this.enabled.get();
    }

    public boolean isEmpty() {
        return ((List)this.value).isEmpty();
    }

    public Group group(String name) {
        return this.add(new Group(name));
    }

    @SafeVarargs
    public final <C extends Choice> MultiChoice<C> multiChoice(C ... choices) {
        this.addAll((Setting<?>[])choices);
        for (C choice : choices) {
            ((Choice)choice).base = this;
            ((Group)choice).toggleable(false);
        }
        return new MultiChoice(choices);
    }

    public SliderSetting sliderSetting(String name, float defaultValue, float min, float max) {
        return this.add(new SliderSetting(name, defaultValue, min, max));
    }

    public <E extends Enum<E>> EnumSetting<E> enumSetting(String name, E defaultValue) {
        return this.add(new EnumSetting<E>(name, defaultValue));
    }

    @SafeVarargs
    public final <C extends Choice> ChoiceSetting<C> choiceSetting(String name, int defaultValue, C ... choices) {
        return this.add(new ChoiceSetting(name, defaultValue, choices));
    }

    @SafeVarargs
    public final <E extends Enum<E>> MultiEnumSetting<E> multiEnumSetting(String name, E ... defaults) {
        return this.add(new MultiEnumSetting(name, defaults));
    }

    public <E extends Enum<E>> MultiEnumSetting<E> multiEnumSetting(String name, Class<E> clazz) {
        return this.add(new MultiEnumSetting<E>(name, clazz));
    }

    public MultiTextSetting multiTextSetting(String name, String ... defaults) {
        return this.add(new MultiTextSetting(name, defaults));
    }

    public KeybindSetting keybindSetting(String name, int key) {
        return this.add(new KeybindSetting(name, key));
    }

    public ColorSetting colorSetting(String name, Color color) {
        return this.add(new ColorSetting(name, color));
    }

    public BinderSetting binderSetting(String name, Map<Item, Integer> defaultValue) {
        return this.add(new BinderSetting(name, defaultValue.entrySet().stream().map(entry -> Map.entry(Registries.field_41178.method_10221((Object)((Item)entry.getKey())), (Integer)entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
    }

    public PointSetting pointSetting(String name, Vector2f defaultValue) {
        return this.add(new PointSetting(name, defaultValue));
    }

    public RangeSetting rangeSetting(String name, float defaultMin, float defaultMax, float min, float max, float increment) {
        return this.add(new RangeSetting(name, defaultMin, defaultMax, min, max, increment));
    }

    public List<SettingRenderer<?>> getSettingRenderers() {
        return this.settingRenderers;
    }

    public boolean isExpanded() {
        return this.expanded;
    }
}

