/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.settings.impl.range;

import net.minecraft.util.math.MathHelper;

public final class FloatRange {
    public float min;
    public float max;

    public void set(float min, float max) {
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    public float clamp(float value) {
        return MathHelper.method_15363((float)value, (float)this.min, (float)this.max);
    }

    public String toString() {
        return "FloatRange(min=" + this.min + ", max=" + this.max + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FloatRange)) {
            return false;
        }
        FloatRange other = (FloatRange)o;
        if (Float.compare(this.min, other.min) != 0) {
            return false;
        }
        return Float.compare(this.max, other.max) == 0;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Float.floatToIntBits(this.min);
        result = result * 59 + Float.floatToIntBits(this.max);
        return result;
    }

    public FloatRange(float min, float max) {
        this.min = min;
        this.max = max;
    }
}

