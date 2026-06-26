/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 */
package im.leet.base.modules.impl.movement;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class LongJump
extends Module {
    public static final LongJump INSTANCE = new LongJump();
    private final EnumSetting<Mode> mode = this.enumSetting("Method", Mode.Fall);
    private final SliderSetting boostSpeed = this.sliderSetting("Boost speed", 2.1f, -3.0f, 8.0f).increment(0.01f);
    private boolean receivedFlag;
    private boolean canBoost;
    private boolean boosted;
    private boolean touchGround;
    EventBus<Event> events = event -> {
        EventPacket p;
        Packet<?> packet;
        if (event instanceof EventReceivePacket && (packet = (p = (EventReceivePacket)event).getPacket()) instanceof PlayerPositionLookS2CPacket) {
            this.receivedFlag = true;
        }
        if (!(event instanceof EventSendPacket) || (packet = (p = (EventSendPacket)event).getPacket()) instanceof PlayerMoveC2SPacket) {
            // empty if block
        }
        if (event instanceof EventGameTick) {
            for (int i = 0; i < 2; ++i) {
                NetworkUtility.sendWithoutEvent(VehicleMoveC2SPacket.method_65307((Entity)LongJump.mc.field_1724));
            }
            NetworkUtility.sendWithoutEvent(VehicleMoveC2SPacket.method_65307((Entity)LongJump.mc.field_1724));
            if (!LongJump.mc.field_1724.method_24828() && this.touchGround) {
                this.touchGround = false;
            }
            if (LongJump.mc.field_1724.method_24828() && !this.touchGround) {
                LongJump.mc.field_1724.method_6043();
                this.boosted = false;
            }
            if (LongJump.mc.field_1724.field_6017 >= 0.25 && !this.boosted) {
                this.canBoost = true;
            }
            if (this.canBoost) {
                NetworkUtility.sendWithoutEvent(VehicleMoveC2SPacket.method_65307((Entity)LongJump.mc.field_1724));
                MoveUtility.setSpeed(this.boostSpeed.get());
                LongJump.mc.field_1724.method_18800(LongJump.mc.field_1724.method_18798().field_1352, 0.42, LongJump.mc.field_1724.method_18798().field_1350);
                this.boosted = true;
            }
            if (this.receivedFlag && this.boosted) {
                this.toggle();
                this.canBoost = false;
                this.receivedFlag = false;
            }
        }
    };

    private LongJump() {
        super("Long Jump", Category.MOVEMENT, "Just a long jump", new Tag[0]);
    }

    @Override
    public void onEnable() {
        this.boosted = false;
        this.canBoost = false;
        this.receivedFlag = false;
        this.touchGround = false;
        if (this.mode.is(Mode.NoGround)) {
            if (LongJump.mc.field_1724.method_24828()) {
                LongJump.mc.field_1724.method_6043();
            }
            this.touchGround = true;
        }
    }

    private static enum Mode {
        Fall,
        NoGround;

    }
}

