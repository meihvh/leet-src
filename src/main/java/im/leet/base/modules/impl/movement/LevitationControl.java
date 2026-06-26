/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.slider.SliderSetting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class LevitationControl
extends Module {
    public static final LevitationControl INSTANCE = new LevitationControl();
    final SliderSetting upSpeed = this.sliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0432\u0432\u0435\u0440\u0445", 0.5f, 0.0f, 2.0f).increment(0.1f);
    final SliderSetting downSpeed = this.sliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0432\u043d\u0438\u0437", 0.5f, 0.0f, 2.0f).increment(0.1f);
    final CheckBox stuck = this.checkbox("\u0417\u0430\u0432\u0438\u0441\u0430\u0442\u044c", true);
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (LevitationControl.mc.field_1724 == null || LevitationControl.mc.field_1687 == null) {
                return;
            }
            if (!LevitationControl.mc.field_1724.method_6059(StatusEffects.field_5902)) {
                return;
            }
            boolean jump = LevitationControl.mc.field_1690.field_1903.method_1434();
            boolean sneak = LevitationControl.mc.field_1690.field_1832.method_1434();
            Double targetY = null;
            if (jump) {
                targetY = this.upSpeed.get();
            } else if (sneak) {
                targetY = -this.downSpeed.get();
            } else if (this.stuck.get()) {
                targetY = 0.0;
            }
            if (targetY != null) {
                Vec3d vel = LevitationControl.mc.field_1724.method_18798();
                LevitationControl.mc.field_1724.method_18800(vel.field_1352, targetY.doubleValue(), vel.field_1350);
            }
        }
    };

    private LevitationControl() {
        super("LevitationControl", Category.MOVEMENT, "Gives control over the levitation effect", new Tag[0]);
    }
}

