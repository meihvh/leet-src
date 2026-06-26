/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.component.type.EquippableComponent
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 */
package im.leet.base.modules.impl.player.elytrahelper;

import im.leet.MinecraftHolder;
import im.leet.utils.player.InventoryUtility;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ElytraSwapper
implements MinecraftHolder {
    public void swap() {
        boolean isElytraEquiped = ElytraSwapper.mc.field_1724.method_31548().method_5438(38).method_7909() == Items.field_8833;
        for (int i = 0; i < 46; ++i) {
            ItemStack stack = ElytraSwapper.mc.field_1724.method_31548().method_5438(i);
            EquippableComponent component = (EquippableComponent)stack.method_58694(DataComponentTypes.field_54196);
            if (component == null || component.comp_3174() != EquipmentSlot.field_6174 || (stack.method_7909() != Items.field_8833 || isElytraEquiped) && (!isElytraEquiped || stack.method_7909() == Items.field_8833)) continue;
            InventoryUtility.swap(InventoryUtility.wrapHotbar(i), 6);
            return;
        }
    }
}

