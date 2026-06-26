/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityPose
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 */
package im.leet.base.rotations.point;

import im.leet.MinecraftHolder;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.easings.Easings;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public final class UBoxPoints
implements MinecraftHolder {
    public static double clamp(double value, double min, double max) {
        return Math.min(max, Math.max(value, min));
    }

    public static int lerp(int a, int b, float f) {
        return a + (int)(f * (float)(b - a));
    }

    public static double lerp(double a, double b, double f) {
        return a + f * (b - a);
    }

    public static HitResult traceBlock(Vec3d startVec, Vec3d endVec, RaycastContext.ShapeType blockMode, RaycastContext.FluidHandling fluidMode) {
        return UBoxPoints.mc.field_1687.method_17742(new RaycastContext(startVec, endVec, blockMode, fluidMode, (Entity)UBoxPoints.mc.field_1724));
    }

    private static double getDistanceXZ(ClientPlayerEntity self, double x, double z) {
        double d0 = self.method_23317() - x;
        double d1 = self.method_23321() - z;
        return MathHelper.method_15355((float)((float)(d0 * d0 + d1 * d1)));
    }

    private static boolean seenOnce3(ClientPlayerEntity self, double x, double y, double z) {
        Vec3d vector3d1 = new Vec3d(x, y, z);
        return UBoxPoints.mc.field_1687 != null && UBoxPoints.traceBlock(self.method_33571(), vector3d1, RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348).method_17783() != HitResult.Type.field_1332;
    }

    private static boolean seenOnceVector3d(ClientPlayerEntity self, Vec3d vec) {
        Vec3d vector3d = new Vec3d(self.method_23317(), self.method_23320(), self.method_23321());
        return UBoxPoints.mc.field_1687 != null && UBoxPoints.traceBlock(vector3d, vec, RaycastContext.ShapeType.field_17558, RaycastContext.FluidHandling.field_1348).method_17783() != HitResult.Type.field_1332;
    }

    private static boolean localSeen(ClientPlayerEntity selfEntity, Vec3d xyz, float scale) {
        return scale == 0.0f ? UBoxPoints.seenOnce3(selfEntity, xyz.field_1352, xyz.field_1351, xyz.field_1350) : UBoxPoints.seenOnce3(selfEntity, xyz.field_1352, xyz.field_1351, xyz.field_1350) && UBoxPoints.seenOnce3(selfEntity, xyz.field_1352, xyz.field_1351 + (double)scale, xyz.field_1350) && UBoxPoints.seenOnce3(selfEntity, xyz.field_1352, xyz.field_1351 - (double)scale, xyz.field_1350) && UBoxPoints.seenOnce3(selfEntity, xyz.field_1352 + (double)scale, xyz.field_1351, xyz.field_1350) && UBoxPoints.seenOnce3(selfEntity, xyz.field_1352 - (double)scale, xyz.field_1351, xyz.field_1350) && UBoxPoints.seenOnce3(selfEntity, xyz.field_1352, xyz.field_1351, xyz.field_1350 + (double)scale) && UBoxPoints.seenOnce3(selfEntity, xyz.field_1352, xyz.field_1351, xyz.field_1350 - (double)scale);
    }

    public static List<Vec3d> entityBoxVec3dsAlternate(Box aabb) {
        ArrayList<Vec3d> vecs = new ArrayList<Vec3d>();
        double offsetXYZ = 0.01f;
        int maxPointsCountXZ = 17;
        int minPointsCountXZ = 5;
        int maxPointsCountY = 24;
        int minPointsCountY = 6;
        aabb = aabb.method_989(-offsetXYZ, -offsetXYZ, -offsetXYZ);
        double[] whh = new double[]{aabb.field_1320 - aabb.field_1323, aabb.field_1325 - aabb.field_1322, (aabb.field_1325 - aabb.field_1322) / 1.05};
        double[] xyz = new double[]{aabb.field_1323 + whh[0] / 2.0, aabb.field_1322, aabb.field_1321 + whh[0] / 2.0};
        double[] xyz1 = new double[]{aabb.field_1323, aabb.field_1322, aabb.field_1321};
        double[] xyz2 = new double[]{aabb.field_1320, aabb.field_1325, aabb.field_1324};
        float sqrtWHH0CubeD2 = (float)Math.sqrt(whh[0] * whh[0] + whh[0] * whh[0] + whh[0] * whh[0]) / 2.0f;
        ClientPlayerEntity me = UBoxPoints.mc.field_1724;
        if (me == null) {
            return null;
        }
        float factorCount = (float)((1.0 - Math.min(me.method_19538().method_1022(new Vec3d(xyz[0], xyz[1], xyz[2])) / 5.0, 1.0)) * Math.min(me.method_19538().method_1022(new Vec3d(xyz[0], me.method_23318(), xyz[2])) / (double)0.6f, 1.0));
        int pointsCountXZ = UBoxPoints.lerp(minPointsCountXZ, maxPointsCountXZ, factorCount);
        int pointsCountY = UBoxPoints.lerp(minPointsCountY, maxPointsCountY, factorCount);
        float scaleSeenCheck = 0.0f;
        int[] nArray = IntStream.range(0, pointsCountXZ).toArray();
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            Integer xsI = nArray[i];
            boolean edgeX = xsI == 0 || xsI == pointsCountXZ - 1;
            double xs = UBoxPoints.lerp(xyz1[0], xyz2[0], (double)((float)xsI.intValue() / (float)(pointsCountXZ - 1)));
            int[] nArray2 = IntStream.range(0, pointsCountXZ).toArray();
            int n2 = nArray2.length;
            block1: for (int j = 0; j < n2; ++j) {
                Integer zsI = nArray2[j];
                boolean edgeZ = zsI == 0 || zsI == pointsCountXZ - 1;
                double zs = UBoxPoints.lerp(xyz1[2], xyz2[2], (double)((float)zsI.intValue() / (float)(pointsCountXZ - 1)));
                int[] nArray3 = IntStream.range(0, pointsCountY).toArray();
                int n3 = nArray3.length;
                for (int k = 0; k < n3; ++k) {
                    Integer ysI = nArray3[k];
                    boolean edgeY = ysI == 0 || ysI == pointsCountY - 1;
                    double ys = UBoxPoints.lerp(xyz1[1], xyz2[1], (double)((float)ysI.intValue() / (float)(pointsCountY - 1)));
                    Vec3d vec = new Vec3d(xs, ys, zs);
                    if (!(!edgeX && !edgeZ && !edgeY || me.method_19538().method_1022(vec.method_1031(0.0, (double)(-me.method_18381(EntityPose.field_18076)), 0.0)) < (double)sqrtWHH0CubeD2 || !UBoxPoints.localSeen(me, vec, scaleSeenCheck) || vecs.add(vec))) continue block1;
                }
            }
        }
        return vecs;
    }

    private static double getDistanceAtVec3dToVec3d(Vec3d first, Vec3d second) {
        double xDiff = first.field_1352 - second.field_1352;
        double yDiff = first.field_1351 - second.field_1351;
        double zDiff = first.field_1350 - second.field_1350;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

    public static Vec3d getBestVector3dOnEntityBox(Box aabb, boolean alwaysMultipoints) {
        if (aabb == null) {
            return UBoxPoints.mc.field_1724.method_33571();
        }
        double[] whh = new double[]{aabb.field_1320 - aabb.field_1323, aabb.field_1325 - aabb.field_1322, (aabb.field_1325 - aabb.field_1322) / (double)1.1f};
        double[] xyz = new double[]{aabb.field_1323 + whh[0] / 2.0, aabb.field_1322, aabb.field_1321 + whh[0] / 2.0};
        double[] diffs = new double[]{UBoxPoints.mc.field_1724.method_23318() - xyz[1], UBoxPoints.getDistanceXZ(UBoxPoints.mc.field_1724, xyz[0], xyz[2])};
        double ddtn = UBoxPoints.clamp(Easings.QUART_OUT.ease((diffs[1] - whh[0] / 2.0) / (5.0 + whh[0] / 2.0)), 0.1, 0.95);
        double pca = UBoxPoints.clamp(ddtn * ddtn, 0.0, 1.0);
        double pitchPointHeight = UBoxPoints.clamp(whh[2] / 2.0 * pca + whh[2] / 2.0 * UBoxPoints.clamp(diffs[0] + pca, 0.0, 1.0), 0.0, whh[2]);
        Vec3d defaultVec = new Vec3d(xyz[0], xyz[1] + pitchPointHeight, xyz[2]);
        if (!alwaysMultipoints && !UBoxPoints.seenOnceVector3d(UBoxPoints.mc.field_1724, defaultVec)) {
            defaultVec = defaultVec.method_1031(0.0, -pitchPointHeight / 2.0, 0.0);
        }
        if (whh[1] <= 1.0 || !alwaysMultipoints && UBoxPoints.seenOnceVector3d(UBoxPoints.mc.field_1724, defaultVec)) {
            return defaultVec;
        }
        List<Vec3d> normalVecs = UBoxPoints.entityBoxVec3dsAlternate(aabb);
        float factorDown = 1.0f - (float)Math.max(Math.min((diffs[1] - 2.0) / 3.0, 1.0), 0.0);
        Vec3d toSortVec = new Vec3d(UBoxPoints.mc.field_1724.method_23317(), UBoxPoints.mc.field_1724.method_23318() + (double)0.6f + UBoxPoints.lerp(pitchPointHeight, pitchPointHeight / 2.5, (double)factorDown), UBoxPoints.mc.field_1724.method_23321());
        if (normalVecs != null && normalVecs.size() > 1) {
            normalVecs.sort(Comparator.comparing(vec3 -> UBoxPoints.getDistanceAtVec3dToVec3d(toSortVec, vec3)));
        }
        return normalVecs != null && !normalVecs.isEmpty() ? normalVecs.getFirst() : defaultVec;
    }

    public static Vec3d getBestVector3dOnEntityBox(Box aabb) {
        return UBoxPoints.getBestVector3dOnEntityBox(aabb, RotationUtility.squaredBoxDistance((Entity)UBoxPoints.mc.field_1724, aabb.method_1005()) > MathUtility.squared(aabb.method_17939() * (double)1.37f));
    }

    private UBoxPoints() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

