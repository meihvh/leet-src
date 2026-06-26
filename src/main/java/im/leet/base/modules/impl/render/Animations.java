/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.Arm
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Quaternionfc
 *  org.joml.Vector2f
 */
package im.leet.base.modules.impl.render;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.point.PointSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.easings.EasingEnum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionfc;
import org.joml.Vector2f;

public class Animations
extends Module {
    public static final Animations INSTANCE = new Animations();
    private final Group animation = this.group("Animation").toggleable(true);
    public final SliderSetting speed = this.animation.sliderSetting("Speed", 10.0f, 1.0f, 20.0f);
    public final SliderSetting strength = this.animation.sliderSetting("Strength", 10.0f, 1.0f, 20.0f);
    public final EnumSetting<SwingAnimation> swing = this.animation.enumSetting("Swing mode", SwingAnimation.Tap);
    public final EnumSetting<EasingEnum> easeProgress = this.animation.enumSetting("Progress easing", EasingEnum.Linear);
    private final Group hands = this.group("Hands position").toggleable(true);
    public final PointSetting rightHand = this.hands.pointSetting("Right hand", new Vector2f(0.0f, 0.0f));
    public final PointSetting leftHand = this.hands.pointSetting("Left hand", new Vector2f(0.0f, 0.0f));
    public final SliderSetting right = this.hands.sliderSetting("Right hand scale", 1.0f, 0.1f, 1.0f).increment(0.01f);
    public final SliderSetting left = this.hands.sliderSetting("Left hand scale", 1.0f, 0.1f, 1.0f).increment(0.01f);
    public final SliderSetting rotateX = this.hands.sliderSetting("Rotate X", 0.0f, -180.0f, 180.0f).increment(0.1f);
    public final SliderSetting rotateY = this.hands.sliderSetting("Rotate Y", 0.0f, -180.0f, 180.0f).increment(0.1f);
    public final SliderSetting rotateZ = this.hands.sliderSetting("Rotate Z", 0.0f, -180.0f, 180.0f).increment(0.1f);

    private Animations() {
        super("Animations", Category.RENDER, "Changes the hand swing animation", new Tag[0]);
    }

    public void addArmTranslate(MatrixStack matrices, Arm arm) {
        if (!this.hands.isEnabled()) {
            return;
        }
        Vector2f trans = arm == Arm.field_6183 ? this.rightHand.get() : this.leftHand.get();
        float scale = Math.max(0.1f, 1.0f - (arm == Arm.field_6183 ? this.right.get() : this.left.get())) * 1.5f;
        matrices.method_46416(trans.x, 0.0f - trans.y, -scale);
        if (this.rotateX.get() != 0.0f) {
            matrices.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees(this.rotateX.get()));
        }
        if (this.rotateY.get() != 0.0f) {
            matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees(this.rotateY.get()));
        }
        if (this.rotateZ.get() != 0.0f) {
            matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees(this.rotateZ.get()));
        }
    }

    public boolean handleMatrix(float swingProgress, float equipProgress, MatrixStack matrices, float armX, Arm arm) {
        swingProgress = this.easeProgress.get().easef(swingProgress);
        float factor = this.strength.get() / this.strength.getMax();
        int i = arm == Arm.field_6183 ? 1 : -1;
        this.addArmTranslate(matrices, arm);
        if (i == -1 || !this.animation.isEnabled()) {
            return false;
        }
        matrices.method_46416((float)i * 0.56f, -0.52f, -0.72f);
        switch (this.swing.get().ordinal()) {
            case 0: {
                float f = MathHelper.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
                matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)i * (45.0f + f * -20.0f)));
                float g = MathHelper.method_15374((float)(MathHelper.method_15355((float)swingProgress) * (float)Math.PI));
                matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)i * g * -20.0f));
                matrices.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees(g * (-90.0f * factor) - 45.0f));
                matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)i * -90.0f));
                break;
            }
            case 1: {
                matrices.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees((float)(i * -45)));
                matrices.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees((float)((double)(i * 45) + (double)(90.0f * factor) * Math.sin((double)swingProgress * Math.PI))));
                matrices.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees((float)i * -90.0f));
                break;
            }
            case 2: 
            case 3: {
                float g = (float)Math.sin((double)swingProgress * Math.PI);
                if (this.swing.is(SwingAnimation.SpinHorizontal)) {
                    matrices.method_22907((Quaternionfc)RotationAxis.field_40717.rotationDegrees(90.0f));
                }
                matrices.method_22907((Quaternionfc)RotationAxis.field_40713.rotationDegrees((float)i * (180.0f * factor) * g));
            }
        }
        return true;
    }

    public static enum SwingAnimation {
        Tap,
        Block,
        SpinHorizontal,
        SpinVertical;

    }
}

