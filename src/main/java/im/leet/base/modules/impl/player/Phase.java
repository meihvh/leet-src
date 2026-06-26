/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.AbstractBlock$AbstractBlockState
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventBlockShape;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.api.events.list.EventTravel;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;
import java.util.ArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class Phase
extends Module {
    public static final Phase INSTANCE = new Phase();
    CheckBox duplicate = (CheckBox)this.checkbox("Duplicate on flag", false).desc("Resend full block movement to server if server flag received");
    int currentTicks = 0;
    boolean isInCollisionCheck = false;
    Vec3d start = Vec3d.field_1353;
    BlinkUtility blink = new BlinkUtility();
    BlinkUtility C0F = new BlinkUtility();
    TimeUtility time = new TimeUtility();
    TimeUtility timeResend = new TimeUtility();
    ArrayList<Packet<?>> buffered = new ArrayList();
    long startedAt = -1L;
    long duration = -1L;
    EventBus<Event> events = event -> {
        Event e;
        if (event instanceof EventSendPacket) {
            e = (EventSendPacket)event;
            if (((EventSendPacket)e).packet instanceof PlayerMoveC2SPacket) {
                this.blink.queue((EventPacket)e);
                this.buffered.add(((EventSendPacket)e).packet);
            }
        }
        if (event instanceof EventReceivePacket) {
            PlayerPositionLookS2CPacket p;
            e = (EventReceivePacket)event;
            Packet patt0$temp = ((EventReceivePacket)e).packet;
            if (patt0$temp instanceof PlayerPositionLookS2CPacket && (p = (PlayerPositionLookS2CPacket)patt0$temp).comp_3228().comp_3148().method_1022(this.start) < 0.3 && this.duplicate.get()) {
                this.timeResend.reset();
            }
        }
        if (event instanceof EventTravel) {
            e = (EventTravel)event;
            boolean hasBlock = Phase.mc.field_1687.method_29546(Phase.mc.field_1724.method_5829().method_1002((double)0.01f, (double)0.01f, (double)0.01f)).anyMatch(AbstractBlock.AbstractBlockState::method_51367);
            if (!hasBlock && this.isInCollisionCheck) {
                this.blink.flush();
                this.isInCollisionCheck = false;
                this.duration = System.currentTimeMillis() - this.startedAt;
                this.timeResend.reset();
                return;
            }
            Phase.mc.field_1724.method_18799(Phase.mc.field_1724.method_18798().method_18805((double)0.6f, 1.0, (double)0.6f));
            this.isInCollisionCheck = hasBlock;
        }
        if (event instanceof EventBlockShape) {
            e = (EventBlockShape)event;
            if (((EventBlockShape)e).pos.method_10264() >= Phase.mc.field_1724.method_31478()) {
                e.cancel();
            }
        }
    };

    private Phase() {
        super("Phase", Category.PLAYER, "Allows you to pass through blocks", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        this.isInCollisionCheck = false;
        this.start = Phase.mc.field_1724.method_19538();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.blink.flush();
        this.isInCollisionCheck = false;
        this.duration = System.currentTimeMillis() - this.startedAt;
        this.timeResend.reset();
    }
}

