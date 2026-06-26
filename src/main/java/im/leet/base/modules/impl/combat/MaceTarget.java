/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.component.type.EquippableComponent
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.util.Hand
 */
package im.leet.base.modules.impl.combat;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventInput;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.InventoryUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;

public class MaceTarget
extends Module {
    public static final MaceTarget INSTANCE = new MaceTarget();
    SliderSetting height = this.sliderSetting("Height", 20.0f, 20.0f, 50.0f);
    CheckBox useStuck = this.checkbox("Air stuck", false);
    EnumSetting<MovementCorrection> correction = this.enumSetting("Correction", MovementCorrection.STRICT);
    TimeUtility firework = new TimeUtility();
    TimeUtility reset = new TimeUtility();
    Stage stage = Stage.FLYING_UP;
    int wait;
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (!TargetsUtility.isValid()) {
                TargetsUtility.find(128.0f, TargetsUtility.Sort.Distance);
                return;
            }
            if (this.wait > 0) {
                --this.wait;
                return;
            }
            boolean hasElytra = MaceTarget.mc.field_1724.method_6118(EquipmentSlot.field_6174).method_58694(DataComponentTypes.field_54197) != null;
            switch (this.stage.ordinal()) {
                case 0: {
                    int slot;
                    if (!hasElytra && (slot = InventoryUtility.findHotbar(Items.field_8833)) != -1) {
                        InventoryUtility.useItem(slot, false);
                        this.wait = 2;
                    }
                    if (this.firework.reached(300L, true)) {
                        InventoryUtility.useItem(InventoryUtility.find(Items.field_8639), false);
                    }
                    if (MaceTarget.mc.field_1724.method_23318() - TargetsUtility.getTarget().method_23318() < (double)this.height.get()) {
                        Angle target = RotationUtility.calcRotate(TargetsUtility.getTarget().method_19538().method_1031(0.0, (double)this.height.get(), 0.0), true);
                        Client.ROTATION.rotate(LinearRotation.INSTANCE, target, 1, false, this.correction.get(), 30);
                        break;
                    }
                    this.stage = Stage.TARGETTING;
                    break;
                }
                case 1: {
                    Angle target = RotationUtility.calcRotate(TargetsUtility.getTarget().method_19538(), true);
                    Client.ROTATION.rotate(LinearRotation.INSTANCE, target, 1, false, this.correction.get(), 30);
                    if (!(MaceTarget.mc.field_1724.method_5739((Entity)TargetsUtility.getTarget()) < 16.0f)) break;
                    this.stage = Stage.ATTACKING;
                    break;
                }
                case 2: {
                    int prev;
                    int slot;
                    Angle target = RotationUtility.calcRotate(TargetsUtility.getTarget().method_19538(), true);
                    Client.ROTATION.rotate(LinearRotation.INSTANCE, target, 1, false, this.correction.get(), 30);
                    if (hasElytra && this.reset.reached(200L)) {
                        slot = -1;
                        for (int i = 0; i < 46; ++i) {
                            ItemStack stack = MaceTarget.mc.field_1724.method_31548().method_5438(i);
                            EquippableComponent component = (EquippableComponent)stack.method_58694(DataComponentTypes.field_54196);
                            if (component == null || component.comp_3174() != EquipmentSlot.field_6174 || stack.method_7909() == Items.field_8833) continue;
                            slot = i;
                            break;
                        }
                        InventoryUtility.useItem(slot, false);
                        this.reset.reset();
                    }
                    if (!this.reset.reached(100L) || !(MaceTarget.mc.field_1724.method_5739((Entity)TargetsUtility.getTarget()) < 3.0f)) break;
                    slot = InventoryUtility.findHotbar(Items.field_49814);
                    if (slot != (prev = MaceTarget.mc.field_1724.method_31548().method_67532())) {
                        NetworkUtility.send(new UpdateSelectedSlotC2SPacket(slot));
                    }
                    MaceTarget.mc.field_1761.method_2918((PlayerEntity)MaceTarget.mc.field_1724, (Entity)TargetsUtility.getTarget());
                    MaceTarget.mc.field_1724.method_6104(Hand.field_5808);
                    if (slot != prev) {
                        NetworkUtility.send(new UpdateSelectedSlotC2SPacket(prev));
                    }
                    this.stage = Stage.FLYING_UP;
                    this.firework.reset();
                    ChatUtility.sendDebug("[macetarget] flying up!");
                }
            }
        }
        if (event instanceof EventInput) {
            EventInput e = (EventInput)event;
            if (TargetsUtility.getTarget() == null) {
                return;
            }
            boolean hasElytra = MoveUtility.hasElytra();
            if (hasElytra) {
                e.setJump(MaceTarget.mc.field_1724.field_6012 % 2 == 0);
                if (!MaceTarget.mc.field_1724.method_6128()) {
                    this.firework.reset();
                }
            }
        }
    };

    private MaceTarget() {
        super("Mace Target", Category.COMBAT, "Automatically flies up and hits the target with a mace", Tag.RAGE);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.stage = Stage.FLYING_UP;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        TargetsUtility.reset();
    }

    static enum Stage {
        FLYING_UP,
        TARGETTING,
        ATTACKING;

    }
}

