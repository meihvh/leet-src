/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.EntityRenderDispatcher
 *  net.minecraft.client.render.item.HeldItemRenderer
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.CrossbowItem
 *  net.minecraft.item.ItemDisplayContext
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.item.ShieldItem
 *  net.minecraft.util.Arm
 *  net.minecraft.util.Hand
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Quaternionfc
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.render.Animations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={HeldItemRenderer.class})
public abstract class HeldItemRendererMixin {
    @Shadow
    @Final
    private MinecraftClient field_4050;
    @Shadow
    @Final
    private EntityRenderDispatcher field_4046;
    @Shadow
    private ItemStack field_4048;

    @Shadow
    public abstract void method_3233(LivingEntity var1, ItemStack var2, ItemDisplayContext var3, MatrixStack var4, VertexConsumerProvider var5, int var6);

    @Shadow
    protected abstract void method_65816(float var1, float var2, MatrixStack var3, int var4, Arm var5);

    @Shadow
    protected abstract void method_3217(MatrixStack var1, Arm var2, float var3);

    @Shadow
    protected abstract void method_3224(MatrixStack var1, Arm var2, float var3);

    @Shadow
    protected abstract void method_49340(MatrixStack var1, float var2, Arm var3, ItemStack var4, PlayerEntity var5, float var6);

    @Shadow
    protected abstract void method_3218(MatrixStack var1, float var2, Arm var3, ItemStack var4, PlayerEntity var5);

    @Shadow
    protected abstract void method_3231(MatrixStack var1, VertexConsumerProvider var2, int var3, float var4, float var5, float var6);

    @Shadow
    protected abstract void method_3222(MatrixStack var1, VertexConsumerProvider var2, int var3, float var4, Arm var5, float var6, ItemStack var7);

    @Shadow
    protected abstract void method_3219(MatrixStack var1, VertexConsumerProvider var2, int var3, float var4, float var5, Arm var6);

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$renderFirstPersonItem(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        ci.cancel();
        if (!player.method_31550()) {
            boolean bl = hand == Hand.field_5808;
            Arm arm = bl ? player.method_6068() : player.method_6068().method_5928();
            matrices.method_22903();
            if (item.method_7960()) {
                if (bl && !player.method_5767()) {
                    if (Animations.INSTANCE.isEnabled()) {
                        Animations.INSTANCE.addArmTranslate(matrices, arm);
                    }
                    this.method_3219(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
                }
            } else if (item.method_57826(DataComponentTypes.field_49646)) {
                if (bl && this.field_4048.method_7960()) {
                    this.method_3231(matrices, vertexConsumers, light, pitch, equipProgress, swingProgress);
                } else {
                    this.method_3222(matrices, vertexConsumers, light, equipProgress, arm, swingProgress, item);
                }
            } else if (item.method_31574(Items.field_8399)) {
                int i;
                boolean bl2 = CrossbowItem.method_7781((ItemStack)item);
                boolean bl3 = arm == Arm.field_6183;
                int n = i = bl3 ? 1 : -1;
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand && !bl2) {
                    this.method_3224(matrices, arm, equipProgress);
                    matrices.method_46416((float)i * -0.4785682f, -0.094387f, 0.05731531f);
                    matrices.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees(-11.935f));
                    matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)i * 65.3f));
                    matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)i * -9.785f));
                    float f = (float)item.method_7935((LivingEntity)player) - ((float)player.method_6014() - tickProgress + 1.0f);
                    float g = f / (float)CrossbowItem.method_7775((ItemStack)item, (LivingEntity)player);
                    if (g > 1.0f) {
                        g = 1.0f;
                    }
                    if (g > 0.1f) {
                        float h = MathHelper.method_15374((float)((f - 0.1f) * 1.3f));
                        float j = g - 0.1f;
                        float k = h * j;
                        matrices.method_46416(k * 0.0f, k * 0.004f, k * 0.0f);
                    }
                    matrices.method_46416(g * 0.0f, g * 0.0f, g * 0.04f);
                    matrices.method_22905(1.0f, 1.0f, 1.0f + g * 0.2f);
                    matrices.method_22907((Quaternionfc)RotationAxis.field_40715.rotationDegrees((float)i * 45.0f));
                } else {
                    this.method_65816(swingProgress, equipProgress, matrices, i, arm);
                    if (bl2 && swingProgress < 0.001f && bl) {
                        matrices.method_46416((float)i * -0.641864f, 0.0f, 0.0f);
                        matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)i * 10.0f));
                    }
                }
                this.method_3233((LivingEntity)player, item, bl3 ? ItemDisplayContext.field_4322 : ItemDisplayContext.field_4321, matrices, vertexConsumers, light);
            } else {
                int l;
                boolean bl2 = arm == Arm.field_6183;
                int n = l = bl2 ? 1 : -1;
                if (player.method_6115() && player.method_6014() > 0 && player.method_6058() == hand) {
                    switch (item.method_7976()) {
                        case field_8952: {
                            this.method_3224(matrices, arm, equipProgress);
                            break;
                        }
                        case field_8950: 
                        case field_8946: {
                            this.method_3218(matrices, tickProgress, arm, item, (PlayerEntity)player);
                            this.method_3224(matrices, arm, equipProgress);
                            break;
                        }
                        case field_8949: {
                            this.method_3224(matrices, arm, equipProgress);
                            if (item.method_7909() instanceof ShieldItem) break;
                            matrices.method_46416((float)l * -0.14142136f, 0.08f, 0.14142136f);
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees(-102.25f));
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)l * 13.365f));
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)l * 78.05f));
                            break;
                        }
                        case field_8953: {
                            this.method_3224(matrices, arm, equipProgress);
                            matrices.method_46416((float)l * -0.2785682f, 0.18344387f, 0.15731531f);
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees(-13.935f));
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)l * 35.3f));
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)l * -9.785f));
                            float mx = (float)item.method_7935((LivingEntity)player) - ((float)player.method_6014() - tickProgress + 1.0f);
                            float fxx = mx / 20.0f;
                            fxx = (fxx * fxx + fxx * 2.0f) / 3.0f;
                            if (fxx > 1.0f) {
                                fxx = 1.0f;
                            }
                            if (fxx > 0.1f) {
                                float gx = MathHelper.method_15374((float)((mx - 0.1f) * 1.3f));
                                float h = fxx - 0.1f;
                                float j = gx * h;
                                matrices.method_46416(j * 0.0f, j * 0.004f, j * 0.0f);
                            }
                            matrices.method_46416(fxx * 0.0f, fxx * 0.0f, fxx * 0.04f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + fxx * 0.2f);
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40715.rotationDegrees((float)l * 45.0f));
                            break;
                        }
                        case field_8951: {
                            this.method_3224(matrices, arm, equipProgress);
                            matrices.method_46416((float)l * -0.5f, 0.7f, 0.1f);
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees(-55.0f));
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)l * 35.3f));
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)l * -9.785f));
                            float m = (float)item.method_7935((LivingEntity)player) - ((float)player.method_6014() - tickProgress + 1.0f);
                            float fx = m / 10.0f;
                            if (fx > 1.0f) {
                                fx = 1.0f;
                            }
                            if (fx > 0.1f) {
                                float gx = MathHelper.method_15374((float)((m - 0.1f) * 1.3f));
                                float h = fx - 0.1f;
                                float j = gx * h;
                                matrices.method_46416(j * 0.0f, j * 0.004f, j * 0.0f);
                            }
                            matrices.method_46416(0.0f, 0.0f, fx * 0.2f);
                            matrices.method_22905(1.0f, 1.0f, 1.0f + fx * 0.2f);
                            matrices.method_22907((Quaternionfc)RotationAxis.field_40715.rotationDegrees((float)l * 45.0f));
                            break;
                        }
                        case field_42717: {
                            this.method_49340(matrices, tickProgress, arm, item, (PlayerEntity)player, equipProgress);
                            break;
                        }
                        case field_55494: {
                            this.method_65816(swingProgress, equipProgress, matrices, l, arm);
                        }
                    }
                } else if (player.method_6123()) {
                    this.method_3224(matrices, arm, equipProgress);
                    matrices.method_46416((float)l * -0.4f, 0.8f, 0.3f);
                    matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)l * 65.0f));
                    matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)l * -85.0f));
                } else if (!Animations.INSTANCE.isEnabled() || !Animations.INSTANCE.handleMatrix(swingProgress, equipProgress, matrices, l, arm)) {
                    this.method_65816(swingProgress, equipProgress, matrices, l, arm);
                }
                this.method_3233((LivingEntity)player, item, bl2 ? ItemDisplayContext.field_4322 : ItemDisplayContext.field_4321, matrices, vertexConsumers, light);
            }
            matrices.method_22909();
        }
    }
}

