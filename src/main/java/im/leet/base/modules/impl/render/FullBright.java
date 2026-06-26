/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.render;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;

public class FullBright
extends Module {
    public static final FullBright INSTANCE = new FullBright();

    private FullBright() {
        super("FullBright", Category.RENDER, "Adds gamma to remove shadows and dark areas", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        if (FullBright.mc.field_1769 != null) {
            FullBright.mc.field_1769.method_3279();
        }
    }

    @Override
    protected void onDisable() {
        if (FullBright.mc.field_1769 != null) {
            FullBright.mc.field_1769.method_3279();
        }
    }
}

