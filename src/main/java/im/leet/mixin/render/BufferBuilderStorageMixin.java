/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.BufferBuilderStorage
 *  net.minecraft.client.render.VertexConsumerProvider$Immediate
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import im.leet.api.render.system.ClientPipelines;
import im.leet.api.render.system.CustomVertexConsumerProvider;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BufferBuilderStorage.class})
public class BufferBuilderStorageMixin {
    @Shadow
    @Final
    private VertexConsumerProvider.Immediate field_46901;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void init(int maxBlockBuildersPoolSize, CallbackInfo ci) {
        ClientPipelines.customVertexConsumerProvider = new CustomVertexConsumerProvider(this.field_46901);
    }
}

