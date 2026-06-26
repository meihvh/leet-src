/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;

public class AntiBounce
extends Module {
    public static final AntiBounce INSTANCE = new AntiBounce();

    private AntiBounce() {
        super("AntiBounce", Category.MOVEMENT, "Prevents bounces from slime blocks", new Tag[0]);
    }
}

