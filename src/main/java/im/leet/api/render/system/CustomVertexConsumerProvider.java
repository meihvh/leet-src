/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.VertexConsumerProvider$Immediate
 *  net.minecraft.client.render.VertexConsumers
 *  net.minecraft.client.util.BufferAllocator
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.ColorHelper
 */
package im.leet.api.render.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

@Environment(value=EnvType.CLIENT)
public class CustomVertexConsumerProvider
implements VertexConsumerProvider {
    private final VertexConsumerProvider.Immediate parent;
    private final VertexConsumerProvider.Immediate plainDrawer = VertexConsumerProvider.method_22991((BufferAllocator)new BufferAllocator(1536));
    private int red = 255;
    private int green = 255;
    private int blue = 255;
    private int alpha = 255;

    public CustomVertexConsumerProvider(VertexConsumerProvider.Immediate parent) {
        this.parent = parent;
    }

    public VertexConsumer getBuffer(RenderLayer renderLayer) {
        VertexConsumer vertexConsumer = this.parent.getBuffer(RenderLayer.method_65215((Identifier)Identifier.method_60654((String)"")));
        VertexConsumer vertexConsumer2 = this.plainDrawer.getBuffer(RenderLayer.method_65215((Identifier)Identifier.method_60654((String)"")));
        OutlineVertexConsumer outlineVertexConsumer = new OutlineVertexConsumer(vertexConsumer2, this.red, this.green, this.blue, this.alpha);
        return VertexConsumers.method_24037((VertexConsumer)outlineVertexConsumer, (VertexConsumer)vertexConsumer);
    }

    public void setColor(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public void draw() {
        this.plainDrawer.method_22993();
    }

    @Environment(value=EnvType.CLIENT)
    record OutlineVertexConsumer(VertexConsumer delegate, int color) implements VertexConsumer
    {
        public OutlineVertexConsumer(VertexConsumer delegate, int red, int green, int blue, int alpha) {
            this(delegate, ColorHelper.method_61324((int)alpha, (int)red, (int)green, (int)blue));
        }

        public VertexConsumer method_22912(float x, float y, float z) {
            this.delegate.method_22912(x, y, z).method_39415(this.color);
            return this;
        }

        public VertexConsumer method_1336(int red, int green, int blue, int alpha) {
            return this;
        }

        public VertexConsumer method_22913(float u, float v) {
            this.delegate.method_22913(u, v);
            return this;
        }

        public VertexConsumer method_60796(int u, int v) {
            return this;
        }

        public VertexConsumer method_22921(int u, int v) {
            return this;
        }

        public VertexConsumer method_22914(float x, float y, float z) {
            return this;
        }
    }
}

