/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.util.Hand
 *  org.joml.Vector3f
 */
package im.leet.utils.attack;

import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.mixin.accessor.ILocalPlayer;
import im.leet.utils.attack.CalcFallDist;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.joml.Vector3f;

public final class AttackHandle
implements MinecraftHolder {
    public static int countHit;
    public static int randomNumber1;
    public static int randomNumber2;
    public static int ticksOnBlock;
    static TimeUtility time;
    static AuraModule aura;

    public static void attackEntity(LivingEntity target, int packets) {
        boolean keepSprint = AttackHandle.aura.auraSetting.get(AuraModule.Feature.KeepSprint);
        for (int packet = 0; packet < packets; ++packet) {
            mc.method_1562().method_52787((Packet)PlayerInteractEntityC2SPacket.method_34206((Entity)target, (boolean)AttackHandle.mc.field_1724.method_5715()));
            AttackHandle.mc.field_1724.method_6104(Hand.field_5808);
            if (keepSprint && !((ILocalPlayer)AttackHandle.mc.field_1724).serverSprintState()) {
                if (!AttackHandle.mc.field_1724.method_24828() && CalcFallDist.convenientFallOffset() > 0.0) {
                    AttackHandle.mc.field_1687.method_43128(null, AttackHandle.mc.field_1724.method_23317(), AttackHandle.mc.field_1724.method_23318(), AttackHandle.mc.field_1724.method_23321(), SoundEvents.field_15016, AttackHandle.mc.field_1724.method_5634(), 1.0f, 1.0f);
                    AttackHandle.mc.field_1724.method_7277((Entity)target);
                }
            } else {
                AttackHandle.mc.field_1724.method_7324((Entity)target);
            }
            AttackHandle.mc.field_1724.method_7350();
            time.reset();
            ++countHit;
        }
        randomNumber1 = MathUtility.random(2, 8);
        randomNumber2 = MathUtility.random(2, 8);
    }

    public static boolean canAttackTick(int ticks) {
        boolean flag1;
        float f2 = AttackHandle.mc.field_1724.method_7261(0.5f + (float)ticks);
        if (AttackHandle.mc.field_1724.method_5765() && (double)f2 > 0.92) {
            return true;
        }
        boolean bl = flag1 = f2 > (AttackHandle.mc.field_1724.method_6047().method_7960() ? 0.99f : 0.94f) && AttackHandle.getFallDistance(1 + ticks) > 0.0f && !AttackHandle.mc.field_1724.method_24828() && !AttackHandle.mc.field_1724.method_6101() && !AttackHandle.mc.field_1724.method_5799();
        if (AttackHandle.mc.field_1724.method_5799() || AttackHandle.mc.field_1724.method_6101() || AttackHandle.aura.smartCrits.get() && AttackHandle.mc.field_1724.method_24828()) {
            return f2 > (AttackHandle.mc.field_1724.method_6047().method_7960() ? 0.99f : 0.94f);
        }
        return flag1 && AttackHandle.mc.field_1724.method_30950((float)(1 + ticks)).method_18805(1.0, 0.0, 1.0).method_1022(TargetsUtility.getTarget().method_30950((float)(1 + ticks)).method_18805(1.0, 0.0, 1.0)) <= (double)AttackHandle.aura.attackRangeSetting.get();
    }

    public static float getFallDistance(int nextTicks) {
        boolean flag;
        Vector3f deltaMove = new Vector3f(0.0f, (float)AttackHandle.mc.field_1724.method_18798().field_1351, 0.0f);
        if (deltaMove.y == 0.0f || AttackHandle.mc.field_1724.method_24828()) {
            return 0.0f;
        }
        float fallDistance = 0.0f;
        double d0 = 0.08;
        boolean bl = flag = (double)deltaMove.y <= 0.0;
        if (flag && AttackHandle.mc.field_1724.method_6059(StatusEffects.field_5906)) {
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

    private AttackHandle() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        randomNumber1 = 2;
        randomNumber2 = 2;
        time = new TimeUtility();
        aura = AuraModule.INSTANCE;
    }
}

