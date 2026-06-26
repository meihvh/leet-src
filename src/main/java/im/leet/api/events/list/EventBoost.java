/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;

public class EventBoost
extends Event {
    private static EventBoost instance = new EventBoost();
    public double speedx;
    public double speedy;
    public double speedz;
    public double a;
    public double b;
    public float yaw;
    public float pitch;

    public static EventBoost build(float yaw, float pitch) {
        EventBoost.instance.speedx = 1.5;
        EventBoost.instance.speedy = 1.5;
        EventBoost.instance.speedz = 1.5;
        EventBoost.instance.a = 0.1;
        EventBoost.instance.b = 0.5;
        EventBoost.instance.yaw = yaw;
        EventBoost.instance.pitch = pitch;
        return instance;
    }
}

