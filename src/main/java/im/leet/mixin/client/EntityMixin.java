/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongSet
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCollisionHandler$Impl
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.MovementType
 *  net.minecraft.entity.damage.DamageSource
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
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
import im.leet.api.events.list.EventPostMove;
import im.leet.api.events.list.EventTravel;
import im.leet.base.modules.impl.combat.Hitbox;
import im.leet.base.modules.impl.combat.resolver.BackTrackPosResolver;
import im.leet.base.rotations.Angle;
import im.leet.utils.client.mixin.IEntity;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.client.mixin.ResolvedPotationEntity;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class EntityMixin
implements ResolvedPositionEntity,
ResolvedPotationEntity,
IEntity {
    @Unique
    public Vec3d resolvedPos = Vec3d.field_1353;
    @Unique
    public Vec2f resolvedRot = Vec2f.field_1340;
    @Unique
    double prevServerX;
    @Unique
    double prevServerY;
    @Unique
    double prevServerZ;
    @Unique
    public List<BackTrackPosResolver.Position> positonHistory = new ArrayList<BackTrackPosResolver.Position>();
    @Unique
    boolean isInWeb = false;
    @Unique
    private boolean client$local;
    @Shadow
    public boolean field_5960;
    @Shadow
    protected Vec3d field_17046;
    @Shadow
    public double field_6017;
    @Shadow
    private World field_6002;
    @Shadow
    private float field_6031;

    @Override
    public List<BackTrackPosResolver.Position> getPositionHistory() {
        return this.positonHistory;
    }

    @Override
    public boolean client$inWeb() {
        return this.isInWeb;
    }

    @Override
    public void client$setWeb(boolean inWeb) {
        this.isInWeb = inWeb;
    }

    @Inject(method={"checkBlockCollision"}, at={@At(value="HEAD")})
    private void checkBlockCollision(Vec3d from, Vec3d _to, EntityCollisionHandler.Impl collisionHandler, LongSet collidedBlockPositions, CallbackInfo ci) {
        this.isInWeb = false;
    }

    @Override
    @Unique
    public Vec3d hachclientport$getResolvedPos() {
        return this.resolvedPos == null ? this.method_19538() : this.resolvedPos;
    }

    @Override
    @Unique
    public Vec2f hachclientport$getResolvedRot() {
        return this.resolvedRot == null ? new Vec2f((float)this.method_5720().method_10216(), (float)this.method_5720().method_10214()) : new Vec2f(this.resolvedRot.field_1343, this.resolvedRot.field_1342);
    }

    @Inject(method={"updateTrackedPositionAndAngles"}, at={@At(value="HEAD")})
    public void onUpdateTrackedPosition(Vec3d pos, float yaw, float pitch, CallbackInfo ci) {
        this.resolvedPos = pos;
        this.resolvedRot = new Vec2f(yaw, pitch);
        this.prevServerX = pos.field_1352;
        this.prevServerY = pos.field_1351;
        this.prevServerZ = pos.field_1350;
        this.positonHistory.add(new BackTrackPosResolver.Position(pos.field_1352, pos.field_1351, pos.field_1350));
        this.positonHistory.removeIf(BackTrackPosResolver.Position::shouldRemove);
    }

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(EntityType<?> type, World world, CallbackInfo ci) {
        this.client$local = (Entity)this instanceof ClientPlayerEntity;
    }

    @Shadow
    protected static Vec3d method_18795(Vec3d movementInput, float speed, float yaw) {
        return null;
    }

    @Shadow
    public abstract void method_5750(double var1, double var3, double var5);

    @Shadow
    public abstract void method_18799(Vec3d var1);

    @Shadow
    public abstract Vec3d method_18798();

    @Shadow
    public abstract Vec3d method_19538();

    @Shadow
    public abstract void method_33574(Vec3d var1);

    @Shadow
    public abstract double method_23317();

    @Shadow
    public abstract float method_5705(float var1);

    @Shadow
    public abstract double method_23318();

    @Shadow
    public abstract double method_23321();

    @Shadow
    public abstract void method_5814(double var1, double var3, double var5);

    @Shadow
    protected abstract Vec3d method_18794(Vec3d var1);

    @Shadow
    protected abstract Vec3d method_18796(Vec3d var1, MovementType var2);

    @Shadow
    protected abstract Vec3d method_17835(Vec3d var1);

    @Shadow
    public abstract World method_37908();

    @Shadow
    public abstract void method_38785();

    @Shadow
    public abstract boolean method_5643(DamageSource var1);

    @Shadow
    protected abstract void method_23311();

    @Shadow
    public abstract Vec3d method_5720();

    @Inject(method={"getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$getRotationVector(float pitch, float yaw, CallbackInfoReturnable<Vec3d> cir) {
        if (!Client.ROTATION.isRotating() || Client.IS_PANIC || !this.client$local) {
            return;
        }
        Angle angle = Client.ROTATION.getRotate();
        float f = angle.getPitch() * ((float)Math.PI / 180);
        float g = -angle.getYaw() * ((float)Math.PI / 180);
        float h = MathHelper.method_15362((float)g);
        float i = MathHelper.method_15374((float)g);
        float j = MathHelper.method_15362((float)f);
        float k = MathHelper.method_15374((float)f);
        cir.setReturnValue((Object)new Vec3d((double)(i * j), (double)(-k), (double)(h * j)));
    }

    @Inject(method={"isLogicalSideForUpdatingMovement"}, at={@At(value="HEAD")}, cancellable=true)
    private void isLogicalSideForUpdatingMovement(CallbackInfoReturnable<Boolean> cir) {
        if (Client.IS_PANIC || !((Entity)this instanceof ClientPlayerEntity)) {
            return;
        }
        cir.setReturnValue((Object)true);
    }

    @Redirect(method={"getLerpedYaw"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getYaw()F"))
    private float getYaw(Entity instance) {
        if (Client.IS_PANIC || !this.client$local || !Client.ROTATION.isRotating()) {
            return instance.method_36454();
        }
        return Client.ROTATION.getVisual().getYaw();
    }

    @Inject(method={"move"}, at={@At(value="HEAD")}, cancellable=true)
    private void move(MovementType type, Vec3d movement, CallbackInfo ci) {
        if (!this.client$local) {
            return;
        }
        EventTravel travel = EventTravel.build();
        Client.EVENTS.post(travel);
        if (travel.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"updateVelocity"}, at={@At(value="TAIL")})
    private void inject2(float speed, Vec3d movementInput, CallbackInfo ci) {
        if (this.client$local) {
            Client.EVENTS.post(EventPostMove.build());
        }
    }

    @Inject(method={"getTargetingMargin"}, at={@At(value="RETURN")}, cancellable=true)
    private void client$getTargetingMargin(CallbackInfoReturnable<Float> cir) {
        PlayerEntity player;
        if (Client.IS_PANIC) {
            return;
        }
        Entity self = (Entity)this;
        if (!(self instanceof PlayerEntity)) {
            return;
        }
        if (!Hitbox.INSTANCE.isEnabled()) {
            return;
        }
        if (Hitbox.INSTANCE.ignFr.get() && self instanceof PlayerEntity && Client.FRIENDS.isFriend(player = (PlayerEntity)self)) {
            return;
        }
        float base = ((Float)cir.getReturnValue()).floatValue();
        float extra = Hitbox.INSTANCE.expand.get();
        cir.setReturnValue((Object)Float.valueOf(base + extra));
    }
}

