/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.screen.DeathScreen
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.option.GameOptions
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.util.Window
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventChangeWorld;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventPacketTick;
import im.leet.api.events.list.EventPostTick;
import im.leet.base.modules.impl.player.GuiWalk;
import im.leet.base.modules.impl.render.Chams;
import im.leet.mixin.accessor.IKeyBinding;
import im.leet.utils.jni.DwmApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MinecraftClient.class})
public abstract class MinecraftClientMixin {
    @Shadow
    @Final
    public GameOptions field_1690;
    @Shadow
    @Nullable
    public ClientPlayerEntity field_1724;

    @Shadow
    protected abstract void method_1523(boolean var1);

    @Shadow
    public abstract Window method_22683();

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        new Client().init();
    }

    @Inject(method={"stop"}, at={@At(value="HEAD")})
    private void onStop(CallbackInfo ci) {
        Client.saveAll();
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gl/Framebuffer;blitToScreen()V", shift=At.Shift.AFTER)})
    private void client$onRender(CallbackInfo ci) {
        Client.RENDERER.prepare();
        Client.RENDERER.render();
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void client$onTick(CallbackInfo ci) {
        if (MinecraftHolder.mc.field_1724 == null || MinecraftHolder.mc.field_1687 == null) {
            return;
        }
        Client.EVENTS.post(EventGameTick.build());
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void tick(CallbackInfo ci) {
        if (MinecraftClient.method_1551().field_1755 instanceof DeathScreen && MinecraftClient.method_1551().field_1724 != null && MinecraftClient.method_1551().field_1724.method_5805()) {
            MinecraftClient.method_1551().field_1755 = null;
        }
        if (MinecraftHolder.mc.field_1724 == null || MinecraftHolder.mc.field_1687 == null) {
            return;
        }
        Client.EVENTS.post(EventPostTick.build());
    }

    @Inject(method={"getWindowTitle"}, at={@At(value="RETURN")}, cancellable=true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        if (!Client.IS_PANIC) {
            cir.setReturnValue((Object)String.format("1337 \u25cf %s", ((String)cir.getReturnValue()).replace("Minecraft", "").replace("*", "").strip()));
        }
    }

    @Inject(method={"onResolutionChanged"}, at={@At(value="HEAD")})
    private void onResolutionChanged(CallbackInfo ci) {
        DwmApi.updateDwm(MinecraftHolder.mc.method_22683().method_4498(), MinecraftHolder.mc.method_22683().method_4490());
    }

    @Inject(method={"hasOutline"}, at={@At(value="HEAD")}, cancellable=true)
    private void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (Client.IS_PANIC) {
            return;
        }
        cir.setReturnValue((Object)(entity.method_5851() || this.field_1724 != null && this.field_1724.method_7325() && this.field_1690.field_1906.method_1434() && entity.method_5864() == EntityType.field_6097 || Chams.INSTANCE.isEnabled() && Chams.INSTANCE.shouldRender(entity) ? 1 : 0));
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;runTasks()V", shift=At.Shift.BEFORE)})
    private void hookPacketTick(CallbackInfo callbackInfo) {
        Client.EVENTS.post(EventPacketTick.instance);
    }

    @WrapOperation(method={"setScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;unpressAll()V")})
    private void onSetScreenKeyBindingUnpressAll(Operation<Void> op) {
        if (!GuiWalk.INSTANCE.isEnabled()) {
            op.call(new Object[0]);
            return;
        }
        GameOptions options = MinecraftHolder.mc.field_1690;
        for (KeyBinding kb : IKeyBinding.client$getKeysById().values()) {
            if (kb == options.field_1894 || kb == options.field_1913 || kb == options.field_1849 || kb == options.field_1881 || kb == options.field_1832 || kb == options.field_1867 || kb == options.field_1903) continue;
            ((IKeyBinding)kb).client$reset();
        }
    }

    @Inject(method={"setWorld"}, at={@At(value="HEAD")})
    private void leet$setWorld(ClientWorld world, CallbackInfo ci) {
        Client.EVENTS.post(new EventChangeWorld(world));
    }
}

