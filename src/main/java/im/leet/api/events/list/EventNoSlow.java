/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventNoSlow
extends Event {
    private static final EventNoSlow instance = new EventNoSlow();

    public static EventNoSlow build() {
        instance.reset();
        return instance;
    }
}

