/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 */
package im.leet.base.modules.impl.movement;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventBlockShape;
import im.leet.api.events.list.EventTravel;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class BoatFly
extends Module {
    public static final BoatFly INSTANCE = new BoatFly();
    EventBus<Event> events = event -> {
        if (event instanceof EventTravel) {
            if (!BoatFly.mc.field_1724.method_6128()) {
                NetworkUtility.sendWithoutEvent(new ClientCommandC2SPacket((Entity)BoatFly.mc.field_1724, ClientCommandC2SPacket.Mode.field_12982));
            }
            BoatFly.mc.field_1724.method_23669();
            BoatFly.mc.field_1724.method_18799(BoatFly.mc.field_1724.method_18798().method_1021((double)1.01f));
        }
        if (event instanceof EventBlockShape) {
            EventBlockShape eventBlockShape = (EventBlockShape)event;
        }
    };

    public BoatFly() {
        super("Boat fly", Category.MOVEMENT, "", new Tag[0]);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        NetworkUtility.sendOnlySneak(true);
    }
}

