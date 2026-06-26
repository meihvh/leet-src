/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.backtrack.BacktrackChoice;
import im.leet.base.modules.impl.combat.backtrack.BacktrackGrim;
import im.leet.base.modules.impl.combat.backtrack.BacktrackLag;
import im.leet.base.settings.impl.choice.ChoiceSetting;

public class Backtrack
extends Module {
    public static final Backtrack INSTANCE = new Backtrack();
    private final ChoiceSetting<BacktrackChoice> mode = this.choiceSetting("Mode", 0, new BacktrackChoice[]{new BacktrackLag(), new BacktrackGrim()});
    EventBus<Event> events = event -> this.mode.onEvent(event);

    private Backtrack() {
        super("Backtrack", Category.COMBAT, "Uses lag to attack more efficiently", new Tag[0]);
    }

    @Override
    protected void onDisable() {
        this.mode.onDisabled();
    }

    @Override
    protected void onEnable() {
        this.mode.onEnabled();
    }
}

