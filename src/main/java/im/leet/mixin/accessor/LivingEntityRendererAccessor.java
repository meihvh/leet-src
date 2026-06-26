/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.LivingEntityRenderer
 *  net.minecraft.client.render.entity.model.EntityModel
 *  net.minecraft.client.render.entity.state.LivingEntityRenderState
 *  net.minecraft.client.util.math.MatrixStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package im.leet.mixin.accessor;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={LivingEntityRenderer.class})
public interface LivingEntityRendererAccessor {
    @Accessor(value="model")
    public EntityModel<?> getModelAccessor();

    @Invoker(value="setupTransforms")
    public void leet$setupTransforms(LivingEntityRenderState var1, MatrixStack var2, float var3, float var4);
}

