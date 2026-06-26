/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.render.OverlayTexture
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.EntityRendererFactory$Context
 *  net.minecraft.client.render.entity.LivingEntityRenderer
 *  net.minecraft.client.render.entity.PlayerEntityRenderer
 *  net.minecraft.client.render.entity.feature.FeatureRendererContext
 *  net.minecraft.client.render.entity.model.EntityModel
 *  net.minecraft.client.render.entity.state.PlayerEntityRenderState
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.Vec3d
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.render.cosmetics.HatCosmetic;
import im.leet.base.modules.impl.render.customyaica.CrazyRabbitFeatureRenderer;
import im.leet.base.modules.impl.render.customyaica.CrazyRabbitModel;
import im.leet.base.modules.impl.render.customyaica.CustomModel;
import im.leet.base.rotations.Angle;
import im.leet.mixin.accessor.LivingEntityRendererAccessor;
import im.leet.utils.client.mixin.IPlayerEntityRenderState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererMixin
extends LivingEntityRenderer {
    private static final CrazyRabbitModel RABBIT_MODEL = new CrazyRabbitModel();

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method={"updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V"}, at={@At(value="HEAD")})
    private void leet$updateRenderState(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        ((IPlayerEntityRenderState)playerEntityRenderState).leet$setEntity((PlayerEntity)abstractClientPlayerEntity);
    }

    @Inject(method={"updateGliding"}, at={@At(value="HEAD")}, cancellable=true)
    private static void updateGliding(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float tickProgress, CallbackInfo ci) {
        ci.cancel();
        state.field_53534 = (float)player.method_6003() + tickProgress;
        Vec3d vec3d = new Angle(player.method_36454(), player.method_36455()).toVector();
        Vec3d vec3d2 = player.method_49339(tickProgress);
        if (vec3d2.method_37268() > (double)1.0E-5f && vec3d.method_37268() > (double)1.0E-5f) {
            state.field_53535 = true;
            double d = vec3d2.method_61890().method_1029().method_1026(vec3d.method_61890().method_1029());
            double e = vec3d2.field_1352 * vec3d.field_1350 - vec3d2.field_1350 * vec3d.field_1352;
            state.field_53521 = (float)(Math.signum(e) * Math.acos(Math.min(1.0, Math.abs(d))));
        } else {
            state.field_53535 = false;
            state.field_53521 = 0.0f;
        }
    }

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.field_4738.add(new CrazyRabbitFeatureRenderer(this));
        this.field_4738.add(HatCosmetic.INSTANCE.featureRenderer((FeatureRendererContext)this));
    }

    @Inject(method={"getTexture(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)Lnet/minecraft/util/Identifier;"}, at={@At(value="HEAD")}, cancellable=true)
    public void getTex(PlayerEntityRenderState s, CallbackInfoReturnable<Identifier> cir) {
        Entity e;
        boolean enabled;
        boolean bl = enabled = CustomModel.INSTANCE.isEnabled() && CustomModel.INSTANCE.model.is(CustomModel.Model.CrazyRabbit);
        if (!enabled) {
            cir.setReturnValue((Object)s.field_53520.comp_1626());
            return;
        }
        boolean local = MinecraftHolder.mc.field_1724 != null && s.field_53528 == MinecraftHolder.mc.field_1724.method_5628();
        boolean friend = false;
        if (MinecraftHolder.mc.field_1687 != null && (e = MinecraftHolder.mc.field_1687.method_8469(s.field_53528)) instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity p = (AbstractClientPlayerEntity)e;
            friend = Client.FRIENDS.isFriend((PlayerEntity)p);
        }
        boolean visible = local || CustomModel.INSTANCE.showOnFriends.get() && friend;
        cir.setReturnValue((Object)(visible ? Identifier.method_60655((String)"leet", (String)"images/models/rabbit.png") : s.field_53520.comp_1626()));
    }

    @Inject(method={"renderRightArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void rightArm(MatrixStack matrices, VertexConsumerProvider vcp, int light, Identifier skinTex, boolean sleeveVisible, CallbackInfo ci) {
        boolean enabled;
        boolean bl = enabled = CustomModel.INSTANCE.isEnabled() && CustomModel.INSTANCE.model.is(CustomModel.Model.CrazyRabbit);
        if (!enabled || MinecraftHolder.mc.field_1724 == null) {
            return;
        }
        ci.cancel();
        EntityModel<?> baseModel = ((LivingEntityRendererAccessor)((Object)this)).getModelAccessor();
        RABBIT_MODEL.copyPoseFromBase(baseModel);
        RenderLayer layer = RenderLayer.method_23578((Identifier)CrazyRabbitFeatureRenderer.RABBIT_TEX);
        VertexConsumer buf = vcp.getBuffer(layer);
        matrices.method_22903();
        PlayerEntityRendererMixin.RABBIT_MODEL.rightArm.method_22698(matrices, buf, light, OverlayTexture.field_21444);
        matrices.method_22909();
    }

    @Inject(method={"renderLeftArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void leftArm(MatrixStack matrices, VertexConsumerProvider vcp, int light, Identifier skinTex, boolean sleeveVisible, CallbackInfo ci) {
        boolean enabled;
        boolean bl = enabled = CustomModel.INSTANCE.isEnabled() && CustomModel.INSTANCE.model.is(CustomModel.Model.CrazyRabbit);
        if (!enabled || MinecraftHolder.mc.field_1724 == null) {
            return;
        }
        ci.cancel();
        EntityModel<?> baseModel = ((LivingEntityRendererAccessor)((Object)this)).getModelAccessor();
        RABBIT_MODEL.copyPoseFromBase(baseModel);
        RenderLayer layer = RenderLayer.method_23578((Identifier)CrazyRabbitFeatureRenderer.RABBIT_TEX);
        VertexConsumer buf = vcp.getBuffer(layer);
        matrices.method_22903();
        PlayerEntityRendererMixin.RABBIT_MODEL.leftArm.method_22698(matrices, buf, light, OverlayTexture.field_21444);
        matrices.method_22909();
    }
}

