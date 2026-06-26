/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.rotations.strategies.impl;

import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.RotationChoice;
import im.leet.utils.math.MathUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class CooldownRotation
extends RotationChoice {
    static float x = 0.0f;
    static float y = 0.0f;

    public CooldownRotation() {
        super("Cooldown");
    }

    @Override
    public Angle calculate(Angle current, Angle target) {
        float yaw = target.getYaw();
        float pitch = target.getPitch();
        Angle deltas = current.delta(new Angle(yaw, pitch));
        float cooldown = AuraModule.INSTANCE.attack.getProgress();
        float speed = MathUtility.random(90.0f, 100.0f) * (cooldown * MathUtility.random(0.01f, 1.1f));
        float getSpeed = MoveUtility.getBPS((LivingEntity)CooldownRotation.mc.field_1724);
        float[] compile = new float[]{Math.min(Math.abs(deltas.getYaw()), speed += getSpeed), Math.min(Math.abs(deltas.getPitch()), speed)};
        return new Angle(current.getYaw() + MathHelper.method_15363((float)deltas.getYaw(), (float)(-compile[0]), (float)compile[0]), MathHelper.method_15363((float)(current.getPitch() + MathHelper.method_15363((float)deltas.getPitch(), (float)(-compile[1]), (float)compile[1])), (float)-90.0f, (float)90.0f));
    }
}

