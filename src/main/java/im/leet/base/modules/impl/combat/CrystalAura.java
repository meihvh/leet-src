/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.crystalaura.Bahalka;
import im.leet.base.modules.impl.combat.crystalaura.Surround;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.group.Group;

public class CrystalAura
extends Module {
    public static final CrystalAura INSTANCE = new CrystalAura();
    Group general = this.group("General");
    public CheckBox surround = (CheckBox)this.general.checkbox("Surround", true).desc("Should block possible blocks to prevent damage");
    EventBus<Event> events = event -> {
        if (this.surround.get() && Surround.handleEvent(event)) {
            return;
        }
        if (Bahalka.handleEvent(event)) {
            return;
        }
    };

    private CrystalAura() {
        super("Crystal Aura", Category.COMBAT, "Automatically places and detonates end crystals to kill enemies", Tag.RAGE, Tag.TEST);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        Surround.toDefensive.clear();
    }
}

