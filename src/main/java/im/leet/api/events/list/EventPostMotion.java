/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventPostMotion
extends Event {
    private static final EventPostMotion instance = new EventPostMotion();

    public static EventPostMotion build() {
        return instance;
    }
}

