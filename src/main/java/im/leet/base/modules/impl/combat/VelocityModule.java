/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.velocity.VelocityGrimCancel;
import im.leet.base.modules.impl.combat.velocity.VelocityGrimNew;
import im.leet.base.modules.impl.combat.velocity.VelocityMatrix;
import im.leet.base.modules.impl.combat.velocity.VelocityVanilla;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;

public class VelocityModule
extends Module {
    public static final VelocityModule INSTANCE = new VelocityModule();
    final ChoiceSetting<Choice> mode = this.choiceSetting("Mode", 0, new Choice[]{new VelocityGrimCancel(), new VelocityGrimNew(), new VelocityMatrix(), new VelocityVanilla()});
    EventBus<Event> events = event -> this.mode.onEvent(event);

    private VelocityModule() {
        super("Velocity", Category.COMBAT, "Attempts to reduce knockback taken", new Tag[0]);
    }
}

