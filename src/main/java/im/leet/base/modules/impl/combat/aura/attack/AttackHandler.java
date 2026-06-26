/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.util.Hand
 *  org.joml.Vector3f
 */
package im.leet.base.modules.impl.combat.aura.attack;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.scripts.api.integrate.ScriptHook;
import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.utils.client.mixin.IEntity;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.joml.Vector3f;

public final class AttackHandler {
    public static int countHit;
    public static int randomNumber1;
    public static int randomNumber2;
    public static int ticksOnBlock;
    static TimeUtility time;
    static AuraModule aura;

    public static void attackEntity(LivingEntity target) {
        NetworkUtility.send(PlayerInteractEntityC2SPacket.method_34206((Entity)target, (boolean)MinecraftHolder.mc.field_1724.method_5715()));
        if (AttackHandler.aura.auraSetting.get(AuraModule.Feature.KeepSprint) && !MinecraftHolder.mc.field_1724.method_24828()) {
            if (AttackHandler.convenientFallOffset() > 0.0) {
                MinecraftHolder.mc.field_1687.method_43128(null, MinecraftHolder.mc.field_1724.method_23317(), MinecraftHolder.mc.field_1724.method_23318(), MinecraftHolder.mc.field_1724.method_23321(), SoundEvents.field_15016, MinecraftHolder.mc.field_1724.method_5634(), 1.0f, 1.0f);
                MinecraftHolder.mc.field_1724.method_7277((Entity)target);
            }
        } else {
            MinecraftHolder.mc.field_1724.method_7324((Entity)target);
        }
        MinecraftHolder.mc.field_1724.method_6104(Hand.field_5808);
        MinecraftHolder.mc.field_1724.method_7350();
        time.reset();
        ++countHit;
        randomNumber1 = MathUtility.random(2, 8);
        randomNumber2 = MathUtility.random(2, 8);
    }

    public static double convenientFallOffset() {
        double fallOffset = MinecraftHolder.mc.field_1724.field_6017;
        if (MinecraftHolder.mc.field_1687 != null && !MinecraftHolder.mc.field_1724.method_24828() && MinecraftHolder.mc.field_1724.method_18798().field_1351 < -0.0784000015258789 && MinecraftHolder.mc.field_1687.method_8320(MinecraftHolder.mc.field_1724.method_24515()).method_26227().method_15769() && !MinecraftHolder.mc.field_1687.method_8320(MinecraftHolder.mc.field_1724.method_24515().method_10084()).method_26227().method_15769() && MinecraftHolder.mc.field_1724.field_6017 < -MinecraftHolder.mc.field_1724.method_18798().field_1351 && ticksOnBlock > 6) {
            fallOffset = -MinecraftHolder.mc.field_1724.method_18798().field_1351;
        }
        return fallOffset;
    }

    public static boolean shouldResetSprinting() {
        return !MinecraftHolder.mc.field_1724.method_24828() && TargetsUtility.getTarget() != null && (AttackHandler.canAttackTick(1) || AttackHandler.shouldAttack());
    }

    public static boolean canAttackTick(int ticks) {
        boolean result;
        boolean flag1;
        if (TargetsUtility.getTarget() == null) {
            return false;
        }
        float f2 = MinecraftHolder.mc.field_1724.method_7261(0.5f + (float)ticks);
        if (MinecraftHolder.mc.field_1724.method_5765() && (double)f2 > 0.92) {
            return true;
        }
        boolean bl = flag1 = f2 > (MinecraftHolder.mc.field_1724.method_6047().method_7960() ? 0.99f : 0.94f) && AttackHandler.getFallDistance(1 + ticks) > 0.0f && !MinecraftHolder.mc.field_1724.method_24828() && !MinecraftHolder.mc.field_1724.method_6101() && !MinecraftHolder.mc.field_1724.method_5799();
        if (MinecraftHolder.mc.field_1724.method_5799() || MinecraftHolder.mc.field_1724.method_6101() || AttackHandler.aura.smartCrits.get() && MinecraftHolder.mc.field_1724.method_24828()) {
            return f2 > (MinecraftHolder.mc.field_1724.method_6047().method_7960() ? 0.99f : 0.94f);
        }
        boolean bl2 = result = flag1 && MinecraftHolder.mc.field_1724.method_30950((float)(1 + ticks)).method_18805(1.0, 0.0, 1.0).method_1022(TargetsUtility.getTarget().method_30950((float)(1 + ticks)).method_18805(1.0, 0.0, 1.0)) <= (double)AttackHandler.aura.attackRangeSetting.get();
        if (ticks > 0) {
            result |= AttackHandler.canAttackTick(ticks - 1);
        }
        return result;
    }

    public static boolean shouldAttack() {
        float getFalDist;
        Object o = Client.SCRIPTS.getScriptHook().solute(ScriptHook.HookValue.AURA_ATTACK, TargetsUtility.getTarget());
        if (o != null) {
            return (Boolean)o;
        }
        if (AuraModule.INSTANCE.resetSprint.is(AuraModule.ResetSprint.Legit) && MinecraftHolder.mc.field_1724.method_5624()) {
            return false;
        }
        if (RotationUtility.getStrictDistance(TargetsUtility.getTarget()) > (double)aura.attackRange()) {
            return false;
        }
        boolean crit = true;
        float f = AttackHandler.aura.randomFallDistanceSetting.get() ? (Math.random() > 0.75 ? MathUtility.random(0.1f, 0.4f) : MathUtility.random(0.0f, 0.1f)) : (getFalDist = 0.0f);
        if (AttackHandler.aura.checkCrit.get()) {
            boolean isLiquid;
            boolean fallDistance = AttackHandler.convenientFallOffset() > (double)getFalDist;
            boolean canCrit = (!MinecraftHolder.mc.field_1724.method_24828() || MinecraftHolder.mc.field_1687.method_8320(MinecraftHolder.mc.field_1724.method_24515().method_10069(0, -1, 0)).method_26215() || MinecraftHolder.mc.field_1724.field_3913.field_54155.comp_3163()) && fallDistance;
            canCrit = canCrit && !MinecraftHolder.mc.field_1724.method_24828() && !MinecraftHolder.mc.field_1724.method_6101() && !MinecraftHolder.mc.field_1724.method_5799();
            boolean isOnGround = MinecraftHolder.mc.field_1724.method_24828() || !MinecraftHolder.mc.field_1724.field_3913.field_54155.comp_3163();
            boolean bl = isLiquid = MinecraftHolder.mc.field_1724.method_5681() || MinecraftHolder.mc.field_1724.method_5799() || MinecraftHolder.mc.field_1724.method_52535();
            crit = AttackHandler.aura.smartCrits.get() ? isOnGround || canCrit || isLiquid : canCrit || isLiquid;
        }
        crit |= ((IEntity)MinecraftHolder.mc.field_1724).client$inWeb();
        return crit |= MinecraftHolder.mc.field_1724.method_6101();
    }

    public static float getFallDistance(int nextTicks) {
        boolean flag;
        Vector3f deltaMove = new Vector3f(0.0f, (float)MinecraftHolder.mc.field_1724.method_18798().field_1351, 0.0f);
        if (deltaMove.y == 0.0f || MinecraftHolder.mc.field_1724.method_24828()) {
            return 0.0f;
        }
        float fallDistance = 0.0f;
        double d0 = 0.08;
        boolean bl = flag = (double)deltaMove.y <= 0.0;
        if (flag && MinecraftHolder.mc.field_1724.method_6059(StatusEffects.field_5906)) {
            d0 = 0.01;
        }
        for (int i = 0; i < nextTicks + 1; ++i) {
            double d2 = deltaMove.y;
            deltaMove.y = (float)((d2 -= d0) * (double)0.98f);
            if (deltaMove.y > 0.0f) {
                fallDistance = 0.0f;
                continue;
            }
            fallDistance -= deltaMove.y;
        }
        return fallDistance;
    }

    private AttackHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        randomNumber1 = 2;
        randomNumber2 = 2;
        time = new TimeUtility();
        aura = AuraModule.INSTANCE;
    }
}

