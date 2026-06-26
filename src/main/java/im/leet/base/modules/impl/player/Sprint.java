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
import im.leet.base.settings.impl.checkbox.CheckBox;

public class Sprint
extends Module {
    public static final Sprint INSTANCE = new Sprint();
    public CheckBox ignoreHunger = this.checkbox("Ignore hunger", false);
    EventBus<Event> onevent = event -> {
        if (event instanceof EventGameTick) {
            EventGameTick e = (EventGameTick)event;
            if (Sprint.mc.field_1724 == null || Sprint.mc.field_1687 == null) {
                return;
            }
            if (Sprint.mc.field_1724.field_3913.field_54155.comp_3159()) {
                Sprint.mc.field_1690.field_1867.method_23481(!Sprint.mc.field_1724.method_5624());
            }
        }
    };

    private Sprint() {
        super("Sprint", Category.PLAYER, "Clamps running", new Tag[0]);
    }
}

