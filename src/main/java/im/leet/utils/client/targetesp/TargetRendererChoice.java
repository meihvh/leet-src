/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.LivingEntity
 */
package im.leet.utils.client.targetesp;

import im.leet.base.settings.impl.choice.Choice;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public abstract class TargetRendererChoice
extends Choice {
    protected TargetRendererChoice(String name) {
        super(name);
    }

    public boolean isWorld() {
        return true;
    }

    public abstract void render(MatrixStack var1, VertexConsumerProvider var2, LivingEntity var3, float var4);
}

