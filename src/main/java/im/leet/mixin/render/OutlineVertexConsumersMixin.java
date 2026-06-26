/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.OutlineVertexConsumerProvider
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import im.leet.api.render.system.ClientPipelines;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={OutlineVertexConsumerProvider.class})
public class OutlineVertexConsumersMixin {
    @Inject(method={"draw"}, at={@At(value="TAIL")})
    private void draw(CallbackInfo ci) {
        ClientPipelines.customVertexConsumerProvider.draw();
    }
}

