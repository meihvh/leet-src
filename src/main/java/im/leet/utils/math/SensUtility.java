/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 */
package im.leet.utils.math;

import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.base.rotations.Angle;
import im.leet.utils.math.MathUtility;
import net.minecraft.client.MinecraftClient;

public final class SensUtility
implements MinecraftHolder {
    public static float fixFPSrotation(float rot, float oldRot) {
        return SensUtility.correctRotation(MathUtility.linear(rot, oldRot, (float)(360.0 / (double)Math.max(MinecraftClient.method_1551().method_47599(), 5))));
    }

    public static double getSensitivity(double rot) {
        double d2 = SensUtility.getGCD() * (double)0.6f + (double)0.2f;
        double d3 = d2 * d2 * d2;
        double d4 = d3 * 8.0;
        return SensUtility.getDeltaMouse(rot) * SensUtility.getGCDValue();
    }

    public static float correctRotation(float rot) {
        float gcd = (float)SensUtility.getGCDValue();
        return (float)Math.round(rot / gcd) * gcd;
    }

    public static double getGCD() {
        if (AuraModule.INSTANCE.isEnabled() && AuraModule.INSTANCE.sensitivityBypass.get()) {
            return 0.012975693;
        }
        return (Double)SensUtility.mc.field_1690.method_42495().method_41753();
    }

    public static double getGCDValue() {
        double d4 = SensUtility.getGCD() * 0.6 + 0.2;
        return d4 * d4 * d4 * 8.0 * 0.15;
    }

    public static double getDeltaMouse(double delta) {
        return Math.ceil(delta / SensUtility.getGCDValue());
    }

    public static Angle applySensitivityPatch(Angle current, Angle previousRotation) {
        double sens = SensUtility.getGCD();
        double d2 = sens * (double)0.6f + (double)0.2f;
        double gcd = d2 * d2 * d2 * 8.0;
        double prevYaw = previousRotation.getYaw();
        double prevPitch = previousRotation.getYaw();
        double currentYaw = current.getYaw();
        double currentPitch = current.getPitch();
        double yaw = Math.ceil((currentYaw - prevYaw) / gcd / (double)0.15f) * gcd * (double)0.15f;
        double pitch = Math.ceil((currentPitch - prevPitch) / gcd / (double)0.15f) * gcd * (double)0.15f;
        return new Angle((float)(prevYaw + yaw), (float)(prevPitch + pitch));
    }

    private SensUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

