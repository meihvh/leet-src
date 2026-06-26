/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventGameTick
extends Event {
    private static final EventGameTick instance = new EventGameTick();

    public static EventGameTick build() {
        return instance;
    }
}

