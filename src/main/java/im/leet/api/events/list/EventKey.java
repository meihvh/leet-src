/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventKey
extends Event {
    private static final EventKey instance = new EventKey();
    public int key;
    public int action;

    public static EventKey build(int key, int action) {
        EventKey.instance.key = key;
        EventKey.instance.action = action;
        instance.reset();
        return instance;
    }

    public int getKey() {
        return this.key;
    }

    public int getAction() {
        return this.action;
    }
}

