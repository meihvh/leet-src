/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.input.Input
 *  net.minecraft.client.network.ClientPlayNetworkHandler
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MovementType
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$LookAndOnGround
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$OnGroundOnly
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Direction$Axis
 *  net.minecraft.util.math.Direction$AxisDirection
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.api.events.list.EventBlockPush;
import im.leet.api.events.list.EventMove;
import im.leet.api.events.list.EventMoveVelocity;
import im.leet.api.events.list.EventNoSlow;
import im.leet.api.events.list.EventPostMotion;
import im.leet.base.modules.impl.player.Sprint;
import im.leet.base.rotations.Angle;
import im.leet.utils.attack.AttackHandle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayerEntity.class})
public abstract class ClientPlayerEntityMixin {
    @Shadow
    private double field_3926;
    @Shadow
    private double field_3940;
    @Shadow
    private double field_3924;
    @Shadow
    private float field_3941;
    @Shadow
    private float field_3925;
    @Shadow
    private int field_3923;
    @Shadow
    @Final
    public ClientPlayNetworkHandler field_3944;
    @Shadow
    private boolean field_3920;
    @Shadow
    private boolean field_53040;
    @Shadow
    @Final
    protected MinecraftClient field_3937;
    @Shadow
    private boolean field_3927;
    @Shadow
    public Input field_3913;

    @Shadow
    protected abstract void method_46742();

    @Shadow
    protected abstract boolean method_3134();

    @Shadow
    protected abstract boolean method_30674(BlockPos var1);

    @Shadow
    public abstract void method_5784(MovementType var1, Vec3d var2);

    @Redirect(method={"applyMovementSpeedFactors"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/Vec2f;multiply(F)Lnet/minecraft/util/math/Vec2f;", ordinal=1))
    private Vec2f cancelItemSlowdown(Vec2f vec2f, float multiplier) {
        ClientPlayerEntity player = (ClientPlayerEntity)this;
        EventNoSlow event = EventNoSlow.build();
        Client.EVENTS.post(event);
        if (event.isCancelled() && player.method_6115() && !player.method_5765()) {
            return vec2f.method_35582(1.0f);
        }
        return vec2f.method_35582(multiplier);
    }

    private boolean zov() {
        return Sprint.INSTANCE.isEnabled() && Sprint.INSTANCE.ignoreHunger.get();
    }

    @Inject(method={"canSprint"}, at={@At(value="RETURN")}, cancellable=true)
    private void hunger(CallbackInfoReturnable<Boolean> cir) {
        if (((Boolean)cir.getReturnValue()).booleanValue() || !this.zov()) {
            return;
        }
        ClientPlayerEntity player = (ClientPlayerEntity)this;
        if (player.method_7344().method_7586() > 6) {
            return;
        }
        if (this.field_3913 == null || !this.field_3913.field_54155.comp_3159()) {
            return;
        }
        cir.setReturnValue((Object)true);
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V", shift=At.Shift.AFTER)})
    private void tick(CallbackInfo ci) {
        Client.EVENTS.post(EventPostMotion.build());
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
    private float getYaw(ClientPlayerEntity clientPlayerEntity) {
        if (Client.IS_PANIC || !Client.ROTATION.isRotating()) {
            return clientPlayerEntity.method_36454();
        }
        return Client.ROTATION.getRotate().getYaw();
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"))
    private float getPitch(ClientPlayerEntity clientPlayerEntity) {
        if (Client.IS_PANIC || !Client.ROTATION.isRotating()) {
            return clientPlayerEntity.method_36455();
        }
        return Client.ROTATION.getRotate().getPitch();
    }

    @ModifyVariable(method={"move"}, at=@At(value="HEAD"), name={"arg2"}, ordinal=0, index=2, argsOnly=true)
    private Vec3d hookMove(Vec3d movement, MovementType type) {
        return Client.EVENTS.post(new EventMoveVelocity((MovementType)type, (Vec3d)movement)).movement;
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")}, cancellable=true)
    private void sendMovementPackets(CallbackInfo ci) {
        ci.cancel();
        Entity main = (Entity)this;
        Angle angle = Client.ROTATION.getRotate();
        EventMove move = EventMove.build(main.method_23317(), main.method_23318(), main.method_23321(), angle.getYaw(), angle.getPitch(), main.method_18798(), main.method_24828(), main.field_5976);
        Client.EVENTS.post(move);
        if (move.isCancelled()) {
            return;
        }
        this.method_46742();
        if (this.method_3134()) {
            boolean changedYaw;
            double diffX = move.getX() - this.field_3926;
            double diffY = move.getY() - this.field_3940;
            double diffZ = move.getZ() - this.field_3924;
            double diffYaw = move.getYaw() - this.field_3941;
            double diffPitch = move.getPitch() - this.field_3925;
            ++this.field_3923;
            boolean changedPosition = MathHelper.method_41190((double)diffX, (double)diffY, (double)diffZ) > MathHelper.method_33723((double)2.0E-4) || this.field_3923 >= 20;
            boolean bl = changedYaw = diffYaw != 0.0 || diffPitch != 0.0;
            if (changedPosition && changedYaw) {
                this.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.Full(move.getPos(), move.getYaw(), move.getPitch(), move.isGround(), move.horizontalCollision));
            } else if (changedPosition) {
                this.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(move.getPos(), move.isGround(), move.horizontalCollision));
            } else if (changedYaw) {
                this.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.LookAndOnGround(move.getYaw(), move.getPitch(), move.isGround(), move.horizontalCollision));
            } else if (this.field_3920 != move.isGround() || this.field_53040 != move.horizontalCollision) {
                this.field_3944.method_52787((Packet)new PlayerMoveC2SPacket.OnGroundOnly(move.isGround(), move.horizontalCollision));
            }
            if (changedPosition) {
                this.field_3926 = move.getX();
                this.field_3940 = move.getY();
                this.field_3924 = move.getZ();
                this.field_3923 = 0;
            }
            if (changedYaw) {
                this.field_3941 = move.getYaw();
                this.field_3925 = move.getPitch();
            }
            AttackHandle.ticksOnBlock = move.isGround() ? ++AttackHandle.ticksOnBlock : 0;
            this.field_3920 = move.isGround();
            this.field_53040 = move.horizontalCollision;
            this.field_3927 = (Boolean)this.field_3937.field_1690.method_42423().method_41753();
        }
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void pushOfBlock(double x, double z, CallbackInfo ci) {
        ci.cancel();
        EventBlockPush eventPush = EventBlockPush.build(x, z);
        Client.EVENTS.post(eventPush);
        if (eventPush.isCancelled()) {
            return;
        }
        BlockPos blockPos = BlockPos.method_49637((double)x, (double)((Entity)this).method_23318(), (double)z);
        if (this.method_30674(blockPos)) {
            Direction[] directions;
            double d = x - (double)blockPos.method_10263();
            double e = z - (double)blockPos.method_10260();
            Direction direction = null;
            double f = Double.MAX_VALUE;
            for (Direction direction2 : directions = new Direction[]{Direction.field_11039, Direction.field_11034, Direction.field_11043, Direction.field_11035}) {
                double h;
                double g = direction2.method_10166().method_10172(d, 0.0, e);
                double d2 = h = direction2.method_10171() == Direction.AxisDirection.field_11056 ? 1.0 - g : g;
                if (!(h < f) || this.method_30674(blockPos.method_10093(direction2))) continue;
                f = h;
                direction = direction2;
            }
            if (direction != null) {
                Vec3d vec3d = ((Entity)this).method_18798();
                if (direction.method_10166() == Direction.Axis.field_11048) {
                    ((Entity)this).method_18800(0.1 * (double)direction.method_10148(), vec3d.field_1351, vec3d.field_1350);
                } else {
                    ((Entity)this).method_18800(vec3d.field_1352, vec3d.field_1351, 0.1 * (double)direction.method_10165());
                }
            }
        }
    }
}

