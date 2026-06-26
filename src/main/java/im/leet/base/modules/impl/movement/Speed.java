/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.EventPriority;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.movement.speed.SpeedGrimGround;
import im.leet.base.modules.impl.movement.speed.SpeedGrimTest;
import im.leet.base.modules.impl.movement.speed.SpeedIntave;
import im.leet.base.modules.impl.movement.speed.SpeedSpartan4043;
import im.leet.base.modules.impl.movement.speed.SpeedVanilla;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;

public class Speed
extends Module {
    public static final Speed INSTANCE = new Speed();
    ChoiceSetting<Choice> mode = this.choiceSetting("Mode", 0, new Choice[]{new SpeedSpartan4043(), new SpeedGrimGround(), new SpeedIntave(), new SpeedGrimTest(), new SpeedVanilla()});
    @EventPriority(value=-50)
    EventBus<Event> events = event -> this.mode.onEvent(event);

    private Speed() {
        super("Speed", Category.MOVEMENT, "Speeds up the player", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        this.mode.onEnabled();
    }

    @Override
    protected void onDisable() {
        this.mode.onDisabled();
    }
}

