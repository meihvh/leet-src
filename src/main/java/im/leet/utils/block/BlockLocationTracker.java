/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.ChunkPos
 *  net.minecraft.world.chunk.WorldChunk
 *  org.jetbrains.annotations.Nullable
 */
package im.leet.utils.block;

import im.leet.utils.block.ChunkScanner;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

public abstract class BlockLocationTracker<T>
implements ChunkScanner.Subscriber {
    @Nullable
    public abstract T getStateFor(BlockPos var1, BlockState var2);

    public abstract boolean untrack(BlockPos var1);

    public abstract void track(BlockPos var1, T var2);

    public abstract Stream<BlockPos> streamPositions();

    public abstract Stream<Map.Entry<BlockPos, T>> stream();

    public abstract boolean isEmpty();

    @Override
    public final void recordBlock(BlockPos pos, BlockState state, boolean isCleared) {
        T newState = this.getStateFor(pos, state);
        if (newState == null) {
            if (!isCleared) {
                this.untrack(pos);
            }
        } else {
            this.track(pos, newState);
        }
    }

    @Override
    public final void onChunkUpdate(WorldChunk chunk) {
    }

    public static abstract class BlockPos2State<T>
    extends BlockLocationTracker<T> {
        private final Map<BlockPos, T> map = new ConcurrentHashMap<BlockPos, T>();

        @Override
        public Stream<BlockPos> streamPositions() {
            return this.map.keySet().stream();
        }

        @Override
        public Stream<Map.Entry<BlockPos, T>> stream() {
            return this.map.entrySet().stream();
        }

        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public void track(BlockPos pos, T state) {
            this.map.put(pos.method_10062(), state);
            this.onUpdate();
        }

        @Override
        public boolean untrack(BlockPos pos) {
            if (this.map.remove(pos) != null) {
                this.onUpdate();
                return true;
            }
            return false;
        }

        @Override
        public void clearAllChunks() {
            this.map.clear();
            this.onUpdate();
        }

        @Override
        public void clearChunk(ChunkPos pos) {
            if (this.map.keySet().removeIf(it -> it.method_10263() >= pos.method_8326() && it.method_10263() < pos.method_8327() && it.method_10260() >= pos.method_8326() && it.method_10260() < pos.method_8327())) {
                this.onUpdate();
            }
        }

        public void onUpdate() {
        }
    }
}

