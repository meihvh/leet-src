/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.projectile.FireworkRocketEntity
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.api.events.list.EventBoost;
import im.leet.base.rotations.Angle;
import im.leet.utils.client.mixin.IShooterEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={FireworkRocketEntity.class})
public class FireworkRocketEntityMixin
implements IShooterEntity {
    @Shadow
    @Nullable
    private LivingEntity field_7616;
    @Unique
    public LivingEntity shooterEntity = null;

    @Override
    @Unique
    public LivingEntity leet$getShooter() {
        return this.shooterEntity;
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal=0))
    private void onSetVelocity(LivingEntity instance, Vec3d origin) {
        this.shooterEntity = this.field_7616;
        if (instance instanceof ClientPlayerEntity) {
            EventBoost boost = EventBoost.build(Client.ROTATION.getRotate().getYaw(), Client.ROTATION.getRotate().getPitch());
            Client.EVENTS.post(boost);
            Vec3d vec3d = new Angle(boost.yaw, boost.pitch).toVector();
            Vec3d vec3d2 = instance.method_18798();
            instance.method_18799(vec3d2.method_1031(vec3d.field_1352 * boost.a + (vec3d.field_1352 * boost.speedx - vec3d2.field_1352) * boost.b, vec3d.field_1351 * boost.a + (vec3d.field_1351 * boost.speedy - vec3d2.field_1351) * boost.b, vec3d.field_1350 * boost.a + (vec3d.field_1350 * boost.speedz - vec3d2.field_1350) * boost.b));
            return;
        }
        instance.method_18799(origin);
    }
}

