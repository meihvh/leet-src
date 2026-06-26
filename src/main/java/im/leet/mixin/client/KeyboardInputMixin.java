/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.input.KeyboardInput
 *  net.minecraft.util.PlayerInput
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventInput;
import im.leet.base.modules.impl.player.GuiWalk;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={KeyboardInput.class})
public abstract class KeyboardInputMixin
implements MinecraftHolder {
    @Shadow
    private static float method_40218(boolean positive, boolean negative) {
        return 0.0f;
    }

    @Redirect(method={"tick"}, at=@At(value="NEW", target="(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;"))
    private PlayerInput onTick(boolean forwardKey, boolean backKey, boolean leftKey, boolean rightKey, boolean jumpKey, boolean sneakKey, boolean sprintKey) {
        EventInput event = EventInput.build(KeyboardInputMixin.method_40218(forwardKey |= GuiWalk.INSTANCE.handle(KeyboardInputMixin.mc.field_1690.field_1894), backKey |= GuiWalk.INSTANCE.handle(KeyboardInputMixin.mc.field_1690.field_1881)), KeyboardInputMixin.method_40218(leftKey |= GuiWalk.INSTANCE.handle(KeyboardInputMixin.mc.field_1690.field_1913), rightKey |= GuiWalk.INSTANCE.handle(KeyboardInputMixin.mc.field_1690.field_1849)), jumpKey |= GuiWalk.INSTANCE.handle(KeyboardInputMixin.mc.field_1690.field_1903), sneakKey |= GuiWalk.INSTANCE.handle(KeyboardInputMixin.mc.field_1690.field_1832), sprintKey |= GuiWalk.INSTANCE.handle(KeyboardInputMixin.mc.field_1690.field_1867));
        Client.EVENTS.post(event);
        forwardKey = event.getForward() >= 1.0f;
        backKey = event.getForward() <= -1.0f;
        leftKey = event.getStrafe() >= 1.0f;
        rightKey = event.getStrafe() <= -1.0f;
        jumpKey = event.isJump();
        sneakKey = event.isSneak();
        sprintKey = event.isSprint();
        if (event.isCancelled()) {
            forwardKey = false;
            backKey = false;
            leftKey = false;
            rightKey = false;
            jumpKey = false;
            sneakKey = false;
        }
        return new PlayerInput(forwardKey, backKey, leftKey, rightKey, jumpKey, sneakKey, sprintKey);
    }
}

