/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Vec2f
 *  org.graalvm.polyglot.HostAccess$Export
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 */
package im.leet.api.scripts.api.bindings;

import im.leet.MinecraftHolder;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.client.mixin.ResolvedPotationEntity;
import im.leet.utils.client.targets.TargetsUtility;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec2f;
import org.graalvm.polyglot.HostAccess;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameProvider
implements MinecraftHolder {
    @HostAccess.Export
    public Vector3f getServerPos(Entity e) {
        return ((ResolvedPositionEntity)e).hachclientport$getResolvedPos().method_46409();
    }

    @HostAccess.Export
    public Vector3f getPos(Entity e) {
        return e.method_30950(mc.method_61966().method_60637(true)).method_46409();
    }

    @HostAccess.Export
    public Vector2f getServerRot(Entity e) {
        Vec2f v = ((ResolvedPotationEntity)e).hachclientport$getResolvedRot();
        return new Vector2f(v.field_1343, v.field_1342);
    }

    @HostAccess.Export
    public ClientPlayerEntity getLocal() {
        return GameProvider.mc.field_1724;
    }

    @HostAccess.Export
    public LivingEntity getTarget() {
        return TargetsUtility.getTarget();
    }

    @HostAccess.Export
    public LivingEntity getLastTarget() {
        return TargetsUtility.getLastTarget();
    }

    @HostAccess.Export
    public void send(String msg) {
        ChatUtility.send(msg);
    }
}

