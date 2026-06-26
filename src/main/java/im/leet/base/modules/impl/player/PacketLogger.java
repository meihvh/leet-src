/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.common.CommonPingS2CPacket
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.utils.LogUtility;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.network.PacketSide;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PacketLogger
extends Module {
    public static final PacketLogger INSTANCE = new PacketLogger();
    final MultiEnumSetting<PacketSide> side = this.multiEnumSetting("Side", new PacketSide[]{PacketSide.SEND});
    final CheckBox transactions = this.checkbox("Transactions", false);
    int last = Integer.MIN_VALUE;
    EventBus<Event> events = event -> {
        EventPacket e;
        if (event instanceof EventPacket) {
            e = (EventPacket)event;
            if (this.side.get(e.side)) {
                MutableText text = Text.method_43470((String)("[" + (e.side == PacketSide.RECEIVE ? "receive" : "send") + "] "));
                Class clazz = e.packet.getClass();
                text.method_27693(clazz.getSimpleName());
                this.addFields(e.packet, text);
                ChatUtility.send((Text)text);
            }
        }
        if (this.transactions.get() && event instanceof EventReceivePacket) {
            e = (EventReceivePacket)event;
            Packet patt0$temp = ((EventReceivePacket)e).packet;
            if (patt0$temp instanceof CommonPingS2CPacket) {
                CommonPingS2CPacket ping = (CommonPingS2CPacket)patt0$temp;
                LogUtility.debug("TRANSACTION! " + ping.method_36950());
                if (this.last != Integer.MIN_VALUE) {
                    LogUtility.debug("DIFF = " + (ping.method_36950() - this.last));
                }
                this.last = ping.method_36950();
            }
        }
    };

    private PacketLogger() {
        super("Packet Logger", Category.PLAYER, "Logs packets.", Tag.DEV);
    }

    @Override
    protected void onEnable() {
        this.last = Integer.MIN_VALUE;
    }

    void addFields(Packet packet, MutableText text) {
        Class clazz = packet.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            field.setAccessible(true);
            text.method_27693("\n\t");
            String value = "<null>";
            try {
                value = field.get(packet).toString();
            }
            catch (Exception exception) {
                // empty catch block
            }
            text.method_27693(field.getName() + ": " + value);
        }
    }
}

