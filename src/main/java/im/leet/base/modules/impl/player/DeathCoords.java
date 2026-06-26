/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket
 *  net.minecraft.text.ClickEvent
 *  net.minecraft.text.ClickEvent$CopyToClipboard
 *  net.minecraft.text.Text
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.utils.client.ChatUtility;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

public class DeathCoords
extends Module {
    public static final DeathCoords INSTANCE = new DeathCoords();
    EventBus<Event> events = event -> {
        if (event instanceof EventSendPacket) {
            EventSendPacket ev = (EventSendPacket)event;
            if (ev.packet instanceof DeathMessageS2CPacket) {
                String text = String.format("%.0f %.0f %.0f", DeathCoords.mc.field_1724.method_23317(), DeathCoords.mc.field_1724.method_23318(), DeathCoords.mc.field_1724.method_23321());
                ChatUtility.send((Text)Text.method_43470((String)text).method_27694(style -> style.method_10958((ClickEvent)new ClickEvent.CopyToClipboard(text))));
            }
        }
    };

    private DeathCoords() {
        super("DeathCoords", Category.PLAYER, "Sends death point coordinates to chat", new Tag[0]);
    }
}

