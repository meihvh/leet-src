/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.ProjectileUtil
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.hit.EntityHitResult
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  org.jetbrains.annotations.NotNull
 */
package im.leet.utils.math;

import im.leet.MinecraftHolder;
import im.leet.base.rotations.Angle;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;

public class RayTraceUtility {
    public static BlockHitResult raycast(double range, Angle angle, boolean includeFluids) {
        return RayTraceUtility.raycast(Objects.requireNonNull(MinecraftHolder.mc.field_1724).method_5836(1.0f), range, angle, includeFluids);
    }

    public static BlockHitResult raycast(Vec3d vec, double range, Angle angle, boolean includeFluids) {
        Entity entity = MinecraftHolder.mc.field_1719;
        if (entity == null) {
            return null;
        }
        Vec3d rotationVec = angle.toVector();
        Vec3d end = vec.method_1031(rotationVec.field_1352 * range, rotationVec.field_1351 * range, rotationVec.field_1350 * range);
        ClientWorld world = MinecraftHolder.mc.field_1687;
        if (world == null) {
            return null;
        }
        RaycastContext.FluidHandling fluidHandling = includeFluids ? RaycastContext.FluidHandling.field_1347 : RaycastContext.FluidHandling.field_1348;
        RaycastContext context = new RaycastContext(vec, end, RaycastContext.ShapeType.field_17559, fluidHandling, entity);
        return world.method_17742(context);
    }

    public static BlockHitResult raycast(Vec3d start, Vec3d end, RaycastContext.ShapeType shapeType) {
        return RayTraceUtility.raycast(start, end, shapeType, (Entity)MinecraftHolder.mc.field_1724);
    }

    public static BlockHitResult raycast(Vec3d start, Vec3d end, RaycastContext.ShapeType shapeType, Entity entity) {
        return MinecraftHolder.mc.field_1687.method_17742(new RaycastContext(start, end, shapeType, RaycastContext.FluidHandling.field_1348, entity));
    }

    public static EntityHitResult raytraceEntity(double range, Angle angle, Predicate<Entity> filter) {
        Entity entity = MinecraftHolder.mc.field_1719;
        if (entity == null) {
            return null;
        }
        Vec3d cameraVec = entity.method_5836(1.0f);
        Vec3d rotationVec = angle.toVector();
        Vec3d vec3d3 = cameraVec.method_1031(rotationVec.field_1352 * range, rotationVec.field_1351 * range, rotationVec.field_1350 * range);
        Box box = entity.method_5829().method_18804(rotationVec.method_1021(range)).method_1009(1.0, 1.0, 1.0);
        return ProjectileUtil.method_18075((Entity)entity, (Vec3d)cameraVec, (Vec3d)vec3d3, (Box)box, e -> !e.method_7325() && filter.test((Entity)e), (double)(range * range));
    }

    public static boolean checkRtx(float yaw, float pitch, float distance, float wallDistance, Entity entity) {
        double maxDistance;
        Box entityArea;
        Vec3d rotationVector;
        Vec3d endPoint;
        EntityHitResult ehr;
        HitResult result = RayTraceUtility.rayTrace(distance, yaw, pitch);
        Vec3d startPoint = MinecraftHolder.mc.field_1724.method_19538().method_1031(0.0, (double)MinecraftHolder.mc.field_1724.method_18381(MinecraftHolder.mc.field_1724.method_18376()), 0.0);
        double distancePow2 = Math.pow(distance, 2.0);
        if (result != null) {
            distancePow2 = startPoint.method_1025(result.method_17784());
        }
        if ((ehr = ProjectileUtil.method_18075((Entity)MinecraftHolder.mc.field_1724, (Vec3d)startPoint, (Vec3d)(endPoint = startPoint.method_1019(rotationVector = RayTraceUtility.getRotationVector(pitch, yaw).method_1021((double)distance))), (Box)(entityArea = MinecraftHolder.mc.field_1724.method_5829().method_18804(rotationVector).method_1009(1.0, 1.0, 1.0)), e -> !e.method_7325() && e.method_5863() && e == entity, (double)(maxDistance = Math.max(distancePow2, Math.pow(wallDistance, 2.0))))) != null) {
            boolean allowWallHit;
            boolean allowedWallDistance = startPoint.method_1025(ehr.method_17784()) <= Math.pow(wallDistance, 2.0);
            boolean wallMissing = result == null;
            boolean wallBehindEntity = startPoint.method_1025(ehr.method_17784()) < distancePow2;
            boolean bl = allowWallHit = wallMissing || allowedWallDistance || wallBehindEntity;
            if (allowWallHit && startPoint.method_1025(ehr.method_17784()) <= Math.pow(distance, 2.0)) {
                return ehr.method_17782() == entity;
            }
        }
        return false;
    }

    @NotNull
    public static Vec3d getRotationVector(float yaw, float pitch) {
        return new Vec3d((double)(MathHelper.method_15374((float)(-pitch * ((float)Math.PI / 180))) * MathHelper.method_15362((float)(yaw * ((float)Math.PI / 180)))), (double)(-MathHelper.method_15374((float)(yaw * ((float)Math.PI / 180)))), (double)(MathHelper.method_15362((float)(-pitch * ((float)Math.PI / 180))) * MathHelper.method_15362((float)(yaw * ((float)Math.PI / 180)))));
    }

    public static HitResult rayTrace(double dst, float yaw, float pitch) {
        Vec3d vec3d = MinecraftHolder.mc.field_1724.method_5836(1.0f);
        Vec3d vec3d2 = RayTraceUtility.getRotationVector(pitch, yaw);
        Vec3d vec3d3 = vec3d.method_1031(vec3d2.field_1352 * dst, vec3d2.field_1351 * dst, vec3d2.field_1350 * dst);
        return MinecraftHolder.mc.field_1687.method_17742(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.field_17559, RaycastContext.FluidHandling.field_1348, (Entity)MinecraftHolder.mc.field_1724));
    }

    public static boolean rayTrace(Vec3d clientVec, double range, Box box) {
        Vec3d cameraVec = Objects.requireNonNull(MinecraftHolder.mc.field_1724).method_33571();
        return box.method_1006(cameraVec) || box.method_992(cameraVec, cameraVec.method_1019(clientVec.method_1021(range))).isPresent();
    }
}

