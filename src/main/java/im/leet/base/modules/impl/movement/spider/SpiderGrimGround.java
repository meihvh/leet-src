/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$OnGroundOnly
 */
package im.leet.base.modules.impl.movement.spider;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventMove;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

class SpiderGrimGround
extends Choice {
    final SliderSetting timer = this.sliderSetting("Timer", 0.5f, 0.0f, 1.0f).increment(0.1f);

    SpiderGrimGround() {
        super("Grim Ground");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMove) {
            EventMove e = (EventMove)event;
            if (!SpiderGrimGround.mc.field_1724.method_24828() && SpiderGrimGround.mc.field_1724.field_5976) {
                SpiderGrimGround.mc.field_1724.method_24830(true);
                e.ground = true;
                NetworkUtility.send(new PlayerMoveC2SPacket.OnGroundOnly(true, SpiderGrimGround.mc.field_1724.field_5976));
                Client.TIMER = this.timer.get();
                SpiderGrimGround.mc.field_1724.method_6043();
            }
        }
    }
}

