/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.api.sound;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class SoundHelper {
    private static final double TO_DEGREES = 57.29577951308232;

    public static Vec2f calculate(Vec3d from, Vec3d to) {
        Vec3d diff = to.method_1020(from);
        double distance = Math.hypot(diff.field_1352, diff.field_1350);
        float yaw = (float)(MathHelper.method_15349((double)diff.field_1350, (double)diff.field_1352) * 57.29577951308232) - 90.0f;
        float pitch = (float)(-(MathHelper.method_15349((double)diff.field_1351, (double)distance) * 57.29577951308232));
        return new Vec2f(yaw, pitch);
    }

    public static float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360.0f;
        return f > 180.0f ? 360.0f - f : f;
    }

    public static double easeInOutExpo(double x) {
        if (x == 0.0 || x == 1.0) {
            return x;
        }
        return x < 0.5 ? Math.pow(2.0, 20.0 * x - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * x + 10.0)) / 2.0;
    }
}

