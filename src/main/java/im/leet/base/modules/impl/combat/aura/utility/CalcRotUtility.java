/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat.aura.utility;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.base.modules.impl.combat.aura.utility.RandomPatterAuraUtility;
import im.leet.base.rotations.Angle;
import im.leet.utils.attack.AttackHandle;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.RayTraceUtility;
import java.security.SecureRandom;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class CalcRotUtility
implements MinecraftHolder {
    static AuraModule aura = AuraModule.INSTANCE;
    private static Vec3d rotationPoint = Vec3d.field_1353;
    private static Vec3d rotationMotion = Vec3d.field_1353;
    private static SecureRandom random = new SecureRandom();

    public static float[] getDeltas(Angle angle) {
        float deltaX = MathHelper.method_15393((float)(angle.getYaw() - Client.ROTATION.getRotate().getYaw()));
        float deltaY = angle.getPitch() - Client.ROTATION.getRotate().getPitch();
        return new float[]{deltaX, MathHelper.method_15363((float)deltaY, (float)-90.0f, (float)90.0f)};
    }

    public static float[] getRandomJitter(Angle angle, float amplitude, float delimiter) {
        int hit = AttackHandle.countHit;
        int patternHit = hit % 8;
        int devisor = (int)MathUtility.logicalRandom(1.0f, 1.5f, 450, 0.5f);
        double time = (float)System.currentTimeMillis() * (amplitude / delimiter);
        float sin = (float)(Math.sin(time * ((double)AttackHandle.randomNumber1 / 10.0)) * (double)amplitude * 0.9);
        float cos = (float)(Math.cos(time * ((double)AttackHandle.randomNumber2 / 10.0 * 0.5)) * (double)amplitude * 0.3);
        float[] jitterValue = new float[]{0.0f, 0.0f};
        switch (patternHit) {
            case 0: {
                jitterValue[0] = sin;
                jitterValue[1] = cos / (float)devisor;
                break;
            }
            case 1: {
                jitterValue[0] = cos;
                jitterValue[1] = sin / (float)devisor;
                break;
            }
            case 2: {
                jitterValue[0] = -cos;
                jitterValue[1] = sin / (float)devisor;
                break;
            }
            case 3: {
                jitterValue[0] = cos;
                jitterValue[1] = -sin / (float)devisor;
                break;
            }
            case 4: {
                jitterValue[0] = sin;
                jitterValue[1] = -cos / (float)devisor;
                break;
            }
            case 5: {
                jitterValue[0] = -sin;
                jitterValue[1] = cos / (float)devisor;
                break;
            }
            case 6: {
                jitterValue[0] = -sin;
                jitterValue[1] = -cos / (float)devisor;
                break;
            }
            case 7: {
                jitterValue[0] = -cos;
                jitterValue[1] = -sin / (float)devisor;
            }
        }
        jitterValue[0] = jitterValue[0] + RandomPatterAuraUtility.random(angle, 0.95f, 1.05f);
        jitterValue[1] = jitterValue[1] + RandomPatterAuraUtility.random(angle, 0.95f, 1.05f);
        return jitterValue;
    }

    public static Vec3d getPoint(Entity target) {
        if (target == null) {
            return Vec3d.field_1353;
        }
        return CalcRotUtility.getBestPoint(CalcRotUtility.mc.field_1724.method_33571(), target);
    }

    public static Vec3d getBestPoint(Vec3d pos, Entity entity) {
        if (entity == null) {
            return Vec3d.field_1353;
        }
        return new Vec3d(MathHelper.method_15350((double)pos.field_1352, (double)entity.method_5829().field_1323, (double)entity.method_5829().field_1320), MathHelper.method_15350((double)pos.field_1351, (double)entity.method_5829().field_1322, (double)entity.method_5829().field_1325), MathHelper.method_15350((double)pos.field_1350, (double)entity.method_5829().field_1321, (double)entity.method_5829().field_1324));
    }

    public static Vec3d getLegitLook(Entity target) {
        float minMotionXZ = 0.003f;
        float maxMotionXZ = 0.03f;
        float minMotionY = 0.001f;
        float maxMotionY = 0.03f;
        double lengthX = target.method_5829().method_17939();
        double lengthY = target.method_5829().method_17940();
        double lengthZ = target.method_5829().method_17941();
        if (rotationMotion.equals((Object)Vec3d.field_1353)) {
            rotationMotion = new Vec3d(random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), random.nextBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
        }
        float attackDist = aura.attackRange() * aura.attackRange();
        rotationPoint = rotationPoint.method_1019(rotationMotion);
        boolean collision = false;
        if (CalcRotUtility.rotationPoint.field_1352 >= (lengthX - 0.05) / 2.0) {
            rotationMotion = new Vec3d((double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), random.nextBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        } else if (CalcRotUtility.rotationPoint.field_1352 <= -(lengthX - 0.05) / 2.0) {
            rotationMotion = new Vec3d((double)MathUtility.random(minMotionXZ, maxMotionXZ), random.nextBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        }
        if (CalcRotUtility.rotationPoint.field_1351 >= lengthY) {
            rotationMotion = new Vec3d(random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), (double)(-MathUtility.random(minMotionY, maxMotionY)), random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        } else if (CalcRotUtility.rotationPoint.field_1351 <= 0.05) {
            rotationMotion = new Vec3d(random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), (double)MathUtility.random(minMotionY, maxMotionY), random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        }
        if (CalcRotUtility.rotationPoint.field_1350 >= (lengthZ - 0.05) / 2.0) {
            rotationMotion = new Vec3d(random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), random.nextBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        } else if (CalcRotUtility.rotationPoint.field_1350 <= -(lengthZ - 0.05) / 2.0) {
            rotationMotion = new Vec3d(random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), random.nextBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), (double)MathUtility.random(minMotionXZ, maxMotionXZ));
            collision = true;
        }
        rotationPoint = collision ? rotationPoint.method_1031((double)MathUtility.random(-0.05f, 0.05f), (double)MathUtility.random(-0.02f, 0.02f), (double)MathUtility.random(-0.05f, 0.05f)) : rotationPoint.method_1031((double)MathUtility.random(-0.01f, 0.01f), 0.0, (double)MathUtility.random(-0.01f, 0.01f));
        if (random.nextFloat() < 0.005f) {
            rotationMotion = new Vec3d(random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), random.nextBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), random.nextBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
        }
        if (!RayTraceUtility.checkRtx(Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), aura.attackRange(), aura.attackRange(), target)) {
            float[] rotation1 = CalcRotUtility.calcAngle(target.method_19538().method_1031(0.0, (double)(target.method_18381(target.method_18376()) / 2.0f), 0.0));
            if (CalcRotUtility.squaredDistanceFromEyes(target.method_19538().method_1031(0.0, (double)(target.method_18381(target.method_18376()) / 2.0f), 0.0)) <= attackDist && RayTraceUtility.checkRtx(rotation1[0], rotation1[1], aura.attackRange(), 0.0f, target)) {
                rotationPoint = new Vec3d((double)MathUtility.random(-0.1f, 0.1f), (double)(target.method_18381(target.method_18376()) / MathUtility.random(1.8f, 2.5f)), (double)MathUtility.random(-0.1f, 0.1f));
            } else {
                float halfBox = (float)(lengthX / 2.0);
                for (float x1 = -halfBox; x1 <= halfBox; x1 += 0.05f) {
                    block1: for (float z1 = -halfBox; z1 <= halfBox; z1 += 0.05f) {
                        float y1 = 0.05f;
                        while ((double)y1 <= target.method_5829().method_17940()) {
                            float[] rotation;
                            Vec3d v1 = new Vec3d(target.method_23317() + (double)x1, target.method_23318() + (double)y1, target.method_23321() + (double)z1);
                            if (!(CalcRotUtility.squaredDistanceFromEyes(v1) > attackDist) && RayTraceUtility.checkRtx((rotation = CalcRotUtility.calcAngle(v1))[0], rotation[1], aura.attackRange(), 0.0f, target)) {
                                rotationPoint = new Vec3d((double)x1, (double)y1, (double)z1);
                                continue block1;
                            }
                            y1 += 0.15f;
                        }
                    }
                }
            }
        }
        return target.method_19538().method_1019(rotationPoint);
    }

    public static float[] calcAngle(Vec3d to) {
        if (to == null) {
            return null;
        }
        double difX = to.field_1352 - CalcRotUtility.mc.field_1724.method_33571().field_1352;
        double difY = (to.field_1351 - CalcRotUtility.mc.field_1724.method_33571().field_1351) * -1.0;
        double difZ = to.field_1350 - CalcRotUtility.mc.field_1724.method_33571().field_1350;
        double dist = MathHelper.method_15355((float)((float)(difX * difX + difZ * difZ)));
        return new float[]{(float)MathHelper.method_15338((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.method_15338((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static float squaredDistanceFromEyes(Vec3d targetPos) {
        if (CalcRotUtility.mc.field_1724 == null) {
            return 0.0f;
        }
        double dx = targetPos.field_1352 - CalcRotUtility.mc.field_1724.method_23317();
        double dy = targetPos.field_1351 - (CalcRotUtility.mc.field_1724.method_23318() + (double)CalcRotUtility.mc.field_1724.method_18381(CalcRotUtility.mc.field_1724.method_18376()));
        double dz = targetPos.field_1350 - CalcRotUtility.mc.field_1724.method_23321();
        return (float)(dx * dx + dy * dy + dz * dz);
    }

    public static float[] calculateSmart(Vec3d vec) {
        Vec3d diff = vec;
        double distance = Math.hypot(diff.field_1352, diff.field_1350);
        float shortestYawPath = (float)(Math.atan2(diff.field_1350, diff.field_1352) * 57.29577951308232 - 90.0 - (double)Client.ROTATION.getRotate().getYaw() + 540.0 - 180.0);
        float yaw = Client.ROTATION.getRotate().getYaw() + shortestYawPath;
        float pitch = MathHelper.method_15363((float)((float)(-(Math.atan2(diff.field_1351, distance) * 57.29577951308232))), (float)-90.0f, (float)90.0f);
        return new float[]{yaw, pitch};
    }

    private CalcRotUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static enum VEC_TYPE {
        FLOATING_POINT,
        MULTIPOINT,
        DEFAULT_POINT;

    }
}

