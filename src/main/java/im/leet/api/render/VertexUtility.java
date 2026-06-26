/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.util.math.MatrixStack$Entry
 */
package im.leet.api.render;

import java.awt.Color;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public final class VertexUtility {
    public static void quad(VertexConsumer v, MatrixStack.Entry mat, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, Color color1, Color color2, Color color3, Color color4) {
        v.method_56824(mat, x1, y1, z1).method_39415(color1.getRGB()).method_56824(mat, x2, y2, z2).method_39415(color2.getRGB()).method_56824(mat, x3, y3, z3).method_39415(color3.getRGB()).method_56824(mat, x4, y4, z4).method_39415(color4.getRGB());
    }

    public static void quadTextured(VertexConsumer v, MatrixStack.Entry mat, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, Color color1, Color color2, Color color3, Color color4) {
        v.method_56824(mat, x1, y1, z1).method_39415(color1.getRGB()).method_22913(0.0f, 0.0f).method_56824(mat, x2, y2, z2).method_39415(color2.getRGB()).method_22913(0.0f, 1.0f).method_56824(mat, x3, y3, z3).method_39415(color3.getRGB()).method_22913(1.0f, 1.0f).method_56824(mat, x4, y4, z4).method_39415(color4.getRGB()).method_22913(1.0f, 0.0f);
    }

    private VertexUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

