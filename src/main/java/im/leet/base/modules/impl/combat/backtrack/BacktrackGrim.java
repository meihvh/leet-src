/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Queues
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.TrackedPosition
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.common.CommonPingS2CPacket
 *  net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket
 *  net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket
 *  net.minecraft.network.packet.s2c.play.EntityS2CPacket
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package im.leet.base.modules.impl.combat.backtrack;

import com.google.common.collect.Queues;
import im.leet.api.events.Event;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventPacketTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.impl.combat.backtrack.BacktrackChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.network.PacketSnapshot;
import java.lang.runtime.SwitchBootstraps;
import java.util.Objects;
import java.util.Queue;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TrackedPosition;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BacktrackGrim
extends BacktrackChoice {
    final SliderSetting delay = this.sliderSetting("Delay", 1000.0f, 0.0f, 100000.0f).increment(100.0f);
    final CheckBox attack = this.checkbox("Flush on attack", true);
    final Group render = this.group("Render").toggleable(true);
    final SliderSetting renderLight = this.render.sliderSetting("Light", 0.3f, 0.01f, 1.0f).increment(0.01f);
    final BlinkUtility blink = new BlinkUtility();
    final Queue<Packet<?>> process = Queues.newConcurrentLinkedQueue();
    final TimeUtility time = new TimeUtility();
    TrackedPosition tracked = new TrackedPosition();

    public BacktrackGrim() {
        super("Grim");
    }

    @Override
    public void onEvent(Event event) {
        LivingEntity target;
        Event e;
        if (event instanceof EventAttack && this.attack.get()) {
            this.flush("attack");
        }
        if (event instanceof EventPacketTick) {
            this.process.removeIf(it -> {
                NetworkUtility.handlePacket(it);
                return true;
            });
        }
        if (event instanceof EventReceivePacket) {
            EntityVelocityUpdateS2CPacket vel;
            Packet packet;
            e = (EventReceivePacket)event;
            target = TargetsUtility.getTarget();
            if (target == null) {
                this.flush(null);
                this.tracked = null;
                return;
            }
            if (this.time.reached(this.delay.getLong())) {
                this.flush("delay");
                return;
            }
            if (e.packet instanceof PlayerPositionLookS2CPacket) {
                this.flush("look");
                return;
            }
            if (e.packet instanceof CommonPingS2CPacket || (packet = e.packet) instanceof EntityVelocityUpdateS2CPacket && (vel = (EntityVelocityUpdateS2CPacket)packet).method_11818() == BacktrackGrim.mc.field_1724.method_5628()) {
                this.blink.queue((EventPacket)e);
                return;
            }
            Vec3d position = null;
            Packet packet2 = e.packet;
            Objects.requireNonNull(packet2);
            packet = packet2;
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{EntityS2CPacket.class, EntityPositionS2CPacket.class, EntityPositionSyncS2CPacket.class}, (Object)packet, n)) {
                case 0: {
                    EntityS2CPacket ep = (EntityS2CPacket)packet;
                    if (ep.method_11645((World)BacktrackGrim.mc.field_1687) != target) {
                        return;
                    }
                    if (this.tracked == null) {
                        this.tracked = new TrackedPosition();
                        this.tracked.method_43494(target.method_43389().method_60933());
                    }
                    position = this.tracked.method_43489((long)ep.method_36150(), (long)ep.method_36151(), (long)ep.method_36152());
                    break;
                }
                case 1: {
                    EntityPositionS2CPacket ep = (EntityPositionS2CPacket)packet;
                    if (ep.comp_3237() != target.method_5628()) {
                        return;
                    }
                    position = ep.comp_3238().comp_3148();
                    break;
                }
                case 2: {
                    EntityPositionSyncS2CPacket sync = (EntityPositionSyncS2CPacket)packet;
                    if (sync.comp_3223() != target.method_5628()) {
                        return;
                    }
                    position = sync.comp_3224().comp_3148();
                    break;
                }
            }
            if (position != null) {
                if (this.tracked == null) {
                    this.tracked = new TrackedPosition();
                }
                this.tracked.method_43494(position);
                if (RotationUtility.squaredBoxDistance((Entity)target, (Entity)BacktrackGrim.mc.field_1724, position) < RotationUtility.squaredBoxDistance((Entity)target, (Entity)BacktrackGrim.mc.field_1724)) {
                    this.flush("closer");
                    return;
                }
            }
            this.blink.queue((EventPacket)e);
        }
        if (event instanceof Event3D) {
            e = (Event3D)event;
            if (this.render.isEnabled()) {
                target = TargetsUtility.getTarget();
                if (target == null || this.tracked == null) {
                    return;
                }
                ((Event3D)e).stack.method_22903();
                ((Event3D)e).stack.method_61958(BacktrackGrim.mc.field_1773.method_19418().method_19326().method_1020(this.tracked.method_60933()));
                int light = Math.round((float)BacktrackGrim.mc.field_1687.method_22339(BlockPos.field_10980) * this.renderLight.get());
                mc.method_1561().method_62424((Entity)target, 0.0, 0.0, 0.0, 1.0f, ((Event3D)e).stack, (VertexConsumerProvider)mc.method_22940().method_23000(), light);
                ((Event3D)e).stack.method_22909();
            }
        }
    }

    void flush(String reason) {
        for (PacketSnapshot snap : this.blink.queue) {
            this.process.add(snap.packet());
        }
        this.blink.queue.clear();
        this.time.reset();
        this.tracked = null;
        if (reason != null) {
            ChatUtility.sendDebug("FLUSH! " + reason + " ts=" + System.currentTimeMillis());
        }
    }

    @Override
    public void onDisabled() {
        this.flush("disable");
    }
}

