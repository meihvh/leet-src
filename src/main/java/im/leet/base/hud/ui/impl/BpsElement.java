/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 */
package im.leet.base.hud.ui.impl;

import im.leet.MinecraftHolder;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.IconUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.animations.Direction;
import im.leet.utils.player.MoveUtility;
import net.minecraft.entity.LivingEntity;

public class BpsElement
extends HudElement
implements MinecraftHolder {
    public BpsElement(Drag drag) {
        super("Bps", drag);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.animation.setDirection(Direction.BACKWARDS);
        double bps = MoveUtility.getBPS((LivingEntity)BpsElement.mc.field_1724);
        this.drawBase(IconUse.CUBE, Float.toString((float)Math.round(bps * 10.0) / 10.0f), 0.0f, 0.0f);
    }
}

