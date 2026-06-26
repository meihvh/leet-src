/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.Blocks
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.block.BlockRenderManager
 *  net.minecraft.client.render.model.BlockModelPart
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.BlockRenderView
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.base.modules.impl.render.Removals;
import java.util.List;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BlockRenderManager.class})
public class BlockRenderManagerMixin {
    @Unique
    private static final Set<BlockState> VEGETATION_BLOCKS = Set.of(Blocks.field_10219.method_9564(), Blocks.field_10214.method_9564(), Blocks.field_10112.method_9564(), Blocks.field_10313.method_9564(), Blocks.field_10182.method_9564(), Blocks.field_10449.method_9564(), Blocks.field_10086.method_9564(), Blocks.field_10226.method_9564(), Blocks.field_10573.method_9564(), Blocks.field_10270.method_9564(), Blocks.field_10048.method_9564(), Blocks.field_10156.method_9564(), Blocks.field_10315.method_9564(), Blocks.field_10554.method_9564(), Blocks.field_9995.method_9564(), Blocks.field_10548.method_9564(), Blocks.field_10606.method_9564(), Blocks.field_10583.method_9564(), Blocks.field_10378.method_9564(), Blocks.field_10430.method_9564(), Blocks.field_10003.method_9564(), Blocks.field_10479.method_9564(), Blocks.field_10428.method_9564(), Blocks.field_10251.method_9564(), Blocks.field_10559.method_9564(), Blocks.field_22121.method_9564(), Blocks.field_22114.method_9564(), Blocks.field_22125.method_9564(), Blocks.field_22116.method_9564(), Blocks.field_22117.method_9564(), Blocks.field_28686.method_9564(), Blocks.field_10424.method_9564(), Blocks.field_10211.method_9564(), Blocks.field_10108.method_9564(), Blocks.field_10376.method_9564(), Blocks.field_10238.method_9564(), Blocks.field_10476.method_9564(), Blocks.field_9993.method_9564(), Blocks.field_10463.method_9564(), Blocks.field_10597.method_9564(), Blocks.field_10588.method_9564(), Blocks.field_28677.method_9564(), Blocks.field_42750.method_9564());

    @Inject(method={"renderBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, List<BlockModelPart> parts, CallbackInfo ci) {
        if (Removals.INSTANCE.removals.get(Removals.Removal.Vegetation) && Removals.INSTANCE.isEnabled() && VEGETATION_BLOCKS.contains(state)) {
            ci.cancel();
        }
    }
}

