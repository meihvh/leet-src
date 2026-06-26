/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.slider.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class SuperBow
extends Module {
    public static final SuperBow INSTANCE = new SuperBow();
    public SliderSetting pow = this.sliderSetting("\u0421\u0438\u043b\u0430", 10.0f, 10.0f, 100.0f);
    EventBus<Event> events = event -> {
        PlayerActionC2SPacket action;
        EventSendPacket esp;
        Packet<?> pkt;
        if (event instanceof EventSendPacket && (pkt = (esp = (EventSendPacket)event).getPacket()) instanceof PlayerActionC2SPacket && (action = (PlayerActionC2SPacket)pkt).method_12363() == PlayerActionC2SPacket.Action.field_12974 && SuperBow.mc.field_1724 != null && SuperBow.mc.field_1724.method_6047().method_7909() == Items.field_8102) {
            mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)SuperBow.mc.field_1724, ClientCommandC2SPacket.Mode.field_12981));
            for (int i = 0; i < (int)this.pow.get(); ++i) {
                this.sendPosRot(SuperBow.mc.field_1724.method_23318() + 1.0E-10, false);
                this.sendPosRot(SuperBow.mc.field_1724.method_23318() - 1.0E-10, true);
            }
        }
    };

    private SuperBow() {
        super("SuperBow", Category.COMBAT, "Increases bow damage", new Tag[0]);
    }

    private void sendPosRot(double y, boolean onGround) {
        if (SuperBow.mc.field_1724 == null) {
            return;
        }
        if (SuperBow.mc.field_1724 == null) {
            return;
        }
        mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.Full(SuperBow.mc.field_1724.method_23317(), y, SuperBow.mc.field_1724.method_23321(), SuperBow.mc.field_1724.method_36454(), SuperBow.mc.field_1724.method_36455(), onGround, false));
    }
}

