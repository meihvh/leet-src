/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.util.math.Vec3d
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.render.CustomWorld;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientWorld.class})
public class ClientWorldMixin {
    @Inject(method={"getSkyColor"}, at={@At(value="HEAD")}, cancellable=true)
    private void getSkyColor(Vec3d cameraPos, float tickProgress, CallbackInfoReturnable<Integer> cir) {
        if (!CustomWorld.INSTANCE.useFog.get() || !CustomWorld.INSTANCE.isEnabled()) {
            return;
        }
        cir.cancel();
        cir.setReturnValue((Object)CustomWorld.INSTANCE.fogColor.get().getRGB());
    }
}

