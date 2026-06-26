/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.PacketByteBuf
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.utils.network.NetworkUtility;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientCommandC2SPacket.class})
public class ClientCommandC2SPacketMixin {
    @Shadow
    @Final
    private ClientCommandC2SPacket.Mode field_12978;

    @Inject(method={"<init>(Lnet/minecraft/network/PacketByteBuf;)V"}, at={@At(value="TAIL")})
    public void onInit(PacketByteBuf buf, CallbackInfo ci) {
        this.update(this.field_12978);
    }

    @Inject(method={"<init>(Lnet/minecraft/entity/Entity;Lnet/minecraft/network/packet/c2s/play/ClientCommandC2SPacket$Mode;I)V"}, at={@At(value="TAIL")})
    public void onInit(Entity entity, ClientCommandC2SPacket.Mode mode, int mountJumpHeight, CallbackInfo ci) {
        this.update(mode);
    }

    @Inject(method={"<init>(Lnet/minecraft/entity/Entity;Lnet/minecraft/network/packet/c2s/play/ClientCommandC2SPacket$Mode;)V"}, at={@At(value="TAIL")})
    public void onInit(Entity entity, ClientCommandC2SPacket.Mode mode, CallbackInfo ci) {
        this.update(mode);
    }

    @Unique
    private void update(ClientCommandC2SPacket.Mode mode) {
        if (mode == ClientCommandC2SPacket.Mode.field_12981) {
            NetworkUtility.updateServerSprint(true);
        } else if (mode == ClientCommandC2SPacket.Mode.field_12985) {
            NetworkUtility.updateServerSprint(false);
        }
    }
}

