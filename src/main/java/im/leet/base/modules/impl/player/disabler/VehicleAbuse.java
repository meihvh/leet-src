/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.player.disabler;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class VehicleAbuse
extends Choice {
    public VehicleAbuse() {
        super("Vehicle Abuse");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventGameTick) {
            EventGameTick e = (EventGameTick)event;
            NetworkUtility.send(new VehicleMoveC2SPacket(new Vec3d(VehicleAbuse.mc.field_1724.method_23317(), VehicleAbuse.mc.field_1724.method_23318(), VehicleAbuse.mc.field_1724.method_23321()), VehicleAbuse.mc.field_1724.method_36454(), VehicleAbuse.mc.field_1724.method_36455(), false));
        }
    }
}

