/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelPipeline
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import im.leet.utils.client.ClientSettings;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraft.network.ClientConnection$1"})
public class ClientConnection1Mixin {
    @Inject(method={"initChannel"}, at={@At(value="TAIL")})
    private void client$applyProxy(Channel channel, CallbackInfo ci, @Local ChannelPipeline pipeline) {
        ClientSettings.INSTANCE.proxy.fire(pipeline);
    }
}

