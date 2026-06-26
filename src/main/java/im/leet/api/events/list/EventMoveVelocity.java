/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.MovementType
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class EventMoveVelocity
extends Event {
    public final MovementType type;
    public Vec3d movement;

    public EventMoveVelocity(MovementType type, Vec3d movement) {
        this.type = type;
        this.movement = movement;
    }
}

