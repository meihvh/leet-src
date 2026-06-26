/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.slider.SliderSetting;

public class Timer
extends Module {
    public static final Timer INSTANCE = new Timer();
    public SliderSetting speed = this.add(new SliderSetting("Speed", 1.0f, 0.1f, 5.0f).increment(0.1f));
    EventBus<Event> hui = event -> {
        if (event instanceof EventGameTick) {
            Client.TIMER = this.speed.get();
        }
    };

    private Timer() {
        super("Timer", Category.PLAYER, "Speeds up or slows down gameplay", new Tag[0]);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        Client.TIMER = 1.0f;
    }
}

