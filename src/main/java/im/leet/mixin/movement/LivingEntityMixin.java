/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.FluidBlock
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Position
 *  net.minecraft.world.World
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package im.leet.mixin.movement;

import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.movement.WaterSpeed;
import im.leet.base.modules.impl.player.AssistModule;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityMixin
extends Entity {
    @Shadow
    private int field_6228;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method={"setSprinting"}, at=@At(value="HEAD"), ordinal=0, argsOnly=true)
    private boolean setSprintingHook(boolean sprinting) {
        LivingEntityMixin self;
        if (WaterSpeed.INSTANCE.isEnabled() && WaterSpeed.INSTANCE.mode.is(WaterSpeed.Mode.Legit) && (self = this) == MinecraftHolder.mc.field_1724) {
            BlockPos blockPos = BlockPos.method_49638((Position)MinecraftHolder.mc.field_1724.method_19538().method_1031(0.0, -0.5, 0.0));
            if (MinecraftHolder.mc.field_1724.method_5799() || MinecraftHolder.mc.field_1687.method_8320(blockPos).method_26204() instanceof FluidBlock) {
                return true;
            }
        }
        return sprinting;
    }

    @Redirect(method={"tickMovement"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/LivingEntity;jumpingCooldown:I", opcode=181))
    private void nullifyJumpCooldown(LivingEntity me, int value) {
        this.field_6228 = AssistModule.INSTANCE.isEnabled() && AssistModule.INSTANCE.jumpDelay.get() ? 0 : value;
    }
}

