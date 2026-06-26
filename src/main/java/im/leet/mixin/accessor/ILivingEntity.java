/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package im.leet.mixin.accessor;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={LivingEntity.class})
public interface ILivingEntity {
    @Accessor(value="lastAttackedTicks")
    public int client$lastAttackedTicks();

    @Accessor(value="jumpingCooldown")
    public void client$jumpingCooldown(int var1);
}

