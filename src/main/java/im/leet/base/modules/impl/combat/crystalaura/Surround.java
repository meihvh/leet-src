/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.Blocks
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.Item
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.util.Hand
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat.crystalaura;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.rotations.Angle;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.SensUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.InventoryUtility;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class Surround
implements MinecraftHolder {
    public static ArrayList<BlockPos> toDefensive = new ArrayList();
    static BlockPos currentDefensive = null;

    public static boolean handleEvent(Event event) {
        if (event instanceof Event3D) {
            Event3D e = (Event3D)event;
            e.stack.method_22903();
            Client.RENDERER.toCamera(e.stack);
            VertexConsumer consumer = e.buffer.getBuffer((RenderLayer)RenderLayer.field_21695);
            for (BlockPos pos : toDefensive) {
                VertexRendering.method_62295((MatrixStack)e.stack, (VertexConsumer)consumer, (Box)new Box(pos), (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            Client.RENDERER.toWorld(e.stack);
            e.stack.method_22909();
            return false;
        }
        if (event instanceof EventGameTick) {
            for (int x = -3; x <= 3; ++x) {
                for (int z = -3; z <= 3; ++z) {
                    for (int y = -3; y < 0; ++y) {
                        boolean valid;
                        BlockPos pos = Surround.mc.field_1724.method_24515().method_10069(x, y, z);
                        Block block = Surround.mc.field_1687.method_8320(pos).method_26204();
                        BlockState state = Surround.mc.field_1687.method_8320(pos);
                        boolean bl = valid = state.method_27852(Blocks.field_10540) && Surround.mc.field_1687.method_8320(pos.method_10084()).method_27852(Blocks.field_10124) && (double)pos.method_10264() <= Surround.mc.field_1724.method_23318() && Surround.mc.field_1724.method_19538().method_1022(pos.method_46558()) < 3.0;
                        if (valid && !toDefensive.contains(pos)) {
                            toDefensive.add(pos);
                        }
                        if (!toDefensive.contains(pos) || valid) continue;
                        toDefensive.remove(pos);
                    }
                }
            }
            Iterator<BlockPos> it = toDefensive.iterator();
            while (it.hasNext()) {
                BlockPos pos = it.next();
                if (!(Surround.mc.field_1724.method_19538().method_1022(pos.method_46558()) > 4.0)) continue;
                it.remove();
            }
            if (toDefensive.isEmpty()) {
                return false;
            }
            if (currentDefensive == null) {
                currentDefensive = toDefensive.getFirst();
                return false;
            }
            if (!toDefensive.contains(currentDefensive)) {
                currentDefensive = null;
                return false;
            }
            Vec3d v = currentDefensive.method_46558().method_1031(0.0, 0.5, 0.0);
            Angle p = RotationUtility.calculate(v);
            Angle current = Client.ROTATION.getRotate().copy();
            float deltaYaw = MathHelper.method_15393((float)(p.getYaw() - current.getYaw()));
            float deltaPitch = p.getPitch() - current.getPitch();
            current.setYaw((float)((double)current.getYaw() + SensUtility.getSensitivity(deltaYaw + MathUtility.gaussian(5.0f, 2.0f))));
            current.setPitch((float)MathHelper.method_15350((double)((double)current.getPitch() + SensUtility.getSensitivity(deltaPitch + MathUtility.gaussian(5.0f, 2.0f))), (double)-90.0, (double)90.0));
            Client.ROTATION.rotate(current, false);
            int slot = -1;
            for (int i = 0; i < 9; ++i) {
                BlockItem b;
                Item item = Surround.mc.field_1724.method_31548().method_5438(i).method_7909();
                if (!(item instanceof BlockItem) || (b = (BlockItem)item).method_7711().method_9564().method_27852(Blocks.field_10540)) continue;
                slot = i;
                break;
            }
            if (slot == -1) {
                slot = InventoryUtility.findHotbar(Blocks.field_10540.method_8389());
            }
            if (slot != -1) {
                int last = Surround.mc.field_1724.method_31548().method_67532();
                if (last != slot) {
                    mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(slot));
                }
                NetworkUtility.sendUse(Hand.field_5808, new BlockHitResult(v, Direction.field_11036, currentDefensive, false));
                Surround.mc.field_1724.method_6104(Hand.field_5808);
                if (last != slot) {
                    mc.method_1562().method_52787((Packet)new UpdateSelectedSlotC2SPacket(last));
                }
            }
            return true;
        }
        return false;
    }

    private Surround() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

