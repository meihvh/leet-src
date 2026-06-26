/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket
 */
package im.leet.base.modules.impl.movement;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventNoSlow;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

public class NoSlowDown
extends Module {
    public static NoSlowDown INSTANCE = new NoSlowDown();
    private final Group mainSettings = this.group("No Slow Down settings");
    public EnumSetting<Modes> mode = this.mainSettings.enumSetting("Modes", Modes.Default);
    public CheckBox noserverswapSetting = this.mainSettings.checkbox("Fix Server Swap", true);
    public SliderSetting ticksOnSlow = (SliderSetting)this.mainSettings.sliderSetting("Ticks", 1.0f, 1.0f, 4.0f).increment(1.0f).visible(() -> this.mode.is(Modes.GrimLatest));
    int ticks = 0;
    EventBus<Event> onEvent = event -> {
        EventReceivePacket eventReceivePacket;
        Packet<?> patt0$temp;
        if (event instanceof EventGameTick) {
            if (NoSlowDown.mc.field_1724.method_6115() && this.mode.is(Modes.GrimLatest)) {
                ++this.ticks;
            }
            if (!NoSlowDown.mc.field_1724.method_6115() && this.mode.is(Modes.Matrix)) {
                this.ticks = 0;
            }
        }
        if (event instanceof EventReceivePacket && (patt0$temp = (eventReceivePacket = (EventReceivePacket)event).getPacket()) instanceof UpdateSelectedSlotS2CPacket) {
            UpdateSelectedSlotS2CPacket packet = (UpdateSelectedSlotS2CPacket)patt0$temp;
            if (this.noserverswapSetting.get() && packet.comp_3325() != NoSlowDown.mc.field_1724.method_31548().method_67532()) {
                NetworkUtility.sendWithoutEvent(new UpdateSelectedSlotC2SPacket(NoSlowDown.mc.field_1724.method_31548().method_67532()));
                event.cancel();
            }
        }
        if (event instanceof EventNoSlow) {
            EventNoSlow eventNoSlow = (EventNoSlow)event;
            switch (this.mode.get().ordinal()) {
                case 0: {
                    eventNoSlow.cancel();
                    break;
                }
                case 1: {
                    if (!((float)this.ticks >= this.ticksOnSlow.get())) break;
                    eventNoSlow.cancel();
                    this.ticks = 0;
                    break;
                }
                case 2: {
                    if (NoSlowDown.mc.field_1724.field_6235 > 0) {
                        this.ticks = 1;
                    }
                    if (this.ticks == 1) {
                        eventNoSlow.cancel();
                        break;
                    }
                    if (NoSlowDown.mc.field_1724.field_6012 % 3 != 0) break;
                    eventNoSlow.cancel();
                }
            }
        }
    };

    public NoSlowDown() {
        super("No Slow Down", Category.MOVEMENT, "Removes the slowdown when using an item", new Tag[0]);
    }

    public static enum Modes implements EnumChoice
    {
        Default("Vanilla"),
        GrimLatest("Grim Tick"),
        Matrix("Matrix Universal");

        final String renderName;

        private Modes(String renderName) {
            this.renderName = renderName;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }
}

