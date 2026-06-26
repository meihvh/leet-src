/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventInput;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Strafe
extends Module {
    public static final Strafe INSTANCE = new Strafe();
    EventBus<Event> events = event -> {
        EventInput e;
        if (Strafe.mc.field_1724 == null || Strafe.mc.field_1687 == null) {
            return;
        }
        if (event instanceof EventInput && ((e = (EventInput)event).getForward() != 0.0f || e.getStrafe() != 0.0f)) {
            Vec2f movement = MoveUtility.getMovementVector(new Vec2f(e.getForward(), -e.getStrafe()), Strafe.mc.field_1724.method_36454());
            Vec3d input = new Vec3d((double)movement.field_1343, 0.0, (double)movement.field_1342);
            Angle angle = RotationUtility.calcRotate(input, false);
            angle.setPitch(Strafe.mc.field_1724.method_36455());
            Client.ROTATION.rotate(LinearRotation.INSTANCE, angle, 1, false, MovementCorrection.STRICT, 20);
            e.setForward(Math.abs(e.getForward()) + Math.abs(e.getStrafe()));
            e.setStrafe(0.0f);
        }
    };

    private Strafe() {
        super("Strafe", Category.MOVEMENT, "Simplifies control over the player", new Tag[0]);
    }
}

