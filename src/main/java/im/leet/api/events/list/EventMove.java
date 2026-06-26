/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.util.math.Vec3d;

public class EventMove
extends Event {
    private static final EventMove instance = new EventMove();
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean ground;
    public boolean horizontalCollision;
    public Vec3d motion;

    public Vec3d getPos() {
        return new Vec3d(this.x, this.y, this.z);
    }

    public static EventMove build(double x, double y, double z, float yaw, float pitch, Vec3d motion, boolean ground, boolean horizontalCollision) {
        EventMove.instance.x = x;
        EventMove.instance.y = y;
        EventMove.instance.z = z;
        EventMove.instance.yaw = yaw;
        EventMove.instance.pitch = pitch;
        EventMove.instance.motion = motion;
        EventMove.instance.ground = ground;
        EventMove.instance.horizontalCollision = horizontalCollision;
        instance.reset();
        return instance;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public boolean isGround() {
        return this.ground;
    }

    public boolean isHorizontalCollision() {
        return this.horizontalCollision;
    }

    public Vec3d getMotion() {
        return this.motion;
    }

    public void setMotion(Vec3d motion) {
        this.motion = motion;
    }
}

