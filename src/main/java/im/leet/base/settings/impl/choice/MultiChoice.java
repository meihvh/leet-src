/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.choice;

import im.leet.api.events.Event;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.group.Group;
import java.util.function.BiFunction;

public class MultiChoice<C extends Choice> {
    final C[] choices;

    public MultiChoice(C[] choices) {
        this.choices = choices;
    }

    public C[] get() {
        return this.choices;
    }

    public void onEnabled() {
        for (C choice : this.choices) {
            if (!((Group)choice).isEnabled()) continue;
            ((Choice)choice).onEnabled();
        }
    }

    public void onDisabled() {
        for (C choice : this.choices) {
            if (!((Group)choice).isEnabled()) continue;
            ((Choice)choice).onDisabled();
        }
    }

    public void onEvent(Event event) {
        for (C choice : this.choices) {
            if (!((Group)choice).isEnabled()) continue;
            ((Choice)choice).onEvent(event);
        }
    }

    public <T> T reduce(T value, BiFunction<C, T, T> reducer) {
        for (C choice : this.choices) {
            if (!((Group)choice).isEnabled()) continue;
            value = reducer.apply(choice, value);
        }
        return value;
    }
}

