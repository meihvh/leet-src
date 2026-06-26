/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$OnGroundOnly
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement.flight;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class GrimGlideFly
extends Choice {
    final SliderSetting speed = this.sliderSetting("Speed", 0.2f, 0.0f, 1.0f).increment(0.01f);
    final SliderSetting interval = this.sliderSetting("Interval", 2.0f, 1.0f, 4.0f);
    final SliderSetting motionY = this.sliderSetting("Motion Y", 0.0f, -1.0f, 1.0f).increment(0.01f);
    final CheckBox antiKick = this.checkbox("Anti kick", false);
    int delay;

    public GrimGlideFly() {
        super("Grim Glide");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventGameTick) {
            if (GrimGlideFly.mc.field_1724.method_24828()) {
                return;
            }
            if (this.delay > 0) {
                --this.delay;
                return;
            }
            if (GrimGlideFly.mc.field_1724.field_6012 % this.interval.getInt() == 0) {
                NetworkUtility.send(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
            }
            Vec3d velocity = GrimGlideFly.mc.field_1724.method_18798();
            velocity.field_1351 = this.motionY.get();
            double sqrtSpeed = Math.sqrt(velocity.field_1352 * velocity.field_1352 + velocity.field_1350 * velocity.field_1350);
            if (sqrtSpeed > 0.0 && this.speed.get() > 0.0f) {
                velocity.field_1352 = velocity.field_1352 / sqrtSpeed * (double)this.speed.get();
                velocity.field_1350 = velocity.field_1350 / sqrtSpeed * (double)this.speed.get();
            }
            if (this.antiKick.get() && GrimGlideFly.mc.field_1724.field_6012 % 40 == 0) {
                velocity.field_1351 = -0.04;
                this.delay = 1;
            }
        }
    }
}

