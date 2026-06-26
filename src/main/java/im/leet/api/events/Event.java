/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events;

public abstract class Event {
    boolean cancelled = false;

    public void cancel() {
        this.cancelled = true;
    }

    public void reset() {
        this.cancelled = false;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}

