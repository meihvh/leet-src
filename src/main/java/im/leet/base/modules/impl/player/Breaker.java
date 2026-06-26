/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BedBlock
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket
 *  net.minecraft.util.Hand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventMove;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.Angle;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.SensUtility;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Breaker
extends Module {
    public static final Breaker INSTANCE = new Breaker();
    BlockPos breaking = null;
    EventBus<Event> events = event -> {
        Event e;
        if (event instanceof EventMove) {
            BlockState state;
            e = (EventMove)event;
            for (int x = -3; x <= 3; ++x) {
                for (int z = -3; z <= 3; ++z) {
                    for (int y = -3; y < 3; ++y) {
                        BlockPos pos = Breaker.mc.field_1724.method_24515().method_10069(x, y, z);
                        BlockState state2 = Breaker.mc.field_1687.method_8320(pos);
                        if (!(state2.method_26204() instanceof BedBlock)) continue;
                        this.breaking = pos;
                    }
                }
            }
            if (this.breaking != null && ((state = Breaker.mc.field_1687.method_8320(this.breaking)).method_26215() || !(state.method_26204() instanceof BedBlock))) {
                this.breaking = null;
            }
            if (this.breaking == null) {
                Client.TIMER = 1.0f;
                Breaker.mc.field_1724.method_6104(Hand.field_5808);
                return;
            }
            Vec3d vec = this.breaking.method_46558().method_1031(0.0, 0.5, 0.0);
            Angle t = RotationUtility.calculate(vec);
            Angle curr = Client.ROTATION.getRotate().copy();
            float deltaYaw = MathHelper.method_15393((float)(t.getYaw() - curr.getYaw()));
            float deltaPitch = t.getPitch() - curr.getPitch();
            curr.setYaw((float)((double)curr.getYaw() + SensUtility.getSensitivity(deltaYaw)));
            curr.setPitch(90.0f);
            Client.ROTATION.rotate(curr, false);
            NetworkUtility.sendWithoutEvent(VehicleMoveC2SPacket.method_65307((Entity)Breaker.mc.field_1724));
            Breaker.mc.field_1761.method_2902(this.breaking, Direction.field_11036);
            e.ground = false;
            Breaker.mc.field_1724.method_6104(Hand.field_5808);
        }
        if (event instanceof Event3D) {
            e = (Event3D)event;
            if (this.breaking == null) {
                return;
            }
            ((Event3D)e).stack.method_22903();
            Client.RENDERER.toCamera(((Event3D)e).stack);
            VertexConsumer consumer = ((Event3D)e).buffer.getBuffer((RenderLayer)RenderLayer.field_21695);
            VertexRendering.method_62295((MatrixStack)((Event3D)e).stack, (VertexConsumer)consumer, (Box)new Box(this.breaking), (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            ((Event3D)e).stack.method_22909();
        }
    };

    private Breaker() {
        super("Breaker", Category.PLAYER, "Automatically breaks the bed", new Tag[0]);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        Client.TIMER = 1.0f;
    }
}

