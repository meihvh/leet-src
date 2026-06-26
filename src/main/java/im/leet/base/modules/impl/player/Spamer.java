/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.multitext.MultiTextSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.TimeUtility;
import java.util.List;

public class Spamer
extends Module {
    public static final Spamer INSTANCE = new Spamer();
    private final MultiTextSetting messages = this.multiTextSetting("Messages", new String[0]);
    private final SliderSetting delay = this.sliderSetting("Delay", 1.0f, 0.0f, 10.0f).increment(0.1f);
    private final TimeUtility timer = new TimeUtility();
    private int messageIndex;
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick && !Spamer.nullCheck()) {
            this.spam();
        }
    };

    private Spamer() {
        super("Spamer", Category.PLAYER, "\u0433\u0430\u043c\u043d\u043e", Tag.DEV);
    }

    private void spam() {
        String message;
        if (!this.timer.reached((long)(this.delay.get() * 1000.0f), true)) {
            return;
        }
        List<String> list = this.messages.get();
        if (list.isEmpty()) {
            return;
        }
        this.messageIndex %= list.size();
        if ((message = list.get(this.messageIndex++).trim()).isEmpty()) {
            return;
        }
        mc.method_1562().method_45729(message);
    }
}

