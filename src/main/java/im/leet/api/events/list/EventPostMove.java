/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventPostMove
extends Event {
    private static EventPostMove instance = new EventPostMove();

    public static EventPostMove build() {
        return instance;
    }
}

