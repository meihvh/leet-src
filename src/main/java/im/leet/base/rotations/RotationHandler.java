/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.rotations;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventVelocity;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.strategies.Rotation;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.utils.player.MoveUtility;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationHandler {
    private Angle rotate = new Angle();
    private Angle prevRotate = null;
    private Angle targetAngle = null;
    private final Angle visual = new Angle();
    private int timeout = 0;
    private MovementCorrection correction = MovementCorrection.NONE;
    private Rotation executor;
    private boolean shouldLerp = false;
    private int priority = 0;
    private boolean rotating = false;
    EventBus<Event> ontick = event -> {
        Event e;
        if (event instanceof EventGameTick) {
            if (this.targetAngle != null && this.executor != null && this.timeout > 0) {
                --this.timeout;
                Angle current = this.getRotate();
                this.rotate = this.executor.calculate(current, this.targetAngle).fix();
            } else {
                this.priority = Integer.MIN_VALUE;
                if (this.rotating) {
                    float d = Math.abs(MathHelper.method_15393((float)(MinecraftHolder.mc.field_1724.method_36454() - this.rotate.getYaw()))) + Math.abs(MinecraftHolder.mc.field_1724.method_36455() - this.rotate.getPitch());
                    if (this.shouldLerp) {
                        this.rotate = this.executor.calculate(this.rotate, Angle.fromPlayer()).fix();
                    } else {
                        this.rotate = Angle.fromPlayer();
                        this.correction = MovementCorrection.NONE;
                    }
                    if (d < 5.0f) {
                        this.rotating = false;
                    }
                }
            }
            if (!this.rotating) {
                this.rotate = Angle.fromPlayer();
            }
        }
        if (event instanceof Event3D) {
            float tick = MinecraftHolder.mc.method_61966().method_60637(true);
            Angle rotate = this.getRotate();
            float targetYaw = rotate.getYaw();
            float targetPitch = rotate.getPitch();
            this.visual.setYaw(MathHelper.method_16439((float)tick, (float)this.visual.getYaw(), (float)(this.visual.getYaw() + MathHelper.method_15393((float)(targetYaw - this.visual.getYaw())) * 0.5f)));
            this.visual.setPitch(MathHelper.method_16439((float)tick, (float)this.visual.getPitch(), (float)targetPitch));
            if (this.correction == MovementCorrection.LOOK && this.rotating) {
                MinecraftHolder.mc.field_1724.method_36456((float)((double)MinecraftHolder.mc.field_1724.method_36454() + (double)MathHelper.method_15393((float)(this.visual.getYaw() - MinecraftHolder.mc.field_1724.method_36454())) * 0.5));
                MinecraftHolder.mc.field_1724.method_36457(MinecraftHolder.mc.field_1724.method_36455() + (this.visual.getPitch() - MinecraftHolder.mc.field_1724.method_36455()) * 0.5f);
            }
        }
        if (event instanceof EventInput) {
            e = (EventInput)event;
            if (this.rotate != null && this.correction == MovementCorrection.SILENT) {
                MoveUtility.silentCorrection(e, this.rotate.getYaw());
            }
        }
        if (event instanceof EventVelocity) {
            e = (EventVelocity)event;
            if (this.rotate != null && this.correction != MovementCorrection.NONE) {
                ((EventVelocity)e).velocity = Entity.method_18795((Vec3d)((EventVelocity)e).movementInput, (float)((EventVelocity)e).speed, (float)this.rotate.getYaw());
            }
        }
    };

    public Angle getRotate() {
        if (this.rotate == null) {
            return Angle.fromPlayer();
        }
        return this.rotate;
    }

    public RotationHandler() {
        Client.EVENTS.register(this);
    }

    @Deprecated
    public void rotate(Angle target, boolean shouldLerp) {
        this.rotate(LinearRotation.INSTANCE, target, 5, shouldLerp, MovementCorrection.STRICT, 0);
    }

    public void rotate(Rotation executor, Angle targetAngle, int timeout, boolean shouldLerp, MovementCorrection correction, int priority) {
        if (this.priority > priority) {
            return;
        }
        this.executor = executor;
        this.targetAngle = targetAngle;
        this.timeout = timeout;
        this.shouldLerp = shouldLerp;
        this.correction = correction;
        this.rotating = true;
        this.prevRotate = this.rotate.copy();
        this.rotate = executor.calculate(this.rotate, targetAngle).fix();
        this.priority = priority;
    }

    public Angle getPrevRotate() {
        return this.prevRotate;
    }

    public Angle getTargetAngle() {
        return this.targetAngle;
    }

    public Angle getVisual() {
        return this.visual;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public MovementCorrection getCorrection() {
        return this.correction;
    }

    public Rotation getExecutor() {
        return this.executor;
    }

    public boolean isShouldLerp() {
        return this.shouldLerp;
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean isRotating() {
        return this.rotating;
    }

    public EventBus<Event> getOntick() {
        return this.ontick;
    }
}

