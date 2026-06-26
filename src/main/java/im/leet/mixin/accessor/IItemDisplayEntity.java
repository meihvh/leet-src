/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.decoration.DisplayEntity$ItemDisplayEntity
 *  net.minecraft.entity.decoration.DisplayEntity$ItemDisplayEntity$Data
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package im.leet.mixin.accessor;

import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={DisplayEntity.ItemDisplayEntity.class})
public interface IItemDisplayEntity {
    @Accessor(value="data")
    public DisplayEntity.ItemDisplayEntity.Data client$data();
}

