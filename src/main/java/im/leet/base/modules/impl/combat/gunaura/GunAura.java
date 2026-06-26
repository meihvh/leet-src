/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat.gunaura;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.RotationSettings;
import im.leet.base.rotations.point.PointTracker;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.RotationUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class GunAura
extends Module {
    public static final GunAura INSTANCE = new GunAura();
    final SliderSetting range = this.sliderSetting("Range", 30.0f, 1.0f, 100.0f);
    final PointTracker pointTracker = this.add(new PointTracker());
    final RotationSettings rotations = this.add(new RotationSettings("Rotations", 35));
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            LivingEntity target = TargetsUtility.find(this.range.get(), TargetsUtility.Sort.Adaptive);
            if (target == null || !GunAura.mc.field_1724.method_6057((Entity)target)) {
                TargetsUtility.reset();
                return;
            }
            Vec3d point = this.pointTracker.getPoint((Entity)target);
            this.rotations.rotate(RotationUtility.calcRotate(point, true));
        }
    };

    private GunAura() {
        super("Gun Aura", Category.COMBAT, "Automatically shoots and reloads guns", Tag.RAGE);
    }

    @Override
    protected void onDisable() {
        TargetsUtility.reset();
    }
}

