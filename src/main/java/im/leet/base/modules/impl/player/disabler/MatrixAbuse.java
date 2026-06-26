/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.common.CommonPongC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.network.packet.s2c.common.CommonPingS2CPacket
 *  net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket
 *  net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket
 *  net.minecraft.text.Text
 */
package im.leet.base.modules.impl.player.disabler;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.text.Text;

public class MatrixAbuse
extends Choice {
    ArrayList<Long> c00s = new ArrayList();
    TimeUtility lastC00s = new TimeUtility();
    ArrayList<DelayedPacket> delayed = new ArrayList();

    public MatrixAbuse() {
        super("Matrix Abuse");
    }

    @Override
    public void onDisabled() {
        this.c00s.clear();
    }

    @Override
    public void onEvent(Event event) {
        Packet packet;
        EventPacket p;
        if (event instanceof EventReceivePacket) {
            p = (EventReceivePacket)event;
            packet = ((EventReceivePacket)p).packet;
            if (packet instanceof CommonPingS2CPacket) {
                CommonPingS2CPacket l = (CommonPingS2CPacket)packet;
                p.cancel();
                this.c00s.add(Long.valueOf(l.method_36950()));
            }
            if (((EventReceivePacket)p).packet instanceof KeepAliveS2CPacket) {
                this.lastC00s.reset();
            }
            if (event instanceof EventReceivePacket) {
                EventReceivePacket e = (EventReceivePacket)event;
                Packet packet2 = e.packet;
                if (packet2 instanceof LoginSuccessS2CPacket) {
                    LoginSuccessS2CPacket d = (LoginSuccessS2CPacket)packet2;
                    ChatUtility.sendDebug("[matrixabuse] cleared C00");
                    this.c00s.clear();
                    this.delayed.clear();
                }
            }
        }
        if (event instanceof EventSendPacket) {
            p = (EventSendPacket)event;
            if (((EventSendPacket)p).packet instanceof CommonPongC2SPacket) {
                // empty if block
            }
            if ((packet = ((EventSendPacket)p).packet) instanceof PlayerMoveC2SPacket) {
                PlayerMoveC2SPacket m = (PlayerMoveC2SPacket)packet;
                if ((double)MoveUtility.getSpeed() < 1.0E-10 && MatrixAbuse.mc.field_1724.method_24828() && !MoveUtility.hasMovement(MatrixAbuse.mc.field_1724.field_3913.field_54155)) {
                    p.cancel();
                }
            }
        }
        if (event instanceof EventGameTick) {
            if ((double)MoveUtility.getSpeed() < 1.0E-10 && MatrixAbuse.mc.field_1724.method_24828() && !MoveUtility.hasMovement(MatrixAbuse.mc.field_1724.field_3913.field_54155)) {
                NetworkUtility.sendWithoutEvent(new PlayerMoveC2SPacket.PositionAndOnGround(MatrixAbuse.mc.field_1724.method_23317(), MatrixAbuse.mc.field_1724.method_23318() - (double)0.1f, MatrixAbuse.mc.field_1724.method_23321(), false, false));
            }
            if (this.lastC00s.reached(10000L, true)) {
                Iterator<Long> it = this.c00s.iterator();
                while (it.hasNext() && this.c00s.size() > 200) {
                    NetworkUtility.sendWithoutEvent(new CommonPongC2SPacket(it.next().intValue()));
                    it.remove();
                }
            }
            MatrixAbuse.mc.field_1705.method_1758(Text.method_30163((String)("C00 " + this.c00s.size())), false);
        }
    }

    record DelayedPacket(Packet<?> packet, long time) {
        public boolean send() {
            long d = this.time - System.currentTimeMillis();
            if (d <= 0L) {
                NetworkUtility.sendWithoutEvent(this.packet);
                return true;
            }
            return false;
        }
    }
}

