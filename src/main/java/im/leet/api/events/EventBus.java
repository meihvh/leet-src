/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events;

import im.leet.api.events.Event;

@FunctionalInterface
public interface EventBus<T extends Event> {
    public void onEvent(T var1);
}

