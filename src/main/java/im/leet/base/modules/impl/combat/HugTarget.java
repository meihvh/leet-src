/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.base.modules.impl.combat.Resolver;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.point.PointTracker;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.range.RangeSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.RotationUtility;
import java.awt.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class HugTarget
extends Module {
    public static final HugTarget INSTANCE = new HugTarget();
    final SliderSetting range = this.sliderSetting("Range", 50.0f, 1.0f, 100.0f);
    final CheckBox look = this.checkbox("Look", true);
    final RangeSetting pitchRange = this.rangeSetting("Pitch range", -45.0f, 90.0f, -90.0f, 90.0f, 1.0f);
    final SliderSetting yExpand = this.sliderSetting("Y box expand", 0.01f, 0.0f, 1.0f).increment(0.01f);
    final CheckBox grimElytra = this.checkbox("Use Grim ElytraFly", true);
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            LivingEntity target;
            if (!TargetsUtility.isValid()) {
                TargetsUtility.find(this.range.get(), TargetsUtility.Sort.Distance);
            }
            if ((target = TargetsUtility.getTarget()) == null || !HugTarget.mc.field_1724.method_6057((Entity)target)) {
                return;
            }
            Vec3d point = this.getPoint(target);
            Angle angle = RotationUtility.calcRotate(point, true);
            angle.setPitch(this.pitchRange.get().clamp(angle.getPitch()));
            Client.ROTATION.rotate(LinearRotation.INSTANCE, angle, 1, false, this.look.get() ? MovementCorrection.LOOK : MovementCorrection.STRICT, 30);
        }
        if (event instanceof Event3D) {
            Event3D e = (Event3D)event;
            LivingEntity target = TargetsUtility.getTarget();
            if (target == null) {
                return;
            }
            e.stack.method_22903();
            Client.RENDERER.toCamera(e.stack);
            VertexConsumer consumer = e.buffer.getBuffer((RenderLayer)RenderLayer.field_21695);
            Vec3d point = this.getPoint(target);
            Box box = new Box(point, point).method_1014(0.1);
            Color color = Color.RED;
            VertexRendering.method_62295((MatrixStack)e.stack, (VertexConsumer)consumer, (Box)box, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            e.stack.method_22909();
        }
    };

    private HugTarget() {
        super("Hug Target", Category.COMBAT, "", new Tag[0]);
    }

    Vec3d getPoint(LivingEntity target) {
        Vec3d center = target.method_19538().method_1031(0.0, (double)target.method_17682() * 0.85, 0.0);
        double halfWidth = (double)target.method_17681() / 2.0;
        Box box = new Box(center, center).method_1009(halfWidth, (double)this.yExpand.get(), halfWidth);
        Vec3d point = PointTracker.Mode.Closest.getPoint(box, HugTarget.mc.field_1724.method_33571());
        return Resolver.INSTANCE.resolveElytra(point, AuraModule.INSTANCE.isEnabled() ? AuraModule.INSTANCE.attack.getProgress() : 1.0f);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        TargetsUtility.reset();
    }
}

