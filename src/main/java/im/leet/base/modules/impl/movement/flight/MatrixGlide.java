/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket
 */
package im.leet.base.modules.impl.movement.flight;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventTravel;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.player.MoveUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public class MatrixGlide
extends Choice {
    int ticks = 0;

    public MatrixGlide() {
        super("Matrix Glide");
    }

    @Override
    public void onEnabled() {
        this.ticks = 0;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventTravel) {
            EventTravel e = (EventTravel)event;
            int maxTick = 24;
            double motionY = 0.0;
            mc.method_1562().method_52787((Packet)new VehicleMoveC2SPacket(MatrixGlide.mc.field_1724.method_19538().method_61889(10.0), 0.0f, 0.0f, false));
            if (this.ticks > 0) {
                MoveUtility.setSpeed(0.252);
                MatrixGlide.mc.field_1724.method_18800(MatrixGlide.mc.field_1724.method_18798().field_1352, motionY, MatrixGlide.mc.field_1724.method_18798().field_1350);
                MatrixGlide.mc.field_1724.method_24830(true);
                --this.ticks;
                return;
            }
            if (MatrixGlide.mc.field_1724.field_6017 > 3.25) {
                e.cancel();
                MatrixGlide.mc.field_1724.method_18800(0.0, motionY, 0.0);
                MatrixGlide.mc.field_1724.method_24830(true);
                MatrixGlide.mc.field_1724.field_6017 = 0.0;
                MatrixGlide.mc.field_1724.method_5728(false);
                this.ticks = maxTick;
            }
        }
    }
}

