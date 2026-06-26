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

public class EventReceivePacket
extends EventPacket {
    private static final EventReceivePacket instance = new EventReceivePacket();

    private EventReceivePacket() {
        super(PacketSide.RECEIVE);
    }

    public static EventReceivePacket build(Packet<?> packet) {
        instance.reset(packet);
        return instance;
    }
}

