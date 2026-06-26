/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.base.modules.impl.render.Animations;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityMixin {
    @Shadow
    public float field_6251;
    @Shadow
    public float field_6229;
    @Unique
    private boolean client$local = false;

    @Shadow
    protected abstract float method_6106();

    @Shadow
    public abstract boolean method_6059(RegistryEntry<StatusEffect> var1);

    @Shadow
    @Nullable
    public abstract StatusEffectInstance method_6112(RegistryEntry<StatusEffect> var1);

    @Shadow
    protected abstract double method_61426();

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void client$init(EntityType<?> entityType, World world, CallbackInfo ci) {
        this.client$local = (Entity)this instanceof ClientPlayerEntity;
    }

    @Inject(method={"jump"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$getYaw(CallbackInfo ci) {
        if (Client.IS_PANIC || !this.client$local || !Client.ROTATION.isRotating()) {
            return;
        }
        float f = this.method_6106();
        if (!(f <= 1.0E-5f)) {
            Vec3d vec3d = ((Entity)this).method_18798();
            ((Entity)this).method_18800(vec3d.field_1352, Math.max((double)f, vec3d.field_1351), vec3d.field_1350);
            if (((Entity)this).method_5624()) {
                float g = Client.ROTATION.getRotate().getYaw() * ((float)Math.PI / 180);
                ((Entity)this).method_45319(new Vec3d((double)(-MathHelper.method_15374((float)g)) * 0.2, 0.0, (double)MathHelper.method_15362((float)g) * 0.2));
            }
            ((Entity)this).field_6007 = true;
        }
        ci.cancel();
    }

    @Redirect(method={"tick", "baseTick"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float getYaw(LivingEntity livingEntity) {
        if (Client.IS_PANIC || !this.client$local || !Client.ROTATION.isRotating()) {
            return livingEntity.method_36454();
        }
        return Client.ROTATION.getRotate().getYaw();
    }

    @Redirect(method={"turnHead"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float turnHead$(LivingEntity livingEntity) {
        if (Client.IS_PANIC || !this.client$local || !Client.ROTATION.isRotating()) {
            return livingEntity.method_36454();
        }
        return Client.ROTATION.getVisual().getYaw();
    }

    @Inject(method={"calcGlidingVelocity"}, at={@At(value="HEAD")}, cancellable=true)
    private void calcGlidingVelocity$(Vec3d oldVelocity, CallbackInfoReturnable<Vec3d> cir) {
        double i;
        if (Client.IS_PANIC || !this.client$local || !Client.ROTATION.isRotating()) {
            return;
        }
        Vec3d vec3d = Client.ROTATION.getRotate().toVector();
        float f = Client.ROTATION.getRotate().getPitch() * ((float)Math.PI / 180);
        double d = Math.sqrt(vec3d.field_1352 * vec3d.field_1352 + vec3d.field_1350 * vec3d.field_1350);
        double e = oldVelocity.method_37267();
        double g = this.method_61426();
        double h = MathHelper.method_33723((double)Math.cos(f));
        oldVelocity = oldVelocity.method_1031(0.0, g * (-1.0 + h * 0.75), 0.0);
        if (oldVelocity.field_1351 < 0.0 && d > 0.0) {
            i = oldVelocity.field_1351 * -0.1 * h;
            oldVelocity = oldVelocity.method_1031(vec3d.field_1352 * i / d, i, vec3d.field_1350 * i / d);
        }
        if (f < 0.0f && d > 0.0) {
            i = e * (double)(-MathHelper.method_15374((float)f)) * 0.04;
            oldVelocity = oldVelocity.method_1031(-vec3d.field_1352 * i / d, i * 3.2, -vec3d.field_1350 * i / d);
        }
        if (d > 0.0) {
            oldVelocity = oldVelocity.method_1031((vec3d.field_1352 / d * e - oldVelocity.field_1352) * 0.1, 0.0, (vec3d.field_1350 / d * e - oldVelocity.field_1350) * 0.1);
        }
        cir.setReturnValue((Object)oldVelocity.method_18805((double)0.99f, (double)0.98f, (double)0.99f));
        cir.cancel();
    }

    @Inject(method={"getHandSwingDuration"}, at={@At(value="HEAD")}, cancellable=true)
    private void getHandSwingProgress(CallbackInfoReturnable<Integer> cir) {
        if (this.client$local && Animations.INSTANCE.isEnabled()) {
            cir.setReturnValue((Object)((int)(Animations.INSTANCE.speed.getMax() - Animations.INSTANCE.speed.get())));
            cir.cancel();
        }
    }
}

