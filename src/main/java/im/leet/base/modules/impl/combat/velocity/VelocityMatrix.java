/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat.velocity;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.settings.impl.choice.Choice;

public class VelocityMatrix
extends Choice {
    public VelocityMatrix() {
        super("Matrix");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventGameTick && VelocityMatrix.mc.field_1724.field_6235 > 0 && !VelocityMatrix.mc.field_1724.method_24828()) {
            double var3 = VelocityMatrix.mc.field_1724.method_36454() * ((float)Math.PI / 180);
            double var5 = Math.sqrt(VelocityMatrix.mc.field_1724.method_18798().field_1352 * VelocityMatrix.mc.field_1724.method_18798().field_1352 + VelocityMatrix.mc.field_1724.method_18798().field_1350 * VelocityMatrix.mc.field_1724.method_18798().field_1350);
            VelocityMatrix.mc.field_1724.method_18800(-Math.sin(var3) * var5, VelocityMatrix.mc.field_1724.method_18798().field_1351, Math.cos(var3) * var5);
            VelocityMatrix.mc.field_1724.method_5728(VelocityMatrix.mc.field_1724.field_6012 % 2 != 0);
        }
    }
}

