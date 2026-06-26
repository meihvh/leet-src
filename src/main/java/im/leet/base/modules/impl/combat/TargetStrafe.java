/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.EventPriority;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.targetstrafe.MotionTargetStrafe;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;

public class TargetStrafe
extends Module {
    public static final TargetStrafe INSTANCE = new TargetStrafe();
    final ChoiceSetting<Choice> mode = this.choiceSetting("Mode", 0, new Choice[]{MotionTargetStrafe.INSTANCE});
    @EventPriority(value=-50)
    EventBus<Event> events = event -> this.mode.onEvent(event);

    private TargetStrafe() {
        super("Target Strafe", Category.COMBAT, "Moves optimally to be in attack range of target", new Tag[0]);
    }
}

