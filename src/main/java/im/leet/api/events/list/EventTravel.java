/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventTravel
extends Event {
    private static EventTravel instance = new EventTravel();

    public static EventTravel build() {
        instance.reset();
        return instance;
    }
}

