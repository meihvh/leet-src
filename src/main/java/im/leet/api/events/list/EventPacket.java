/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import im.leet.utils.network.PacketSide;
import im.leet.utils.network.PacketSnapshot;
import net.minecraft.network.packet.Packet;

public abstract class EventPacket
extends Event {
    public Packet<?> packet;
    public final PacketSide side;
    public long ts;

    protected EventPacket(PacketSide side) {
        this.side = side;
    }

    protected void reset(Packet<?> packet) {
        super.reset();
        this.packet = packet;
        this.ts = System.currentTimeMillis();
    }

    public PacketSnapshot toSnapshot() {
        return new PacketSnapshot(this.packet, this.side, this.ts);
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public PacketSide getSide() {
        return this.side;
    }

    public long getTs() {
        return this.ts;
    }
}

