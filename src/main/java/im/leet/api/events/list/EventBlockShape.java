/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.shape.VoxelShape
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

public class EventBlockShape
extends Event {
    static EventBlockShape instance = new EventBlockShape();
    public BlockPos pos;
    public BlockState state;
    public VoxelShape shape;

    public static EventBlockShape build(BlockPos pos, BlockState state, VoxelShape shape) {
        EventBlockShape.instance.pos = pos;
        EventBlockShape.instance.state = state;
        EventBlockShape.instance.shape = shape;
        instance.reset();
        return instance;
    }
}

