/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.RotationAxis
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 */
package im.leet.base.modules.impl.render.renderer;

import im.leet.MinecraftHolder;
import im.leet.api.events.list.Event3D;
import im.leet.api.render.system.ClientPipelines;
import im.leet.base.modules.impl.render.Particles;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.ColorUtility;
import java.util.List;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public final class ParticleRenderer {
    private static final Identifier TRIANGLE_TEX = Identifier.method_60655((String)"leet", (String)"images/world/triangle.png");
    private static final Identifier CROSS_TEX = Identifier.method_60655((String)"leet", (String)"images/world/cross.png");

    public static void renderAll(Event3D e, List<Particles.Particle> particles, float size) {
        if (particles.isEmpty()) {
            return;
        }
        if (Particles.INSTANCE.mode.is(Particles.Mode.Default)) {
            ParticleRenderer.renderPass(e, particles, size, TRIANGLE_TEX);
        }
        if (Particles.INSTANCE.mode.is(Particles.Mode.Cross)) {
            ParticleRenderer.renderPass(e, particles, size, CROSS_TEX);
        }
    }

    private static void renderPass(Event3D e, List<Particles.Particle> particles, float size, Identifier tex) {
        VertexConsumer consumer = e.buffer.getBuffer(ClientPipelines.PARTICLES.apply(tex));
        Vec3d camPos = MinecraftHolder.mc.field_1773.method_19418().method_19326();
        Quaternionf camRot = MinecraftHolder.mc.field_1773.method_19418().method_23767();
        for (Particles.Particle p : particles) {
            Vec3d pos = p.position();
            float alpha = p.alpha();
            int a = Math.max(0, Math.min(255, (int)(alpha * 255.0f)));
            int argb = ColorUtility.injectAlpha(ClientSettings.INSTANCE.getColor((int)((float)(System.currentTimeMillis() - p.start) / 1000.0f)), a).getRGB();
            e.stack.method_22903();
            e.stack.method_22904(pos.field_1352 - camPos.field_1352, pos.field_1351 - camPos.field_1351, pos.field_1350 - camPos.field_1350);
            e.stack.method_22907((Quaternionfc)camRot);
            e.stack.method_22907((Quaternionfc)RotationAxis.field_40713.rotationDegrees((float)(System.currentTimeMillis() - p.start) / 15.0f));
            e.stack.method_22907((Quaternionfc)RotationAxis.field_40717.rotationDegrees((float)(System.currentTimeMillis() - p.start) / 15.0f));
            e.stack.method_22907((Quaternionfc)RotationAxis.field_40715.rotationDegrees((float)(-(System.currentTimeMillis() - p.start)) / 15.0f));
            consumer.method_56824(e.stack.method_23760(), -size, -size, 0.0f).method_39415(argb).method_22913(0.0f, 0.0f);
            consumer.method_56824(e.stack.method_23760(), -size, size, 0.0f).method_39415(argb).method_22913(0.0f, 1.0f);
            consumer.method_56824(e.stack.method_23760(), size, size, 0.0f).method_39415(argb).method_22913(1.0f, 1.0f);
            consumer.method_56824(e.stack.method_23760(), size, -size, 0.0f).method_39415(argb).method_22913(1.0f, 0.0f);
            e.stack.method_22909();
        }
    }
}

