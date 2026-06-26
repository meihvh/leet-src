/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Vector4f
 */
package im.leet.utils.math;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.ElytraAura;
import im.leet.base.modules.impl.combat.aura.utility.CalcRotUtility;
import im.leet.base.modules.impl.combat.elytraaura.math.ElytraAuraResolve;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.point.UBoxPoints;
import im.leet.utils.client.targets.TargetsUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4f;

public final class RotationUtility {
    public static Vec3d getClosestVec(Vec3d vec, Box AABB) {
        return new Vec3d(MathHelper.method_15350((double)vec.method_10216(), (double)AABB.field_1323, (double)AABB.field_1320), MathHelper.method_15350((double)vec.method_10214(), (double)AABB.field_1322, (double)AABB.field_1325), MathHelper.method_15350((double)vec.method_10215(), (double)AABB.field_1321, (double)AABB.field_1324));
    }

    public static Vec3d getClosestVec(Vec3d vec, Entity entity) {
        return RotationUtility.getClosestVec(vec, entity.method_5829());
    }

    public static Vec3d getClosestVec(Entity entity) {
        Vec3d eyePosVec = MinecraftHolder.mc.field_1724.method_33571();
        return RotationUtility.getClosestVec(eyePosVec, entity).method_1020(eyePosVec);
    }

    public static double getStrictDistance(LivingEntity entity) {
        return RotationUtility.getClosestVec((Entity)entity).method_1033();
    }

    public static Angle calculate(Vec3d vec, LivingEntity entity) {
        Vec3d diff = vec.method_1020(entity.method_33571());
        double distance = Math.hypot(diff.field_1352, diff.field_1350);
        float yaw = (float)(MathHelper.method_15349((double)diff.field_1350, (double)diff.field_1352) * 57.29577951308232) - 90.0f;
        float pitch = (float)(-(MathHelper.method_15349((double)diff.field_1351, (double)distance) * 57.29577951308232));
        return new Angle(yaw, pitch);
    }

    public static Angle calculate(Vec3d vec) {
        Vec3d diff = vec.method_1020(MinecraftHolder.mc.field_1724.method_33571());
        double distance = Math.hypot(diff.field_1352, diff.field_1350);
        float yaw = (float)(MathHelper.method_15349((double)diff.field_1350, (double)diff.field_1352) * 57.29577951308232) - 90.0f;
        float pitch = (float)(-(MathHelper.method_15349((double)diff.field_1351, (double)distance) * 57.29577951308232));
        return new Angle(yaw, pitch);
    }

    public static Angle calcRotateOnElytra(Vec3d vec, boolean eyepos) {
        Vec3d diff = eyepos ? vec.method_1020(MinecraftHolder.mc.field_1724.method_33571()) : vec;
        float shortestYawPath = (float)(Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352)) - 90.0 - (double)Client.ROTATION.getRotate().getYaw() + 540.0 - 180.0);
        float yaw = Client.ROTATION.getRotate().getYaw() + shortestYawPath;
        float pitch = MathHelper.method_15363((float)((float)(-Math.toDegrees(Math.atan2(diff.field_1351, Math.hypot(diff.field_1350, diff.field_1352))))), (float)-90.0f, (float)90.0f);
        yaw = MathHelper.method_15393((float)yaw);
        LivingEntity target = TargetsUtility.getTarget();
        float[] deltats = CalcRotUtility.getDeltas(new Angle(yaw, pitch));
        if (ElytraAura.INSTANCE.predictYawandPitchSetting.get() && target != null && MinecraftHolder.mc.field_1724.method_19538().method_1022(ElytraAuraResolve.getFinalTargetVector(target, false)) > (double)3.4f && target.method_6128() && !ElytraAura.INSTANCE.antiAimIsActive && !ElytraAuraResolve.isStoyak(target)) {
            float getYawDelt = deltats[0] / 1.5f;
            float getPitchDelt = deltats[1] / 3.0f;
            if (getYawDelt > 4.0f) {
                yaw += getYawDelt;
            }
            if (getPitchDelt > 3.0f) {
                pitch += getPitchDelt;
            }
        }
        return new Angle(yaw, pitch);
    }

    public static Angle calcRotate(Vec3d vec, boolean eyepos) {
        Vec3d diff = eyepos ? vec.method_1020(MinecraftHolder.mc.field_1724.method_33571()) : vec;
        float shortestYawPath = (float)(Math.toDegrees(Math.atan2(diff.field_1350, diff.field_1352)) - 90.0 - (double)Client.ROTATION.getRotate().getYaw() + 540.0 - 180.0);
        float yaw = Client.ROTATION.getRotate().getYaw() + shortestYawPath;
        float pitch = MathHelper.method_15363((float)((float)(-Math.toDegrees(Math.atan2(diff.field_1351, Math.hypot(diff.field_1350, diff.field_1352))))), (float)-90.0f, (float)90.0f);
        return new Angle(MathHelper.method_15393((float)yaw), pitch);
    }

    public static Vector4f calculateRotationFromCamera(LivingEntity target) {
        Vec3d vec = UBoxPoints.getBestVector3dOnEntityBox(target.method_5829()).method_1020(MinecraftHolder.mc.field_1724.method_33571());
        float rawYaw = (float)MathHelper.method_15338((double)(Math.toDegrees(Math.atan2(vec.field_1350, vec.field_1352)) - 90.0));
        float rawPitch = (float)(-Math.toDegrees(Math.atan2(vec.field_1351, Math.sqrt(Math.pow(vec.field_1352, 2.0) + Math.pow(vec.field_1350, 2.0)))));
        float yawDelta = MathHelper.method_15393((float)(rawYaw - Client.ROTATION.getRotate().getYaw()));
        float pitchDelta = rawPitch - Client.ROTATION.getRotate().getPitch();
        return new Vector4f(rawYaw, rawPitch, yawDelta, pitchDelta);
    }

    public static double calculateFOVFromCamera(LivingEntity target) {
        Vector4f rotation = RotationUtility.calculateRotationFromCamera(target);
        float yawDelta = rotation.z;
        float pitchDelta = rotation.w;
        return Math.sqrt(yawDelta * yawDelta + pitchDelta * pitchDelta);
    }

    public static double squaredBoxDistance(Box from, Vec3d to) {
        Vec3d pos = RotationUtility.getClosestVec(to, from);
        return pos.method_1025(to);
    }

    public static double squaredBoxDistance(Entity from, Vec3d to) {
        return RotationUtility.squaredBoxDistance(from.method_5829(), to);
    }

    public static double squaredBoxDistance(Entity from, Entity to) {
        return RotationUtility.squaredBoxDistance(from, to.method_33571());
    }

    public static double squaredBoxDistance(Entity from, Entity to, Vec3d offs) {
        return RotationUtility.squaredBoxDistance(from.method_5829().method_997(offs.method_1020(from.method_19538())), to.method_33571());
    }

    private RotationUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

