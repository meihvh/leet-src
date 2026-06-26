/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 */
package im.leet.base.modules.impl.combat.elytraaura.defensive;

import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.ElytraAura;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public final class ElytraAuraDef
implements MinecraftHolder {
    static ElytraAura elytraAura = ElytraAura.INSTANCE;
    public static float lastAntiAimYaw = 0.0f;
    public static float lastAntiAimPitch = 0.0f;

    public static Vec3d updateAntiAimRotation(LivingEntity target, Vec2f[] customYawOffsets, Vec2f[] customPitchOffsets) {
        Random random = new Random();
        Vec2f[] yawOffsets = customYawOffsets;
        Vec2f[] pitchOffsets = customPitchOffsets;
        ArrayList<Vec2f> availableYawOffsets = new ArrayList<Vec2f>();
        Vec3d resultVector = Vec3d.field_1353;
        float targetYaw = (float)Math.toDegrees(Math.atan2(target.method_33571().field_1350 - ElytraAuraDef.mc.field_1724.method_33571().field_1350, target.method_33571().field_1352 - ElytraAuraDef.mc.field_1724.method_33571().field_1352)) - 90.0f;
        for (Vec2f offset : yawOffsets) {
            float potentialYaw = MathHelper.method_15393((float)(targetYaw + offset.field_1343));
            if (!(Math.abs(MathHelper.method_15393((float)(potentialYaw - lastAntiAimYaw))) > 45.0f)) continue;
            availableYawOffsets.add(offset);
        }
        if (availableYawOffsets.isEmpty()) {
            availableYawOffsets.addAll(Arrays.asList(yawOffsets));
        }
        Vec2f selectedYawOffset = (Vec2f)availableYawOffsets.get(random.nextInt(availableYawOffsets.size()));
        resultVector = new Vec3d((double)MathHelper.method_15393((float)(targetYaw + selectedYawOffset.field_1343)), 0.0, 0.0);
        Vec2f selectedPitchOffset = pitchOffsets[random.nextInt(pitchOffsets.length)];
        resultVector = new Vec3d(resultVector.field_1352, (double)selectedPitchOffset.field_1342, 0.0);
        Vec3d lookVec = ElytraAuraDef.getLookVector((float)resultVector.field_1352, (float)resultVector.field_1351);
        BlockHitResult collisionResult = ElytraAuraDef.mc.field_1687.method_17742(new RaycastContext(ElytraAuraDef.mc.field_1724.method_33571(), ElytraAuraDef.mc.field_1724.method_33571().method_1019(lookVec.method_1021(20.0)), RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)ElytraAuraDef.mc.field_1724));
        if (collisionResult.method_17783() == HitResult.Type.field_1332) {
            for (Vec2f offset : availableYawOffsets) {
                if (offset.method_1016(selectedYawOffset)) continue;
                float alternativeYaw = MathHelper.method_15393((float)(targetYaw + offset.field_1343));
                Vec3d altLookVec = ElytraAuraDef.getLookVector(alternativeYaw, (float)resultVector.field_1351);
                BlockHitResult altResult = ElytraAuraDef.mc.field_1687.method_17742(new RaycastContext(ElytraAuraDef.mc.field_1724.method_33571(), ElytraAuraDef.mc.field_1724.method_33571().method_1019(altLookVec.method_1021(20.0)), RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348, (Entity)ElytraAuraDef.mc.field_1724));
                if (altResult.method_17783() == HitResult.Type.field_1332) continue;
                resultVector = new Vec3d((double)alternativeYaw, resultVector.field_1351, 0.0);
                break;
            }
        }
        lastAntiAimYaw = (float)resultVector.field_1352;
        lastAntiAimPitch = (float)resultVector.field_1351;
        return resultVector;
    }

    private static Vec3d getLookVector(float yaw, float pitch) {
        float radYaw = (float)Math.toRadians(yaw);
        float radPitch = (float)Math.toRadians(pitch);
        float cosPitch = MathHelper.method_15362((float)(-radPitch));
        return new Vec3d((double)(-MathHelper.method_15374((float)radYaw) * cosPitch), (double)(-MathHelper.method_15374((float)(-radPitch))), (double)(MathHelper.method_15362((float)radYaw) * cosPitch));
    }

    private ElytraAuraDef() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

