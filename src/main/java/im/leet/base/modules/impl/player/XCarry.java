/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry
extends Module {
    public static final XCarry INSTANCE = new XCarry();
    EventBus<Event> events = event -> {
        if (event instanceof EventSendPacket) {
            CloseHandledScreenC2SPacket zalupa2;
            EventSendPacket zalupa = (EventSendPacket)event;
            Packet patt0$temp = zalupa.packet;
            if (patt0$temp instanceof CloseHandledScreenC2SPacket && (zalupa2 = (CloseHandledScreenC2SPacket)patt0$temp).method_36168() == 0) {
                zalupa.cancel();
            }
        }
    };

    private XCarry() {
        super("XCarry", Category.PLAYER, "Stores items in crafting slots in your inventory", new Tag[0]);
    }
}

