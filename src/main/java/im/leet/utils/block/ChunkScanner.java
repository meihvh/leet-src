/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 *  it.unimi.dsi.fastutil.longs.LongSet
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket
 *  net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket
 *  net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$Mutable
 *  net.minecraft.util.math.ChunkPos
 *  net.minecraft.world.chunk.ChunkSection
 *  net.minecraft.world.chunk.WorldChunk
 */
package im.leet.utils.block;

import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.EventPriority;
import im.leet.api.events.list.EventChangeWorld;
import im.leet.api.events.list.EventLoadChunk;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.utils.LogUtility;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.runtime.SwitchBootstraps;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

public class ChunkScanner
implements MinecraftHolder {
    private ForkJoinPool executor = new ForkJoinPool(Math.max(Runtime.getRuntime().availableProcessors() / 2, 2));
    private final LongSet loadedChunks = new LongOpenHashSet();
    private final ThreadLocal<BlockPos.Mutable> tls_blockpos = ThreadLocal.withInitial(BlockPos.Mutable::new);
    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<Subscriber>();
    private final List<Subscriber> recordOnUpdateSubscribers = new CopyOnWriteArrayList<Subscriber>();
    @EventPriority(value=-50)
    EventBus<Event> events = event -> {
        Event e;
        if (event instanceof EventChangeWorld) {
            this.executor.shutdownNow();
            this.executor = new ForkJoinPool(Math.max(Runtime.getRuntime().availableProcessors() / 2, 2));
            this.loadedChunks.clear();
            this.subscribers.forEach(Subscriber::clearAllChunks);
        }
        if (event instanceof EventLoadChunk) {
            e = (EventLoadChunk)event;
            if (ChunkScanner.mc.field_1687 == null) {
                return;
            }
            WorldChunk chunk = ChunkScanner.mc.field_1687.method_8497(e.x, e.z);
            if (chunk == null || chunk.method_12223()) {
                return;
            }
            this.loadedChunks.add(ChunkPos.method_8331((int)e.x, (int)e.z));
            if (this.subscribers.isEmpty()) {
                return;
            }
            this.executor.execute(() -> this.onChunkLoad(chunk));
        }
        if (event instanceof EventReceivePacket) {
            e = (EventReceivePacket)event;
            Packet packet = ((EventReceivePacket)e).packet;
            Objects.requireNonNull(packet);
            Packet selector0$temp = packet;
            int index$1 = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{BlockUpdateS2CPacket.class, ChunkDeltaUpdateS2CPacket.class, UnloadChunkS2CPacket.class}, (Object)selector0$temp, index$1)) {
                case 0: {
                    BlockUpdateS2CPacket packet2 = (BlockUpdateS2CPacket)selector0$temp;
                    this.executor.execute(() -> this.onBlockUpdate(packet2.method_11309(), packet2.method_11308()));
                    break;
                }
                case 1: {
                    ChunkDeltaUpdateS2CPacket packet3 = (ChunkDeltaUpdateS2CPacket)selector0$temp;
                    this.executor.execute(() -> this.onChunkSectionUpdate(packet3));
                    break;
                }
                case 2: {
                    UnloadChunkS2CPacket packet4 = (UnloadChunkS2CPacket)selector0$temp;
                    mc.execute(() -> {
                        this.loadedChunks.remove(packet4.comp_1726().method_8324());
                        this.executor.execute(() -> this.onChunkUnload(packet4.comp_1726()));
                    });
                    break;
                }
            }
        }
    };

    public void subscribe(Subscriber subscriber) {
        ClientWorld world;
        if (this.subscribers.contains(subscriber)) {
            return;
        }
        this.subscribers.add(subscriber);
        if (subscriber.recordAllOnUpdate()) {
            this.recordOnUpdateSubscribers.add(subscriber);
        }
        if ((world = ChunkScanner.mc.field_1687) == null) {
            return;
        }
        ObjectArrayList chunks = new ObjectArrayList(this.loadedChunks.longParallelStream().mapToObj(it -> world.method_8497(ChunkPos.method_8325((long)it), ChunkPos.method_8332((long)it))).filter(WorldChunk::method_12223).toList());
        if (chunks.isEmpty()) {
            return;
        }
        this.executor.execute(() -> this.lambda$subscribe$1(subscriber, (List)chunks));
    }

    public void unsubscribe(Subscriber subscriber) {
        this.subscribers.remove(subscriber);
        if (subscriber.recordAllOnUpdate()) {
            this.recordOnUpdateSubscribers.remove(subscriber);
        }
    }

    void scanChunkSections(WorldChunk chunk, BiConsumer<BlockPos, BlockState> fn) {
        int sections = chunk.method_12040() + 1;
        for (int section_idx = 0; section_idx < sections; ++section_idx) {
            int start_x = chunk.method_12004().method_8326();
            int start_z = chunk.method_12004().method_8328();
            BlockPos.Mutable blockpos = this.tls_blockpos.get();
            ChunkSection section = chunk.method_38259(section_idx);
            for (int section_y = 0; section_y < 16; ++section_y) {
                int y = section_idx + (chunk.method_31607() >> 4) << 4 | section_y;
                for (int x = 0; x < 16; ++x) {
                    for (int z = 0; z < 16; ++z) {
                        BlockState state = section.method_12254(x, section_y, z);
                        BlockPos.Mutable pos = blockpos.method_10103(start_x | x, y, start_z | z);
                        fn.accept((BlockPos)pos, state);
                    }
                }
            }
        }
    }

    void onNewSubscriber(Subscriber subscriber, List<WorldChunk> chunks) {
        long now1 = System.nanoTime();
        chunks.forEach(subscriber::onChunkUpdate);
        if (subscriber.recordAllOnUpdate()) {
            for (WorldChunk chunk : chunks) {
                this.scanChunkSections(chunk, (pos, state) -> subscriber.recordBlock((BlockPos)pos, (BlockState)state, true));
            }
        }
        long now2 = System.nanoTime();
        LogUtility.debug(String.format("Scanning %d chunks for %s took %d ns", chunks.size(), subscriber.getClass().getSimpleName(), now2 - now1));
    }

    void onChunkLoad(WorldChunk chunk) {
        long now1 = System.nanoTime();
        this.subscribers.forEach(subscriber -> subscriber.onChunkUpdate(chunk));
        if (!this.recordOnUpdateSubscribers.isEmpty()) {
            this.scanChunkSections(chunk, (pos, state) -> {
                for (Subscriber subscriber : this.recordOnUpdateSubscribers) {
                    subscriber.recordBlock((BlockPos)pos, (BlockState)state, true);
                }
            });
        }
        long now2 = System.nanoTime();
        LogUtility.debug(String.format("Scanning chunk{%d;%d} took %d ns", chunk.method_12004().field_9181, chunk.method_12004().field_9180, now2 - now1));
    }

    void onBlockUpdate(BlockPos pos, BlockState state) {
        this.subscribers.forEach(subscriber -> subscriber.recordBlock(pos, state, false));
    }

    void onChunkUnload(ChunkPos pos) {
        this.subscribers.forEach(subscriber -> subscriber.clearChunk(pos));
    }

    void onChunkSectionUpdate(ChunkDeltaUpdateS2CPacket packet) {
        packet.method_30621((pos, state) -> this.subscribers.forEach(subscriber -> subscriber.recordBlock((BlockPos)pos, (BlockState)state, false)));
    }

    private /* synthetic */ void lambda$subscribe$1(Subscriber subscriber, List chunks) {
        this.onNewSubscriber(subscriber, chunks);
    }

    public static interface Subscriber {
        default public boolean recordAllOnUpdate() {
            return true;
        }

        public void recordBlock(BlockPos var1, BlockState var2, boolean var3);

        public void onChunkUpdate(WorldChunk var1);

        public void clearChunk(ChunkPos var1);

        public void clearAllChunks();
    }
}

