/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket
 *  net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;

public class FakePing
extends Module {
    public static final FakePing INSTANCE = new FakePing();
    SliderSetting delay = this.sliderSetting("Delay", 1000.0f, 0.0f, 10000.0f);
    long id = -1L;
    TimeUtility time = new TimeUtility();
    EventBus<Event> events = event -> {
        if (event instanceof EventReceivePacket) {
            EventReceivePacket e = (EventReceivePacket)event;
            Packet patt0$temp = e.packet;
            if (patt0$temp instanceof KeepAliveS2CPacket) {
                KeepAliveS2CPacket p = (KeepAliveS2CPacket)patt0$temp;
                this.id = p.method_11517();
                this.time.reset();
                e.cancel();
            }
        }
        if (event instanceof EventGameTick && this.time.reached((long)this.delay.get()) && this.id != -1L) {
            NetworkUtility.sendWithoutEvent(new KeepAliveC2SPacket(this.id));
            this.id = -1L;
        }
    };

    public FakePing() {
        super("Fake ping", Category.PLAYER, "", new Tag[0]);
    }
}

