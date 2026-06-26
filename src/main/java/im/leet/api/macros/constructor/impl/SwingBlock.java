/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 */
package im.leet.api.macros.constructor.impl;

import im.leet.MinecraftHolder;
import im.leet.api.macros.constructor.MacroBlock;
import net.minecraft.util.Hand;

public class SwingBlock
implements MacroBlock,
MinecraftHolder {
    @Override
    public void execute() {
        SwingBlock.mc.field_1724.method_6104(Hand.field_5808);
    }

    @Override
    public String renderName() {
        return "Swing hand";
    }
}

