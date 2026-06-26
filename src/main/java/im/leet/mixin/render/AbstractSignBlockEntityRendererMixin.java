/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.entity.SignText
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.block.entity.AbstractSignBlockEntityRenderer
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.math.BlockPos
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.render.Removals;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.AbstractSignBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={AbstractSignBlockEntityRenderer.class})
public class AbstractSignBlockEntityRendererMixin {
    @Inject(method={"renderText"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderText(BlockPos pos, SignText text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int textLineHeight, int maxTextWidth, boolean front, CallbackInfo ci) {
        if (Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.SignText)) {
            ci.cancel();
        }
    }
}

