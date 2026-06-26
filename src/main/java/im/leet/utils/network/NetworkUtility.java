/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayNetworkHandler
 *  net.minecraft.client.network.PendingUpdateManager
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.network.listener.PacketListener
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket
 *  net.minecraft.util.Hand
 *  net.minecraft.util.PlayerInput
 *  net.minecraft.util.hit.BlockHitResult
 */
package im.leet.utils.network;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.rotations.Angle;
import im.leet.mixin.accessor.ClientConnectionAccessor;
import im.leet.mixin.accessor.IClientWorld;
import im.leet.utils.math.TimeUtility;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.hit.BlockHitResult;

public final class NetworkUtility
implements MinecraftHolder {
    private static boolean shouldTriggerEvent = true;
    private static boolean serverSprinting = false;
    private static float tpsFactor = 0.0f;
    private static int received = 0;
    private static long lastReceive = 0L;
    private static TimeUtility tpsTimer = new TimeUtility();

    public static void pauseEvents() {
        shouldTriggerEvent = false;
    }

    public static void resumeEvents() {
        shouldTriggerEvent = true;
    }

    public static boolean shouldTriggerEvent() {
        return shouldTriggerEvent;
    }

    public static void updateServerSprint(boolean sprint) {
        serverSprinting = sprint;
    }

    public static boolean serverSprinting() {
        return serverSprinting;
    }

    public static void sendAngle(Angle angle) {
        NetworkUtility.send(new PlayerMoveC2SPacket.Full(NetworkUtility.mc.field_1724.method_23317(), NetworkUtility.mc.field_1724.method_23318(), NetworkUtility.mc.field_1724.method_23321(), angle.getYaw(), angle.getPitch(), NetworkUtility.mc.field_1724.method_24828(), NetworkUtility.mc.field_1724.field_5976));
    }

    public static void sendWithoutEvent(Runnable runnable) {
        NetworkUtility.pauseEvents();
        runnable.run();
        NetworkUtility.resumeEvents();
    }

    public static void sendWithoutEvent(Packet<?> packet) {
        NetworkUtility.pauseEvents();
        NetworkUtility.send(packet);
        NetworkUtility.resumeEvents();
    }

    public static void send(Packet<?> packet) {
        if (mc.method_1562() == null) {
            return;
        }
        if (packet instanceof ClickSlotC2SPacket) {
            ClickSlotC2SPacket click = (ClickSlotC2SPacket)packet;
            NetworkUtility.mc.field_1761.method_2906(click.comp_3842(), (int)click.comp_3844(), (int)click.comp_3845(), click.comp_3846(), (PlayerEntity)NetworkUtility.mc.field_1724);
        } else {
            mc.method_1562().method_52787(packet);
        }
    }

    public static void sendInputPacket(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean sneak, boolean sprint) {
        PlayerInput input = new PlayerInput(forward, backward, left, right, jump, sneak, sprint);
        mc.method_1562().method_52787((Packet)new PlayerInputC2SPacket(input));
    }

    public static void sendOnlySneak(boolean sneak) {
        PlayerInput playerInput = NetworkUtility.mc.field_1724.field_3913.field_54155;
        NetworkUtility.sendInputPacket(playerInput.comp_3159(), playerInput.comp_3160(), playerInput.comp_3161(), playerInput.comp_3162(), playerInput.comp_3163(), sneak, playerInput.comp_3165());
    }

    public static void sendUse(Hand hand) {
        NetworkUtility.sendUse(hand, Client.ROTATION.getRotate());
    }

    public static void sendUse(Hand hand, Angle angle) {
        try (PendingUpdateManager pendingUpdateManager = ((IClientWorld)NetworkUtility.mc.field_1687).client$pending().method_41937();){
            int i = pendingUpdateManager.method_41942();
            PlayerInteractItemC2SPacket packet = new PlayerInteractItemC2SPacket(hand, i, angle.getYaw(), angle.getPitch());
            NetworkUtility.send(packet);
        }
    }

    public static void sendUse(Hand hand, BlockHitResult hitResult) {
        try (PendingUpdateManager pendingUpdateManager = ((IClientWorld)NetworkUtility.mc.field_1687).client$pending().method_41937();){
            int i = pendingUpdateManager.method_41942();
            PlayerInteractBlockC2SPacket packet = new PlayerInteractBlockC2SPacket(hand, hitResult, i);
            NetworkUtility.send(packet);
        }
    }

    public static boolean is(String server) {
        return mc.method_1562() != null && mc.method_1562().method_45734() != null && NetworkUtility.mc.method_1562().method_45734().field_3761.contains(server);
    }

    public static void handleCPacket(Packet<?> packet) {
        if (packet instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket e = (PlayerMoveC2SPacket)packet;
            PlayerState.lastGround = e.method_12273();
            PlayerState.lastVertical = NetworkUtility.mc.field_1724.field_5992;
        }
    }

    public static void handleSPacket(Packet<?> packet) {
        if (packet instanceof WorldTimeUpdateS2CPacket) {
            WorldTimeUpdateS2CPacket e = (WorldTimeUpdateS2CPacket)packet;
            lastReceive = System.currentTimeMillis();
        }
    }

    public static void handlePacket(Packet<?> packet) {
        ClientPlayNetworkHandler clientPlayNetworkHandler = mc.method_1562();
        if (!(clientPlayNetworkHandler instanceof ClientPlayNetworkHandler)) {
            return;
        }
        ClientPlayNetworkHandler net = clientPlayNetworkHandler;
        if (mc.method_18854()) {
            ClientConnectionAccessor.handlePacket(packet, (PacketListener)net);
        } else {
            mc.execute(() -> ClientConnectionAccessor.handlePacket(packet, (PacketListener)net));
        }
    }

    public static UUID offlineUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }

    private NetworkUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static float getTpsFactor() {
        return tpsFactor;
    }

    static {
        Client.EVENTS.register(new HandlerHelper());
    }

    public static final class PlayerState {
        public static boolean lastGround = false;
        public static boolean lastVertical = false;
        public static int lastTp = 0;

        private PlayerState() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    private static class HandlerHelper {
        EventBus<Event> events = event -> {
            if (event instanceof EventGameTick && tpsTimer.reached(1000L, true)) {
                tpsFactor = 20.0f - 1000.0f / ((float)(System.currentTimeMillis() - lastReceive) * 50.0f);
            }
        };

        private HandlerHelper() {
        }
    }
}

