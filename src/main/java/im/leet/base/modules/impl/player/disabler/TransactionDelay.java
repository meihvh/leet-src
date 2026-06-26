/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.s2c.common.CommonPingS2CPacket
 */
package im.leet.base.modules.impl.player.disabler;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventPacketTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.network.BlinkUtility;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;

public class TransactionDelay
extends Choice {
    final CheckBox forever = this.checkbox("Forever", false);
    final SliderSetting delay = (SliderSetting)this.sliderSetting("Delay (ms)", 1000.0f, 0.0f, 10000.0f).visible(() -> !this.forever.get());
    final BlinkUtility blink = new BlinkUtility();

    public TransactionDelay() {
        super("Transaction Delay");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventReceivePacket) {
            EventReceivePacket e = (EventReceivePacket)event;
            if (e.packet instanceof CommonPingS2CPacket) {
                this.blink.queue(e);
            }
        }
        if (event instanceof EventPacketTick) {
            this.blink.flushTime(this.delay.getLong());
        }
    }
}

