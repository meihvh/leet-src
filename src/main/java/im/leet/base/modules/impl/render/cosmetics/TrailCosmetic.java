/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.option.Perspective
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.util.math.MatrixStack$Entry
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.render.cosmetics;

import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.render.system.ClientPipelines;
import im.leet.base.modules.Module;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class TrailCosmetic
extends Choice {
    public static final TrailCosmetic INSTANCE = new TrailCosmetic();
    Position last;

    private TrailCosmetic() {
        super("Trail");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventGameTick) {
            if (Module.nullCheck() || TrailCosmetic.mc.field_1724.field_6012 % 2 != 0) {
                return;
            }
            this.last = Position.fromPlayer();
        }
        if (event instanceof Event3D) {
            Perspective perspective;
            Event3D e = (Event3D)event;
            if (TrailCosmetic.mc.field_1690 != null && (perspective = TrailCosmetic.mc.field_1690.method_31044()).method_31034() && mc.method_1560() == TrailCosmetic.mc.field_1724) {
                return;
            }
            e.stack.method_22903();
            VertexConsumer v = e.buffer.getBuffer((RenderLayer)ClientPipelines.WORLD_TRIANGLE_STRIP);
            Vec3d camera = TrailCosmetic.mc.field_1773.method_19418().method_19326();
            e.stack.method_46416((float)(-camera.field_1352), (float)(-camera.field_1351), (float)(-camera.field_1350));
            Color color = ColorUtility.injectAlpha(ClientSettings.INSTANCE.getColor(0), 150.0f);
            MatrixStack.Entry matrix = e.stack.method_23760();
            float tickDelta = mc.method_61966().method_60637(true);
            Position position = Position.fromPlayer();
            float nheight = position.height;
            float noffset = nheight * 0.1f;
            float nx = MathUtility.linear(position.x1, position.x2, tickDelta);
            float ny = MathUtility.linear(position.y1, position.y2, tickDelta);
            float nz = MathUtility.linear(position.z1, position.z2, tickDelta);
            if (this.last != null) {
                float pheight = this.last.height;
                float poffset = pheight * 0.1f;
                float px = MathUtility.linear(this.last.x1, this.last.x2, tickDelta);
                float py = MathUtility.linear(this.last.y1, this.last.y2, tickDelta);
                float pz = MathUtility.linear(this.last.z1, this.last.z2, tickDelta);
                v.method_56824(matrix, px, py + pheight - poffset, pz).method_39415(color.getRGB());
                v.method_56824(matrix, px, py + poffset, pz).method_39415(color.getRGB());
            }
            v.method_56824(matrix, nx, ny + nheight - noffset, nz).method_39415(color.getRGB());
            v.method_56824(matrix, nx, ny + noffset, nz).method_39415(color.getRGB());
            e.stack.method_22909();
        }
    }

    record Position(float x1, float y1, float z1, float x2, float y2, float z2, float height) {
        public static Position fromPlayer() {
            return new Position((float)MinecraftHolder.mc.field_1724.field_6014, (float)MinecraftHolder.mc.field_1724.field_6036, (float)MinecraftHolder.mc.field_1724.field_5969, (float)MinecraftHolder.mc.field_1724.method_23317(), (float)MinecraftHolder.mc.field_1724.method_23318(), (float)MinecraftHolder.mc.field_1724.method_23321(), MinecraftHolder.mc.field_1724.method_17682());
        }
    }
}

