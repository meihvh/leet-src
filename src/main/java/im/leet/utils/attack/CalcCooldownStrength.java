/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.attack;

import im.leet.MinecraftHolder;
import im.leet.mixin.accessor.ILivingEntity;

public class CalcCooldownStrength
implements MinecraftHolder {
    public static boolean getAttackCooldownStrength(MODE mode, boolean old) {
        float tick;
        float base = old ? 0.5f : 1.5f;
        float f = tick = old ? 1.0f : 9.0f;
        if (mode.equals((Object)MODE.ELYTRA)) {
            return CalcCooldownStrength.getAttackCooldownStrength(MODE.ELYTRA, base, old ? 0.3f : 0.99f, (int)tick);
        }
        if (mode.equals((Object)MODE.DEFAULT)) {
            return CalcCooldownStrength.getAttackCooldownStrength(MODE.DEFAULT, base, old ? 0.3f : 0.94f, (int)tick);
        }
        return true;
    }

    public static boolean getAttackCooldownStrength(MODE mode, float baseTick, float minCD, int ticks) {
        if (mode.equals((Object)MODE.ELYTRA) || mode.equals((Object)MODE.DEFAULT)) {
            return CalcCooldownStrength.getAttackCooldownStrength(baseTick, minCD, ticks);
        }
        return true;
    }

    public static boolean getAttackCooldownStrength(float baseTick, float minCD, int ticks) {
        return CalcCooldownStrength.mc.field_1724.method_7261(baseTick) >= minCD && ((ILivingEntity)CalcCooldownStrength.mc.field_1724).client$lastAttackedTicks() > ticks;
    }

    public static enum MODE {
        ELYTRA,
        DEFAULT;

    }
}

