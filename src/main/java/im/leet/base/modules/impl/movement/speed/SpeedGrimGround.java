/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$OnGroundOnly
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.modules.impl.movement.speed;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventMoveVelocity;
import im.leet.api.events.list.EventPostMotion;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;

public class SpeedGrimGround
extends Choice {
    int tick;
    int ground;
    boolean flag;
    boolean jump;
    final SliderSetting speed = this.sliderSetting("Speed", 1.0f, 0.0f, 1.0f).increment(0.001f);
    final CheckBox yMotion = this.checkbox("Change Y motion", false);
    final SliderSetting motionY = this.sliderSetting("Motion Y", 0.0f, -1.0f, 1.0f).increment(0.001f);

    public SpeedGrimGround() {
        super("Grim Ground");
    }

    @Override
    public void onEvent(Event event) {
        Event e;
        if (event instanceof EventMoveVelocity) {
            e = (EventMoveVelocity)event;
            double factor = 0.03;
            if (this.tick % 2 == 0) {
                factor = SpeedGrimGround.mc.field_1724.method_24828() ? 0.085 : 0.03;
            }
            float yaw = (Client.ROTATION.getRotate().getYaw() + 90.0f) * ((float)Math.PI / 180);
            float sin = MathHelper.method_15374((float)yaw);
            float cos = MathHelper.method_15362((float)yaw);
            ((EventMoveVelocity)e).movement.field_1352 += (factor *= (double)this.speed.get()) * (double)cos;
            ((EventMoveVelocity)e).movement.field_1350 += factor * (double)sin;
            if (this.yMotion.get() && !SpeedGrimGround.mc.field_1724.method_24828()) {
                ((EventMoveVelocity)e).movement.field_1351 += (double)this.motionY.get();
            }
            this.jump = false;
        }
        if (event instanceof EventReceivePacket) {
            Packet packet;
            e = (EventReceivePacket)event;
            if (((EventReceivePacket)e).packet instanceof PlayerPositionLookS2CPacket && this.tick % 2 == 1) {
                ++this.tick;
            }
            if ((packet = ((EventReceivePacket)e).packet) instanceof EntityVelocityUpdateS2CPacket) {
                EntityVelocityUpdateS2CPacket p = (EntityVelocityUpdateS2CPacket)packet;
                this.flag = false;
                if (p.method_11818() == SpeedGrimGround.mc.field_1724.method_5628()) {
                    this.jump = true;
                }
            }
        }
        if (event instanceof EventPostMotion) {
            NetworkUtility.send(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
            if (this.tick % 2 == 0) {
                NetworkUtility.send(new PlayerMoveC2SPacket.OnGroundOnly(false, false));
                this.flag = true;
            }
        }
        if (event instanceof EventInput) {
            e = (EventInput)event;
            if (this.jump) {
                ((EventInput)e).setJump(true);
            }
        }
    }

    @Override
    public void onEnabled() {
        this.tick = 0;
        this.ground = 0;
        NetworkUtility.send(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
        NetworkUtility.send(new PlayerMoveC2SPacket.OnGroundOnly(false, false));
    }
}

