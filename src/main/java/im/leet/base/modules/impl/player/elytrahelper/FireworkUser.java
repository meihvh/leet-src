/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.screen.slot.SlotActionType
 */
package im.leet.base.modules.impl.player.elytrahelper;

import im.leet.MinecraftHolder;
import im.leet.utils.player.InventoryUtility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;

public class FireworkUser
implements MinecraftHolder {
    public void useItemOnHotbar(Item item) {
        int freeHotbarSlot;
        int fireworkSlot;
        int slot = this.getItemOnHotbar(item);
        if (slot == -1 && (fireworkSlot = InventoryUtility.find(item)) != -1 && (freeHotbarSlot = this.findFreeHotbarSlot()) != -1) {
            this.swapSlots(fireworkSlot, freeHotbarSlot);
            slot = freeHotbarSlot;
        }
        if (slot != -1 && !FireworkUser.mc.field_1724.method_7357().method_7904(item.method_7854())) {
            InventoryUtility.useItem(slot, false);
        }
    }

    private int findFreeHotbarSlot() {
        for (int i = 0; i < 9; ++i) {
            if (!FireworkUser.mc.field_1724.method_31548().method_5438(i).method_7960()) continue;
            return i;
        }
        return -1;
    }

    private void swapSlots(int fromSlot, int toSlot) {
        FireworkUser.mc.field_1761.method_2906(FireworkUser.mc.field_1724.field_7512.field_7763, fromSlot, toSlot, SlotActionType.field_7791, (PlayerEntity)FireworkUser.mc.field_1724);
    }

    public int getItemOnHotbar(Item items) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (FireworkUser.mc.field_1724.method_31548().method_5438(i).method_7909() != items) continue;
            slot = i;
            break;
        }
        return slot;
    }
}

