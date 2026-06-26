/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayNetworkHandler
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerPosition
 *  net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket
 *  net.minecraft.network.packet.s2c.play.PositionFlag
 *  net.minecraft.util.math.Vec3d
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventChatMessage;
import im.leet.api.events.list.EventLoadChunk;
import im.leet.api.events.list.EventSetRotation;
import java.util.Set;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayNetworkHandler.class})
public class ClientPlayNetworkHandlerMixin
implements MinecraftHolder {
    @Inject(method={"sendChatMessage"}, at={@At(value="HEAD")}, cancellable=true)
    private void InjectSendChatMessage(String content, CallbackInfo ci) {
        if (Client.IS_PANIC) {
            if (content.startsWith(".clientpanic")) {
                Client.stopPanic();
                ci.cancel();
            }
            return;
        }
        Client.EVENTS.post(EventChatMessage.build(content));
        if (EventChatMessage.instance.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"setPosition"}, at={@At(value="HEAD")}, cancellable=true)
    private static void InjectSetPosition(PlayerPosition pos, Set<PositionFlag> flags, Entity entity, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        boolean bl2;
        if (!(entity instanceof ClientPlayerEntity)) {
            return;
        }
        cir.cancel();
        PlayerPosition playerPosition = PlayerPosition.method_63638((Entity)entity);
        PlayerPosition playerPosition2 = PlayerPosition.method_63639((PlayerPosition)playerPosition, (PlayerPosition)pos, flags);
        boolean bl3 = bl2 = playerPosition.comp_3148().method_1025(playerPosition2.comp_3148()) > 4096.0;
        if (bl && !bl2) {
            EventSetRotation event = EventSetRotation.build(playerPosition2.comp_3150(), playerPosition2.comp_3151());
            Client.EVENTS.post(event);
            if (!event.isCancelled()) {
                entity.method_66246(playerPosition2.comp_3148(), playerPosition2.comp_3150(), playerPosition2.comp_3151());
            } else {
                entity.method_66246(playerPosition2.comp_3148(), entity.method_36454(), entity.method_36455());
            }
            entity.method_18799(playerPosition2.comp_3149());
            cir.setReturnValue((Object)true);
        } else {
            entity.method_33574(playerPosition2.comp_3148());
            entity.method_18799(playerPosition2.comp_3149());
            EventSetRotation event = EventSetRotation.build(playerPosition2.comp_3150(), playerPosition2.comp_3151());
            Client.EVENTS.post(event);
            if (!event.isCancelled()) {
                entity.method_36456(event.yaw);
                entity.method_36457(event.pitch);
            }
            PlayerPosition playerPosition3 = new PlayerPosition(entity.method_61411(), Vec3d.field_1353, entity.field_5982, entity.field_6004);
            PlayerPosition playerPosition4 = PlayerPosition.method_63639((PlayerPosition)playerPosition3, (PlayerPosition)pos, flags);
            entity.method_63615(playerPosition4.comp_3148(), playerPosition4.comp_3150(), playerPosition4.comp_3151());
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"onChunkData"}, at={@At(value="RETURN")})
    private void leet$onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        Client.EVENTS.post(new EventLoadChunk(packet.method_11523(), packet.method_11524()));
    }
}

