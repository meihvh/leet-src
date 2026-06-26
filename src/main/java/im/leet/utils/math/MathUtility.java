/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector3f
 *  org.joml.Vector4f
 *  org.lwjgl.opengl.GL11
 */
package im.leet.utils.math;

import im.leet.MinecraftHolder;
import im.leet.utils.math.TimeUtility;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public final class MathUtility {
    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();
    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();
    public static MatrixStack worldStack = new MatrixStack();
    public static final double TO_DEGREES = 57.29577951308232;
    public static final double TO_RADIANS = Math.PI / 180;
    public static final float TO_RADIANS_F = (float)Math.PI / 180;
    static TimeUtility timeUtility = new TimeUtility();
    private static float last = 0.0f;
    private static final Random random = new Random();

    public static boolean mouseIn(float x, float y, float width, float height, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseX <= (double)(x + width) && mouseY >= (double)y && mouseY <= (double)(y + height);
    }

    public static double squared(double value) {
        return value * value;
    }

    public static float squaref(float value) {
        return value * value;
    }

    public static float clampRot(float delta, float value) {
        return Math.min(Math.abs(delta), value);
    }

    public static float getMaxOrMin(float value, boolean isMax) {
        return isMax ? Math.max(1.0f, value) : Math.min(1.0f, value);
    }

    public static float getMaxOrMin(float value, float value2, boolean isMax) {
        return isMax ? Math.max(value, value2) : Math.min(value, value2);
    }

    public static float logicalRandom(float first, float second, int MS, float chance) {
        return timeUtility.reached(MS, true) && Math.random() > (double)chance ? first : second;
    }

    public static float fastAnim(float end, float start, float multiple) {
        float clampedDelta = MathHelper.method_15363((float)(MathUtility.deltaTime() * multiple), (float)0.05f, (float)1.0f);
        return (1.0f - clampedDelta) * end + clampedDelta * start;
    }

    public static float deltaTime() {
        return MinecraftClient.method_1551().method_47599() > 0 ? 1.0f / (float)MinecraftClient.method_1551().method_47599() : 1.0f;
    }

    public static float linearFps(float source, float target, float amount) {
        return MathUtility.linear(source, target, amount * (1.0f / (float)Math.max(1, MinecraftClient.method_1551().method_47599())));
    }

    public static float linear(float source, float target, float amount) {
        if (Float.isNaN(source) || Float.isNaN(target) || Float.isNaN(amount)) {
            return 0.0f;
        }
        return source + (target - source) * amount;
    }

    public static void scale(MatrixStack stack, float x, float y, float scale) {
        MathUtility.scale(stack, x, y, scale, scale);
    }

    public static void scale(MatrixStack stack, float x, float y, float scaleX, float scaleY) {
        stack.method_46416(MathUtility.scaledX(x), MathUtility.scaledY(y), 0.0f);
        stack.method_22905(scaleX, scaleY, 1.0f);
        stack.method_46416(-MathUtility.scaledX(x), -MathUtility.scaledY(y), 0.0f);
    }

    public static float scaledX(float f) {
        return f / ((float)MinecraftHolder.window.method_4486() / (float)MinecraftHolder.window.method_4480());
    }

    public static float scaledY(float f) {
        return f / ((float)MinecraftHolder.window.method_4502() / (float)MinecraftHolder.window.method_4507());
    }

    public static float approach(float current, float target, float speed) {
        return current < target ? Math.min(current + speed, target) : Math.max(current - speed, target);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static float randomFloat() {
        return random.nextFloat();
    }

    public static double random(double min, double max) {
        if (min == max) {
            return min;
        }
        return random.nextDouble() * (max - min) + min;
    }

    public static float random(float min, float max, int MS) {
        if (min == max) {
            return min;
        }
        if (last == 0.0f) {
            last = (float)random.nextDouble(min, max);
        }
        if (timeUtility.reached(MS, true)) {
            last = (float)random.nextDouble(min, max);
        }
        return last;
    }

    public static int random(int min, int max) {
        if (min == max) {
            return min;
        }
        return random.nextInt(min, max);
    }

    public static float gaussian(float min, float max) {
        float value;
        if (min == max) {
            return min;
        }
        float mean = (min + max) / 2.0f;
        float stdDev = (max - min) / 6.0f;
        while ((value = (float)(random.nextGaussian() * (double)stdDev + (double)mean)) < min || value > max) {
        }
        return value;
    }

    public static long random(long min, long max) {
        if (min == max) {
            return min;
        }
        return random.nextLong(min, max);
    }

    public static float random(float min, float max) {
        if (min == max) {
            return min;
        }
        return (float)(random.nextDouble() * (double)(max - min) + (double)min);
    }

    public static float delta(float a, float b) {
        a = Math.abs(a);
        b = Math.abs(b);
        return Math.max(a, b) - Math.min(a, b);
    }

    public static Vec3d worldSpaceToScreenSpace(Vec3d pos) {
        Camera camera = MinecraftHolder.mc.method_1561().field_4686;
        int displayHeight = MinecraftHolder.mc.method_22683().method_4507();
        int[] viewport = new int[4];
        GL11.glGetIntegerv((int)2978, (int[])viewport);
        Vector3f target = new Vector3f();
        double deltaX = pos.field_1352 - camera.method_19326().field_1352;
        double deltaY = pos.field_1351 - camera.method_19326().field_1351;
        double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
        Vector4f transformedCoordinates = new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0f).mul((Matrix4fc)lastWorldSpaceMatrix);
        Matrix4f matrixProj = new Matrix4f((Matrix4fc)lastProjMat);
        Matrix4f matrixModel = new Matrix4f((Matrix4fc)lastModMat);
        matrixProj.mul((Matrix4fc)matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
        return new Vec3d((double)target.x / MathUtility.getScaleFactor(), (double)((float)displayHeight - target.y) / MathUtility.getScaleFactor(), (double)target.z);
    }

    public static double getScaleFactor() {
        return MinecraftHolder.mc.method_22683().method_4495();
    }

    public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = MathHelper.method_15355((float)(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal));
        return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }

    public static Vec3d clampToBox(Vec3d vec3d, Box box) {
        return new Vec3d(MathHelper.method_15350((double)vec3d.field_1352, (double)box.field_1323, (double)box.field_1320), MathHelper.method_15350((double)vec3d.field_1351, (double)box.field_1322, (double)box.field_1325), MathHelper.method_15350((double)vec3d.field_1350, (double)box.field_1321, (double)box.field_1324));
    }

    public static double forwardX(float yaw, double speed) {
        return Math.sin((double)(yaw + 90.0f) * (Math.PI / 180)) * speed;
    }

    public static double forwardZ(float yaw, double speed) {
        return Math.cos((double)(yaw + 90.0f) * (Math.PI / 180)) * speed;
    }

    public static Vec3d copy(Vec3d vec3d) {
        return new Vec3d(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350);
    }

    public static Vec3d strafe(float yaw, double speed) {
        return new Vec3d(-Math.sin(yaw) * speed, 0.0, Math.cos(yaw) * speed);
    }

    private MathUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

