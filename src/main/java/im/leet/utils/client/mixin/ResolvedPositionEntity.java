/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.utils.client.mixin;

import im.leet.base.modules.impl.combat.resolver.BackTrackPosResolver;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public interface ResolvedPositionEntity {
    public Vec3d hachclientport$getResolvedPos();

    public double getPrevServerX();

    public double getPrevServerY();

    public double getPrevServerZ();

    public List<BackTrackPosResolver.Position> getPositionHistory();
}

