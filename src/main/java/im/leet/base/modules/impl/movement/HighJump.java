/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 */
package im.leet.base.modules.impl.movement;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class HighJump
extends Module {
    public static HighJump INSTANCE = new HighJump();
    private final Group mainSettings = this.group("High Jump settings");
    public EnumSetting<Modes> mode = this.mainSettings.enumSetting("Modes", Modes.GrimLatest);
    EventBus<Event> onEvent = event -> {
        if (event instanceof EventReceivePacket) {
            EntityVelocityUpdateS2CPacket pos;
            Packet<?> patt0$temp;
            EventReceivePacket e = (EventReceivePacket)event;
            if (this.mode.is(Modes.GrimLatest) && (patt0$temp = e.getPacket()) instanceof EntityVelocityUpdateS2CPacket && (pos = (EntityVelocityUpdateS2CPacket)patt0$temp).method_11818() == HighJump.mc.field_1724.method_5628()) {
                HighJump.mc.field_1724.method_18800(pos.method_11815(), pos.method_11816() * pos.method_11816(), pos.method_11819());
                event.cancel();
            }
        }
    };

    public HighJump() {
        super("High Jump", Category.MOVEMENT, "\u042d\u0422\u041e \u0429\u0422\u041e \u041d\u0410\u0417\u0423\u0419", new Tag[0]);
    }

    public static enum Modes implements EnumChoice
    {
        GrimLatest("Grim Explosion");

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

