/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.world.WorldEventHandler
 *  net.minecraft.util.math.BlockPos
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.api.events.list.EventWorldEmit;
import net.minecraft.client.world.WorldEventHandler;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={WorldEventHandler.class})
public class WorldEventHandlerMixin {
    @Inject(method={"processWorldEvent"}, at={@At(value="HEAD")}, cancellable=true)
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo ci) {
        EventWorldEmit emit = EventWorldEmit.build(eventId, data, pos);
        Client.EVENTS.post(emit);
        if (emit.isCancelled()) {
            ci.cancel();
        }
    }
}

