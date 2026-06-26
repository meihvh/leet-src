/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.hud.InGameOverlayRenderer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.client.util.math.MatrixStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.render.Removals;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameOverlayRenderer.class})
public abstract class InGameOverlayRendererMixin {
    @Inject(method={"renderFireOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private static void injectRenderFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (Removals.INSTANCE.removals.get(Removals.Removal.FireOverlay)) {
            ci.cancel();
        }
    }

    @Inject(method={"renderInWallOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private static void injectRenderInWallOverlay(Sprite sprite, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (Removals.INSTANCE.removals.get(Removals.Removal.BlockOverlay)) {
            ci.cancel();
        }
    }
}

