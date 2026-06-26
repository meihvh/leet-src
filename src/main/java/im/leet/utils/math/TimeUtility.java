/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.math;

public class TimeUtility {
    private long lastTime = 0L;

    public void reset() {
        this.lastTime = System.currentTimeMillis();
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastTime;
    }

    public boolean reached(long time) {
        if (time == 0L) {
            return true;
        }
        return System.currentTimeMillis() - this.lastTime > time;
    }

    public boolean reached(long time, boolean reset) {
        boolean elapsed;
        boolean bl = elapsed = System.currentTimeMillis() - this.lastTime > time;
        if (elapsed && reset) {
            this.reset();
        }
        return elapsed;
    }

    public void setTime(long time) {
        this.lastTime = System.currentTimeMillis();
    }
}

