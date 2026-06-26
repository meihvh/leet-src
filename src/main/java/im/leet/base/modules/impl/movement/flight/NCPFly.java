/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$LookAndOnGround
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement.flight;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.impl.movement.Flight;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.player.MoveUtility;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class NCPFly
extends Choice {
    private final CheckBox noVanillaKick = this.checkbox("\u041d\u0435 \u043a\u0438\u043a\u0430\u0442\u044c \u043d\u0430 \u0432\u0430\u043d\u0438\u043b\u0435", false);
    private final Set<PlayerMoveC2SPacket> allowedPackets = new HashSet<PlayerMoveC2SPacket>();
    private final HashMap<Integer, Vec3d> allowedPositionsAndIDs = new HashMap();
    private int tpID = -1;

    public NCPFly() {
        super("NCP");
    }

    @Override
    public void onEnabled() {
        this.tpID = -1;
        this.allowedPackets.clear();
        this.allowedPositionsAndIDs.clear();
        if (NCPFly.mc.field_1724 != null) {
            NCPFly.mc.field_1724.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.LookAndOnGround(NCPFly.mc.field_1724.method_36454(), 90.0f, false, false));
        }
    }

    @Override
    public void onDisabled() {
        this.allowedPackets.clear();
        this.allowedPositionsAndIDs.clear();
    }

    @Override
    public void onEvent(Event event) {
        EventReceivePacket e;
        Packet<?> motionY22;
        PlayerMoveC2SPacket packet;
        EventSendPacket e2;
        Packet<?> motionY22;
        if (event instanceof EventGameTick) {
            int boostFactor;
            boolean hasMotion;
            PlayerMoveC2SPacket firstKey;
            if (NCPFly.mc.field_1724 == null || NCPFly.mc.field_1687 == null) {
                return;
            }
            if (this.allowedPackets.size() >= 400 && (firstKey = this.allowedPackets.iterator().next()) != null) {
                this.allowedPackets.remove(firstKey);
            }
            double motionX = 0.0;
            double motionY22 = 0.0;
            double motionZ = 0.0;
            double ySpeed = 0.0624;
            double moveSpeed = 0.2543;
            int ticksFlying = NCPFly.mc.field_1724.field_6012;
            boolean bl = hasMotion = MoveUtility.hasMovement(NCPFly.mc.field_1724.field_3913.field_54155) && !NCPFly.mc.field_1690.field_1903.method_1434();
            boolean tickBoost = hasMotion ? ticksFlying % 4 == 3 : ticksFlying % 5 == 2;
            boolean tickFlyTickDown = this.noVanillaKick.get() && ticksFlying % 60 >= 58;
            boolean tickFlyTickUp = this.noVanillaKick.get() && !tickFlyTickDown && ticksFlying % 60 >= 56;
            int n = boostFactor = tickBoost ? 2 : 1;
            if (NCPFly.mc.field_1690.field_1903.method_1434() && ticksFlying > 1) {
                motionY22 = ySpeed;
                NCPFly.mc.field_1724.method_18800(NCPFly.mc.field_1724.method_18798().field_1352, 0.0, NCPFly.mc.field_1724.method_18798().field_1350);
            } else {
                motionY22 = NCPFly.mc.field_1724.field_3913.field_54155.comp_3164() ? -ySpeed : 0.0;
            }
            boolean antiKicking = false;
            if ((tickFlyTickUp || tickFlyTickDown) && motionY22 == 0.0 || motionY22 > 0.0 && tickFlyTickDown || motionY22 < 0.0 && tickFlyTickUp) {
                motionY22 = tickFlyTickUp ? 0.05 : -0.05;
                antiKicking = true;
            }
            if (hasMotion) {
                double motionYaw = Math.toRadians(MoveUtility.getdir());
                motionX = -Math.sin(motionYaw) * moveSpeed;
                motionZ = Math.cos(motionYaw) * moveSpeed;
            } else {
                boostFactor += boostFactor > 1 && !antiKicking ? 1 : 0;
            }
            NCPFly.mc.field_1724.method_18800(motionX * (double)boostFactor, motionY22 * (double)boostFactor, motionZ * (double)boostFactor);
            if (ticksFlying == 0 || motionX != 0.0 || motionY22 != 0.0 || motionZ != 0.0) {
                this.sendMovePackets(motionX, motionY22, motionZ, boostFactor);
            }
        }
        if (event instanceof EventSendPacket && (motionY22 = (e2 = (EventSendPacket)event).getPacket()) instanceof PlayerMoveC2SPacket && !this.allowedPackets.contains(packet = (PlayerMoveC2SPacket)motionY22)) {
            e2.cancel();
        }
        if (event instanceof EventReceivePacket && (motionY22 = (e = (EventReceivePacket)event).getPacket()) instanceof PlayerPositionLookS2CPacket) {
            packet = (PlayerPositionLookS2CPacket)motionY22;
            if (NCPFly.mc.field_1724 == null) {
                return;
            }
            Vec3d changePos = packet.comp_3228().comp_3148();
            Vec3d rubberBandPos = new Vec3d(changePos.field_1352, changePos.field_1351, changePos.field_1350);
            if (rubberBandPos.method_1022(NCPFly.mc.field_1724.method_19538()) > 8.0) {
                Flight.INSTANCE.toggle();
                return;
            }
            if (this.allowedPositionsAndIDs.containsKey(packet.comp_3133()) && this.allowedPositionsAndIDs.get(packet.comp_3133()).equals((Object)rubberBandPos)) {
                this.allowedPositionsAndIDs.remove(packet.comp_3133());
                NCPFly.mc.field_1724.field_3944.method_52787((Packet)new TeleportConfirmC2SPacket(packet.comp_3133()));
                e.cancel();
                return;
            }
            this.tpID = packet.comp_3133();
            NCPFly.mc.field_1724.field_3944.method_52787((Packet)new TeleportConfirmC2SPacket(packet.comp_3133()));
        }
    }

    private void sendMovePackets(double motionX, double motionY, double motionZ, int factor) {
        if (NCPFly.mc.field_1724 == null) {
            return;
        }
        for (int i = 1; i < factor + 1; ++i) {
            Vec3d pos = NCPFly.mc.field_1724.method_19538().method_1031(motionX * (double)i, motionY * (double)i, motionZ * (double)i);
            PlayerMoveC2SPacket.PositionAndOnGround packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.field_1352, pos.field_1351, pos.field_1350, true, false);
            PlayerMoveC2SPacket.PositionAndOnGround bounds = new PlayerMoveC2SPacket.PositionAndOnGround(pos.field_1352, pos.field_1351 + 512.0, pos.field_1350, true, false);
            this.allowedPackets.add((PlayerMoveC2SPacket)packet);
            this.allowedPackets.add((PlayerMoveC2SPacket)bounds);
            NCPFly.mc.field_1724.field_3944.method_52787((Packet)packet);
            NCPFly.mc.field_1724.field_3944.method_52787((Packet)bounds);
            if (this.tpID < 0) break;
            ++this.tpID;
            NCPFly.mc.field_1724.field_3944.method_52787((Packet)new TeleportConfirmC2SPacket(this.tpID));
            this.allowedPositionsAndIDs.put(this.tpID, pos);
        }
    }
}

