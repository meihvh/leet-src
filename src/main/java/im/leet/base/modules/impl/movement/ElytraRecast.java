/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 */
package im.leet.base.modules.impl.movement;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventTravel;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.Angle;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import java.util.function.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class ElytraRecast
extends Module {
    public static ElytraRecast INSTANCE = new ElytraRecast();
    public Group mainGroup = this.group("Main Settings");
    public CheckBox doubleJumpSetting = this.mainGroup.checkbox("Double Jump", false);
    public CheckBox setCustomPitchSetting = this.mainGroup.checkbox("Set Custom Pitch", false);
    public CheckBox abuseElytraSetting = this.mainGroup.checkbox("Abuse Elytra", false);
    public CheckBox addMultiplierSetting;
    public SliderSetting customPitchValueSetting;
    EventBus<Event> onEvent;

    public ElytraRecast() {
        super("Elytra Recast", Category.MOVEMENT, "BunnyHop on Elytra", new Tag[0]);
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.abuseElytraSetting::get;
        this.addMultiplierSetting = (CheckBox)this.mainGroup.checkbox("Add Multiplier", true).visible(supplierArray);
        Supplier[] supplierArray2 = new Supplier[1];
        supplierArray2[0] = this.setCustomPitchSetting::get;
        this.customPitchValueSetting = (SliderSetting)this.mainGroup.sliderSetting("Pitch Value", 80.0f, -90.0f, 90.0f).increment(1.0f).visible(supplierArray2);
        this.onEvent = event -> {
            if (event instanceof EventGameTick) {
                if (MoveUtility.hasElytra() && !this.abuseElytraSetting.get() && ElytraRecast.mc.field_1724.field_6017 > 0.0 && !ElytraRecast.mc.field_1724.method_6128() && !ElytraRecast.mc.field_1724.method_24828() && ElytraRecast.mc.field_1724.method_18798().field_1351 != 0.0) {
                    MoveUtility.startGliding();
                }
                if (this.setCustomPitchSetting.get() && MoveUtility.hasElytra()) {
                    Client.ROTATION.rotate(new Angle(ElytraRecast.mc.field_1724.method_36454(), this.customPitchValueSetting.get()), true);
                }
                if (this.doubleJumpSetting.get() && MoveUtility.hasElytra() && !this.abuseElytraSetting.get() && ElytraRecast.mc.field_1724.method_24828() && !ElytraRecast.mc.field_1724.method_6128()) {
                    ElytraRecast.mc.field_1724.method_6043();
                }
            }
            if (event instanceof EventTravel && this.abuseElytraSetting.get()) {
                if (!ElytraRecast.mc.field_1724.method_6128()) {
                    ElytraRecast.mc.field_1724.method_23669();
                    NetworkUtility.sendWithoutEvent(new ClientCommandC2SPacket((Entity)ElytraRecast.mc.field_1724, ClientCommandC2SPacket.Mode.field_12982));
                }
                if (this.addMultiplierSetting.get() && ElytraRecast.mc.field_1724.method_24828() && MoveUtility.getBPS((LivingEntity)ElytraRecast.mc.field_1724) < 45.0f) {
                    ElytraRecast.mc.field_1724.method_18799(ElytraRecast.mc.field_1724.method_18798().method_1021((double)1.01f));
                }
            }
            if (event instanceof EventInput) {
                EventInput input = (EventInput)event;
                if (MoveUtility.hasElytra() && !this.abuseElytraSetting.get() && !ElytraRecast.mc.field_1724.method_6128() && !ElytraRecast.mc.field_1724.method_24828() && ElytraRecast.mc.field_1724.method_18798().field_1351 != 0.0) {
                    input.setJump(ElytraRecast.mc.field_1724.field_6012 % 2 == 0);
                }
            }
        };
    }
}

