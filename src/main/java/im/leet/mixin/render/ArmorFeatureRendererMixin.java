/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.feature.ArmorFeatureRenderer
 *  net.minecraft.client.render.entity.model.BipedEntityModel
 *  net.minecraft.client.render.entity.state.BipedEntityRenderState
 *  net.minecraft.client.render.entity.state.PlayerEntityRenderState
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import im.leet.Client;
import im.leet.base.modules.impl.render.Removals;
import im.leet.base.modules.impl.render.customyaica.CustomModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ArmorFeatureRenderer.class})
public class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> {
    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
        if (bipedEntityRenderState instanceof PlayerEntityRenderState && Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.Armor)) {
            ci.cancel();
        }
    }

    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void cancelArmor(MatrixStack matrices, VertexConsumerProvider vcp, int light, BipedEntityRenderState state, float limbAngle, float limbDistance, CallbackInfo ci) {
        boolean enabled;
        boolean bl = enabled = CustomModel.INSTANCE.isEnabled() && CustomModel.INSTANCE.model.is(CustomModel.Model.CrazyRabbit);
        if (!enabled) {
            return;
        }
        if (state instanceof PlayerEntityRenderState) {
            PlayerEntityRenderState ps = (PlayerEntityRenderState)state;
            MinecraftClient mc = MinecraftClient.method_1551();
            if (mc.field_1687 == null) {
                return;
            }
            Entity e = mc.field_1687.method_8469(ps.field_53528);
            if (e instanceof AbstractClientPlayerEntity) {
                boolean visible;
                AbstractClientPlayerEntity p = (AbstractClientPlayerEntity)e;
                boolean local = mc.field_1724 != null && p.method_5628() == mc.field_1724.method_5628();
                boolean friend = Client.FRIENDS.isFriend((PlayerEntity)p);
                boolean bl2 = visible = local || CustomModel.INSTANCE.showOnFriends.get() && friend;
                if (visible) {
                    ci.cancel();
                }
            }
        }
    }
}

