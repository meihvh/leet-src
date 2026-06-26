/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.Items
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.util.math.Vec2f
 */
package im.leet.base.modules.impl.combat.elytraaura.utilities;

import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.ElytraAura;
import im.leet.base.modules.impl.combat.elytraaura.math.ElytraAuraResolve;
import im.leet.mixin.accessor.ILivingEntity;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.player.InventoryUtility;
import java.util.ArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Vec2f;

public final class ElytraAuraUtility
implements MinecraftHolder {
    static ElytraAura elytraAura = ElytraAura.INSTANCE;
    static TimeUtility fireworkTime = new TimeUtility();
    public static boolean hut = false;

    public static Vec2f[] parsePitchOffsets(String pitchOffsetsString) {
        if (pitchOffsetsString == null || pitchOffsetsString.trim().isEmpty()) {
            return new Vec2f[]{new Vec2f(0.0f, 0.0f)};
        }
        String[] parts = pitchOffsetsString.split(",");
        ArrayList<Vec2f> offsets = new ArrayList<Vec2f>();
        for (String part : parts) {
            try {
                float pitchValue = Float.parseFloat(part.trim());
                offsets.add(new Vec2f(0.0f, pitchValue));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        if (offsets.isEmpty()) {
            return new Vec2f[]{new Vec2f(0.0f, 0.0f)};
        }
        return offsets.toArray(new Vec2f[0]);
    }

    public static boolean useFireWork(LivingEntity entity) {
        if (ElytraAuraUtility.elytraAura.autoFireWorkSetting.get()) {
            if (ElytraAuraUtility.elytraAura.autoAirStackSetting.get() && ElytraAuraUtility.elytraAura.ifworkAitStack.get(ElytraAura.Feature.IF_TARGET_ON_STOYAK)) {
                boolean iso;
                boolean bl = iso = ElytraAuraUtility.mc.field_1724.method_19538().method_1022(TargetsUtility.getTarget().method_19538()) > 5.4 && ElytraAuraResolve.isStoyak(TargetsUtility.getTarget()) && !TargetsUtility.getTarget().method_24828() && !elytraAura.isLeave(TargetsUtility.getTarget());
                if (iso) {
                    return false;
                }
            }
            if (((ILivingEntity)ElytraAuraUtility.mc.field_1724).client$lastAttackedTicks() == 2 && fireworkTime.reached(200L, true) && ElytraAuraUtility.mc.field_1724.method_6128()) {
                ElytraAuraUtility.useItemOnHotbar(Items.field_8639);
                return true;
            }
            if (ElytraAuraUtility.elytraAura.defensive.isEnabled() && ElytraAuraUtility.elytraAura.lastAntiaim && !ElytraAuraUtility.elytraAura.antiAimIsActive) {
                ElytraAuraUtility.elytraAura.lastAntiaim = false;
                ElytraAuraUtility.useItemOnHotbar(Items.field_8639);
                return true;
            }
            if (ElytraAuraUtility.elytraAura.smartUseFireWorkSetting.get()) {
                int delay = 400;
                if (ElytraAuraUtility.mc.field_1724.method_19538().method_1022(entity.method_19538()) < 4.0) {
                    delay = 270;
                }
                if (elytraAura.targetIsLeave(entity)) {
                    delay = 400;
                }
                if (entity.method_24828() || !entity.method_6128() || ElytraAuraResolve.isStoyak(entity)) {
                    delay = 450;
                }
                if (!ElytraAuraUtility.elytraAura.antiAimIsActive && hut && fireworkTime.reached(10L, true)) {
                    ElytraAuraUtility.useItemOnHotbar(Items.field_8639);
                    hut = false;
                    return true;
                }
                if (fireworkTime.reached(delay, true) && ElytraAuraUtility.mc.field_1724.method_6128()) {
                    ElytraAuraUtility.useItemOnHotbar(Items.field_8639);
                    return true;
                }
            } else if (fireworkTime.reached((long)ElytraAuraUtility.elytraAura.delayUseFireWorkSetting.get(), true) && ElytraAuraUtility.mc.field_1724.method_6128()) {
                ElytraAuraUtility.useItemOnHotbar(Items.field_8639);
                return true;
            }
        }
        return false;
    }

    public static void useItemOnHotbar(Item item) {
        int freeHotbarSlot;
        int fireworkSlot;
        int slot = ElytraAuraUtility.getItemOnHotbar(item);
        if (slot == -1 && (fireworkSlot = InventoryUtility.find(Items.field_8639)) != -1 && (freeHotbarSlot = ElytraAuraUtility.findFreeHotbarSlot()) != -1) {
            ElytraAuraUtility.swapSlots(fireworkSlot, freeHotbarSlot);
            slot = freeHotbarSlot;
        }
        if (slot != -1 && !ElytraAuraUtility.mc.field_1724.method_7357().method_7904(item.method_7854())) {
            InventoryUtility.useItem(slot, false, true, false);
        }
    }

    private static int findFreeHotbarSlot() {
        for (int i = 0; i < 9; ++i) {
            if (!ElytraAuraUtility.mc.field_1724.method_31548().method_5438(i).method_7960()) continue;
            return i;
        }
        return -1;
    }

    private static void swapSlots(int fromSlot, int toSlot) {
        ElytraAuraUtility.mc.field_1761.method_2906(ElytraAuraUtility.mc.field_1724.field_7512.field_7763, fromSlot, toSlot, SlotActionType.field_7791, (PlayerEntity)ElytraAuraUtility.mc.field_1724);
    }

    public static int getItemOnHotbar(Item items) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (ElytraAuraUtility.mc.field_1724.method_31548().method_5438(i).method_7909() != items) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private ElytraAuraUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

