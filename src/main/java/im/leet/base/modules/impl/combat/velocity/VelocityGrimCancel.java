/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.common.CommonPongC2SPacket
 *  net.minecraft.network.packet.s2c.common.CommonPingS2CPacket
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 */
package im.leet.base.modules.impl.combat.velocity;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import java.util.ArrayList;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class VelocityGrimCancel
extends Choice {
    int ticks;
    boolean waitingTransaction = false;
    final ArrayList<Integer> transactions = new ArrayList();
    final TimeUtility delay = new TimeUtility();

    public VelocityGrimCancel() {
        super("Grim Cancel");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventReceivePacket) {
            EntityVelocityUpdateS2CPacket pos;
            EventReceivePacket e = (EventReceivePacket)event;
            if (VelocityGrimCancel.mc.field_1724 == null) {
                return;
            }
            Object object = e.packet;
            if (object instanceof EntityVelocityUpdateS2CPacket && (pos = (EntityVelocityUpdateS2CPacket)object).method_11818() == VelocityGrimCancel.mc.field_1724.method_5628()) {
                e.cancel();
                this.waitingTransaction = true;
                this.ticks = 4;
            }
            if ((object = e.packet) instanceof CommonPingS2CPacket) {
                CommonPingS2CPacket ping = (CommonPingS2CPacket)object;
                if (this.waitingTransaction) {
                    this.transactions.add(ping.method_36950());
                    this.waitingTransaction = false;
                    e.cancel();
                }
            }
            if ((object = e.packet) instanceof PlayerPositionLookS2CPacket) {
                PlayerPositionLookS2CPacket move = (PlayerPositionLookS2CPacket)object;
                for (Integer i : this.transactions) {
                    NetworkUtility.sendWithoutEvent(new CommonPongC2SPacket(i.intValue()));
                }
                this.transactions.clear();
            }
        }
        if (event instanceof EventGameTick && this.delay.reached(500L, true) && !this.transactions.isEmpty()) {
            NetworkUtility.sendWithoutEvent(new CommonPongC2SPacket(this.transactions.getFirst().intValue()));
        }
    }
}

