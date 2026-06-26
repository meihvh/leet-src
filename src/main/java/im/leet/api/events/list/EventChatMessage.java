/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventChatMessage
extends Event {
    public static final EventChatMessage instance = new EventChatMessage();
    public String message;

    public static EventChatMessage build(String message) {
        EventChatMessage.instance.message = message;
        instance.reset();
        return instance;
    }
}

