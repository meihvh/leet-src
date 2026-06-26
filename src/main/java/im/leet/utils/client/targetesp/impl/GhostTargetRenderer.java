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
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Quaternionfc
 */
package im.leet.utils.client.targetesp.impl;

import com.mojang.blaze3d.opengl.GlStateManager;
import im.leet.api.render.system.ClientPipelines;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
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
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionfc;

public class GhostTargetRenderer
extends TargetRendererChoice {
    final CheckBox useCustomColor = this.checkbox("Custom color", false);
    final ColorSetting customColor;
    final EnumSetting<Image> image;
    final CheckBox dreamcore;
    final SliderSetting size;
    final SliderSetting length;
    final CheckBox kurva;
    long lastTime;

    public GhostTargetRenderer() {
        super("Ghost");
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.useCustomColor::get;
        this.customColor = (ColorSetting)this.colorSetting("Color", new Color(0)).visible(supplierArray);
        this.image = this.enumSetting("Image", Image.Glow);
        this.dreamcore = this.checkbox("Dreamcore", true);
        this.size = this.sliderSetting("Size", 0.5f, 0.4f, 10.0f).increment(0.1f);
        this.length = this.sliderSetting("Length", 25.0f, 15.0f, 40.0f);
        this.kurva = this.checkbox("Curve", true);
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public void render(MatrixStack stack, VertexConsumerProvider consumers, LivingEntity target, float alpha) {
        VertexConsumer consumer = consumers.getBuffer(ClientPipelines.TARGET_ESP.apply(this.image.get().id));
        GlStateManager._depthMask((boolean)false);
        GlStateManager._disableCull();
        GhostTargetRenderer.mc.field_1773.method_22974().method_3315();
        if (this.dreamcore.get()) {
            GlStateManager._blendFuncSeparate((int)770, (int)1, (int)0, (int)1);
        }
        GlStateManager._enableBlend();
        stack.method_46416(0.0f, target.method_17682() * 0.5f + 0.3f, 0.0f);
        this.particle(stack, consumer, (sin, cos) -> new Vec3d(sin, cos, -cos), alpha);
        this.particle(stack, consumer, (sin, cos) -> new Vec3d(-sin, sin, -cos), alpha);
        this.particle(stack, consumer, (sin, cos) -> new Vec3d(-sin, -sin, cos), alpha);
        if (this.dreamcore.get()) {
            GlStateManager._blendFuncSeparate((int)1, (int)0, (int)1, (int)0);
        }
        GlStateManager._disableBlend();
        GhostTargetRenderer.mc.field_1773.method_22974().method_3316();
        GlStateManager._enableCull();
        GlStateManager._depthMask((boolean)true);
    }

    void particle(MatrixStack stack, VertexConsumer consumer, Transformation transformation, float alpha) {
        double radius = 0.67 + (double)(this.size.get() / this.size.getMax());
        double distance = this.image.is(Image.Triangle) ? 7.0 : 10.0 + (double)this.length.get() * 0.2;
        float size = this.size.get();
        int alphaFactor = 15;
        int i = 0;
        while ((float)i < this.length.get() * alpha) {
            Color color2;
            Color color1;
            stack.method_22903();
            double angle = 0.15 * ((double)((float)(System.currentTimeMillis() - this.lastTime) / 2.0f) - (double)i * distance) / 30.0;
            double sin = Math.sin(angle) * radius;
            double cos = Math.cos(angle) * radius;
            stack.method_61958(transformation.make(sin, cos));
            if (this.kurva.get()) {
                stack.method_22904(0.0, Math.sin((angle + (double)i) * 15.0 * (Math.PI / 180)) * 0.1, 0.0);
            }
            stack.method_22907((Quaternionfc)GhostTargetRenderer.mc.field_1773.method_19418().method_23767());
            stack.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees(((float)(System.currentTimeMillis() - this.lastTime) - (float)i * 100.0f) / 10.0f));
            stack.method_46416(size / 2.0f, size / 2.0f, 0.0f);
            if (this.useCustomColor.get()) {
                color1 = this.customColor.get();
                color2 = this.customColor.get();
            } else {
                color1 = ClientSettings.INSTANCE.getColor(0);
                color2 = ClientSettings.INSTANCE.getColor(90);
            }
            color1 = ColorUtility.injectAlpha(color1, (float)(255 - i * alphaFactor) * alpha);
            color2 = ColorUtility.injectAlpha(color2, (float)(255 - i * alphaFactor) * alpha);
            MatrixStack.Entry matrix = stack.method_23760();
            consumer.method_56824(matrix, 0.0f, -size, 0.0f).method_22913(0.0f, 0.0f).method_39415(color2.getRGB());
            consumer.method_56824(matrix, -size, -size, 0.0f).method_22913(0.0f, 1.0f).method_39415(color1.getRGB());
            consumer.method_56824(matrix, -size, 0.0f, 0.0f).method_22913(1.0f, 1.0f).method_39415(color2.getRGB());
            consumer.method_56824(matrix, 0.0f, 0.0f, 0.0f).method_22913(1.0f, 0.0f).method_39415(color1.getRGB());
            stack.method_22909();
            ++i;
        }
    }

    static enum Image {
        Glow("glow"),
        Triangle("triangle");

        public final Identifier id;

        private Image(String image) {
            this.id = Identifier.method_60655((String)"leet", (String)("images/world/ghost-" + image + ".png"));
        }
    }

    @FunctionalInterface
    static interface Transformation {
        public Vec3d make(double var1, double var3);
    }
}

