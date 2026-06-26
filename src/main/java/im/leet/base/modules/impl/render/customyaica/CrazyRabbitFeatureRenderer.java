/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.feature.FeatureRenderer
 *  net.minecraft.client.render.entity.feature.FeatureRendererContext
 *  net.minecraft.client.render.entity.model.EntityModel
 *  net.minecraft.client.render.entity.state.PlayerEntityRenderState
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 */
package im.leet.base.modules.impl.render.customyaica;

import im.leet.Client;
import im.leet.base.modules.impl.render.customyaica.CrazyRabbitModel;
import im.leet.base.modules.impl.render.customyaica.CustomModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class CrazyRabbitFeatureRenderer<M extends EntityModel<? super PlayerEntityRenderState>>
extends FeatureRenderer<PlayerEntityRenderState, M> {
    public static final Identifier RABBIT_TEX = Identifier.method_60655((String)"leet", (String)"images/models/rabbit.png");
    private final CrazyRabbitModel model = new CrazyRabbitModel();

    public CrazyRabbitFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, M> context) {
        super(context);
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vcp, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        boolean visible;
        Entity e;
        boolean enabled;
        boolean bl = enabled = CustomModel.INSTANCE.isEnabled() && CustomModel.INSTANCE.model.is(CustomModel.Model.CrazyRabbit);
        if (!enabled) {
            return;
        }
        MinecraftClient mc = MinecraftClient.method_1551();
        boolean local = mc.field_1724 != null && state.field_53528 == mc.field_1724.method_5628();
        boolean friend = false;
        if (mc.field_1687 != null && (e = mc.field_1687.method_8469(state.field_53528)) instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity p = (AbstractClientPlayerEntity)e;
            friend = Client.FRIENDS.isFriend((PlayerEntity)p);
        }
        boolean bl2 = visible = local || CustomModel.INSTANCE.showOnFriends.get() && friend;
        if (!visible) {
            return;
        }
        EntityModel base = this.method_17165();
        this.model.copyPoseFromBase(base);
        VertexConsumer buf = vcp.getBuffer(RenderLayer.method_23580((Identifier)RABBIT_TEX));
        matrices.method_22903();
        matrices.method_22905(1.25f, 1.25f, 1.25f);
        matrices.method_46416(0.0f, -0.3f, 0.0f);
        this.model.renderAll(matrices, buf, light);
        matrices.method_22909();
    }
}

