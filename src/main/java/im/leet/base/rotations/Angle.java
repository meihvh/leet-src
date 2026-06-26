/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.rotations;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.SensUtility;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Angle
implements MinecraftHolder {
    public float yaw;
    public float pitch;

    public Angle(float yaw, float pitch) {
        this.yaw = Float.isNaN(yaw) ? 0.0f : yaw;
        this.pitch = Float.isNaN(pitch) ? 0.0f : pitch;
    }

    public Angle(float[] angles) {
        this(angles[0], angles[1]);
    }

    public Angle(Vec2f vec2f) {
        this.yaw = vec2f.field_1343;
        this.pitch = vec2f.field_1342;
    }

    public Angle(float yaw, float pitch, boolean forPlayer) {
        if (forPlayer) {
            this.yaw = Client.ROTATION.getRotate().getYaw() + (Float.isNaN(yaw) ? 0.0f : yaw);
            this.pitch = MathHelper.method_15363((float)(Client.ROTATION.getRotate().getPitch() + (Float.isNaN(pitch) ? 0.0f : pitch)), (float)-90.0f, (float)90.0f);
        } else {
            this.yaw = Float.isNaN(yaw) ? 0.0f : yaw;
            this.pitch = Float.isNaN(pitch) ? 0.0f : pitch;
        }
    }

    public Angle() {
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }

    public final Vec3d toVector() {
        return Vec3d.method_1030((float)this.pitch, (float)this.yaw);
    }

    public void set(Angle other) {
        this.yaw = other.yaw;
        this.pitch = other.pitch;
    }

    public Angle add(Angle other, boolean applyGcd) {
        float yaw = (float)((double)this.yaw + (applyGcd ? SensUtility.getSensitivity(other.yaw) : (double)other.yaw));
        float pitch = (float)((double)this.pitch + (applyGcd ? SensUtility.getSensitivity(other.pitch) : (double)other.pitch));
        return new Angle(yaw, pitch);
    }

    public Angle copy() {
        return new Angle(this.yaw, this.pitch);
    }

    public Angle lerpTowards(Angle other, float yawFactor, float pitchFactor) {
        return new Angle(MathUtility.linear(this.yaw, other.yaw, yawFactor), MathUtility.linear(this.pitch, other.pitch, pitchFactor));
    }

    public Angle clamp(float yawSpeed, float pitchSpeed) {
        return new Angle(MathHelper.method_15363((float)this.yaw, (float)(-yawSpeed), (float)yawSpeed), MathHelper.method_15363((float)this.pitch, (float)(-pitchSpeed), (float)pitchSpeed));
    }

    public Angle towards(Angle other, float yawFactor, float pitchFactor) {
        Angle delta = this.delta(other);
        float length = delta.length();
        float slYaw = Math.abs(delta.yaw / length) * yawFactor;
        float slPitch = Math.abs(delta.pitch / length) * pitchFactor;
        return new Angle(this.yaw + MathHelper.method_15363((float)delta.yaw, (float)(-slYaw), (float)slYaw), this.pitch + MathHelper.method_15363((float)delta.pitch, (float)(-slPitch), (float)slPitch));
    }

    public Angle delta(Angle other) {
        float yaw = MathHelper.method_15393((float)(other.getYaw() - this.yaw));
        float pitch = other.getPitch() - this.pitch;
        return new Angle(yaw, MathHelper.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }

    public float length() {
        return MathHelper.method_15355((float)(this.yaw * this.yaw + this.pitch * this.pitch));
    }

    public Angle fix() {
        Angle current = Client.ROTATION.getRotate();
        float getYaw = current.getYaw();
        float getPitch = current.getPitch();
        float yawDelta = MathHelper.method_15393((float)(this.yaw - getYaw));
        float pitchDelta = MathHelper.method_15393((float)(this.pitch - getPitch));
        yawDelta = (float)SensUtility.getSensitivity(yawDelta);
        pitchDelta = (float)SensUtility.getSensitivity(pitchDelta);
        return new Angle(getYaw + yawDelta, MathHelper.method_15363((float)(getPitch + pitchDelta), (float)-90.0f, (float)90.0f));
    }

    public float angleTo(Angle other) {
        return this.delta(other).length();
    }

    public Angle towardsLinear(Angle target, float yawSpeed, float pitchSpeed) {
        Angle diff = this.delta(target);
        float angle = diff.length();
        float slYaw = Math.abs(diff.yaw / angle) * yawSpeed;
        float slPitch = Math.abs(diff.pitch / angle) * pitchSpeed;
        return new Angle(this.yaw + MathHelper.method_15363((float)diff.yaw, (float)(-slYaw), (float)slYaw), this.pitch + MathHelper.method_15363((float)diff.pitch, (float)(-slPitch), (float)slPitch));
    }

    public static Angle fromPlayer() {
        return Angle.fromEntity((Entity)Angle.mc.field_1724);
    }

    public static Angle fromEntity(Entity entity) {
        return new Angle(entity.method_36454(), entity.method_36455());
    }

    public static Angle deltaFrom(Angle current, Vec3d vector) {
        Angle target = RotationUtility.calculate(vector);
        float deltaYaw = MathHelper.method_15393((float)(target.getYaw() - current.getYaw()));
        float deltaPitch = target.getPitch() - current.getPitch();
        return new Angle(deltaYaw, deltaPitch);
    }

    public static Angle fromRelative(Vec3d point, Vec3d from) {
        return Angle.fromDiffs(point.method_1020(from));
    }

    public static Angle fromDiffs(Vec3d diffs) {
        return new Angle(MathHelper.method_15393((float)((float)Math.toDegrees(Math.atan2(diffs.field_1350, diffs.field_1352)) - 90.0f)), MathHelper.method_15393((float)((float)(-Math.toDegrees(Math.atan2(diffs.field_1351, Math.hypot(diffs.field_1352, diffs.field_1350)))))));
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Angle)) {
            return false;
        }
        Angle other = (Angle)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Float.compare(this.getYaw(), other.getYaw()) != 0) {
            return false;
        }
        return Float.compare(this.getPitch(), other.getPitch()) == 0;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Angle;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Float.floatToIntBits(this.getYaw());
        result = result * 59 + Float.floatToIntBits(this.getPitch());
        return result;
    }

    public String toString() {
        return "Angle(yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ")";
    }
}

