/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.common.CommonPongC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 */
package im.leet.base.modules.impl.movement.flight;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventMove;
import im.leet.api.events.list.EventSendPacket;
import im.leet.api.events.list.EventTravel;
import im.leet.base.rotations.Angle;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.network.BlinkUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class MatrixClip
extends Choice {
    int ticks = 0;
    BlinkUtility blinkUtil = new BlinkUtility();

    public MatrixClip() {
        super("Matrix Clip");
    }

    @Override
    public void onEnabled() {
        this.ticks = 0;
        this.blinkUtil.flush();
    }

    @Override
    public void onDisabled() {
        this.blinkUtil.flush();
    }

    @Override
    public void onEvent(Event event) {
        Event e;
        if (event instanceof EventSendPacket) {
            e = (EventSendPacket)event;
            if (((EventSendPacket)e).packet instanceof PlayerMoveC2SPacket || ((EventSendPacket)e).packet instanceof CommonPongC2SPacket) {
                // empty if block
            }
        }
        if (event instanceof EventMove) {
            e = (EventMove)event;
        }
        if (event instanceof EventTravel) {
            e = (EventTravel)event;
            e.cancel();
            MatrixClip.mc.field_1724.method_18800(0.0, 0.0, 0.0);
            ++this.ticks;
            float dir = MoveUtility.getdir();
            if (dir != -1.0f) {
                double mult1 = 0.2;
                double sin = -Math.sin((double)dir * (Math.PI / 180));
                double cos = Math.cos((double)dir * (Math.PI / 180));
                int tries = 5;
                boolean hasBlock = !MatrixClip.mc.field_1687.method_8320(MatrixClip.mc.field_1724.method_24515().method_10074()).method_26215();
                Angle angle = Client.ROTATION.getRotate();
                for (int i = 0; i < tries; ++i) {
                    MoveUtility.DPSMUSORA(sin * mult1, 0.0, cos * mult1);
                    NetworkUtility.send(new PlayerMoveC2SPacket.Full(MatrixClip.mc.field_1724.method_23317(), MatrixClip.mc.field_1724.method_23318(), MatrixClip.mc.field_1724.method_23321(), angle.getYaw(), angle.getPitch(), true, MatrixClip.mc.field_1724.field_5976));
                }
            }
        }
    }
}

