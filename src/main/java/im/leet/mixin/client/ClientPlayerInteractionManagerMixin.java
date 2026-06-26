/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.network.SequencedPacketCreator
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.ActionResult$Success
 *  net.minecraft.util.Hand
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.world.GameMode
 *  net.minecraft.world.World
 *  org.apache.commons.lang3.mutable.MutableObject
 *  org.slf4j.Logger
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.api.events.list.EventAttack;
import im.leet.base.modules.impl.player.NoInteract;
import im.leet.base.rotations.Angle;
import java.util.Objects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow
    private GameMode field_3719;
    @Shadow
    @Final
    private MinecraftClient field_3712;
    @Shadow
    @Final
    private static Logger field_20316;

    @Shadow
    protected abstract void method_2911();

    @Shadow
    protected abstract void method_41931(ClientWorld var1, SequencedPacketCreator var2);

    @Inject(method={"attackEntity"}, at={@At(value="HEAD")}, cancellable=true)
    public void client$attackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        EventAttack attack = EventAttack.build(target);
        Client.EVENTS.post(attack);
        if (attack.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"interactItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (NoInteract.INSTANCE.shouldBlock()) {
            cir.setReturnValue((Object)ActionResult.field_5811);
            return;
        }
        cir.cancel();
        Angle angle = Client.ROTATION.getRotate();
        if (this.field_3719 == GameMode.field_9219) {
            cir.setReturnValue((Object)ActionResult.field_5811);
        } else {
            this.method_2911();
            MutableObject mutableObject = new MutableObject();
            this.method_41931(this.field_3712.field_1687, sequence -> {
                ItemStack itemStack2;
                PlayerInteractItemC2SPacket playerInteractItemC2SPacket = new PlayerInteractItemC2SPacket(hand, sequence, angle.getYaw(), angle.getPitch());
                ItemStack itemStack = player.method_5998(hand);
                if (player.method_7357().method_7904(itemStack)) {
                    mutableObject.setValue((Object)ActionResult.field_5811);
                    return playerInteractItemC2SPacket;
                }
                ActionResult actionResult = itemStack.method_7913((World)this.field_3712.field_1687, player, hand);
                if (actionResult instanceof ActionResult.Success) {
                    ActionResult.Success success = (ActionResult.Success)actionResult;
                    itemStack2 = Objects.requireNonNullElseGet(success.method_61396(), () -> player.method_5998(hand));
                } else {
                    itemStack2 = player.method_5998(hand);
                }
                if (itemStack2 != itemStack) {
                    player.method_6122(hand, itemStack2);
                }
                mutableObject.setValue((Object)actionResult);
                return playerInteractItemC2SPacket;
            });
            cir.setReturnValue((Object)((ActionResult)mutableObject.getValue()));
        }
    }

    @Inject(method={"interactBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (NoInteract.INSTANCE.shouldBlock()) {
            cir.setReturnValue((Object)ActionResult.field_5811);
        }
    }

    @Inject(method={"interactEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$interactEntity(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (NoInteract.INSTANCE.shouldBlock()) {
            cir.setReturnValue((Object)ActionResult.field_5811);
        }
    }
}

