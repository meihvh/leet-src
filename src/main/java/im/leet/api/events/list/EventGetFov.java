/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventGetFov
extends Event {
    public float fov;

    public EventGetFov(float fov) {
        this.fov = fov;
    }
}

