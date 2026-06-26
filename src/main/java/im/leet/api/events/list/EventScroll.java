/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventScroll
extends Event {
    public double horizontal;
    public double vertical;

    public EventScroll(double horizontal, double vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}

