/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement.flight;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventMove;
import im.leet.base.settings.impl.choice.Choice;

public class MatrixFallFly
extends Choice {
    public MatrixFallFly() {
        super("Matrix Fall");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMove) {
            EventMove e = (EventMove)event;
            if ((MatrixFallFly.mc.field_1724.method_24828() || MatrixFallFly.mc.field_1724.field_6017 > 0.0) && !MatrixFallFly.mc.field_1724.method_6128()) {
                e.ground = true;
                MatrixFallFly.mc.field_1724.method_18800(0.0, 3.0, 0.0);
            }
        }
    }
}

