/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.primitives.Shorts
 *  com.google.common.primitives.SignedBytes
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.slot.Slot
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.screen.sync.ComponentChangesHash$ComponentHasher
 *  net.minecraft.screen.sync.ItemStackHash
 *  net.minecraft.util.Hand
 *  net.minecraft.util.collection.DefaultedList
 */
package im.leet.utils.player;

import com.google.common.collect.Lists;
import com.google.common.primitives.Shorts;
import com.google.common.primitives.SignedBytes;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.player.GuiWalk;
import im.leet.utils.network.NetworkUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ComponentChangesHash;
import net.minecraft.screen.sync.ItemStackHash;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

public final class InventoryUtility
implements MinecraftHolder {
    public static boolean skipClient = false;

    public static void swap(int from, int to) {
        NetworkUtility.send(InventoryUtility.click(0, from, 0, SlotActionType.field_7790));
        NetworkUtility.send(InventoryUtility.click(0, to, 0, SlotActionType.field_7790));
        NetworkUtility.send(InventoryUtility.click(0, from, 0, SlotActionType.field_7790));
    }

    public static int find(Item item) {
        for (int i = 0; i < 44; ++i) {
            if (InventoryUtility.mc.field_1724.method_31548().method_5438(i).method_7909() != item) continue;
            return i;
        }
        return -1;
    }

    public static int find(Item item, boolean enchanted) {
        for (int i = 0; i < 44; ++i) {
            ItemStack stack = InventoryUtility.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() != item || stack.method_7942() && enchanted) continue;
            return i;
        }
        return InventoryUtility.find(item);
    }

    public static int wrapHotbar(int slot) {
        return slot < 9 ? slot + 36 : slot;
    }

    public static void useItem(int slot, boolean swing) {
        InventoryUtility.useItem(slot, swing, false, false);
    }

    public static void useItem(int slot, boolean swing, boolean cooldown, boolean delay) {
        if (slot == -1) {
            return;
        }
        GuiWalk.INSTANCE.startWrite();
        if (slot < 9) {
            if (InventoryUtility.mc.field_1724.method_31548().method_67532() != slot) {
                NetworkUtility.send(new UpdateSelectedSlotC2SPacket(slot));
            }
            NetworkUtility.sendUse(Hand.field_5808);
            if (swing) {
                InventoryUtility.mc.field_1724.method_6104(Hand.field_5808);
            }
            if (delay) {
                Client.SCHEDULER.scheduleOnce(() -> {
                    if (InventoryUtility.mc.field_1724.method_31548().method_67532() != slot) {
                        NetworkUtility.send(new UpdateSelectedSlotC2SPacket(InventoryUtility.mc.field_1724.method_31548().method_67532()));
                    }
                }, 3);
            } else if (InventoryUtility.mc.field_1724.method_31548().method_67532() != slot) {
                NetworkUtility.send(new UpdateSelectedSlotC2SPacket(InventoryUtility.mc.field_1724.method_31548().method_67532()));
            }
        } else {
            int s = InventoryUtility.mc.field_1724.method_31548().method_67532();
            if (cooldown) {
                s = (s + 1) % 8;
            }
            InventoryUtility.swap(InventoryUtility.wrapHotbar(slot), InventoryUtility.wrapHotbar(s));
            if (cooldown) {
                if (InventoryUtility.mc.field_1724.method_31548().method_67532() != s) {
                    NetworkUtility.send(new UpdateSelectedSlotC2SPacket(s));
                }
                NetworkUtility.sendUse(Hand.field_5808);
                if (swing) {
                    InventoryUtility.mc.field_1724.method_6104(Hand.field_5808);
                }
                if (InventoryUtility.mc.field_1724.method_31548().method_67532() != s) {
                    NetworkUtility.send(new UpdateSelectedSlotC2SPacket(InventoryUtility.mc.field_1724.method_31548().method_67532()));
                }
            } else {
                NetworkUtility.sendUse(Hand.field_5808);
                if (swing) {
                    InventoryUtility.mc.field_1724.method_6104(Hand.field_5808);
                }
            }
            int finalS = s;
            Client.SCHEDULER.scheduleOnce(() -> {
                InventoryUtility.swap(InventoryUtility.wrapHotbar(slot), InventoryUtility.wrapHotbar(finalS));
                NetworkUtility.send(new CloseHandledScreenC2SPacket(finalS));
            }, 3);
        }
        GuiWalk.INSTANCE.stopWrite();
    }

    public static int findHotbar(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (InventoryUtility.mc.field_1724.method_31548().method_5438(i).method_7909() != item) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static ClickSlotC2SPacket click(int syncId, int slotId, int button, SlotActionType actionType) {
        ScreenHandler screenHandler = InventoryUtility.mc.field_1724.field_7512;
        DefaultedList defaultedList = screenHandler.field_7761;
        int i = defaultedList.size();
        ArrayList list = Lists.newArrayListWithCapacity((int)i);
        for (Slot slot : defaultedList) {
            list.add(slot.method_7677().method_7972());
        }
        Int2ObjectOpenHashMap int2ObjectMap = new Int2ObjectOpenHashMap();
        for (int j = 0; j < i; ++j) {
            ItemStack itemStack2;
            ItemStack itemStack = (ItemStack)list.get(j);
            if (ItemStack.method_7973((ItemStack)itemStack, (ItemStack)(itemStack2 = ((Slot)defaultedList.get(j)).method_7677()))) continue;
            int2ObjectMap.put(j, (Object)ItemStackHash.method_68853((ItemStack)itemStack2, (ComponentChangesHash.ComponentHasher)mc.method_1562().method_68823()));
        }
        ItemStackHash itemStackHash = ItemStackHash.method_68853((ItemStack)(slotId == -999 ? screenHandler.method_34255() : InventoryUtility.mc.field_1724.method_31548().method_5438(slotId)), (ComponentChangesHash.ComponentHasher)mc.method_1562().method_68823());
        return new ClickSlotC2SPacket(syncId, screenHandler.method_37421(), Shorts.checkedCast((long)slotId), SignedBytes.checkedCast((long)button), actionType, (Int2ObjectMap)int2ObjectMap, itemStackHash);
    }

    public static void selectSlot(int slot) {
        NetworkUtility.send(new UpdateSelectedSlotC2SPacket(slot));
    }

    private InventoryUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

