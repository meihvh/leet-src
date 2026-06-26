/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.network.packet.s2c.play.EntityS2CPacket
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Vector3f
 */
package im.leet.base.modules.impl.combat.resolver;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.impl.combat.resolver.ResolverMode;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class Lag2Resolver
extends ResolverMode {
    ArrayList<Vec3d> deltas = new ArrayList();
    public Vec3d resolved = Vec3d.field_1353;
    Vec3d lastLagged = Vec3d.field_1353;
    TimeUtility last = new TimeUtility();
    public TimeUtility resolveTimeout = new TimeUtility();

    public Lag2Resolver() {
        super("Lag2");
        this.toggleable(false);
    }

    @Override
    public Vec3d resolveElytra(Vec3d origin, float cooldown) {
        if (this.resolveTimeout.reached(300L)) {
            return origin;
        }
        return this.resolved;
    }

    @Override
    public boolean isFakeLagging() {
        return this.resolveTimeout.reached(100L);
    }

    @Override
    public void onEvent(Event event) {
        Object longest;
        if (event instanceof EventGameTick && TargetsUtility.getTarget() != null) {
            LivingEntity target = TargetsUtility.getTarget();
            Vec3d server = ((ResolvedPositionEntity)target).hachclientport$getResolvedPos();
            if (this.last.reached(50L) && this.lastLagged.method_1022(server) > 2.0) {
                this.lastLagged = server;
                this.deltas.add(this.lastLagged);
                this.resolveTimeout.reset();
            }
            if (this.deltas.size() > 2) {
                this.deltas.removeFirst();
            }
            if (!this.deltas.isEmpty()) {
                longest = this.deltas.getFirst();
                for (Vec3d delta : this.deltas) {
                    if (!(server.method_1022(delta) > server.method_1022((Vec3d)longest)) || !(delta.field_1351 > ((Vec3d)longest).field_1351)) continue;
                    longest = delta;
                }
                this.resolved = longest;
            }
        }
        if (event instanceof EventReceivePacket) {
            EntityS2CPacket m;
            EventReceivePacket p = (EventReceivePacket)event;
            longest = p.packet;
            if (longest instanceof EntityS2CPacket && (m = (EntityS2CPacket)longest).method_22826()) {
                this.last.reset();
            }
        }
        if (event instanceof Event3D) {
            Event3D e = (Event3D)event;
            e.stack.method_22903();
            Client.RENDERER.toCamera(e.stack);
            VertexConsumer consumer = e.buffer.getBuffer((RenderLayer)RenderLayer.field_21695);
            for (Vec3d v : this.deltas) {
                VertexRendering.method_62295((MatrixStack)e.stack, (VertexConsumer)consumer, (Box)new Box(v.method_61888(0.25), v.method_61889(0.25)), (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            VertexRendering.method_62295((MatrixStack)e.stack, (VertexConsumer)consumer, (Box)new Box(this.resolved.method_61888(0.25), this.resolved.method_61889(0.25)), (float)1.0f, (float)1.0f, (float)0.0f, (float)1.0f);
            Vec3d g = Lag2Resolver.mc.field_1724.method_30950(mc.method_61966().method_60637(true));
            Vec3d f = this.resolved;
            Vector3f j = MathUtility.getNormal((float)g.field_1352, (float)g.field_1351, (float)g.field_1350, (float)f.field_1352, (float)f.field_1351, (float)f.field_1350);
            consumer.method_56824(e.stack.method_23760(), (float)g.field_1352, (float)g.field_1351, (float)g.field_1350).method_39415(Color.GREEN.getRGB()).method_61959(e.stack.method_23760(), j);
            consumer.method_56824(e.stack.method_23760(), (float)f.field_1352, (float)f.field_1351, (float)f.field_1350).method_39415(Color.GREEN.getRGB()).method_61959(e.stack.method_23760(), j);
            e.stack.method_22909();
        }
    }
}

