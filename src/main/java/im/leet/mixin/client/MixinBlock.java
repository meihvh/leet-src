/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.entity.Entity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package im.leet.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import im.leet.base.modules.impl.movement.AntiBounce;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value={Entity.class})
public class MixinBlock {
    @ModifyExpressionValue(method={"bypassesLandingEffects"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;isSneaking()Z")})
    private boolean hookAntiBounce(boolean original) {
        return AntiBounce.INSTANCE.isEnabled() || original;
    }
}

