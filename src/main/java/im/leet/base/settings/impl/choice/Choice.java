/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.choice;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.settings.impl.group.Group;

public abstract class Choice
extends Group
implements EventBus<Event> {
    protected Choice(String name) {
        super(name);
    }

    public void onEnabled() {
    }

    public void onDisabled() {
    }

    @Override
    public void onEvent(Event event) {
    }
}

