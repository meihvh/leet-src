/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Vec3d
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.movement;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventVelocity;
import im.leet.base.modules.impl.movement.AntiPush;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class EntityMixin {
    @Inject(method={"pushAwayFrom"}, at={@At(value="HEAD")}, cancellable=true)
    private void pushAwayFromHook(Entity entity, CallbackInfo ci) {
        EntityMixin self;
        if (AntiPush.INSTANCE.isEnabled() && AntiPush.INSTANCE.playerPush.get() && (self = this) == MinecraftHolder.mc.field_1724) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"updateVelocity"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;")})
    public Vec3d hookVelocity(Vec3d original, @Local(argsOnly=true) Vec3d movementInput, @Local(argsOnly=true) float speed, @Local(argsOnly=true) float yaw) {
        if (this != MinecraftClient.method_1551().field_1724) {
            return original;
        }
        EventVelocity event = new EventVelocity(movementInput, speed, yaw, original);
        Client.EVENTS.post(event);
        return event.velocity;
    }
}

