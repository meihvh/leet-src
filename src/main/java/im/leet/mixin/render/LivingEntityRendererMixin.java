/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.LivingEntityRenderer
 *  net.minecraft.client.render.entity.model.EntityModel
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.render.entity.state.PlayerEntityRenderState
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.ColorHelper
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.render.Removals;
import im.leet.base.modules.impl.render.customyaica.CustomModel;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin {
    @Shadow
    protected EntityModel field_4737;
    @Unique
    private LivingEntityRenderState origin;

    @Shadow
    public abstract EntityModel<?> method_4038();

    @Shadow
    protected abstract void method_4058(LivingEntityRenderState var1, MatrixStack var2, float var3, float var4);

    @Shadow
    protected abstract void method_4042(LivingEntityRenderState var1, MatrixStack var2);

    @Shadow
    @Nullable
    protected abstract RenderLayer method_24302(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4);

    @Shadow
    protected abstract boolean method_4056(LivingEntityRenderState var1);

    @Shadow
    public static int method_23622(LivingEntityRenderState state, float whiteOverlayProgress) {
        return 0;
    }

    @Shadow
    protected abstract float method_23185(LivingEntityRenderState var1);

    @Shadow
    protected abstract int method_62484(LivingEntityRenderState var1);

    @Shadow
    protected abstract boolean method_62483(LivingEntityRenderState var1);

    @Shadow
    public abstract Identifier method_3885(LivingEntityRenderState var1);

    @Redirect(method={"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/LivingEntity;headYaw:F"))
    private float headYaw(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity && !Client.IS_PANIC) {
            return Client.ROTATION.getVisual().getYaw();
        }
        return instance.field_6241;
    }

    @Redirect(method={"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/LivingEntity;lastHeadYaw:F"))
    private float lastHeadYaw(LivingEntity instance) {
        if (instance instanceof ClientPlayerEntity && !Client.IS_PANIC) {
            return Client.ROTATION.getVisual().getYaw();
        }
        return instance.field_6259;
    }

    @ModifyExpressionValue(method={"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F")})
    private float bodyYaw(float original, LivingEntity instance) {
        return original;
    }

    @Redirect(method={"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F"))
    private float bodyPitch(LivingEntity instance, float v) {
        if (instance instanceof ClientPlayerEntity && !Client.IS_PANIC) {
            return Client.ROTATION.getVisual().getPitch();
        }
        return instance.method_61414(v);
    }

    @Inject(method={"render*"}, at={@At(value="HEAD")})
    private void hideBase(LivingEntityRenderState s, MatrixStack matrices, VertexConsumerProvider vc, int light, CallbackInfo ci) {
        boolean visible;
        Entity e;
        boolean enabled;
        this.origin = s;
        if (!(s instanceof PlayerEntityRenderState)) {
            return;
        }
        PlayerEntityRenderState ps = (PlayerEntityRenderState)s;
        boolean bl = enabled = CustomModel.INSTANCE.isEnabled() && CustomModel.INSTANCE.model.is(CustomModel.Model.CrazyRabbit);
        if (!enabled) {
            return;
        }
        boolean local = MinecraftHolder.mc.field_1724 != null && ps.field_53528 == MinecraftHolder.mc.field_1724.method_5628();
        boolean friend = false;
        if (MinecraftHolder.mc.field_1687 != null && (e = MinecraftHolder.mc.field_1687.method_8469(ps.field_53528)) instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity p = (AbstractClientPlayerEntity)e;
            friend = Client.FRIENDS.isFriend((PlayerEntity)p);
        }
        boolean bl2 = visible = local || CustomModel.INSTANCE.showOnFriends.get() && friend;
        if (!visible) {
            return;
        }
        this.method_4038().method_63512().field_3665 = false;
    }

    @Inject(method={"render*"}, at={@At(value="TAIL")})
    private void showBase(LivingEntityRenderState s, MatrixStack matrices, VertexConsumerProvider vc, int light, CallbackInfo ci) {
        if (!(s instanceof PlayerEntityRenderState)) {
            return;
        }
        this.method_4038().method_63512().field_3665 = true;
    }

    @Redirect(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
    @Nullable
    private RenderLayer client$getRenderLayer(LivingEntityRenderer instance, LivingEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.method_3885(state);
        if (translucent || Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.Invisibility) && state.field_53333) {
            return RenderLayer.method_29379((Identifier)identifier);
        }
        if (showBody) {
            return this.field_4737.method_23500(identifier);
        }
        return showOutline ? RenderLayer.method_23287((Identifier)identifier) : null;
    }

    @Redirect(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/ColorHelper;mix(II)I"))
    private int client$mix(int first, int second) {
        if (Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.Invisibility) && this.origin.field_53333) {
            first = ColorUtility.injectAlpha(Color.WHITE, 100.0f).getRGB();
        }
        return ColorHelper.method_61322((int)first, (int)second);
    }
}

