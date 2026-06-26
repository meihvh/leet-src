/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.LightmapTextureManager
 *  net.minecraft.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.render.FullBright;
import im.leet.base.modules.impl.render.Removals;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LightmapTextureManager.class})
public class LightmapTextureManagerMixin {
    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Ljava/lang/Double;floatValue()F", ordinal=1))
    private float leet$getValue(Double instance) {
        if (FullBright.INSTANCE.isEnabled()) {
            return 200.0f;
        }
        return instance.floatValue();
    }

    @Inject(method={"getDarkness"}, at={@At(value="HEAD")}, cancellable=true)
    private void getDarknessFactor(LivingEntity entity, float factor, float tickProgress, CallbackInfoReturnable<Float> info) {
        if (Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.Darkness)) {
            info.setReturnValue((Object)Float.valueOf(0.0f));
        }
    }
}

