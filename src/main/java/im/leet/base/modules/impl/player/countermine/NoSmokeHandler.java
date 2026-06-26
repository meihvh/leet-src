/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.Entity$RemovalReason
 *  net.minecraft.entity.decoration.DisplayEntity$ItemDisplayEntity
 */
package im.leet.base.modules.impl.player.countermine;

import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.impl.player.countermine.SkinEntityUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;

public class NoSmokeHandler
implements MinecraftHolder {
    public void handle(Event event) {
        if (event instanceof EventGameTick) {
            this.onTick();
        }
    }

    private void onTick() {
        if (NoSmokeHandler.mc.field_1687 == null || NoSmokeHandler.mc.field_1724 == null) {
            return;
        }
        for (Entity entity : NoSmokeHandler.mc.field_1687.method_18112()) {
            if (!(entity instanceof DisplayEntity.ItemDisplayEntity) || !SkinEntityUtility.isSmokeGrenade(entity)) continue;
            NoSmokeHandler.mc.field_1687.method_2945(entity.method_5628(), Entity.RemovalReason.field_26999);
        }
    }
}

