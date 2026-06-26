/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package im.leet.base.settings.impl.choice;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceRenderer;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import java.util.List;

public class ChoiceSetting<C extends Choice>
extends Setting.Basic<C, ChoiceSetting<C>>
implements EventBus<Event> {
    private final List<C> choices;
    private boolean expanded = true;
    public SmoothStepAnimation expandAnim = new SmoothStepAnimation(150, 1.0);
    private static final String ACTIVE = "_active";
    static final String EXPANDED = "_expanded";

    public ChoiceSetting<C> expanded(boolean expanded) {
        this.expanded = expanded;
        this.expandAnim.setDirection(expanded ? Direction.FORWARDS : Direction.BACKWARDS);
        return this;
    }

    @SafeVarargs
    public ChoiceSetting(String name, int defaultIndex, C ... choices) {
        super(name, choices[defaultIndex]);
        this.choices = List.of(choices);
    }

    @Override
    public void onEvent(Event event) {
        ((Choice)this.value).onEvent(event);
    }

    public void onEnabled() {
        ((Choice)this.value).onEnabled();
    }

    public void onDisabled() {
        ((Choice)this.value).onDisabled();
    }

    public C get() {
        return (C)((Choice)this.value);
    }

    public void select(int index) {
        this.select((Choice)this.choices.get(index));
    }

    public void select(C choice) {
        ((Choice)this.value).onDisabled();
        this.value = choice;
        ((Choice)this.value).onEnabled();
    }

    @Override
    public SettingRenderer<?> wrap() {
        return new ChoiceRenderer(this);
    }

    @Override
    public void save(JsonObject json) {
        JsonObject j = new JsonObject();
        json.add(this.name, (JsonElement)j);
        j.addProperty(ACTIVE, ((Choice)this.value).name);
        j.addProperty(EXPANDED, Boolean.valueOf(this.expanded));
        for (Choice choice : this.choices) {
            choice.save(j);
        }
    }

    @Override
    public void load(JsonObject json) {
        if (json.has(this.name) && json.get(this.name).isJsonObject()) {
            JsonObject j = json.getAsJsonObject(this.name);
            if (j.has(EXPANDED) && j.get(EXPANDED).isJsonPrimitive()) {
                this.expanded(j.get(EXPANDED).getAsBoolean());
            }
            String active = j.get(ACTIVE).getAsString();
            this.value = this.choices.stream().filter(choice -> choice.name.equalsIgnoreCase(active)).findAny().orElse((Choice)this.choices.getFirst());
            for (Choice choice2 : this.choices) {
                choice2.load(j);
            }
        }
    }

    public List<C> getChoices() {
        return this.choices;
    }

    public boolean isExpanded() {
        return this.expanded;
    }
}

