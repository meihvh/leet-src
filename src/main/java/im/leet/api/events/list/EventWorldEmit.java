/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.util.math.BlockPos;

public class EventWorldEmit
extends Event {
    private static final EventWorldEmit instance = new EventWorldEmit();
    public int eventId;
    public int data;
    public BlockPos pos;

    public static EventWorldEmit build(int eventId, int data, BlockPos pos) {
        EventWorldEmit.instance.eventId = eventId;
        EventWorldEmit.instance.data = data;
        EventWorldEmit.instance.pos = pos;
        instance.reset();
        return instance;
    }
}

