/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 */
package im.leet.base.modules.impl.combat.velocity;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.settings.impl.choice.Choice;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class VelocityVanilla
extends Choice {
    public VelocityVanilla() {
        super("Vanilla");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventReceivePacket) {
            EntityVelocityUpdateS2CPacket pos;
            EventReceivePacket e = (EventReceivePacket)event;
            Packet packet = e.packet;
            if (packet instanceof EntityVelocityUpdateS2CPacket && (pos = (EntityVelocityUpdateS2CPacket)packet).method_11818() == VelocityVanilla.mc.field_1724.method_5628()) {
                event.cancel();
            }
        }
    }
}

