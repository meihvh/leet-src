/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement.flight;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.impl.movement.Flight;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class DamageFly
extends Choice {
    int ticks = 0;

    public DamageFly() {
        super("Damage");
    }

    @Override
    public void onEnabled() {
        NetworkUtility.send(new PlayerMoveC2SPacket.Full(DamageFly.mc.field_1724.method_23317(), DamageFly.mc.field_1724.method_23318(), DamageFly.mc.field_1724.method_23321(), Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), false, false));
        NetworkUtility.send(new PlayerMoveC2SPacket.Full(DamageFly.mc.field_1724.method_23317(), DamageFly.mc.field_1724.method_23318() + 4.0, DamageFly.mc.field_1724.method_23321(), Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), true, false));
        NetworkUtility.send(new PlayerMoveC2SPacket.Full(DamageFly.mc.field_1724.method_23317(), DamageFly.mc.field_1724.method_23318(), DamageFly.mc.field_1724.method_23321(), Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), false, false));
        this.ticks = 0;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventGameTick) {
            if (DamageFly.mc.field_1724.field_6235 == 9) {
                this.ticks = 5;
            }
            if (this.ticks > 0) {
                --this.ticks;
                Client.TIMER = 0.05f;
                float dir = MoveUtility.getdir();
                float speed = 4.0f;
                double sin = -Math.sin((double)dir * (Math.PI / 180));
                double cos = Math.cos((double)dir * (Math.PI / 180));
                DamageFly.mc.field_1724.method_18799(Vec3d.field_1353);
                MoveUtility.setSpeed(5.0);
            }
            if (DamageFly.mc.field_1724.field_6012 % 10 == 0 && this.ticks <= 0) {
                Flight.INSTANCE.toggle();
            }
        }
    }
}

