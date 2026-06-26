/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.consume.UseAction
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.text.Text
 *  net.minecraft.util.Hand
 */
package im.leet.base.modules.impl.combat;

import im.leet.Client;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.aura.attack.AttackHandler;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.RayTraceUtility;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class ShieldUtility
extends Module {
    public static final ShieldUtility INSTANCE = new ShieldUtility();
    final Group breakShield = this.group("Break shield").toggleable(true);
    final SliderSetting breakShieldClicks = this.breakShield.sliderSetting("Clicks", 1.0f, 1.0f, 10.0f);
    final Group autoBlocking = this.group("Auto blocking").toggleable(true);
    final EnumSetting<BlockMode> autoBlockingBlockMode = this.autoBlocking.enumSetting("Block mode", BlockMode.InteractItem);
    final EnumSetting<UnblockMode> autoBlockingUnblockMode = this.autoBlocking.enumSetting("Unblock mode", UnblockMode.StopUsingItem);

    private ShieldUtility() {
        super("Shield Utility", Category.COMBAT, "Automatically press and unpress shield, and automatically break shields", new Tag[0]);
    }

    public boolean breakShield() {
        LivingEntity target = TargetsUtility.getTarget();
        if (!this.isEnabled() || !this.breakShield.isEnabled() || ShieldUtility.nullCheck() || target == null || target.method_6039()) {
            return false;
        }
        for (int i = 0; i < 8; ++i) {
            ItemStack stack = ShieldUtility.mc.field_1724.method_31548().method_5438(i);
            if (!(stack.method_7909() instanceof AxeItem)) continue;
            if (!RayTraceUtility.rayTrace(Client.ROTATION.getRotate().toVector(), 3.0, TargetsUtility.getTarget().method_5829())) {
                return false;
            }
            int currSlot = ShieldUtility.mc.field_1724.method_31548().method_67532();
            if (currSlot != i) {
                ShieldUtility.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(i));
            }
            for (int c = 0; c < this.breakShieldClicks.getInt(); ++c) {
                AttackHandler.attackEntity(target);
            }
            if (currSlot != i) {
                ShieldUtility.mc.field_1724.field_3944.method_52787((Packet)new UpdateSelectedSlotC2SPacket(currSlot));
            }
            ChatUtility.send((Text)Text.method_43470((String)"Broke shield of ").method_10852(target.method_5476()));
            return true;
        }
        return false;
    }

    public void startBlocking() {
        Hand hand;
        if (!this.isEnabled() || !this.autoBlocking.isEnabled() || ShieldUtility.nullCheck() || ShieldUtility.mc.field_1724.method_6039()) {
            return;
        }
        if (this.canBlock(ShieldUtility.mc.field_1724.method_6047())) {
            hand = Hand.field_5808;
        } else if (this.canBlock(ShieldUtility.mc.field_1724.method_6079())) {
            hand = Hand.field_5810;
        } else {
            return;
        }
        switch (this.autoBlockingBlockMode.get().ordinal()) {
            case 0: {
                ShieldUtility.mc.field_1761.method_2919((PlayerEntity)ShieldUtility.mc.field_1724, hand);
            }
        }
    }

    public void stopBlocking(boolean pauses) {
        if (!this.isEnabled() || !this.autoBlocking.isEnabled() || ShieldUtility.nullCheck() || !ShieldUtility.mc.field_1724.method_6039()) {
            return;
        }
        switch (this.autoBlockingUnblockMode.get().ordinal()) {
            case 0: {
                ShieldUtility.mc.field_1761.method_2897((PlayerEntity)ShieldUtility.mc.field_1724);
                break;
            }
            case 1: {
                int current = ShieldUtility.mc.field_1724.method_31548().method_67532();
                int next = (current + 1) % 8;
                NetworkUtility.send(new UpdateSelectedSlotC2SPacket(next));
                NetworkUtility.send(new UpdateSelectedSlotC2SPacket(current));
                break;
            }
            case 2: {
                if (pauses) break;
                ShieldUtility.mc.field_1761.method_2897((PlayerEntity)ShieldUtility.mc.field_1724);
            }
        }
    }

    private boolean canBlock(ItemStack stack) {
        if (stack.method_7960() || !stack.method_45435(ShieldUtility.mc.field_1687.method_45162()) || stack.method_7909() == null) {
            return false;
        }
        return stack.method_7909().method_7853(stack) == UseAction.field_8949;
    }

    static enum BlockMode {
        InteractItem;

    }

    static enum UnblockMode {
        StopUsingItem,
        ChangeSlot,
        None;

    }
}

