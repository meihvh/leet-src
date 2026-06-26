/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.utils.animations;

import im.leet.utils.math.MathUtility;
import im.leet.utils.math.easings.Easing;
import net.minecraft.util.math.MathHelper;

public class TweenAnimation {
    public long startNanos = 0L;
    public long durationNanos;
    public long delayNanos;
    public float from;
    public float to;
    public Easing easing;
    public static final long MillisToNanos = 1000000L;

    public TweenAnimation(long durationNanos, long delayNanos, float from, float to, Easing easing) {
        this.durationNanos = durationNanos;
        this.delayNanos = delayNanos;
        this.from = from;
        this.to = to;
        this.easing = easing;
    }

    public float get() {
        long nanos = System.nanoTime();
        if ((nanos = MathHelper.method_53062((long)(nanos - this.startNanos - this.delayNanos), (long)0L, (long)this.durationNanos)) > this.durationNanos) {
            return this.to;
        }
        float frac = this.easing.easef((float)nanos / (float)this.durationNanos);
        return MathUtility.linear(this.from, this.to, frac);
    }

    public void reset() {
        this.startNanos = System.nanoTime();
    }

    public float get(boolean reset) {
        float value = this.get();
        if (reset && value == this.to) {
            this.reset();
        }
        return value;
    }

    public static TweenAnimation millis(long duration, long delay, float from, float to, Easing easing) {
        return new TweenAnimation(duration * 1000000L, delay * 1000000L, from, to, easing);
    }
}

