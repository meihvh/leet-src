/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.scripts.api.bindings.support;

public class TimerScripted {
    private long lastTime = 0L;

    public void reset() {
        this.lastTime = System.currentTimeMillis();
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastTime;
    }

    public boolean reached(double time, boolean reset) {
        boolean elapsed;
        boolean bl = elapsed = (double)(System.currentTimeMillis() - this.lastTime) > time;
        if (elapsed && reset) {
            this.reset();
        }
        return elapsed;
    }
}

