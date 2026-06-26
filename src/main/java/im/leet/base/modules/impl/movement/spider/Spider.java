/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement.spider;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.movement.spider.SpiderGrimGround;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;

public class Spider
extends Module {
    public static final Spider INSTANCE = new Spider();
    private final ChoiceSetting<Choice> mode = this.choiceSetting("Mode", 0, new Choice[]{new SpiderGrimGround()});
    EventBus<Event> events = event -> this.mode.onEvent(event);

    private Spider() {
        super("Spider", Category.MOVEMENT, "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043f\u0440\u0435\u043e\u0434\u043e\u043b\u044f\u0442\u044c \u0432\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u044c\u043d\u044b\u0435 \u043f\u0440\u0435\u043f\u044f\u0442\u0441\u0442\u0432\u0438\u044f", new Tag[0]);
    }

    @Override
    protected void onDisable() {
        Client.TIMER = 1.0f;
    }
}

