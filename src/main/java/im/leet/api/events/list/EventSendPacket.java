/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 */
package im.leet.api.events.list;

import im.leet.api.events.list.EventPacket;
import im.leet.utils.network.PacketSide;
import net.minecraft.network.packet.Packet;

public class EventSendPacket
extends EventPacket {
    private static final EventSendPacket instance = new EventSendPacket();

    private EventSendPacket() {
        super(PacketSide.SEND);
    }

    public static EventSendPacket build(Packet<?> packet) {
        instance.reset(packet);
        return instance;
    }
}

