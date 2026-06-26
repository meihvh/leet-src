/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.player.disabler.MatrixAbuse;
import im.leet.base.modules.impl.player.disabler.TransactionDelay;
import im.leet.base.modules.impl.player.disabler.VehicleAbuse;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.MultiChoice;

public class Disabler
extends Module {
    public static final Disabler INSTANCE = new Disabler();
    final MultiChoice<Choice> disablers = this.multiChoice(new Choice[]{new MatrixAbuse(), new TransactionDelay(), new VehicleAbuse()});
    EventBus<Event> events = event -> this.disablers.onEvent(event);

    private Disabler() {
        super("Disabler", Category.PLAYER, "Attempts to disable anti-cheat checks", new Tag[0]);
    }

    @Override
    public void onEnable() {
        this.disablers.onEnabled();
    }

    @Override
    protected void onDisable() {
        this.disablers.onDisabled();
    }
}

