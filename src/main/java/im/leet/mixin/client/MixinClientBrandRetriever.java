/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.ClientBrandRetriever
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.utils.ClientSpoof;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientBrandRetriever.class})
public class MixinClientBrandRetriever {
    @Inject(method={"getClientModName"}, at={@At(value="HEAD")}, cancellable=true, remap=false)
    private static void getClientModNameHook(CallbackInfoReturnable<String> cir) {
        if (ClientSpoof.INSTANCE.clientSpoof.get()) {
            cir.setReturnValue((Object)ClientSpoof.INSTANCE.getClientName());
        }
    }
}

