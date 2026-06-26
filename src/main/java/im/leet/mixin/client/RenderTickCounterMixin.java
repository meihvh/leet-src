/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderTickCounter$Dynamic
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.Client;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={RenderTickCounter.Dynamic.class})
public class RenderTickCounterMixin {
    @Shadow
    private float field_51958;

    @Inject(method={"beginRenderTick(J)I"}, at={@At(value="FIELD", target="Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastTimeMillis:J", shift=At.Shift.AFTER)})
    private void tick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        this.field_51958 *= Client.TIMER;
    }
}

