/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat.backtrack;

import im.leet.base.settings.impl.choice.Choice;

public abstract class BacktrackChoice
extends Choice {
    private boolean running;

    protected BacktrackChoice(String name) {
        super(name);
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }
}

