/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventBlockPush
extends Event {
    private static EventBlockPush instance = new EventBlockPush();
    double x;
    double z;

    public static EventBlockPush build(double x, double z) {
        EventBlockPush.instance.x = x;
        EventBlockPush.instance.z = z;
        instance.reset();
        return instance;
    }
}

