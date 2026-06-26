/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.math;

public class TickUtility {
    private int tick = 0;

    public void reset() {
        this.tick = 0;
    }

    public boolean reached(int ticks) {
        return this.tick > ticks;
    }

    public boolean reached(int ticks, boolean reset) {
        boolean elapsed = this.reached(ticks);
        if (elapsed && reset) {
            this.reset();
        }
        return elapsed;
    }

    public void tick() {
        ++this.tick;
    }

    public int getTick() {
        return this.tick;
    }
}

