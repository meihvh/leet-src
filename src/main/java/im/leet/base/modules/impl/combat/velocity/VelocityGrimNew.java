/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Position
 */
package im.leet.base.modules.impl.combat.velocity;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;

public class VelocityGrimNew
extends Choice {
    boolean flag = false;
    int look = 0;

    public VelocityGrimNew() {
        super("Grim New");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventReceivePacket) {
            EntityVelocityUpdateS2CPacket pos;
            EventReceivePacket e = (EventReceivePacket)event;
            if (VelocityGrimNew.mc.field_1724 == null) {
                return;
            }
            if (this.look > 0) {
                --this.look;
                return;
            }
            Packet packet = e.packet;
            if (packet instanceof EntityVelocityUpdateS2CPacket && (pos = (EntityVelocityUpdateS2CPacket)packet).method_11818() == VelocityGrimNew.mc.field_1724.method_5628()) {
                e.cancel();
                this.flag = true;
            }
            if (e.packet instanceof PlayerPositionLookS2CPacket) {
                this.look = 5;
            }
        }
        if (event instanceof EventGameTick && this.flag && this.look <= 0) {
            NetworkUtility.send(new PlayerMoveC2SPacket.Full(VelocityGrimNew.mc.field_1724.method_23317(), VelocityGrimNew.mc.field_1724.method_23318(), VelocityGrimNew.mc.field_1724.method_23321(), Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), VelocityGrimNew.mc.field_1724.method_24828(), VelocityGrimNew.mc.field_1724.field_5976));
            NetworkUtility.send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.field_12973, BlockPos.method_49638((Position)VelocityGrimNew.mc.field_1724.method_19538()), Direction.field_11033));
        }
    }
}

