/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.model.ModelData
 *  net.minecraft.client.model.ModelPart
 *  net.minecraft.client.model.ModelPartBuilder
 *  net.minecraft.client.model.ModelPartData
 *  net.minecraft.client.model.ModelTransform
 *  net.minecraft.client.model.TexturedModelData
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.entity.model.EntityModel
 *  net.minecraft.client.util.math.MatrixStack
 */
package im.leet.base.modules.impl.render.customyaica;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value=EnvType.CLIENT)
public class CrazyRabbitModel {
    private final ModelPart root;
    public final ModelPart torso;
    public final ModelPart head;
    public final ModelPart leftArm;
    public final ModelPart rightArm;
    public final ModelPart leftLeg;
    public final ModelPart rightLeg;

    public CrazyRabbitModel() {
        TexturedModelData data = CrazyRabbitModel.createTexturedModelData();
        this.root = data.method_32109();
        this.torso = this.root.method_32086("torso");
        this.head = this.root.method_32086("head");
        this.leftArm = this.root.method_32086("left_arm");
        this.rightArm = this.root.method_32086("right_arm");
        this.leftLeg = this.root.method_32086("left_leg");
        this.rightLeg = this.root.method_32086("right_leg");
    }

    public static TexturedModelData createTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.method_32111();
        int texW = 100;
        int texH = 80;
        root.method_32117("torso", ModelPartBuilder.method_32108().method_32101(28, 45).method_32097(-5.0f, -13.0f, -5.0f, 10.0f, 11.0f, 8.0f), ModelTransform.method_32090((float)0.0f, (float)24.0f, (float)0.0f));
        root.method_32117("left_arm", ModelPartBuilder.method_32108().method_32101(0, 64).method_32097(0.0f, 0.0f, -2.0f, 2.0f, 8.0f, 4.0f), ModelTransform.method_32091((float)5.0f, (float)11.0f, (float)-1.0f, (float)0.0f, (float)0.0f, (float)-0.0873f));
        root.method_32117("right_arm", ModelPartBuilder.method_32108().method_32101(0, 64).method_32096().method_32097(-2.0f, 0.0f, -2.0f, 2.0f, 8.0f, 4.0f), ModelTransform.method_32091((float)-5.0f, (float)11.0f, (float)-1.0f, (float)0.0f, (float)0.0f, (float)0.0873f));
        root.method_32117("left_leg", ModelPartBuilder.method_32108().method_32101(0, 74).method_32097(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f), ModelTransform.method_32090((float)3.0f, (float)22.0f, (float)-1.0f));
        root.method_32117("right_leg", ModelPartBuilder.method_32108().method_32101(0, 74).method_32096().method_32097(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f), ModelTransform.method_32090((float)-3.0f, (float)22.0f, (float)-1.0f));
        ModelPartData head = root.method_32117("head", ModelPartBuilder.method_32108().method_32101(0, 45).method_32097(-4.0f, -11.0f, -4.0f, 8.0f, 11.0f, 8.0f).method_32101(0, 0).method_32097(-3.0f, 0.0f, -4.0f, 6.0f, 1.0f, 6.0f), ModelTransform.method_32090((float)0.0f, (float)10.0f, (float)-1.0f));
        head.method_32117("left_ear", ModelPartBuilder.method_32108().method_32101(46, 0).method_32097(1.0f, -20.0f, 0.0f, 3.0f, 9.0f, 1.0f), ModelTransform.field_27701);
        head.method_32117("right_ear", ModelPartBuilder.method_32108().method_32101(46, 0).method_32096().method_32097(-4.0f, -20.0f, 0.0f, 3.0f, 9.0f, 1.0f), ModelTransform.field_27701);
        return TexturedModelData.method_32110((ModelData)modelData, (int)texW, (int)texH);
    }

    public void renderTorso(MatrixStack matrices, VertexConsumer consumer, int light) {
        this.torso.method_22698(matrices, consumer, light, 0);
    }

    public void renderHead(MatrixStack matrices, VertexConsumer consumer, int light) {
        this.head.method_22698(matrices, consumer, light, 0);
    }

    public void renderLeftArm(MatrixStack matrices, VertexConsumer consumer, int light) {
        this.leftArm.method_22698(matrices, consumer, light, 0);
    }

    public void renderRightArm(MatrixStack matrices, VertexConsumer consumer, int light) {
        this.rightArm.method_22698(matrices, consumer, light, 0);
    }

    public void renderLeftLeg(MatrixStack matrices, VertexConsumer consumer, int light) {
        this.leftLeg.method_22698(matrices, consumer, light, 0);
    }

    public void renderRightLeg(MatrixStack matrices, VertexConsumer consumer, int light) {
        this.rightLeg.method_22698(matrices, consumer, light, 0);
    }

    public void renderAll(MatrixStack matrices, VertexConsumer consumer, int light) {
        this.root.method_22698(matrices, consumer, light, 0);
    }

    public void copyPoseFromBase(EntityModel<?> base) {
        ModelPart baseRoot = base.method_63512();
        ModelPart baseHead = baseRoot.method_32086("head");
        ModelPart baseLeftArm = baseRoot.method_32086("left_arm");
        ModelPart baseRightArm = baseRoot.method_32086("right_arm");
        ModelPart baseLeftLeg = baseRoot.method_32086("left_leg");
        ModelPart baseRightLeg = baseRoot.method_32086("right_leg");
        this.head.field_3654 = baseHead.field_3654;
        this.head.field_3675 = baseHead.field_3675;
        this.head.field_3674 = baseHead.field_3674;
        this.leftArm.field_3654 = baseLeftArm.field_3654;
        this.leftArm.field_3675 = baseLeftArm.field_3675;
        this.leftArm.field_3674 = baseLeftArm.field_3674 - 0.0873f;
        this.rightArm.field_3654 = baseRightArm.field_3654;
        this.rightArm.field_3675 = baseRightArm.field_3675;
        this.rightArm.field_3674 = baseRightArm.field_3674 + 0.0873f;
        this.leftLeg.field_3654 = baseLeftLeg.field_3654;
        this.leftLeg.field_3675 = baseLeftLeg.field_3675;
        this.leftLeg.field_3674 = baseLeftLeg.field_3674;
        this.rightLeg.field_3654 = baseRightLeg.field_3654;
        this.rightLeg.field_3675 = baseRightLeg.field_3675;
        this.rightLeg.field_3674 = baseRightLeg.field_3674;
    }
}

