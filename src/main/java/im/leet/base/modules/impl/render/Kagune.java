/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.option.Perspective
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Quaternionfc
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
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionfc;

public class Kagune
extends Module {
    public static final Kagune INSTANCE = new Kagune();
    private final SliderSetting trailLength = this.sliderSetting("Trail Length", 15.0f, 5.0f, 30.0f).increment(1.0f);
    private final SliderSetting size = this.sliderSetting("Size", 0.3f, 0.1f, 1.5f).increment(0.05f);
    private final SliderSetting alpha = this.sliderSetting("Alpha", 0.7f, 0.1f, 1.0f).increment(0.05f);
    private final SliderSetting distance = this.sliderSetting("Distance", 0.5f, 0.1f, 2.0f).increment(0.1f);
    private final EnumSetting<ColorMode> colorMode = this.enumSetting("Color Mode", ColorMode.Client);
    private final ColorSetting customColor = (ColorSetting)this.colorSetting("Custom Color", new Color(255, 100, 255)).visible(() -> this.colorMode.is(ColorMode.Custom));
    private static final Identifier GLOW_TEXTURE = Identifier.method_60655((String)"leet", (String)"images/world/ghost-glow.png");
    private final Deque<TrailPoint> trail = new ArrayDeque<TrailPoint>();
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            boolean moving;
            if (Kagune.mc.field_1724 == null || Kagune.mc.field_1687 == null) {
                return;
            }
            long now = System.currentTimeMillis();
            this.trail.removeIf(point -> !point.isAlive(now));
            Vec3d currentPos = Kagune.mc.field_1724.method_19538().method_1031(0.0, (double)Kagune.mc.field_1724.method_17682() / 2.0, 0.0);
            Vec3d velocity = Kagune.mc.field_1724.method_18798();
            double horizontalSq = velocity.method_37268();
            double verticalAbs = Math.abs(velocity.field_1351);
            boolean bl = moving = horizontalSq > 1.0E-4 || !Kagune.mc.field_1724.method_24828() && verticalAbs > 1.0E-4;
            if (moving) {
                double yawRad = Math.toRadians(Kagune.mc.field_1724.method_36454() + 180.0f);
                Vec3d offset = new Vec3d(Math.sin(yawRad) * (double)this.distance.get(), 0.0, -Math.cos(yawRad) * (double)this.distance.get());
                Vec3d targetPos = currentPos.method_1019(offset);
                TrailPoint head = this.trail.peekFirst();
                if (head == null || head.pos.method_1025(targetPos) > 1.0E-4) {
                    this.trail.addFirst(new TrailPoint(targetPos, now));
                }
            }
            int maxPoints = Math.max(1, Math.round(this.trailLength.get()));
            while (this.trail.size() > maxPoints) {
                this.trail.removeLast();
            }
        }
        if (event instanceof Event3D) {
            Perspective perspective;
            Event3D e = (Event3D)event;
            if (this.trail.isEmpty()) {
                return;
            }
            if (Kagune.mc.field_1690 != null && (perspective = Kagune.mc.field_1690.method_31044()).method_31034() && mc.method_1560() == Kagune.mc.field_1724) {
                return;
            }
            MatrixStack stack = e.stack;
            VertexConsumer consumer = e.buffer.getBuffer(ClientPipelines.TARGET_ESP.apply(GLOW_TEXTURE));
            Vec3d camPos = Kagune.mc.field_1773.method_19418().method_19326();
            long now = System.currentTimeMillis();
            for (TrailPoint point2 : this.trail) {
                float ageProgress = Math.min(1.0f, (float)(now - point2.createdAt) / 1000.0f);
                float pointAlpha = (1.0f - ageProgress) * this.alpha.get();
                float pointSize = this.size.get() * (1.0f - ageProgress * 0.5f);
                Vec3d renderPos = point2.pos;
                Color color = this.colorMode.is(ColorMode.Rainbow) ? ColorUtility.rainbowEffect(10, (int)(ageProgress * 100.0f)) : (this.colorMode.is(ColorMode.Custom) ? this.customColor.get() : ClientSettings.INSTANCE.getColor((int)(ageProgress * 90.0f)));
                color = ColorUtility.injectAlpha(color, (int)(pointAlpha * 255.0f));
                int argb = color.getRGB();
                stack.method_22903();
                stack.method_22904(renderPos.field_1352 - camPos.field_1352, renderPos.field_1351 - camPos.field_1351, renderPos.field_1350 - camPos.field_1350);
                stack.method_22907((Quaternionfc)Kagune.mc.field_1773.method_19418().method_23767());
                consumer.method_56824(stack.method_23760(), -pointSize, -pointSize, 0.0f).method_39415(argb).method_22913(0.0f, 0.0f);
                consumer.method_56824(stack.method_23760(), -pointSize, pointSize, 0.0f).method_39415(argb).method_22913(0.0f, 1.0f);
                consumer.method_56824(stack.method_23760(), pointSize, pointSize, 0.0f).method_39415(argb).method_22913(1.0f, 1.0f);
                consumer.method_56824(stack.method_23760(), pointSize, -pointSize, 0.0f).method_39415(argb).method_22913(1.0f, 0.0f);
                stack.method_22909();
            }
        }
    };

    private Kagune() {
        super("Kagune", Category.RENDER, "Ghost glow trail effect behind player", new Tag[0]);
    }

    @Override
    public void onDisable() {
        this.trail.clear();
        super.onDisable();
    }

    static enum ColorMode {
        Client,
        Rainbow,
        Custom;

    }

    static final class TrailPoint {
        static final long LIFETIME = 1000L;
        final Vec3d pos;
        final long createdAt;

        TrailPoint(Vec3d pos, long createdAt) {
            this.pos = pos;
            this.createdAt = createdAt;
        }

        boolean isAlive(long now) {
            return now - this.createdAt < 1000L;
        }
    }
}

