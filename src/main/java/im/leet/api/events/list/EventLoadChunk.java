/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.ChunkPos
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.util.math.ChunkPos;

public class EventLoadChunk
extends Event {
    public int x;
    public int z;

    public ChunkPos chunkPos() {
        return new ChunkPos(this.x, this.z);
    }

    public EventLoadChunk(int x, int z) {
        this.x = x;
        this.z = z;
    }
}

