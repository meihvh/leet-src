/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Element
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.ingame.HandledScreen
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.slot.Slot
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.text.Text
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.api.events.list.EventClickSlot;
import im.leet.base.modules.impl.player.BedwarsShop;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={HandledScreen.class})
public class HandledScreenMixin
extends Screen {
    @Shadow
    @Final
    protected ScreenHandler field_2797;

    public HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method={"close"}, at={@At(value="HEAD")}, cancellable=true)
    private void onClose(CallbackInfo ci) {
        if (BedwarsShop.INSTANCE.isEnabled() && BedwarsShop.INSTANCE.saveScreen((Screen)((HandledScreen)this))) {
            ci.cancel();
        }
    }

    @Inject(at={@At(value="TAIL")}, method={"init"})
    public void addButtons(CallbackInfo ci) {
        if (!BedwarsShop.INSTANCE.buttons.get() || Client.IS_PANIC) {
            return;
        }
        this.method_37063((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Close without packet"), button -> {
            if (this.field_22787 != null) {
                this.field_22787.method_1507(null);
            }
        }).method_46432(115).method_46433(5, 5).method_46431());
        this.method_37063((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Save GUI"), button -> {
            if (this.field_22787 != null && this.field_22787.field_1724 != null && this.field_22787.field_1755 != null) {
                BedwarsShop.INSTANCE.saveScreen(this.field_22787.field_1755);
            }
        }).method_46432(115).method_46433(5, 35).method_46431());
        this.method_37063((Element)ButtonWidget.method_46430((Text)Text.method_30163((String)"Send close packet"), button -> {
            if (this.field_22787 != null && this.field_22787.field_1724 != null && this.field_22787.method_1562() != null) {
                this.field_22787.method_1562().method_52787((Packet)new CloseHandledScreenC2SPacket(this.field_22787.field_1724.field_7512.field_7763));
            }
        }).method_46432(115).method_46433(5, 65).method_46431());
    }

    @Inject(method={"onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V", shift=At.Shift.BEFORE)}, cancellable=true)
    private void leet$onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        EventClickSlot event = EventClickSlot.build(this.field_2797.field_7763, slot != null ? slot.field_7874 : slotId, button, actionType);
        Client.EVENTS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}

