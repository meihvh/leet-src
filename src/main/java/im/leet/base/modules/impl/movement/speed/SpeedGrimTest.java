/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement.speed;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.player.MoveUtility;

public class SpeedGrimTest
extends Choice {
    public SpeedGrimTest() {
        super("Grim Test");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventGameTick) {
            SpeedGrimTest.mc.field_1724.method_31549().field_7479 = true;
            SpeedGrimTest.mc.field_1724.method_7355();
            SpeedGrimTest.mc.field_1724.method_18800(0.0, 0.0, 0.0);
            if (SpeedGrimTest.mc.field_1690.field_1903.method_1434()) {
                SpeedGrimTest.mc.field_1724.method_18800(0.0, 2.5, 0.0);
            }
            if (SpeedGrimTest.mc.field_1690.field_1832.method_1434()) {
                SpeedGrimTest.mc.field_1724.method_18800(0.0, -2.5, 0.0);
            }
            MoveUtility.setSpeed(2.5);
        }
    }
}

