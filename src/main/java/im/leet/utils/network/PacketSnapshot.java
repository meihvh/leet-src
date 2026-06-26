/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayNetworkHandler
 *  net.minecraft.network.packet.Packet
 */
package im.leet.utils.network;

import im.leet.MinecraftHolder;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.network.PacketSide;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;

public record PacketSnapshot(Packet<?> packet, PacketSide side, long ts) {
    public static PacketSnapshot send(Packet<?> packet) {
        return new PacketSnapshot(packet, PacketSide.SEND, System.currentTimeMillis());
    }

    public static PacketSnapshot receive(Packet<?> packet) {
        return new PacketSnapshot(packet, PacketSide.RECEIVE, System.currentTimeMillis());
    }

    public void handle() {
        ClientPlayNetworkHandler net = MinecraftHolder.mc.method_1562();
        if (net == null) {
            return;
        }
        switch (this.side) {
            case SEND: {
                NetworkUtility.sendWithoutEvent(this.packet);
                break;
            }
            case RECEIVE: {
                NetworkUtility.handlePacket(this.packet);
            }
        }
    }
}

