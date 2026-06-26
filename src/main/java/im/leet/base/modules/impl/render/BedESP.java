/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BedBlock
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.DoubleBlockProperties$Type
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.Nullable
 */
package im.leet.base.modules.impl.render;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.block.BlockLocationTracker;
import java.util.List;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class BedESP
extends Module {
    public static final BedESP INSTANCE = new BedESP();
    private static final BlockTracker tracker = new BlockTracker();
    private final SliderSetting maxLayers = (SliderSetting)this.sliderSetting("Max layers", 5.0f, 1.0f, 5.0f).onChanged(ignored -> tracker.rescan());
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            // empty if block
        }
    };

    private BedESP() {
        super("Bed ESP", Category.RENDER, "Renders overlays on top of beds", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        Client.ChunkScanner.subscribe(tracker);
    }

    @Override
    protected void onDisable() {
        Client.ChunkScanner.unsubscribe(tracker);
    }

    static class BlockTracker
    extends BlockLocationTracker.BlockPos2State<BedInfo> {
        BlockTracker() {
        }

        public void rescan() {
            Client.ChunkScanner.unsubscribe(this);
            Client.ChunkScanner.subscribe(this);
        }

        @Override
        @Nullable
        public BedInfo getStateFor(BlockPos pos, BlockState state) {
            if (!(state.method_26204() instanceof BedBlock) || BedBlock.method_24164((BlockState)state) == DoubleBlockProperties.Type.field_21784) {
                // empty if block
            }
            return null;
        }
    }

    record DefenseBlock(Block block, int count, boolean canBreak) {
    }

    record BedInfo(Vec3d pos, List<DefenseBlock> defenseBlocks) {
    }
}

