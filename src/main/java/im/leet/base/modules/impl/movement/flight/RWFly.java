/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.common.CommonPongC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 */
package im.leet.base.modules.impl.movement.flight;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventMove;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.api.events.list.EventTravel;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class RWFly
extends Choice {
    BlinkUtility blink = new BlinkUtility();
    BlinkUtility C0F = new BlinkUtility();
    TimeUtility time = new TimeUtility();
    TimeUtility timeC0F = new TimeUtility();

    public RWFly() {
        super("ReallyWorld");
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        this.blink.flush();
        this.C0F.flush();
        NetworkUtility.sendOnlySneak(RWFly.mc.field_1724.method_5715());
    }

    @Override
    public void onEvent(Event event) {
        Event e;
        if (event instanceof EventSendPacket) {
            e = (EventSendPacket)event;
            if (((EventSendPacket)e).packet instanceof PlayerMoveC2SPacket) {
                this.blink.queue((EventPacket)e);
            }
            if (((EventSendPacket)e).packet instanceof CommonPongC2SPacket) {
                this.C0F.queue((EventPacket)e);
            }
        }
        if (event instanceof EventReceivePacket) {
            e = (EventReceivePacket)event;
            Packet packet = ((EventReceivePacket)e).packet;
            if (packet instanceof PlayerPositionLookS2CPacket) {
                PlayerPositionLookS2CPacket playerPositionLookS2CPacket = (PlayerPositionLookS2CPacket)packet;
            }
        }
        if (event instanceof EventMove) {
            e = (EventMove)event;
        }
        if (event instanceof EventTravel) {
            e = (EventTravel)event;
            if (this.time.reached(2000L, true)) {
                this.blink.flush();
            }
            if (this.timeC0F.reached(100L, true)) {
                this.C0F.flush();
            }
            e.cancel();
            float dir = MoveUtility.getdir();
            if (dir != -1.0f) {
                float sin = (float)(-Math.sin((double)dir * (Math.PI / 180)));
                float cos = (float)Math.cos((double)dir * (Math.PI / 180));
                float mult = 0.12f;
                MoveUtility.DPSMUSORA(sin * mult, 0.0, cos * mult);
            }
        }
    }
}

