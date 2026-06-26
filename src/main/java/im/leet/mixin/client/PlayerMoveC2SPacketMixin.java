/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 */
package im.leet.mixin.client;

import im.leet.utils.client.mixin.IPlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={PlayerMoveC2SPacket.class})
public class PlayerMoveC2SPacketMixin
implements IPlayerMoveC2SPacket {
    @Shadow
    @Mutable
    private float field_12887;
    @Shadow
    @Mutable
    private float field_12885;

    @Override
    public void setYaw(float yaw) {
        this.field_12887 = yaw;
    }

    @Override
    public void setPitch(float pitch) {
        this.field_12885 = pitch;
    }
}

