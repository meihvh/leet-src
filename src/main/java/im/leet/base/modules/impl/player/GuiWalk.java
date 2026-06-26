/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.ingame.BookEditScreen
 *  net.minecraft.client.gui.screen.ingame.SignEditScreen
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket
 *  net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventClickSlot;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.mixin.accessor.IKeyBinding;
import im.leet.utils.client.InputUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.InventoryUtility;
import im.leet.utils.player.MoveUtility;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;

public class GuiWalk
extends Module {
    public static final GuiWalk INSTANCE = new GuiWalk();
    public CheckBox sneak = this.checkbox("Sneak", false);
    public EnumSetting<Mode> mode = this.enumSetting("Mode", Mode.Silent);
    public SliderSetting postMoveDelay = (SliderSetting)this.sliderSetting("Delay after packets", 2.0f, 0.0f, 20.0f).visible(() -> this.mode.is(Mode.Legit));
    public SliderSetting packetDelay = (SliderSetting)this.sliderSetting("Packets delay", 1.0f, 0.0f, 20.0f).visible(() -> this.mode.is(Mode.Legit));
    public boolean writing = false;
    CopyOnWriteArrayList<Packet<?>> queue = new CopyOnWriteArrayList();
    int tickDelay = 0;
    int preTick = 0;
    int postTick = 0;
    boolean shouldStop = false;
    boolean wasClosed = false;
    EventBus<Event> events = event -> {
        Object p;
        Event e;
        if (this.mode.is(Mode.Silent)) {
            return;
        }
        if (event instanceof EventReceivePacket) {
            e = (EventReceivePacket)event;
            Packet packet = ((EventReceivePacket)e).packet;
            if (packet instanceof CloseScreenS2CPacket && (p = (CloseScreenS2CPacket)packet).method_36148() == 0) {
                e.cancel();
            }
        }
        if (event instanceof EventGameTick) {
            boolean hasItem;
            if (this.postTick > 0) {
                --this.postTick;
            }
            boolean bl = hasItem = !GuiWalk.mc.field_1724.field_7512.method_34255().method_7960();
            if (this.queue.isEmpty()) {
                boolean bl2 = this.shouldStop = this.postTick > 0 || hasItem;
                if (!hasItem && !this.wasClosed && GuiWalk.mc.field_1755 == null) {
                    NetworkUtility.sendWithoutEvent(new CloseHandledScreenC2SPacket(0));
                    this.wasClosed = true;
                    this.postTick = (int)this.postMoveDelay.get();
                    GuiWalk.mc.field_1724.field_7512.method_34254(ItemStack.field_8037);
                }
                return;
            }
            if (this.preTick > 0) {
                --this.preTick;
                this.shouldStop = true;
                return;
            }
            if (this.tickDelay > 0) {
                --this.tickDelay;
                return;
            }
            if (!MoveUtility.hasMovement(GuiWalk.mc.field_1724.field_3913.field_54155)) {
                for (Packet packet : this.queue) {
                    NetworkUtility.sendWithoutEvent(packet);
                }
                this.queue.clear();
                return;
            }
            Packet<?> packet = this.queue.getFirst();
            NetworkUtility.sendWithoutEvent(packet);
            this.queue.removeFirst();
            this.tickDelay = (int)this.packetDelay.get();
            this.shouldStop = true;
        }
        if (event instanceof EventInput) {
            e = (EventInput)event;
            if (this.shouldStop) {
                e.cancel();
            } else if (GuiWalk.mc.field_1755 != null) {
                KeyBinding[] handle;
                if (GuiWalk.mc.field_1755 instanceof ChatScreen || GuiWalk.mc.field_1755 instanceof BookEditScreen || GuiWalk.mc.field_1755 instanceof SignEditScreen) {
                    return;
                }
                for (KeyBinding key : handle = new KeyBinding[]{GuiWalk.mc.field_1690.field_1894, GuiWalk.mc.field_1690.field_1881, GuiWalk.mc.field_1690.field_1913, GuiWalk.mc.field_1690.field_1849, GuiWalk.mc.field_1690.field_1903}) {
                    key.method_23481(InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)((IKeyBinding)key).client$boundKey().method_1444()));
                }
            }
        }
        if (event instanceof EventSendPacket) {
            e = (EventSendPacket)event;
            Packet patt0$temp = ((EventSendPacket)e).packet;
            if (patt0$temp instanceof ClickSlotC2SPacket) {
                p = (ClickSlotC2SPacket)patt0$temp;
                this.queue.add((Packet<?>)p);
                e.cancel();
                if (this.wasClosed) {
                    this.preTick = 2;
                }
                this.wasClosed = false;
            } else {
                Packet patt1$temp = ((EventSendPacket)e).packet;
                if (patt1$temp instanceof CloseHandledScreenC2SPacket) {
                    CloseHandledScreenC2SPacket closeHandledScreenC2SPacket = (CloseHandledScreenC2SPacket)patt1$temp;
                    e.cancel();
                } else if (this.writing) {
                    e.cancel();
                    this.queue.add(((EventSendPacket)e).packet);
                }
            }
        }
        if (event instanceof EventClickSlot) {
            e = (EventClickSlot)event;
            this.queue.add((Packet<?>)InventoryUtility.click(((EventClickSlot)e).syncId, ((EventClickSlot)e).slotId, ((EventClickSlot)e).button, ((EventClickSlot)e).actionType));
            e.cancel();
            if (this.wasClosed) {
                this.preTick = 2;
            }
            this.wasClosed = false;
        }
    };

    private GuiWalk() {
        super("Gui walk", Category.PLAYER, "Allows you to walk with your inventory open", new Tag[0]);
    }

    public void startWrite() {
        this.writing = true;
    }

    public void stopWrite() {
        this.writing = false;
    }

    public boolean handle(KeyBinding key) {
        Screen screen = GuiWalk.mc.field_1755;
        if (screen == null) {
            return false;
        }
        if (!this.isEnabled() || screen instanceof ChatScreen) {
            return false;
        }
        if (key == GuiWalk.mc.field_1690.field_1832 && !this.sneak.get()) {
            return false;
        }
        return InputUtility.isKeyPressed(((IKeyBinding)key).client$boundKey().method_1444());
    }

    public static enum Mode {
        Silent,
        Legit;

    }
}

