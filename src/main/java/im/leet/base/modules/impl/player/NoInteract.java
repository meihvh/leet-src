/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.player;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.AuraModule;
import im.leet.base.settings.impl.checkbox.CheckBox;

public class NoInteract
extends Module {
    public static final NoInteract INSTANCE = new NoInteract();
    public CheckBox onlyAura = this.checkbox("Only while Aura", false);

    private NoInteract() {
        super("NoInteract", Category.PLAYER, "Blocks the use of something", new Tag[0]);
    }

    public boolean shouldBlock() {
        if (!this.isEnabled()) {
            return false;
        }
        if (this.onlyAura.get()) {
            return AuraModule.INSTANCE.isEnabled();
        }
        return true;
    }
}

