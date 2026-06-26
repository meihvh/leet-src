/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.network.OtherClientPlayerEntity
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.util.math.Vec3d
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package im.leet.mixin.client;

import com.mojang.authlib.GameProfile;
import im.leet.MinecraftHolder;
import im.leet.base.modules.impl.combat.resolver.BackTrackPosResolver;
import im.leet.utils.client.mixin.IOtherClientPlayerEntity;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={OtherClientPlayerEntity.class})
public class MixinOtherClientPlayerEntity
extends AbstractClientPlayerEntity
implements IOtherClientPlayerEntity {
    @Unique
    private double backUpX;
    @Unique
    private double backUpY;
    @Unique
    private double backUpZ;
    @Unique
    Vec3d lastServer = Vec3d.field_1353;

    public MixinOtherClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public void resolve(BackTrackPosResolver.Resolve mode) {
        this.backUpX = this.method_23317();
        this.backUpY = this.method_23318();
        this.backUpZ = this.method_23321();
        if (mode == BackTrackPosResolver.Resolve.BackTrack) {
            double minDst = 999.0;
            BackTrackPosResolver.Position bestPos = null;
            for (BackTrackPosResolver.Position p : ((ResolvedPositionEntity)((Object)this)).getPositionHistory()) {
                double dst = MinecraftHolder.mc.field_1724.method_5649(p.getX(), p.getY(), p.getZ());
                if (!(dst < minDst)) continue;
                minDst = dst;
                bestPos = p;
            }
            if (bestPos != null) {
                this.method_5814(bestPos.getX(), bestPos.getY(), bestPos.getZ());
            }
            return;
        }
        Vec3d from = this.lastServer;
        Vec3d to = ((ResolvedPositionEntity)((Object)this)).hachclientport$getResolvedPos();
        if (mode == BackTrackPosResolver.Resolve.Advantage) {
            if (MinecraftHolder.mc.field_1724.method_5707(from) > MinecraftHolder.mc.field_1724.method_5707(to)) {
                this.method_5814(to.field_1352, to.field_1351, to.field_1350);
            } else {
                this.method_5814(from.field_1352, from.field_1351, from.field_1350);
            }
        } else {
            this.method_5814(to.field_1352, to.field_1351, to.field_1350);
        }
        this.lastServer = to;
    }

    @Override
    public void releaseResolver() {
        if (this.backUpY != -999.0) {
            this.method_5814(this.backUpX, this.backUpY, this.backUpZ);
            this.backUpY = -999.0;
        }
    }
}

