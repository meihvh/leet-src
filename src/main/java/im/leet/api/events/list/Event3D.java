/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.VertexConsumerProvider$Immediate
 *  net.minecraft.client.util.math.MatrixStack
 */
package im.leet.api.events.list;

import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class Event3D
extends Event
implements MinecraftHolder {
    private static final Event3D instance = new Event3D();
    public MatrixStack stack;
    public VertexConsumerProvider buffer;

    public static Event3D build(MatrixStack stack, VertexConsumerProvider.Immediate immediate) {
        Event3D.instance.stack = stack;
        Event3D.instance.buffer = immediate;
        return instance;
    }
}

