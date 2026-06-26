/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.macros.constructor;

import im.leet.api.macros.constructor.MacroBlock;
import im.leet.api.macros.constructor.impl.JumpBlock;
import im.leet.api.macros.constructor.impl.SwingBlock;
import java.util.ArrayList;
import java.util.List;

public class MacroBlocks {
    private ArrayList<MacroBlock> handled = new ArrayList();

    public MacroBlocks() {
        this.handled.addAll(List.of(new JumpBlock(), new SwingBlock()));
    }

    public ArrayList<MacroBlock> getHandled() {
        return this.handled;
    }
}

