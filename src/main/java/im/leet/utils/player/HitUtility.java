/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.player;

import im.leet.MinecraftHolder;
import im.leet.mixin.accessor.ILivingEntity;

public class HitUtility
implements MinecraftHolder {
    public static boolean isPerfect() {
        return HitUtility.mc.field_1724.method_7261(1.5f) >= (HitUtility.mc.field_1724.method_24828() || HitUtility.mc.field_1724.field_6017 == 0.0 ? 0.99f : 0.9f) && ((ILivingEntity)HitUtility.mc.field_1724).client$lastAttackedTicks() > 9;
    }
}

