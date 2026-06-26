/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.rotations.point;

import im.leet.base.rotations.Angle;
import im.leet.base.rotations.point.UBoxPoints;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.range.RangeSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.math.MathUtility;
import java.util.function.Function;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PointTracker
extends Group {
    final EnumSetting<Mode> mode = this.enumSetting("Mode", Mode.Random);
    final EnumSetting<Point> high = this.enumSetting("High", Point.Head);
    final EnumSetting<Point> low = this.enumSetting("Low", Point.Feet);
    final SliderSetting shrinkBox = this.sliderSetting("Shrink box", 0.05f, 0.0f, 3.0f).increment(0.01f);
    private Vec3d rotationPoint = Vec3d.field_1353;
    private Vec3d rotationMotion = Vec3d.field_1353;
    private final Group bounce = (Group)this.group("Bounce").visible(() -> this.mode.is(Mode.Bounce));
    private final RangeSetting bounceMotionXZ = this.bounce.rangeSetting("Motion XZ", 0.003f, 0.03f, 0.0f, 1.0f, 0.001f);
    private final RangeSetting bounceMotionY = this.bounce.rangeSetting("Motion Y", 0.001f, 0.03f, 0.0f, 1.0f, 0.001f);
    private final SliderSetting bounceThreshold = this.bounce.sliderSetting("Threshold", 0.05f, 0.0f, 1.0f).increment(0.001f);
    private final SliderSetting collisionMotionXZ = this.bounce.sliderSetting("Collision Motion XZ", 0.05f, 0.0f, 1.0f).increment(0.001f);
    private final SliderSetting collisionMotionY = this.bounce.sliderSetting("Collision Motion Y", 0.02f, 0.0f, 1.0f).increment(0.001f);
    private final SliderSetting startMotionXZ = this.bounce.sliderSetting("Start Motion XZ", 0.01f, 0.0f, 1.0f).increment(0.001f);
    private final SliderSetting startMotionY = this.bounce.sliderSetting("Start Motion Y", 0.0f, 0.0f, 1.0f).increment(0.001f);
    private final SliderSetting resetThreshold = this.bounce.sliderSetting("Reset threshold", 0.005f, 0.0f, 1.0f).increment(0.001f);

    public PointTracker() {
        super("Point Tracker");
    }

    public Vec3d getPoint(Entity target) {
        if (this.low.get().isHigherThan(this.high.get())) {
            this.high.select(this.low.get());
        }
        if (this.mode.is(Mode.Bounce)) {
            return ((ResolvedPositionEntity)target).hachclientport$getResolvedPos().method_1019(this.getBounce(target.method_5829()));
        }
        Box box = target.method_5829().method_1014((double)(-this.shrinkBox.get()));
        Box offsetBox = new Box(box.field_1323, box.field_1322 + (double)this.low.get().getOffset.apply(target).floatValue(), box.field_1321, box.field_1320, box.field_1325 - (double)(target.method_17682() - this.high.get().getOffset.apply(target).floatValue()), box.field_1324);
        return this.mode.get().getPoint.getPoint(offsetBox, PointTracker.mc.field_1724.method_33571());
    }

    public Vec3d getBounce(Box box) {
        float y;
        float xz;
        float minMotionXZ = this.bounceMotionXZ.getMin();
        float maxMotionXZ = this.bounceMotionXZ.getMax();
        float minMotionY = this.bounceMotionY.getMin();
        float maxMotionY = this.bounceMotionY.getMax();
        float threshold = this.bounceThreshold.get();
        double lengthX = box.method_17939();
        double lengthY = box.method_17940();
        double lengthZ = box.method_17941();
        if (this.rotationMotion.equals((Object)Vec3d.field_1353)) {
            this.rotationMotion = new Vec3d(MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
        }
        this.rotationPoint = this.rotationPoint.method_1019(this.rotationMotion);
        boolean collision = false;
        if (this.rotationPoint.field_1352 >= (lengthX - (double)threshold) / 2.0) {
            this.rotationMotion = new Vec3d((double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        } else if (this.rotationPoint.field_1352 <= -(lengthX - (double)threshold) / 2.0) {
            this.rotationMotion = new Vec3d((double)MathUtility.random(minMotionXZ, maxMotionXZ), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        }
        if (this.rotationPoint.field_1351 >= lengthY) {
            this.rotationMotion = new Vec3d(MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), (double)(-MathUtility.random(minMotionY, maxMotionY)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        } else if (this.rotationPoint.field_1351 <= (double)threshold) {
            this.rotationMotion = new Vec3d(MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), (double)MathUtility.random(minMotionY, maxMotionY), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        }
        if (this.rotationPoint.field_1350 >= (lengthZ - (double)threshold) / 2.0) {
            this.rotationMotion = new Vec3d(MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
            collision = true;
        } else if (this.rotationPoint.field_1350 <= -(lengthZ - (double)threshold) / 2.0) {
            this.rotationMotion = new Vec3d(MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), (double)MathUtility.random(minMotionXZ, maxMotionXZ));
            collision = true;
        }
        if (collision) {
            xz = this.collisionMotionXZ.get();
            y = this.collisionMotionY.get();
        } else {
            xz = this.startMotionXZ.get();
            y = this.startMotionY.get();
        }
        this.rotationPoint = this.rotationPoint.method_1031((double)MathUtility.random(-xz, xz), (double)MathUtility.random(-y, y), (double)MathUtility.random(-xz, xz));
        if (MathUtility.randomFloat() < this.resetThreshold.get()) {
            this.rotationMotion = new Vec3d(MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionY, maxMotionY) : (double)(-MathUtility.random(minMotionY, maxMotionY)), MathUtility.randomBoolean() ? (double)MathUtility.random(minMotionXZ, maxMotionXZ) : (double)(-MathUtility.random(minMotionXZ, maxMotionXZ)));
        }
        return this.rotationPoint;
    }

    public static enum Mode {
        Random((box, eyes) -> new Vec3d(MathUtility.random(box.field_1323, box.field_1320), MathUtility.random(box.field_1322, box.field_1325), MathUtility.random(box.field_1321, box.field_1324))),
        Closest((box, eyes) -> MathUtility.clampToBox(eyes, box)),
        Assist((box, eyes) -> MathUtility.clampToBox(eyes.method_1019(Angle.fromPlayer().toVector()), box)),
        Center((box, eyes) -> box.method_1005()),
        Bounce((box, eyes) -> eyes),
        Multi((box, eyes) -> UBoxPoints.getBestVector3dOnEntityBox(box));

        final GetPoint getPoint;

        public Vec3d getPoint(Box box, Vec3d eyes) {
            return this.getPoint.getPoint(box, eyes);
        }

        private Mode(GetPoint getPoint) {
            this.getPoint = getPoint;
        }

        public GetPoint getGetPoint() {
            return this.getPoint;
        }
    }

    static enum Point {
        Head(entity -> Float.valueOf(entity.method_18381(entity.method_18376()))),
        Body(entity -> Float.valueOf(entity.method_17682() / 2.0f)),
        Feet(entity -> Float.valueOf(0.0f));

        final Function<Entity, Float> getOffset;

        public boolean isHigherThan(Point other) {
            return this.ordinal() < other.ordinal();
        }

        private Point(Function<Entity, Float> getOffset) {
            this.getOffset = getOffset;
        }

        public Function<Entity, Float> getGetOffset() {
            return this.getOffset;
        }
    }

    @FunctionalInterface
    static interface GetPoint {
        public Vec3d getPoint(Box var1, Vec3d var2);
    }
}

