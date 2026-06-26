/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.SlimeBlock
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package im.leet.mixin.client;

import im.leet.base.modules.impl.movement.SlimeJump;
import net.minecraft.block.SlimeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={SlimeBlock.class})
public class SlimeBlockMixin {
    @ModifyVariable(method={"bounce"}, at=@At(value="STORE"), ordinal=0)
    private double bounce(double original) {
        if (SlimeJump.INSTANCE.isEnabled()) {
            return original * (double)SlimeJump.INSTANCE.multiplier.get();
        }
        return original;
    }
}

