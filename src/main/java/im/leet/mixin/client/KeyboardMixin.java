/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Keyboard
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.utils.client.mixin.KeyboardHandler;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Keyboard.class})
public class KeyboardMixin {
    @Inject(method={"onKey"}, at={@At(value="HEAD")}, cancellable=true)
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (KeyboardHandler.handleKey(key, scancode, action, modifiers)) {
            ci.cancel();
        }
    }

    @Inject(method={"onChar"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        if (KeyboardHandler.handleChar((char)codePoint, modifiers)) {
            ci.cancel();
        }
    }
}

