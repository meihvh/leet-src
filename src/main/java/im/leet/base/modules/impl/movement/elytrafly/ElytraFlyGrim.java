/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$OnGroundOnly
 *  net.minecraft.util.PlayerInput
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement.elytrafly;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventMoveVelocity;
import im.leet.base.modules.impl.movement.elytrafly.ElytraFly;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.InputUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import java.util.function.Supplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraFlyGrim
extends Choice {
    final SliderSetting factor = this.sliderSetting("Factor", 0.9f, 0.0f, 1.0f).increment(0.01f);
    final CheckBox insanity = this.checkbox("Insanity", true);
    final SliderSetting insanityBPS;
    final Group up;
    final SliderSetting upMotion;
    final KeybindSetting upKey;
    final Group packetUp;
    final KeybindSetting packetUpKey;
    final SliderSetting packetUpInterval;
    final CheckBox autoPacketUp;
    final SliderSetting autoPacketUpPitch;

    ElytraFlyGrim() {
        super("Grim");
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.insanity::get;
        this.insanityBPS = (SliderSetting)this.sliderSetting("Insanity min BPS", 30.0f, 0.0f, 100.0f).visible(supplierArray);
        this.up = this.group("Up").toggleable(true);
        this.upMotion = this.up.sliderSetting("Motion", 0.03f, 0.0f, 1.0f).increment(0.01f);
        this.upKey = this.up.keybindSetting("Key", 32);
        this.packetUp = this.group("Packet up").toggleable(true);
        this.packetUpKey = this.packetUp.keybindSetting("Packet up key", 258);
        this.packetUpInterval = this.packetUp.sliderSetting("Packet up interval", 4.0f, 0.0f, 20.0f);
        this.autoPacketUp = this.packetUp.checkbox("Automatic", true);
        Supplier[] supplierArray2 = new Supplier[1];
        supplierArray2[0] = this.autoPacketUp::get;
        this.autoPacketUpPitch = (SliderSetting)this.sliderSetting("Automatic pitch", -45.0f, -90.0f, 90.0f).visible(supplierArray2);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMoveVelocity) {
            EventMoveVelocity e = (EventMoveVelocity)event;
            boolean isForward = this.checkForward();
            if (isForward) {
                this.forward(e.movement);
                if (this.insanity.get() && MoveUtility.getBPS((LivingEntity)ElytraFlyGrim.mc.field_1724) >= this.insanityBPS.get()) {
                    e.movement.field_1351 += (double)(ElytraFlyGrim.mc.field_1724.field_6012 % 2);
                }
            } else if (this.up.isEnabled() && (InputUtility.isKeyPressed(this.upKey.getBind()) || ElytraFly.INSTANCE.forceUp)) {
                e.movement.field_1351 += (double)this.upMotion.get();
                ElytraFly.INSTANCE.forceUp = false;
            }
            if (this.packetUp.isEnabled() && (InputUtility.isKeyPressed(this.packetUpKey.getBind()) || this.autoPacketUp.get() && Client.ROTATION.getRotate().getPitch() > this.autoPacketUpPitch.get()) && ElytraFlyGrim.mc.field_1724.field_6012 % this.packetUpInterval.getInt() == 0) {
                NetworkUtility.sendWithoutEvent(new PlayerMoveC2SPacket.OnGroundOnly(false, false));
            }
        }
    }

    private void forward(Vec3d e) {
        double factor = (double)this.factor.get() / 10.0;
        float yaw = (Client.ROTATION.getRotate().getYaw() + 90.0f) * ((float)Math.PI / 180);
        float sin = MathHelper.method_15374((float)yaw);
        float cos = MathHelper.method_15362((float)yaw);
        e.field_1352 += factor * (double)cos;
        e.field_1350 += factor * (double)sin;
    }

    private boolean checkForward() {
        PlayerInput input = ElytraFlyGrim.mc.field_1724.field_3913.field_54155;
        return ElytraFly.INSTANCE.cruise || input.comp_3159() || input.comp_3160() || input.comp_3161() || input.comp_3162() || ElytraFly.INSTANCE.forceForward != 0.0f || ElytraFly.INSTANCE.forceStrafe != 0.0f;
    }
}

