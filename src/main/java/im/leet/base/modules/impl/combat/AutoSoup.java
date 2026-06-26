/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.util.Hand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.InventoryUtility;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoSoup
extends Module {
    public static final AutoSoup INSTANCE = new AutoSoup();
    public final Group general = this.group("General");
    public final SliderSetting trigger = (SliderSetting)this.general.sliderSetting("Health", 4.0f, 1.0f, 20.0f).desc("Trigger health to eat soup");
    final TimeUtility delay = new TimeUtility();
    EventBus<Event> events = event -> {
        int slot;
        if (event instanceof EventGameTick && this.delay.reached(200L) && AutoSoup.mc.field_1724.method_6032() < this.trigger.get() && (slot = InventoryUtility.find(Items.field_8208)) < 9 && slot != -1) {
            if (AutoSoup.mc.field_1724.method_31548().method_67532() != slot) {
                mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(slot));
            }
            NetworkUtility.sendUse(Hand.field_5808);
            mc.method_1562().method_52787((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.field_12975, BlockPos.field_10980, Direction.field_11036));
            if (AutoSoup.mc.field_1724.method_31548().method_67532() != slot) {
                mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(AutoSoup.mc.field_1724.method_31548().method_67532()));
            }
            this.delay.reset();
        }
    };

    private AutoSoup() {
        super("Auto Soup", Category.COMBAT, "Automatically drinks soup", Tag.v1_8);
    }
}

