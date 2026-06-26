/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.Blocks
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.decoration.EndCrystalEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Items
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
import im.leet.api.events.list.EventGameTick;
import im.leet.base.rotations.Angle;
import im.leet.mixin.accessor.ILivingEntity;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.RayTraceUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.SensUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.InventoryUtility;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class Bahalka
implements MinecraftHolder {
    static BlockPos current = null;

    public static boolean handleEvent(Event event) {
        if (event instanceof EventGameTick) {
            EndCrystalEntity ent = null;
            for (Entity entity : Bahalka.mc.field_1687.method_18112()) {
                if (!(entity instanceof EndCrystalEntity)) continue;
                EndCrystalEntity p = (EndCrystalEntity)entity;
                Box AABB = p.method_5829();
                Vec3d vec3d = new Vec3d(MathHelper.method_15350((double)Bahalka.mc.field_1724.method_33571().method_10216(), (double)AABB.field_1323, (double)AABB.field_1320), MathHelper.method_15350((double)Bahalka.mc.field_1724.method_33571().method_10214(), (double)AABB.field_1322, (double)AABB.field_1325), MathHelper.method_15350((double)Bahalka.mc.field_1724.method_33571().method_10215(), (double)AABB.field_1321, (double)AABB.field_1324));
                if (!(vec3d.method_1022(Bahalka.mc.field_1724.method_33571()) < 3.0)) continue;
                ent = p;
                break;
            }
            if (ent == null) {
                for (int x = -3; x <= 3; ++x) {
                    for (int z = -3; z <= 3; ++z) {
                        for (int y = -1; y < 2; ++y) {
                            boolean valid;
                            BlockPos pos = Bahalka.mc.field_1724.method_24515().method_10069(x, y, z);
                            BlockState state = Bahalka.mc.field_1687.method_8320(pos);
                            boolean bl = valid = state.method_27852(Blocks.field_10540) || state.method_27852(Blocks.field_9987);
                            if (!valid || !((double)pos.method_10264() >= Bahalka.mc.field_1724.method_23318())) continue;
                            current = pos;
                        }
                    }
                }
                if (current != null && Bahalka.mc.field_1724.method_33571().method_1022(current.method_60913(Bahalka.mc.field_1724.method_33571())) > 3.1) {
                    current = null;
                    return false;
                }
                if (current == null) {
                    return false;
                }
                Vec3d vec = current.method_46558().method_1031(0.0, 0.5, 0.0);
                t = RotationUtility.calculate(vec);
                Angle curr = Client.ROTATION.getRotate().copy();
                float deltaYaw = MathHelper.method_15393((float)(t.getYaw() - curr.getYaw()));
                float deltaPitch = t.getPitch() - curr.getPitch();
                curr.setYaw((float)((double)curr.getYaw() + SensUtility.getSensitivity(deltaYaw + MathUtility.gaussian(5.0f, 2.0f))));
                curr.setPitch((float)MathHelper.method_15350((double)((double)curr.getPitch() + SensUtility.getSensitivity(deltaPitch + MathUtility.gaussian(5.0f, 2.0f))), (double)-90.0, (double)90.0));
                Client.ROTATION.rotate(curr, false);
                int slot = InventoryUtility.findHotbar(Items.field_8301);
                int last = Bahalka.mc.field_1724.method_31548().method_67532();
                if (slot != -1) {
                    if (last != slot) {
                        InventoryUtility.selectSlot(slot);
                    }
                    NetworkUtility.sendUse(Hand.field_5808, new BlockHitResult(vec, Direction.field_11036, current, false));
                    Bahalka.mc.field_1724.method_6104(Hand.field_5808);
                    if (last != slot) {
                        InventoryUtility.selectSlot(last);
                    }
                }
            } else {
                Vec3d vec = ent.method_5829().method_1005();
                t = RotationUtility.calculate(vec);
                Angle current = Client.ROTATION.getRotate().copy();
                float deltaYaw = MathHelper.method_15393((float)(t.getYaw() - current.getYaw()));
                float deltaPitch = t.getPitch() - current.getPitch();
                if (!RayTraceUtility.rayTrace(current.toVector(), 3.0, ent.method_5829())) {
                    current.setYaw((float)((double)current.getYaw() + SensUtility.getSensitivity(deltaYaw + MathUtility.gaussian(5.0f, 2.0f))));
                    current.setPitch((float)MathHelper.method_15350((double)((double)current.getPitch() + SensUtility.getSensitivity(deltaPitch + MathUtility.gaussian(5.0f, 2.0f))), (double)-90.0, (double)90.0));
                }
                Client.ROTATION.rotate(current, false);
                if (((ILivingEntity)Bahalka.mc.field_1724).client$lastAttackedTicks() > 3) {
                    Bahalka.mc.field_1761.method_2918((PlayerEntity)Bahalka.mc.field_1724, ent);
                    Bahalka.mc.field_1724.method_6104(Hand.field_5808);
                }
            }
            return true;
        }
        return false;
    }

    private Bahalka() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

