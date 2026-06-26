/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.screen.slot.SlotActionType
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.player.ArmorUtility;
import im.leet.utils.player.InventoryUtility;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor
extends Module {
    public static final AutoArmor INSTANCE = new AutoArmor();
    public CheckBox notInScreen = (CheckBox)this.checkbox("Not in screen", true).desc("Only swap armor when no screen is opened");
    public SliderSetting delay = (SliderSetting)this.sliderSetting("Delay", 100.0f, 0.0f, 500.0f).increment(10.0f).desc("Delay between armor swaps (ms)");
    private final TimeUtility swapDelay = new TimeUtility();
    private final EquipmentSlot[] armorSlots = new EquipmentSlot[]{EquipmentSlot.field_6166, EquipmentSlot.field_6172, EquipmentSlot.field_6174, EquipmentSlot.field_6169};
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (AutoArmor.mc.field_1724 == null || AutoArmor.mc.field_1687 == null) {
                return;
            }
            if (this.notInScreen.get() && AutoArmor.mc.field_1755 != null) {
                return;
            }
            if (!this.swapDelay.reached((long)this.delay.get(), false)) {
                return;
            }
            for (EquipmentSlot slot : this.armorSlots) {
                ItemStack bestArmor;
                ItemStack currentArmor = AutoArmor.mc.field_1724.method_6118(slot);
                if (slot == EquipmentSlot.field_6174 && currentArmor.method_7909() == Items.field_8833) continue;
                int armorSlotIndex = 8 - slot.method_5927();
                int bestSlot = ArmorUtility.getBestArmorSlot(slot);
                if (bestSlot != -1 && ArmorUtility.isBetter(bestArmor = AutoArmor.mc.field_1724.method_31548().method_5438(bestSlot), currentArmor)) {
                    InventoryUtility.swap(InventoryUtility.wrapHotbar(bestSlot), armorSlotIndex);
                    this.swapDelay.reset();
                    return;
                }
                if (!ArmorUtility.isBroken(currentArmor)) continue;
                for (int i = 9; i < 45; ++i) {
                    if (!AutoArmor.mc.field_1724.method_31548().method_5438(i).method_7960()) continue;
                    InventoryUtility.click(0, armorSlotIndex, 0, SlotActionType.field_7794);
                    this.swapDelay.reset();
                    return;
                }
            }
        }
    };

    private AutoArmor() {
        super("Auto armor", Category.PLAYER, "Automatically equips the best armor", new Tag[0]);
    }
}

