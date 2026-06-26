/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.render.RenderTickCounter
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.VertexConsumerProvider$Immediate
 *  net.minecraft.client.render.WorldRenderer
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventRenderEntity;
import im.leet.base.modules.impl.render.Removals;
import im.leet.utils.client.ClientSettings;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={WorldRenderer.class})
public abstract class WorldRendererMixin {
    @Shadow
    @Final
    private MinecraftClient field_4088;

    @Inject(method={"renderBlockDamage"}, at={@At(value="TAIL")})
    private void renderBlockDamage(MatrixStack matrices, Camera camera, VertexConsumerProvider.Immediate vertexConsumers, CallbackInfo ci) {
        if (MinecraftHolder.mc.field_1724 != null && MinecraftHolder.mc.field_1687 != null) {
            Client.EVENTS.post(Event3D.build(new MatrixStack(), vertexConsumers));
            ClientSettings.INSTANCE.targetRenderer.render(matrices, (VertexConsumerProvider)vertexConsumers);
        }
    }

    @Inject(method={"renderEntities"}, at={@At(value="HEAD")})
    private void renderEntities(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, Camera camera, RenderTickCounter tickCounter, List<Entity> entities, CallbackInfo ci) {
        Client.EVENTS.post(EventRenderEntity.build(vertexConsumers, matrices));
    }

    @Inject(method={"hasBlindnessOrDarkness(Lnet/minecraft/client/render/Camera;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void hasBlindnessOrDarkness(Camera camera, CallbackInfoReturnable<Boolean> info) {
        if (Removals.INSTANCE.isEnabled() && (Removals.INSTANCE.removals.get(Removals.Removal.Blindness) || Removals.INSTANCE.removals.get(Removals.Removal.Darkness))) {
            info.setReturnValue(null);
        }
    }
}

