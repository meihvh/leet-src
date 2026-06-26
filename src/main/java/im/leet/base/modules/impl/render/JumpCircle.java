/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Position
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.shape.VoxelShape
 *  net.minecraft.world.BlockView
 */
package im.leet.base.modules.impl.render;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.render.system.ClientPipelines;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class JumpCircle
extends Module {
    public static final JumpCircle INSTANCE = new JumpCircle();
    final CheckBox useCustomColor = this.checkbox("Custom color", false);
    final ColorSetting customColor;
    final EnumSetting<Image> image;
    private final List<Circle> circles;
    private boolean wasOnGround;
    EventBus<Event> events;

    private JumpCircle() {
        super("JumpCircle", Category.RENDER, "Draws a circle under the jump location", new Tag[0]);
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.useCustomColor::get;
        this.customColor = (ColorSetting)this.colorSetting("Color", new Color(0)).visible(supplierArray);
        this.image = this.enumSetting("Image", Image.Circle);
        this.circles = new CopyOnWriteArrayList<Circle>();
        this.wasOnGround = false;
        this.events = event -> {
            if (event instanceof EventGameTick) {
                if (JumpCircle.mc.field_1724 == null || JumpCircle.mc.field_1687 == null) {
                    return;
                }
                this.circles.removeIf(c -> !c.alive());
                boolean onGround = JumpCircle.mc.field_1724.method_24828();
                double vy = JumpCircle.mc.field_1724.method_18798().field_1351;
                if (this.wasOnGround && !onGround && vy > 0.08) {
                    Vec3d origin = JumpCircle.mc.field_1724.method_19538().method_1031(0.0, 0.1, 0.0);
                    double yBelow = this.raycastDownY(origin, 3.5);
                    double y = Double.isFinite(yBelow) ? yBelow + 0.01 : Math.floor(JumpCircle.mc.field_1724.method_5829().field_1322) + 0.01;
                    this.circles.add(new Circle(new Vec3d(origin.field_1352, y, origin.field_1350)));
                }
                this.wasOnGround = onGround;
            }
            if (event instanceof Event3D) {
                Event3D e = (Event3D)event;
                if (this.circles.isEmpty()) {
                    return;
                }
                MatrixStack stack = e.stack;
                VertexConsumer consumer = e.buffer.getBuffer(ClientPipelines.TARGET_ESP.apply(this.image.get().id));
                Vec3d camPos = JumpCircle.mc.field_1773.method_19418().method_19326();
                for (Circle c2 : this.circles) {
                    Color c1;
                    Color c22;
                    float alpha = c2.alpha();
                    float sz = c2.size();
                    int a = (int)(alpha * 255.0f);
                    if (this.useCustomColor.get()) {
                        c1 = c22 = this.customColor.get();
                    } else {
                        c1 = ClientSettings.INSTANCE.getColor(0);
                        c22 = ClientSettings.INSTANCE.getColor(90);
                    }
                    c1 = ColorUtility.injectAlpha(c1, a);
                    c22 = ColorUtility.injectAlpha(c22, a);
                    stack.method_22903();
                    stack.method_22904(c2.pos.field_1352 - camPos.field_1352, c2.pos.field_1351 - camPos.field_1351, c2.pos.field_1350 - camPos.field_1350);
                    consumer.method_56824(stack.method_23760(), -sz, 0.0f, -sz).method_39415(c22.getRGB()).method_22913(0.0f, 0.0f);
                    consumer.method_56824(stack.method_23760(), -sz, 0.0f, sz).method_39415(c1.getRGB()).method_22913(0.0f, 1.0f);
                    consumer.method_56824(stack.method_23760(), sz, 0.0f, sz).method_39415(c22.getRGB()).method_22913(1.0f, 1.0f);
                    consumer.method_56824(stack.method_23760(), sz, 0.0f, -sz).method_39415(c1.getRGB()).method_22913(1.0f, 0.0f);
                    stack.method_22909();
                }
            }
        };
    }

    private double raycastDownY(Vec3d origin, double max) {
        for (double dy = 0.0; dy <= max; dy += 0.05) {
            Vec3d p = origin.method_1023(0.0, dy, 0.0);
            BlockPos pos = BlockPos.method_49638((Position)p);
            BlockState state = JumpCircle.mc.field_1687.method_8320(pos);
            VoxelShape shape = state.method_26220((BlockView)JumpCircle.mc.field_1687, pos);
            if (shape.method_1110()) continue;
            Box box = shape.method_1107();
            return (double)pos.method_10264() + box.field_1325;
        }
        return Double.NaN;
    }

    static enum Image {
        Circle("circle");

        final Identifier id;

        private Image(String image) {
            this.id = Identifier.method_60655((String)"leet", (String)("images/world/" + image + ".png"));
        }
    }

    static class Circle {
        public final Vec3d pos;
        public final long start = System.currentTimeMillis();
        public final long durationMs = 1000L;
        public final float startSize = 0.8f;
        public final float endSize = 3.0f;

        public Circle(Vec3d pos) {
            this.pos = pos;
        }

        public float t() {
            return Math.min(1.0f, (float)(System.currentTimeMillis() - this.start) / 1000.0f);
        }

        private float easeOutCubic(float x) {
            float i = 1.0f - x;
            return 1.0f - i * i * i;
        }

        private float easeOutSine(float x) {
            return (float)Math.sin((double)x * Math.PI / 2.0);
        }

        public boolean alive() {
            return System.currentTimeMillis() - this.start <= 1000L;
        }

        public float size() {
            float k = this.easeOutCubic(this.t());
            float base = 0.8f + 2.2f * k;
            float puls = (float)Math.sin((double)System.currentTimeMillis() * 0.012) * 0.08f * (1.0f - this.t());
            return base + puls;
        }

        public float alpha() {
            return 1.0f - this.easeOutSine(this.t());
        }
    }
}

