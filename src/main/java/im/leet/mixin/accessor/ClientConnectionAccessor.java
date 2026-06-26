/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.ClientConnection
 *  net.minecraft.network.listener.PacketListener
 *  net.minecraft.network.packet.Packet
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package im.leet.mixin.accessor;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={ClientConnection.class})
public interface ClientConnectionAccessor {
    @Accessor(value="packetListener")
    public PacketListener client$listener();

    @Invoker(value="handlePacket")
    public static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener) {
        throw new UnsupportedOperationException();
    }
}

