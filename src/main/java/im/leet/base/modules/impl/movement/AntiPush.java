/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;

public class AntiPush
extends Module {
    public static final AntiPush INSTANCE = new AntiPush();
    public final CheckBox playerPush = this.checkbox("From players", true);

    private AntiPush() {
        super("Anti Push", Category.MOVEMENT, "Prevents you from being pushed away from anything", new Tag[0]);
    }
}

