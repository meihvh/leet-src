/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventSetRotation
extends Event {
    public static final EventSetRotation instance = new EventSetRotation();
    public float yaw;
    public float pitch;

    public static EventSetRotation build(float yaw, float pitch) {
        EventSetRotation.instance.yaw = yaw;
        EventSetRotation.instance.pitch = pitch;
        instance.reset();
        return instance;
    }
}

