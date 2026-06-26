/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Vector3f
 */
package im.leet.base.modules.impl.combat.elytraaura.utilities;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventPacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.impl.combat.ElytraAura;
import im.leet.base.modules.impl.combat.elytraaura.math.ElytraAuraResolve;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public final class ElytraAuraDefensive
implements MinecraftHolder {
    static ElytraAura elytraAura = ElytraAura.INSTANCE;
    private static TimeUtility blinkTime = new TimeUtility();
    private static TimeUtility motionTime = new TimeUtility();
    public static boolean blinking = false;
    static final Queue<Packet<?>> packetQueue = new ConcurrentLinkedQueue();
    static boolean defensiving = false;
    public static boolean isMotion = false;
    static Vec3d lastBlinked = Vec3d.field_1353;
    static ArrayList<Vec3d> points = new ArrayList();

    public static void handleEvent(Event event) {
        Event e;
        LivingEntity entity = TargetsUtility.getTarget();
        if (entity != null) {
            boolean bl = isMotion = !entity.method_6128();
        }
        if (event instanceof Event3D) {
            e = (Event3D)event;
            ((Event3D)e).stack.method_22903();
            Client.RENDERER.toCamera(((Event3D)e).stack);
            VertexConsumer consumer = ((Event3D)e).buffer.getBuffer((RenderLayer)RenderLayer.field_21695);
            Vec3d pos = lastBlinked;
            Vec3d pos2 = ElytraAuraDefensive.mc.field_1724.method_30950(mc.method_61966().method_60637(true));
            Vector3f v = MathUtility.getNormal((float)pos.field_1352, (float)pos.field_1351, (float)pos.field_1350, (float)pos2.field_1352, (float)pos2.field_1351, (float)pos2.field_1350);
            consumer.method_56824(((Event3D)e).stack.method_23760(), (float)pos.field_1352, (float)pos.field_1351, (float)pos.field_1350).method_39415(Color.GREEN.getRGB()).method_61959(((Event3D)e).stack.method_23760(), v);
            consumer.method_56824(((Event3D)e).stack.method_23760(), (float)pos2.field_1352, (float)pos2.field_1351, (float)pos2.field_1350).method_39415(Color.GREEN.getRGB()).method_61959(((Event3D)e).stack.method_23760(), v);
            VertexRendering.method_62295((MatrixStack)((Event3D)e).stack, (VertexConsumer)consumer, (Box)new Box(pos.method_61888((double)0.1f), pos.method_61889((double)0.1f)), (float)1.0f, (float)1.0f, (float)0.0f, (float)1.0f);
            ((Event3D)e).stack.method_22909();
        }
        if (!ElytraAura.INSTANCE.defensive.isEnabled() || ElytraAura.INSTANCE.isAirStack.get() && InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)ElytraAura.INSTANCE.bindAirStackSetting.getBind())) {
            blinking = false;
            ElytraAuraDefensive.blink();
            return;
        }
        if (event instanceof EventAttack) {
            e = (EventAttack)event;
            if (((EventAttack)e).target == TargetsUtility.getTarget() && !ElytraAura.INSTANCE.targetIsLeave(entity) && !ElytraAuraDefensive.elytraAura.antiAimIsActive) {
                blinkTime.reset();
            }
        }
        if (ElytraAura.INSTANCE.targetIsLeave(entity) || !isMotion) {
            blinking = false;
            ElytraAuraDefensive.blink();
            return;
        }
        if (entity == null) {
            return;
        }
        if (event instanceof EventGameTick) {
            if (ElytraAuraDefensive.mc.field_1724.field_6235 > 0 || ElytraAuraDefensive.mc.field_1724.method_19538().method_1022(ElytraAuraDefensive.mc.field_1724.method_61411()) < 0.15 || blinkTime.reached(600L) || ElytraAuraDefensive.mc.field_1724.method_5739((Entity)entity) > 5.0f || ElytraAura.INSTANCE.targetIsLeave(entity)) {
                ElytraAuraDefensive.blink();
                blinkTime.reset();
            }
            blinking = isMotion;
            if (entity.method_19538().method_1022(entity.method_61411()) != 0.0) {
                motionTime.reset();
            }
            points.add(((ResolvedPositionEntity)entity).hachclientport$getResolvedPos());
            if (points.size() > 10) {
                points.removeFirst();
            }
        }
        if (event instanceof EventSendPacket) {
            boolean work;
            e = (EventSendPacket)event;
            if (((EventSendPacket)e).packet instanceof KeepAliveC2SPacket) {
                return;
            }
            boolean bl = work = !ElytraAuraDefensive.elytraAura.ifWorkitFakeLagSetting.is(ElytraAura.FakeLagCondition.TargetIsntGliding) || !entity.method_6128() || entity.method_24828() || ElytraAuraResolve.isStoyak(entity);
            if (ElytraAuraDefensive.elytraAura.isFakeLag.get() && work) {
                packetQueue.add(((EventPacket)e).getPacket());
                e.cancel();
                blinking = true;
            }
        }
    }

    private static void blink() {
        for (Packet packet : packetQueue) {
            if (packet instanceof PlayerMoveC2SPacket) {
                PlayerMoveC2SPacket move = (PlayerMoveC2SPacket)packet;
                lastBlinked = new Vec3d(move.method_12269(ElytraAuraDefensive.mc.field_1724.method_23317()), move.method_12268(ElytraAuraDefensive.mc.field_1724.method_23318()), move.method_12274(ElytraAuraDefensive.mc.field_1724.method_23321()));
            }
            NetworkUtility.sendWithoutEvent(packet);
        }
        packetQueue.clear();
        blinking = false;
    }

    public static void onDisable() {
        blinking = false;
        ElytraAuraDefensive.blink();
    }

    private ElytraAuraDefensive() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isDefensiving() {
        return defensiving;
    }
}

