/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.attack;

import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.aura.attack.AttackHandler;

public final class CalcFallDist
implements MinecraftHolder {
    public static double convenientFallOffset() {
        double fallOffset = CalcFallDist.mc.field_1724.field_6017;
        if (CalcFallDist.mc.field_1687 != null && !CalcFallDist.mc.field_1724.method_24828() && CalcFallDist.mc.field_1724.method_18798().field_1351 < -0.0784000015258789 && CalcFallDist.mc.field_1687.method_8320(CalcFallDist.mc.field_1724.method_24515()).method_26227().method_15769() && !CalcFallDist.mc.field_1687.method_8320(CalcFallDist.mc.field_1724.method_24515().method_10084()).method_26227().method_15769() && CalcFallDist.mc.field_1724.field_6017 < -CalcFallDist.mc.field_1724.method_18798().field_1351 && AttackHandler.ticksOnBlock > 6) {
            fallOffset = -CalcFallDist.mc.field_1724.method_18798().field_1351;
        }
        return fallOffset;
    }

    private CalcFallDist() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

