/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.render.CustomWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={World.class})
public class WorldMixin {
    @Inject(method={"getRainGradient"}, cancellable=true, at={@At(value="HEAD")})
    private void hookGetRainGradient(float delta, CallbackInfoReturnable<Float> cir) {
        float gradient = CustomWorld.INSTANCE.getRainGradient();
        if (gradient != -1.0f) {
            cir.setReturnValue((Object)Float.valueOf(gradient));
        }
    }

    @Inject(method={"getThunderGradient"}, cancellable=true, at={@At(value="HEAD")})
    private void hookGetThunderGradient(float delta, CallbackInfoReturnable<Float> cir) {
        float gradient = CustomWorld.INSTANCE.getThunderGradient();
        if (gradient != -1.0f) {
            cir.setReturnValue((Object)Float.valueOf(gradient));
        }
    }
}

