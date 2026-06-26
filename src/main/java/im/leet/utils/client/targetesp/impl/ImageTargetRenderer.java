/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.opengl.GlStateManager
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.util.math.MatrixStack$Entry
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package im.leet.utils.client.targetesp.impl;

import com.mojang.blaze3d.opengl.GlStateManager;
import im.leet.api.render.system.ClientPipelines;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.client.targetesp.TargetRendererChoice;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class ImageTargetRenderer
extends TargetRendererChoice {
    final CheckBox useCustomColor = this.checkbox("Custom color", false);
    final ColorSetting customColor;
    final SliderSetting speed;
    final CheckBox dreamcore;

    public ImageTargetRenderer() {
        super("Image");
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.useCustomColor::get;
        this.customColor = (ColorSetting)this.colorSetting("Color", new Color(0)).visible(supplierArray);
        this.speed = this.sliderSetting("Speed", 360.0f, 0.0f, 360.0f);
        this.dreamcore = this.checkbox("Dreamcore", true);
    }

    @Override
    public void render(MatrixStack stack, VertexConsumerProvider consumers, LivingEntity target, float alpha) {
        Color color2;
        Color color1;
        VertexConsumer consumer = consumers.getBuffer(ClientPipelines.TARGET_ESP.apply(Identifier.method_60655((String)"leet", (String)"images/world/target.png")));
        GlStateManager._depthMask((boolean)false);
        GlStateManager._disableCull();
        ImageTargetRenderer.mc.field_1773.method_22974().method_3315();
        if (this.dreamcore.get()) {
            GlStateManager._blendFuncSeparate((int)770, (int)1, (int)0, (int)1);
        } else {
            GlStateManager._enableBlend();
        }
        Quaternionf quaternionf = ImageTargetRenderer.mc.field_1773.method_19418().method_23767();
        float size = 0.7f + (1.0f - alpha) * 0.5f;
        stack.method_46416(0.0f, target.method_17682() / 2.0f, 0.0f);
        stack.method_22907((Quaternionfc)quaternionf);
        stack.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)Math.sin((double)System.currentTimeMillis() / 1000.0) * this.speed.get()));
        stack.method_22905(size, size, 1.0f);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-size, -size, 0.0f), new Vector3f(-size, size, 0.0f), new Vector3f(size, size, 0.0f), new Vector3f(size, -size, 0.0f)};
        if (this.useCustomColor.get()) {
            color1 = this.customColor.get();
            color2 = this.customColor.get();
        } else {
            color1 = ClientSettings.INSTANCE.getColor(0);
            color2 = ClientSettings.INSTANCE.getColor(90);
        }
        color1 = ColorUtility.injectAlpha(color1, alpha * 255.0f);
        color2 = ColorUtility.injectAlpha(color2, alpha * 255.0f);
        MatrixStack.Entry matrix = stack.method_23760();
        consumer.method_56824(matrix, avector3f[0].x, avector3f[0].y, avector3f[0].z).method_39415(color2.getRGB()).method_22913(0.0f, 0.0f);
        consumer.method_56824(matrix, avector3f[1].x, avector3f[1].y, avector3f[1].z).method_39415(color1.getRGB()).method_22913(0.0f, 1.0f);
        consumer.method_56824(matrix, avector3f[2].x, avector3f[2].y, avector3f[2].z).method_39415(color2.getRGB()).method_22913(1.0f, 1.0f);
        consumer.method_56824(matrix, avector3f[3].x, avector3f[3].y, avector3f[3].z).method_39415(color1.getRGB()).method_22913(1.0f, 0.0f);
        GlStateManager._depthMask((boolean)true);
        if (this.dreamcore.get()) {
            GlStateManager._blendFuncSeparate((int)1, (int)0, (int)1, (int)0);
        } else {
            GlStateManager._disableBlend();
        }
        ImageTargetRenderer.mc.field_1773.method_22974().method_3316();
        GlStateManager._enableCull();
    }
}

