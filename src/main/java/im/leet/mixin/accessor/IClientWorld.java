/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.PendingUpdateManager
 *  net.minecraft.client.world.ClientWorld
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package im.leet.mixin.accessor;

import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={ClientWorld.class})
public interface IClientWorld {
    @Invoker(value="getPendingUpdateManager")
    public PendingUpdateManager client$pending();
}

