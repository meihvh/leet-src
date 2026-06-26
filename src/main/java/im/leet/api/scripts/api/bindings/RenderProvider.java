/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.util.math.MatrixStack$Entry
 *  org.graalvm.polyglot.HostAccess$Export
 *  org.joml.Vector4f
 */
package im.leet.api.scripts.api.bindings;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.scripts.api.bindings.FontProvider;
import java.awt.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.graalvm.polyglot.HostAccess;
import org.joml.Vector4f;

public class RenderProvider {
    VertexConsumer last = null;

    @HostAccess.Export
    public void rect(double x, double y, double width, double height, double round, Color color) {
        Client.RENDERER.rect((float)x, (float)y, (float)width, (float)height, new Vector4f((float)round), 1.0f, color, color, color, color);
    }

    @HostAccess.Export
    public void string(String text, String id, double size, double x, double y, Color color) {
        FontProvider provider = FontProvider.valueOf(id);
        if (TextureUse.values().length < provider.ordinal()) {
            return;
        }
        TextureUse using = TextureUse.values()[provider.ordinal()];
        Client.RENDERER.text(text, (float)x, (float)y, using, (float)size, color);
    }

    @HostAccess.Export
    public float width(String text, String id, double size) {
        FontProvider provider = FontProvider.valueOf(id);
        if (TextureUse.values().length < provider.ordinal()) {
            return -1.0f;
        }
        TextureUse using = TextureUse.values()[provider.ordinal()];
        return Client.RENDERER.textWidth(text, using, (float)size);
    }

    @HostAccess.Export
    public VertexConsumer lineBuffer(VertexConsumerProvider provider) {
        this.last = provider.getBuffer((RenderLayer)RenderLayer.field_21695);
        return this.last;
    }

    @HostAccess.Export
    public VertexConsumer quadBuffer(VertexConsumerProvider provider) {
        this.last = provider.getBuffer(RenderLayer.method_49042());
        return this.last;
    }

    @HostAccess.Export
    public MatrixStack.Entry entry(MatrixStack in) {
        return in.method_23760();
    }

    @HostAccess.Export
    public RenderProvider vertex(MatrixStack.Entry stack, double x, double y, double z, Color color, double ... normal) {
        if (this.last == null) {
            return this;
        }
        this.last.method_56824(stack, (float)x, (float)y, (float)z).method_39415(color.getRGB()).method_22914((float)normal[0], (float)normal[1], (float)normal[2]);
        return this;
    }

    @HostAccess.Export
    public Color color(int r, int g, int b, int a) {
        return new Color(r, g, b, a);
    }
}

