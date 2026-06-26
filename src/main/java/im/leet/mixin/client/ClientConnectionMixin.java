/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandlerContext
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.network.ClientConnection
 *  net.minecraft.network.packet.Packet
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSendPacket;
import im.leet.utils.network.NetworkUtility;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientConnection.class})
public class ClientConnectionMixin {
    @Shadow
    private Channel field_11651;

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;Lio/netty/channel/ChannelFutureListener;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void client$send(Packet<?> packet, @Nullable ChannelFutureListener channelFutureListener, boolean flush, CallbackInfo ci) {
        if (MinecraftClient.method_1551().field_1724 == null) {
            return;
        }
        EventSendPacket build = EventSendPacket.build(packet);
        if (NetworkUtility.shouldTriggerEvent()) {
            Client.EVENTS.post(build);
            if (build.isCancelled()) {
                ci.cancel();
            }
        }
        NetworkUtility.handleCPacket(packet);
    }

    @Inject(method={"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private static void client$handlePacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (MinecraftClient.method_1551().field_1724 == null) {
            return;
        }
        if (NetworkUtility.shouldTriggerEvent()) {
            EventReceivePacket build = EventReceivePacket.build(packet);
            Client.EVENTS.post(build);
            if (build.isCancelled()) {
                ci.cancel();
            }
        }
        NetworkUtility.handleSPacket(packet);
    }
}

