/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.macros;

import im.leet.api.macros.constructor.MacroBlock;
import im.leet.api.ui.widgets.impl.MacrosWidget;
import java.util.ArrayList;

public class Macro {
    int key = -1;
    ArrayList<MacroBlock> insn = new ArrayList();

    public Macro(ArrayList<MacrosWidget.BlockSample> samples) {
        for (MacrosWidget.BlockSample sample : samples) {
            this.insn.add(sample.getOriginal());
        }
    }

    public void execute() {
        for (MacroBlock block : this.insn) {
            block.execute();
        }
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

