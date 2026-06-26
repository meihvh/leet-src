/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Hand
 */
package im.leet.base.modules.impl.combat.elytraaura.attack;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.ElytraAura;
import im.leet.base.modules.impl.combat.elytraaura.math.ElytraAuraResolve;
import im.leet.mixin.accessor.ILivingEntity;
import im.leet.utils.attack.CalcCooldownStrength;
import im.leet.utils.math.RayTraceUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public final class ElytraAuraAttackHandler
implements MinecraftHolder {
    static ElytraAura elytraAura = ElytraAura.INSTANCE;

    public static void updateAttack(LivingEntity entity) {
        if (ElytraAuraAttackHandler.mc.field_1724 == null || ElytraAuraAttackHandler.mc.field_1687 == null || entity == null) {
            return;
        }
        if (ElytraAuraAttackHandler.mc.field_1724.method_19538().method_1022(ElytraAuraResolve.getPointOnTarget(entity)) <= (double)ElytraAuraAttackHandler.attackDistance(entity) && ((ILivingEntity)ElytraAuraAttackHandler.mc.field_1724).client$lastAttackedTicks() > 5 && CalcCooldownStrength.getAttackCooldownStrength(CalcCooldownStrength.MODE.ELYTRA, false)) {
            ElytraAuraAttackHandler.mc.field_1761.method_2918((PlayerEntity)ElytraAuraAttackHandler.mc.field_1724, (Entity)entity);
            ElytraAuraAttackHandler.mc.field_1724.method_6104(Hand.field_5808);
        }
    }

    public static float attackDistance(LivingEntity entity) {
        return entity.method_6128() ? Math.max(elytraAura.isLeave(entity) ? 4.5f : 3.2f, ElytraAuraAttackHandler.elytraAura.attackRangeSetting.get()) : 2.8f;
    }

    public static boolean raycast(LivingEntity entity) {
        return RayTraceUtility.rayTrace(Client.ROTATION.getRotate().toVector(), (double)ElytraAuraAttackHandler.attackDistance(entity), entity.method_5829());
    }

    private ElytraAuraAttackHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

