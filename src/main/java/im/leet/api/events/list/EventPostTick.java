/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventPostTick
extends Event {
    private static final EventPostTick instance = new EventPostTick();

    public static EventPostTick build() {
        return instance;
    }
}

