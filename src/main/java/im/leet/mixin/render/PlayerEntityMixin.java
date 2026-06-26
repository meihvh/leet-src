/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.player.MinecraftBetter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PlayerEntity.class})
public class PlayerEntityMixin {
    float old_health = 0.0f;

    @Inject(method={"attack"}, at={@At(value="HEAD")})
    private void mixin$attack(Entity target, CallbackInfo ci) {
        if (!(target instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingTarget = (LivingEntity)target;
        this.old_health = livingTarget.method_6032();
    }

    @Inject(method={"getBlockBreakingSpeed"}, at={@At(value="RETURN")}, cancellable=true)
    private void \u0431\u043b\u0430\u0431\u043b\u0430(BlockState state, CallbackInfoReturnable<Float> cir) {
        if (!MinecraftBetter.INSTANCE.isEnabled() || !MinecraftBetter.INSTANCE.noFluidSlowdown.get()) {
            return;
        }
        PlayerEntity player = (PlayerEntity)this;
        if (player.method_5869() || player.method_5771()) {
            float speed = ((Float)cir.getReturnValue()).floatValue();
            if (player.method_24828()) {
                cir.setReturnValue((Object)Float.valueOf(speed * 5.0f));
            } else {
                cir.setReturnValue((Object)Float.valueOf(speed * 25.0f));
            }
        }
    }
}

