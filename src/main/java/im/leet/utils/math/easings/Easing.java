/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.math.easings;

@FunctionalInterface
public interface Easing {
    public double ease(double var1);

    default public float easef(float value) {
        return (float)this.ease(value);
    }
}

