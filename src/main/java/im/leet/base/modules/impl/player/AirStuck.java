/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventSendPacket;
import im.leet.api.events.list.EventTravel;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AirStuck
extends Module {
    public static final AirStuck INSTANCE = new AirStuck();
    EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Motion));
    EventBus<Event> events = event -> {
        Event e;
        if (event instanceof EventTravel) {
            e = (EventTravel)event;
            e.cancel();
        }
        if (event instanceof EventSendPacket) {
            e = (EventSendPacket)event;
            if (((EventSendPacket)e).packet instanceof PlayerMoveC2SPacket && this.mode.is(Mode.CancelPacket)) {
                e.cancel();
            }
        }
    };

    private AirStuck() {
        super("AirStuck", Category.PLAYER, "Freezes the player", new Tag[0]);
    }

    public static enum Mode implements EnumChoice
    {
        Motion("Motion"),
        CancelPacket("Cancel packet");

        final String renderName;

        private Mode(String renderName) {
            this.renderName = renderName;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }
}

