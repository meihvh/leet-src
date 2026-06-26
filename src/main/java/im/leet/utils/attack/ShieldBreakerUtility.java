/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.utils.attack;

import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.aura.attack.AttackHandler;
import im.leet.utils.client.targets.TargetsUtility;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.MathHelper;

public final class ShieldBreakerUtility
implements MinecraftHolder {
    public static boolean shieldBreaker(int packets) {
        if (TargetsUtility.getTarget() == null) {
            return false;
        }
        if (TargetsUtility.getTarget().method_6079().method_7909().equals(Items.field_8255)) {
            if (Math.abs(MathHelper.method_15393((float)(ShieldBreakerUtility.mc.field_1724.method_36454() - TargetsUtility.getTarget().method_36454() - 180.0f))) > 90.0f) {
                return false;
            }
            for (int i = 0; i < 36; ++i) {
                ItemStack stack = ShieldBreakerUtility.mc.field_1724.method_31548().method_5438(i);
                if (!(stack.method_7909() instanceof AxeItem)) continue;
                int currSlot = ShieldBreakerUtility.mc.field_1724.method_31548().method_67532();
                if (currSlot != i) {
                    ShieldBreakerUtility.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(i));
                }
                AttackHandler.attackEntity(TargetsUtility.getTarget());
                if (currSlot != i) {
                    ShieldBreakerUtility.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(currSlot));
                }
                return true;
            }
        }
        return false;
    }

    private ShieldBreakerUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

