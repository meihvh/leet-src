/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.EquipmentSlot$Type
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 */
package im.leet.base.modules.impl.player.countermine;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public final class F5ArmorFix {
    private F5ArmorFix() {
    }

    public static void removeAllArmor(MinecraftClient client) {
        if (client == null || client.field_1687 == null || client.field_1724 == null || client.field_1724.method_7325()) {
            return;
        }
        for (Entity entity : client.field_1687.method_18112()) {
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity livingEntity = (LivingEntity)entity;
            F5ArmorFix.removeArmorFromEntity(livingEntity);
        }
    }

    private static void removeArmorFromEntity(LivingEntity entity) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack armorStack;
            if (slot.method_5925() != EquipmentSlot.Type.field_6178 || (armorStack = entity.method_6118(slot)).method_7960()) continue;
            entity.method_5673(slot, ItemStack.field_8037);
        }
    }
}

