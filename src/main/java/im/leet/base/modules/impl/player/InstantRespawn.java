/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.utils.math.MathUtility;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;

public class InstantRespawn
extends Module {
    public static final InstantRespawn INSTANCE = new InstantRespawn();
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            EventGameTick gameTick = (EventGameTick)event;
            if (InstantRespawn.mc.field_1724 == null) {
                return;
            }
            if (InstantRespawn.mc.field_1724.method_6032() <= 1.0f && InstantRespawn.mc.field_1724.field_6235 > 0) {
                InstantRespawn.mc.field_1724.method_18800(InstantRespawn.mc.field_1724.method_18798().field_1352, 0.5, InstantRespawn.mc.field_1724.method_18798().field_1350);
            }
        }
        if (event instanceof EventReceivePacket) {
            HealthUpdateS2CPacket p;
            EventReceivePacket receivePacket = (EventReceivePacket)event;
            if (InstantRespawn.mc.field_1724 == null) {
                return;
            }
            Packet<?> patt0$temp = receivePacket.getPacket();
            if (patt0$temp instanceof HealthUpdateS2CPacket && (p = (HealthUpdateS2CPacket)patt0$temp).method_11833() <= 1.0f) {
                InstantRespawn.mc.field_1724.method_18800(InstantRespawn.mc.field_1724.method_18798().field_1352, (double)MathUtility.random(0.01f, 0.1f), InstantRespawn.mc.field_1724.method_18798().field_1350);
                mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.Full(InstantRespawn.mc.field_1724.method_23317(), InstantRespawn.mc.field_1724.method_23318() - (double)0.3f, InstantRespawn.mc.field_1724.method_23321(), Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), !InstantRespawn.mc.field_1724.method_24828(), false));
            }
        }
    };

    private InstantRespawn() {
        super("Instant Respawn", Category.PLAYER, "Resurrects you at the place of death", Tag.EXPLOIT);
    }
}

