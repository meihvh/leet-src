/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.Camera
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.player.MinecraftBetter;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Camera.class})
public class CameraMixin {
    @Inject(method={"clipToSpace"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookCameraNoClip(float f, CallbackInfoReturnable<Float> xui) {
        if (MinecraftBetter.INSTANCE.isEnabled() && MinecraftBetter.INSTANCE.cameraNoClip.get()) {
            xui.setReturnValue((Object)Float.valueOf(f));
            xui.cancel();
        }
    }
}

