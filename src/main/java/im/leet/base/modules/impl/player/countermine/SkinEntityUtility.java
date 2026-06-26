/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.Blocks
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.decoration.DisplayEntity$ItemDisplayEntity
 *  net.minecraft.entity.decoration.DisplayEntity$ItemDisplayEntity$Data
 *  net.minecraft.entity.decoration.DisplayEntity$TextDisplayEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Position
 */
package im.leet.base.modules.impl.player.countermine;

import im.leet.MinecraftHolder;
import im.leet.mixin.accessor.IItemDisplayEntity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;

public class SkinEntityUtility
implements MinecraftHolder {
    public static final String SMOKE_GRENADE_SKIN = "minecraft:229253ea86489a63";
    public static final List<String> COUNTERMINE_SKINS = List.of("minecraft:05aba16cdc76d532", "minecraft:0690f8af68a9db2b", "minecraft:0b8b9c34d1adc0c3", "minecraft:105c0ea4662d38a3", "minecraft:136878647cbce0d3", "minecraft:142b6529689896b8", "minecraft:15afd281c6ac1f8a", "minecraft:2ecf3b470478d4d5", "minecraft:3676ab168bf664ab", "minecraft:4178409ffeb382dc", "minecraft:43c90169107dfe15", "minecraft:54837b32f9de8638", "minecraft:5f76e7c7a1f3743c", "minecraft:62e0957de0946c44", "minecraft:63c848962debde58", "minecraft:66c5a9a31593e097", "minecraft:6b341411a05002d0", "minecraft:6d9c41761b0ff136", "minecraft:6f4797e7acadd8ba", "minecraft:72fc9512570bebca", "minecraft:8685c69ef293f8e2", "minecraft:8ad41dcee5554013", "minecraft:8d9f5ce9a4421880", "minecraft:99e584e9dfcccf5d", "minecraft:9d61a17ffacb0247", "minecraft:b0df49f36e2d41f6", "minecraft:b2e8de1fea48aac3", "minecraft:bc504a5e55227c04", "minecraft:bd7a2247c537017f", "minecraft:bfcdbc047ce7ff08", "minecraft:ca00a03d84155b63", "minecraft:cc51a96870e7cfc6", "minecraft:d4499fdfb133be7b", "minecraft:d613184e0e9a42c8", "minecraft:d8b76592831bc53f", "minecraft:e5afd4ccc1eb0a95", "minecraft:ee0aea119ef7a97c");

    public static String getEntitySkinModel(Entity entity) {
        if (!(entity instanceof DisplayEntity.ItemDisplayEntity)) {
            return "";
        }
        DisplayEntity.ItemDisplayEntity displayEntity = (DisplayEntity.ItemDisplayEntity)entity;
        DisplayEntity.ItemDisplayEntity.Data data = ((IItemDisplayEntity)displayEntity).client$data();
        if (data == null) {
            return "";
        }
        Identifier model = (Identifier)data.comp_1322().method_58694(DataComponentTypes.field_54199);
        if (model == null) {
            return "";
        }
        return model.toString();
    }

    public static List<Entity> getSkinEntities() {
        ArrayList<Entity> result = new ArrayList<Entity>();
        if (SkinEntityUtility.mc.field_1687 == null || SkinEntityUtility.mc.field_1724 == null) {
            return result;
        }
        for (Entity entity : SkinEntityUtility.mc.field_1687.method_18112()) {
            if (entity == SkinEntityUtility.mc.field_1724 || !(entity instanceof DisplayEntity.ItemDisplayEntity) || !COUNTERMINE_SKINS.contains(SkinEntityUtility.getEntitySkinModel(entity))) continue;
            result.add(entity);
        }
        return result;
    }

    public static boolean isTeammate(Entity entity) {
        if (entity.method_37908() == null) {
            return false;
        }
        List textDisplays = entity.method_37908().method_8390(DisplayEntity.TextDisplayEntity.class, entity.method_5829().method_1014(2.0), e -> true);
        boolean team = false;
        for (DisplayEntity.TextDisplayEntity ent : textDisplays) {
            if (ent.method_48915() == null || ent.method_48915().getString().isEmpty()) continue;
            team = true;
            break;
        }
        return team;
    }

    public static boolean checkIsAlive(Entity entity) {
        if (entity.method_37908() == null) {
            return false;
        }
        List textDisplays = entity.method_37908().method_8390(DisplayEntity.TextDisplayEntity.class, entity.method_5829().method_1014(2.0), e -> true);
        return !textDisplays.isEmpty();
    }

    public static boolean oldCheckIsAlive(Entity entity) {
        if (SkinEntityUtility.mc.field_1687 == null) {
            return false;
        }
        BlockPos pos = BlockPos.method_49638((Position)entity.method_19538());
        Block block = SkinEntityUtility.mc.field_1687.method_8320(pos).method_26204();
        return block != Blocks.field_9987;
    }

    public static boolean isSmokeGrenade(Entity entity) {
        String skinModel = SkinEntityUtility.getEntitySkinModel(entity);
        return skinModel.equals(SMOKE_GRENADE_SKIN);
    }
}

