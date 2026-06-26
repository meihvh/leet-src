/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.macros;

import im.leet.Client;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventKey;
import im.leet.api.macros.Macro;
import im.leet.api.macros.constructor.MacroBlocks;
import im.leet.api.ui.widgets.impl.MacrosWidget;
import java.util.ArrayList;

public class Macros {
    private ArrayList<Macro> macros = new ArrayList();
    private MacroBlocks blocks = new MacroBlocks();
    EventBus<EventKey> eventKey = key -> {
        if (key.action == 1) {
            for (Macro macro : this.macros) {
                if (macro.getKey() != key.getKey()) continue;
                macro.execute();
            }
        }
    };

    public Macros() {
        Client.EVENTS.register(this);
    }

    public Macro addFromBuilder(ArrayList<MacrosWidget.BlockSample> samples) {
        Macro macro = new Macro(samples);
        this.macros.add(macro);
        return macro;
    }

    public MacroBlocks getBlocks() {
        return this.blocks;
    }
}

