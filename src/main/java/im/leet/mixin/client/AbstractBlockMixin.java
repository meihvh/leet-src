/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.AbstractBlock
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.ShapeContext
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.shape.VoxelShape
 *  net.minecraft.util.shape.VoxelShapes
 *  net.minecraft.world.BlockView
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventBlockShape;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractBlock.class})
public class AbstractBlockMixin {
    @Shadow
    @Final
    protected boolean field_23159;

    @Inject(method={"getCollisionShape"}, at={@At(value="RETURN")}, cancellable=true)
    private void getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (Client.EVENTS == null || cir.getReturnValue() == null || MinecraftHolder.mc.method_1542()) {
            return;
        }
        VoxelShape vx = this.field_23159 ? state.method_26218(world, pos) : VoxelShapes.method_1073();
        EventBlockShape shape = EventBlockShape.build(pos, state, vx);
        Client.EVENTS.post(shape);
        if (shape.isCancelled()) {
            cir.setReturnValue((Object)VoxelShapes.method_1073());
            return;
        }
        cir.setReturnValue((Object)shape.shape);
    }
}

