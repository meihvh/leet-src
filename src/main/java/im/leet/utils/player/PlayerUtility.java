/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.world.World
 */
package im.leet.utils.player;

import im.leet.MinecraftHolder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

public final class PlayerUtility {
    public static boolean isMoving() {
        Vec2f vector2f = MinecraftHolder.mc.field_1724.field_3913.method_3128();
        return vector2f.field_1343 != 0.0f || vector2f.field_1342 != 0.0f || MinecraftHolder.mc.field_1724.field_3913.field_54155.comp_3159();
    }

    public static boolean isOnSolidGround(LivingEntity entity, int checkDepth) {
        BlockPos entityPos = entity.method_24515();
        World world = entity.method_37908();
        for (int i = 1; i <= checkDepth; ++i) {
            BlockPos checkPos = entityPos.method_10087(i);
            BlockState blockState = world.method_8320(checkPos);
            if (blockState.method_26215() || blockState.method_26227().method_15769() || !blockState.method_51366()) continue;
            return true;
        }
        return false;
    }

    public static double compareArmor(LivingEntity entity) {
        return -entity.method_6096();
    }

    private PlayerUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

