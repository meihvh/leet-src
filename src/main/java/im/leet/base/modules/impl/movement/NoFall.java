/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventMove;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class NoFall
extends Module {
    public static final NoFall INSTANCE = new NoFall();
    private final EnumSetting<Mode> mode = this.enumSetting("Reset mode", Mode.Packet);
    int flags = 0;
    boolean shouldReset = false;
    boolean lastGround = false;
    private Vec3d vec3 = new Vec3d(1337.0, 0.0, 1337.0);
    private double prevFallDistance = 0.0;
    private boolean prevOnGround = false;
    private boolean flag = false;
    EventBus<Event> events = event -> {
        Event e;
        Packet patt0$temp;
        EventPacket p;
        if (event instanceof EventGameTick) {
            switch (this.mode.get().ordinal()) {
                case 0: 
                case 1: {
                    BlockState block = NoFall.mc.field_1687.method_8320(NoFall.mc.field_1724.method_24515().method_10069(0, (int)(-Math.min(2.0, Math.abs(NoFall.mc.field_1724.method_18798().field_1351) + 1.0)), 0)).method_26204();
                    if (block.method_9564().method_26215() || block.method_9564().method_51176() || !(NoFall.mc.field_1724.field_6017 > 3.25)) break;
                    if (this.mode.is(Mode.Packet)) {
                        NetworkUtility.send(new PlayerMoveC2SPacket.Full(NoFall.mc.field_1724.method_23317(), NoFall.mc.field_1724.method_23318() + 1.0E-9, NoFall.mc.field_1724.method_23321(), Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), false, false));
                    } else {
                        NoFall.mc.field_1724.method_18800(0.0, 0.1, 0.0);
                    }
                    NoFall.mc.field_1724.field_6017 = 0.0;
                    break;
                }
                case 2: {
                    if (this.shouldReset) break;
                    this.shouldReset = NoFall.mc.field_1724.field_6017 > 3.25;
                    break;
                }
                case 3: {
                    BlockState block = NoFall.mc.field_1687.method_8320(NoFall.mc.field_1724.method_24515().method_10069(0, -1, 0));
                    if (!(NoFall.mc.field_1724.field_6017 > 3.25)) break;
                    this.shouldReset = true;
                    NoFall.mc.field_1724.field_6017 = 0.0;
                }
            }
        }
        if (event instanceof EventMove) {
            EventMove move = (EventMove)event;
            switch (this.mode.get().ordinal()) {
                case 4: {
                    if (move.ground && !this.prevOnGround && this.prevFallDistance >= 3.0) {
                        this.flag = true;
                        move.x = this.vec3.field_1352;
                        move.y = this.vec3.field_1351;
                        move.z = this.vec3.field_1350;
                        NoFall.mc.field_1724.field_6017 = 0.0;
                    }
                    this.prevOnGround = move.ground;
                    this.prevFallDistance = NoFall.mc.field_1724.field_6017;
                    if (!this.flag || !NoFall.mc.field_1724.method_24828()) break;
                    this.flag = false;
                }
            }
        }
        if (event instanceof EventSendPacket) {
            p = (EventSendPacket)event;
            patt0$temp = ((EventSendPacket)p).packet;
            if (patt0$temp instanceof PlayerMoveC2SPacket) {
                PlayerMoveC2SPacket f = (PlayerMoveC2SPacket)patt0$temp;
                switch (this.mode.get().ordinal()) {
                    case 2: {
                        if (!f.method_12273() || !this.shouldReset) break;
                        p.cancel();
                        NetworkUtility.sendWithoutEvent(new PlayerMoveC2SPacket.Full(NoFall.mc.field_1724.method_23317(), NoFall.mc.field_1724.method_23318() + 1.0E-4, NoFall.mc.field_1724.method_23321(), Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch(), false, false));
                    }
                }
            }
        }
        if (event instanceof EventMove) {
            e = (EventMove)event;
            if (this.mode.is(Mode.JumpReset) && this.shouldReset && ((EventMove)e).ground) {
                ((EventMove)e).ground = false;
                ((EventMove)e).y += 0.01;
            }
        }
        if (event instanceof EventReceivePacket) {
            p = (EventReceivePacket)event;
            patt0$temp = ((EventReceivePacket)p).packet;
            if (patt0$temp instanceof PlayerPositionLookS2CPacket) {
                PlayerPositionLookS2CPacket l = (PlayerPositionLookS2CPacket)patt0$temp;
                if (this.mode.is(Mode.JumpReset)) {
                    ++this.flags;
                }
            }
        }
        if (event instanceof EventInput) {
            e = (EventInput)event;
            switch (this.mode.get().ordinal()) {
                case 2: {
                    if (!this.shouldReset || !NoFall.mc.field_1724.method_24828()) break;
                    ((EventInput)e).setJump(true);
                    ((EventInput)e).setSneak(true);
                    ((EventInput)e).setStrafe(0.0f);
                    ((EventInput)e).setForward(0.0f);
                    this.shouldReset = false;
                    break;
                }
                case 3: {
                    if (!this.shouldReset) break;
                    ((EventInput)e).setJump(true);
                    ((EventInput)e).setSneak(true);
                    ((EventInput)e).setStrafe(0.0f);
                    ((EventInput)e).setForward(0.0f);
                    if (!NoFall.mc.field_1724.method_24828()) break;
                    this.shouldReset = false;
                }
            }
        }
    };

    private NoFall() {
        super("No Fall", Category.MOVEMENT, "Prevents falling damage", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        this.flags = 0;
        this.shouldReset = false;
    }

    private static enum Mode {
        Packet,
        Motion,
        MatrixGround,
        JumpReset,
        GrimLast;

    }
}

