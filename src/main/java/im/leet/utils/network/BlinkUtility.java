/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Queues
 *  net.minecraft.network.packet.Packet
 */
package im.leet.utils.network;

import com.google.common.collect.Queues;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventPacket;
import im.leet.utils.network.PacketSide;
import im.leet.utils.network.PacketSnapshot;
import java.util.Queue;
import java.util.function.Predicate;
import net.minecraft.network.packet.Packet;

public class BlinkUtility
implements MinecraftHolder {
    public final Queue<PacketSnapshot> queue = Queues.newConcurrentLinkedQueue();

    public void queue(EventPacket event) {
        event.cancel();
        this.queue.add(event.toSnapshot());
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public void queue(Packet<?> packet, PacketSide side) {
        this.queue.add(new PacketSnapshot(packet, side, System.currentTimeMillis()));
    }

    public void flush() {
        this.flush(ignored -> true);
    }

    public void clear() {
        this.queue.clear();
    }

    public void flushTime(long delay) {
        long now = System.currentTimeMillis();
        this.flush(it -> now - it.ts() >= delay);
    }

    public void flush(Predicate<PacketSnapshot> filter) {
        this.queue.removeIf(snap -> {
            if (filter.test((PacketSnapshot)snap)) {
                snap.handle();
                return true;
            }
            return false;
        });
    }
}

