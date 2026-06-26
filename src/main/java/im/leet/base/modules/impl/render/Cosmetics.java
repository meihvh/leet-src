/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.render;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.render.cosmetics.HatCosmetic;
import im.leet.base.modules.impl.render.cosmetics.TrailCosmetic;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.MultiChoice;

public class Cosmetics
extends Module {
    public static final Cosmetics INSTANCE = new Cosmetics();
    private final MultiChoice<Choice> modes = this.multiChoice(new Choice[]{HatCosmetic.INSTANCE, TrailCosmetic.INSTANCE});
    EventBus<Event> events = event -> this.modes.onEvent(event);

    private Cosmetics() {
        super("Cosmetics", Category.RENDER, "Draws cosmetics on your player model", new Tag[0]);
    }
}

