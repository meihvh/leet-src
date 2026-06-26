/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 */
package im.leet.base.modules.impl.movement.speed;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.api.events.list.EventTravel;
import im.leet.base.rotations.Angle;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class SpeedIntave
extends Choice {
    BlinkUtility blink = new BlinkUtility();
    TimeUtility time = new TimeUtility();

    public SpeedIntave() {
        super("Intave 12.5.5");
    }

    @Override
    public void onDisabled() {
        this.blink.flush();
        Client.TIMER = 1.0f;
    }

    @Override
    public void onEvent(Event event) {
        PlayerPositionLookS2CPacket p;
        Event e;
        if (SpeedIntave.mc.field_1724 == null) {
            return;
        }
        if (event instanceof EventTravel) {
            e = (EventTravel)event;
            this.blink.flushTime(3000L);
            Angle angle = Client.ROTATION.getRotate();
            float dir = MoveUtility.getdir();
            SpeedIntave.mc.field_1724.method_24830(true);
            if (dir != -1.0f && SpeedIntave.mc.field_1724.method_24828() && SpeedIntave.mc.field_1724.field_6012 % 2 == 0) {
                for (int i2 = 0; i2 < 5; ++i2) {
                    float m = 0.2f;
                    MoveUtility.DPSMUSORA(-Math.sin((double)dir * (Math.PI / 180)) * (double)m, 0.0, Math.cos((double)dir * (Math.PI / 180)) * (double)m);
                    NetworkUtility.sendWithoutEvent(new PlayerMoveC2SPacket.Full(SpeedIntave.mc.field_1724.method_23317(), SpeedIntave.mc.field_1724.method_23318(), SpeedIntave.mc.field_1724.method_23321(), angle.getYaw(), angle.getPitch(), false, SpeedIntave.mc.field_1724.field_5976));
                }
                NetworkUtility.sendWithoutEvent(new PlayerMoveC2SPacket.Full(SpeedIntave.mc.field_1724.method_23317(), SpeedIntave.mc.field_1724.method_23318() - (double)0.1f, SpeedIntave.mc.field_1724.method_23321(), angle.getYaw(), angle.getPitch(), false, SpeedIntave.mc.field_1724.field_5976));
            }
        }
        if (event instanceof EventReceivePacket) {
            e = (EventReceivePacket)event;
            Packet dir = ((EventReceivePacket)e).packet;
            if (dir instanceof PlayerPositionLookS2CPacket && (p = (PlayerPositionLookS2CPacket)dir).comp_3228().comp_3148().method_1022(SpeedIntave.mc.field_1724.method_19538()) < 3.0 && this.time.reached(300L, true)) {
                float dir2 = MoveUtility.getdir();
                Angle angle = Client.ROTATION.getRotate();
                if (dir2 != -1.0f) {
                    this.time.reset();
                }
            }
        }
        if (event instanceof EventSendPacket) {
            e = (EventSendPacket)event;
            Packet packet = ((EventSendPacket)e).packet;
            if (packet instanceof PlayerMoveC2SPacket && (p = (PlayerMoveC2SPacket)packet).method_36171()) {
                e.cancel();
            }
        }
    }
}

