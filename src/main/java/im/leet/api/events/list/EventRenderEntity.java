/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.VertexConsumerProvider$Immediate
 *  net.minecraft.client.util.math.MatrixStack
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class EventRenderEntity
extends Event {
    private static EventRenderEntity instance = new EventRenderEntity();
    public VertexConsumerProvider.Immediate vertexConsumers;
    public MatrixStack matrixStack;

    public static EventRenderEntity build(VertexConsumerProvider.Immediate vertexConsumers, MatrixStack matrixStack) {
        EventRenderEntity.instance.vertexConsumers = vertexConsumers;
        EventRenderEntity.instance.matrixStack = matrixStack;
        instance.reset();
        return instance;
    }
}

