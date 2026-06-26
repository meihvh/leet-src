/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.util.math.Vec3d;

public class EventVelocity
extends Event {
    public final Vec3d movementInput;
    public final float speed;
    public final float yaw;
    public Vec3d velocity;

    public EventVelocity(Vec3d movementInput, float speed, float yaw, Vec3d velocity) {
        this.movementInput = movementInput;
        this.speed = speed;
        this.yaw = yaw;
        this.velocity = velocity;
    }
}

