/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import java.util.ArrayList;
import java.util.Iterator;

public class Scheduler {
    private final ArrayList<SchedulerAction> actions = new ArrayList();
    EventBus<Event> ticker = event -> {
        if (event instanceof EventGameTick) {
            if (this.actions.isEmpty()) {
                return;
            }
            Iterator<SchedulerAction> iterator = this.actions.iterator();
            while (iterator.hasNext()) {
                SchedulerAction action = iterator.next();
                --action.tick;
                if (action.tick > 0) continue;
                action.action.run();
                if (!action.forever) {
                    iterator.remove();
                    continue;
                }
                action.tick = action.startTick;
            }
        }
    };

    public Scheduler() {
        Client.EVENTS.register(this);
    }

    public void scheduleOnce(Runnable runnable, int ticks) {
        this.actions.add(new SchedulerAction(runnable, ticks, false));
    }

    public void scheduleForever(Runnable runnable, int interval) {
        this.actions.add(new SchedulerAction(runnable, interval, true));
    }

    private static class SchedulerAction {
        int tick;
        int startTick;
        Runnable action;
        boolean forever;

        public SchedulerAction(Runnable action, int tick, boolean forever) {
            this.action = action;
            this.tick = tick;
            this.startTick = tick;
            this.forever = forever;
        }
    }
}

