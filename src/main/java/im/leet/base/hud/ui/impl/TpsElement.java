/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.hud.ui.impl;

import im.leet.api.drags.Drag;
import im.leet.api.render.system.IconUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.animations.Direction;
import im.leet.utils.network.NetworkUtility;

public class TpsElement
extends HudElement {
    public TpsElement(Drag drag) {
        super("TPS", drag);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.animation.setDirection(Direction.BACKWARDS);
        float value = NetworkUtility.getTpsFactor();
        String tps = String.valueOf(Math.ceil(value * 10.0f) / 10.0);
        if (value == -1.0f) {
            tps = "unk.";
        }
        if ((double)value >= 19.9) {
            tps = tps.concat("*");
        }
        this.drawBase(IconUse.SPUTNIK, tps, 0.0f, 0.0f);
    }
}

