/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.macros.constructor.impl;

import im.leet.MinecraftHolder;
import im.leet.api.macros.constructor.MacroBlock;

public class JumpBlock
implements MacroBlock,
MinecraftHolder {
    @Override
    public void execute() {
        if (JumpBlock.mc.field_1724.method_24828() && !JumpBlock.mc.field_1690.field_1903.method_1434()) {
            JumpBlock.mc.field_1724.method_6043();
        }
    }

    @Override
    public String renderName() {
        return "Jump from ground";
    }
}

