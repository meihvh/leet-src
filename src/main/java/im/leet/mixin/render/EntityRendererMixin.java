/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.Frustum
 *  net.minecraft.client.render.entity.EntityRenderer
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.entity.Entity
 *  net.minecraft.text.Text
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import im.leet.base.modules.impl.render.ESP;
import im.leet.base.modules.impl.render.Removals;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityRenderer.class})
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Inject(method={"getDisplayName"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookNametag(T entity, CallbackInfoReturnable<Text> cir) {
        if (ESP.INSTANCE.isEnabled() && ESP.INSTANCE.dontRenderNametag((Entity)entity)) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method={"shouldRender"}, at={@At(value="HEAD")}, cancellable=true)
    private void entityRemoval(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (Removals.INSTANCE.isEnabled() && Removals.INSTANCE.doesNotRenderEntity((Entity)entity)) {
            cir.setReturnValue((Object)false);
        }
    }
}

