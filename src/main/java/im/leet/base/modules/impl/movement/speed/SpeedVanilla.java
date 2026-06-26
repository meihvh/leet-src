/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement.speed;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventMoveVelocity;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.slider.SliderSetting;

public class SpeedVanilla
extends Choice {
    final SliderSetting horizontal = this.sliderSetting("Horizontal", 1.0f, 0.0f, 10.0f).increment(0.01f);
    final SliderSetting vertical = this.sliderSetting("Vertical", 1.0f, 0.0f, 10.0f).increment(0.01f);

    public SpeedVanilla() {
        super("Vanilla");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMoveVelocity) {
            EventMoveVelocity e = (EventMoveVelocity)event;
            e.movement.field_1352 *= (double)this.horizontal.get();
            e.movement.field_1351 *= (double)this.vertical.get();
            e.movement.field_1350 *= (double)this.horizontal.get();
        }
    }
}

