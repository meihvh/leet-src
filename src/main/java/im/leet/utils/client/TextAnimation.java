/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.client;

import im.leet.Client;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;

public class TextAnimation {
    String[] texts;
    StringBuilder builder = new StringBuilder();
    int delay = 1;
    int delayTick = 0;
    int interval = 5;
    int intervalTick = 0;
    int index = 0;
    int charindex = 0;
    EventBus<EventGameTick> event = e -> {
        if (this.intervalTick > 0) {
            --this.intervalTick;
            return;
        }
        if (this.delayTick > 0) {
            --this.delayTick;
            return;
        }
        if (this.index >= this.texts.length) {
            return;
        }
        if (this.delayTick == 0 && !this.texts[this.index].isEmpty() && this.charindex < this.texts[this.index].length()) {
            this.delayTick = this.delay;
            this.builder.append(this.texts[this.index].charAt(this.charindex));
            ++this.charindex;
        }
        if (this.charindex >= this.texts[this.index].length()) {
            this.charindex = 0;
            ++this.index;
            this.intervalTick = this.interval;
        }
    };

    public TextAnimation() {
        Client.EVENTS.register(this);
    }

    public TextAnimation delay(int delay) {
        this.delay = delay;
        return this;
    }

    public TextAnimation interval(int interval) {
        this.interval = interval;
        return this;
    }

    public TextAnimation texts(String ... texts) {
        this.texts = texts;
        return this;
    }

    public String current() {
        return this.texts[Math.min(this.texts.length - 1, this.index)];
    }

    public TextAnimation reset() {
        this.index = 0;
        this.charindex = 0;
        this.builder.setLength(0);
        return this;
    }

    public boolean done() {
        return this.index >= this.texts.length;
    }

    public String get() {
        return this.builder.toString();
    }
}

