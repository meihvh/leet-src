/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.utils.network.BlinkUtility;

public class Blink
extends Module {
    public static final Blink INSTANCE = new Blink();
    final BlinkUtility utility = new BlinkUtility();
    EventBus<Event> events = event -> {
        if (event instanceof EventSendPacket) {
            EventSendPacket e = (EventSendPacket)event;
            this.utility.queue(e);
        }
    };

    private Blink() {
        super("Blink", Category.PLAYER, "Delays outgoing packets", new Tag[0]);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.utility.flush();
    }
}

