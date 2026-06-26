/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat.targetstrafe;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventMoveVelocity;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathShortcuts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class MotionTargetStrafe
extends Choice {
    public static final MotionTargetStrafe INSTANCE = new MotionTargetStrafe();
    final SliderSetting antiAim = this.sliderSetting("Anti Aim", 0.2f, -1.0f, 1.0f).increment(0.01f);

    private MotionTargetStrafe() {
        super("Motion");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMoveVelocity) {
            EventMoveVelocity e = (EventMoveVelocity)event;
            LivingEntity target = TargetsUtility.getTarget();
            if (target == null) {
                return;
            }
            Vec3d velocity = MotionTargetStrafe.mc.field_1724.method_18798();
            Vec3d targetPos = target.method_19538();
            Vec3d myPos = MotionTargetStrafe.mc.field_1724.method_19538();
            double mySpeed = Math.hypot(velocity.field_1352, velocity.field_1350);
            double yaw = Math.atan2(targetPos.field_1350 - myPos.field_1350, targetPos.field_1352 - myPos.field_1352) - 1.5707963267948966;
            Vec3d strafe = this.vectorize(yaw, mySpeed);
            e.movement.field_1352 = strafe.field_1352;
            e.movement.field_1350 = strafe.field_1350;
        }
    }

    Vec3d vectorize(double yaw, double speed) {
        return new Vec3d(-MathShortcuts.dsin(yaw += (double)this.antiAim.get() * Math.PI) * speed, 0.0, MathShortcuts.dcos(yaw) * speed);
    }
}

