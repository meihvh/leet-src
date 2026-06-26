/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.utils.client.targetesp;

import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.targetesp.TargetRendererChoice;
import im.leet.utils.client.targetesp.impl.GhostTargetRenderer;
import im.leet.utils.client.targetesp.impl.ImageTargetRenderer;
import im.leet.utils.client.targets.TargetsUtility;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class TargetRenderer
extends ChoiceSetting<TargetRendererChoice> {
    public SmoothStepAnimation ANIMATION = new SmoothStepAnimation(300, 1.0);

    public TargetRenderer() {
        super("Target Renderer", 0, (Choice[])new TargetRendererChoice[]{new ImageTargetRenderer(), new GhostTargetRenderer()});
    }

    public void render(MatrixStack stack, VertexConsumerProvider provider) {
        if (TargetsUtility.getTarget() != null) {
            TargetsUtility.setLastTarget(TargetsUtility.getTarget());
        }
        if (TargetsUtility.getLastTarget() == null) {
            return;
        }
        this.ANIMATION.setDirection(TargetsUtility.getTarget() == null ? Direction.BACKWARDS : Direction.FORWARDS);
        stack.method_22903();
        LivingEntity target = TargetsUtility.getLastTarget();
        Vec3d pos = TargetRenderer.mc.field_1773.method_19418().method_19326().method_1020(target.method_30950(mc.method_61966().method_60637(true)));
        stack.method_22904(-pos.field_1352, -pos.field_1351, -pos.field_1350);
        ((TargetRendererChoice)this.get()).render(stack, provider, target, this.ANIMATION.getOutput());
        stack.method_22909();
    }
}

