/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.entity.player.PlayerEntity
 */
package im.leet.utils.client.targets;

import im.leet.MinecraftHolder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class MatrixAntiBotUtility
implements MinecraftHolder {
    public static boolean isMatrixBot(PlayerEntity p) {
        if (p == null || mc == null) {
            return false;
        }
        if (p instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity pl = (AbstractClientPlayerEntity)p;
            return pl.method_7334().getProperties().isEmpty();
        }
        return false;
    }

    private MatrixAntiBotUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

