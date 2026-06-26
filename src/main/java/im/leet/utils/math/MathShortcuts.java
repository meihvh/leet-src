/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.math;

public final class MathShortcuts {
    public static final double PI = Math.PI;
    public static final float FPI = (float)Math.PI;
    public static final double DtoRadians = Math.PI / 180;
    public static final float FtoRadians = (float)Math.PI / 180;

    public static double dcos(double value) {
        return Math.cos(value);
    }

    public static float cos(float value) {
        return (float)MathShortcuts.dcos(value);
    }

    public static double dsin(double value) {
        return Math.sin(value);
    }

    public static float sin(float value) {
        return (float)MathShortcuts.dsin(value);
    }

    public static double datan2(double y, double x) {
        return Math.atan2(y, x);
    }

    public static float atan2(float y, float x) {
        return (float)MathShortcuts.datan2(y, x);
    }

    private MathShortcuts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

