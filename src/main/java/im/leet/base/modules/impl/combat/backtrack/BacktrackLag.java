/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat.backtrack;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.impl.combat.backtrack.BacktrackChoice;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;

public class BacktrackLag
extends BacktrackChoice {
    BlinkUtility blink = new BlinkUtility();
    TimeUtility time = new TimeUtility();

    public BacktrackLag() {
        super("Lag");
    }

    @Override
    public void onEvent(Event event) {
        Event e;
        if (event instanceof EventReceivePacket) {
            e = (EventReceivePacket)event;
            if (TargetsUtility.getTarget() != null) {
                this.blink.queue((EventPacket)e);
            }
        }
        if (event instanceof EventAttack) {
            // empty if block
        }
        if (event instanceof EventGameTick) {
            e = (EventGameTick)event;
            this.blink.flushTime(300L);
        }
    }
}

