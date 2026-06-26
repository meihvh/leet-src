/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.component.type.AttributeModifiersComponent
 *  net.minecraft.component.type.EquippableComponent
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.enchantment.Enchantments
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.attribute.EntityAttributes
 *  net.minecraft.item.ItemStack
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.registry.RegistryKeys
 *  net.minecraft.registry.entry.RegistryEntry
 */
package im.leet.utils.player;

import im.leet.MinecraftHolder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public final class ArmorUtility
implements MinecraftHolder {
    public static float calculateArmorValue(ItemStack stack) {
        if (stack.method_7960() || !ArmorUtility.isArmor(stack)) {
            return 0.0f;
        }
        float value = ArmorUtility.getAttributeValue(stack, EntityAttributes.field_23724);
        value += ArmorUtility.getAttributeValue(stack, EntityAttributes.field_23725);
        value += (float)ArmorUtility.getEnchantLevel(stack, (RegistryKey<Enchantment>)Enchantments.field_9111);
        value += (float)ArmorUtility.getEnchantLevel(stack, (RegistryKey<Enchantment>)Enchantments.field_9107) * 0.5f;
        value += (float)ArmorUtility.getEnchantLevel(stack, (RegistryKey<Enchantment>)Enchantments.field_9095) * 0.5f;
        value += (float)ArmorUtility.getEnchantLevel(stack, (RegistryKey<Enchantment>)Enchantments.field_9096) * 0.5f;
        value += (float)ArmorUtility.getEnchantLevel(stack, (RegistryKey<Enchantment>)Enchantments.field_9119) * 0.1f;
        return value += (float)ArmorUtility.getEnchantLevel(stack, (RegistryKey<Enchantment>)Enchantments.field_9101) * 0.2f;
    }

    private static boolean isArmor(ItemStack stack) {
        EquippableComponent eq = (EquippableComponent)stack.method_58694(DataComponentTypes.field_54196);
        if (eq == null) {
            return false;
        }
        EquipmentSlot slot = eq.comp_3174();
        return slot == EquipmentSlot.field_6169 || slot == EquipmentSlot.field_6174 || slot == EquipmentSlot.field_6172 || slot == EquipmentSlot.field_6166;
    }

    private static float getAttributeValue(ItemStack stack, RegistryEntry<?> attribute) {
        AttributeModifiersComponent mods = (AttributeModifiersComponent)stack.method_58694(DataComponentTypes.field_49636);
        if (mods == null) {
            return 0.0f;
        }
        return (float)mods.comp_2393().stream().filter(e -> e.comp_2395().equals((Object)attribute)).mapToDouble(e -> e.comp_2396().comp_2449()).sum();
    }

    private static int getEnchantLevel(ItemStack stack, RegistryKey<Enchantment> enchKey) {
        RegistryEntry entry = (RegistryEntry)ArmorUtility.mc.field_1687.method_30349().method_30530(RegistryKeys.field_41265).method_10223(enchKey.method_29177()).orElseThrow();
        return EnchantmentHelper.method_8225((RegistryEntry)entry, (ItemStack)stack);
    }

    public static boolean hasCurseOfBinding(ItemStack stack) {
        return ArmorUtility.getEnchantLevel(stack, (RegistryKey<Enchantment>)Enchantments.field_9113) > 0;
    }

    public static boolean isBroken(ItemStack stack) {
        return stack.method_7963() && (double)stack.method_7919() / (double)stack.method_7936() > 0.98;
    }

    public static int getBestArmorSlot(EquipmentSlot slot) {
        float best = -1.0f;
        int bestSlot = -1;
        for (int i = 0; i < 36; ++i) {
            float value;
            ItemStack stack = ArmorUtility.mc.field_1724.method_31548().method_5438(i);
            EquippableComponent eq = (EquippableComponent)stack.method_58694(DataComponentTypes.field_54196);
            if (eq == null || eq.comp_3174() != slot || ArmorUtility.isBroken(stack) || ArmorUtility.hasCurseOfBinding(stack) || !((value = ArmorUtility.calculateArmorValue(stack)) > best)) continue;
            best = value;
            bestSlot = i;
        }
        return bestSlot;
    }

    public static boolean isBetter(ItemStack newArmor, ItemStack currentArmor) {
        if (currentArmor.method_7960()) {
            return true;
        }
        if (!ArmorUtility.isArmor(newArmor) || !ArmorUtility.isArmor(currentArmor)) {
            return false;
        }
        return ArmorUtility.calculateArmorValue(newArmor) > ArmorUtility.calculateArmorValue(currentArmor);
    }

    private ArmorUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

