/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.Mouse
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventKey;
import im.leet.api.events.list.EventScroll;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Mouse.class})
public class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient field_1779;

    @Inject(method={"onMouseButton"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (Client.IS_PANIC) {
            return;
        }
        if (Client.CLICKGUI.isOpened()) {
            int mouseX = (int)this.field_1779.field_1729.method_68879(this.field_1779.method_22683());
            int mouseY = (int)this.field_1779.field_1729.method_68883(this.field_1779.method_22683());
            if (action == 1) {
                Client.CLICKGUI.click(mouseX, mouseY, button);
            } else if (action == 0) {
                Client.CLICKGUI.release(button);
            }
            ci.cancel();
        } else {
            if (button == -1) {
                return;
            }
            EventKey eventKey = EventKey.build(button, action);
            if (MinecraftHolder.mc.field_1755 == null) {
                Client.EVENTS.post(eventKey);
            }
        }
    }

    @Inject(method={"onMouseScroll"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        int mouseX = (int)this.field_1779.field_1729.method_68879(this.field_1779.method_22683());
        int mouseY = (int)this.field_1779.field_1729.method_68883(this.field_1779.method_22683());
        if (Client.CLICKGUI.isOpened()) {
            Client.CLICKGUI.mouseScrolled(mouseX, mouseY, horizontal, vertical);
            ci.cancel();
        } else if (MinecraftHolder.mc.field_1755 == null && Client.EVENTS.post(new EventScroll(horizontal, vertical)).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"onCursorPos"}, at={@At(value="HEAD")})
    private void client$onCursorPos(long window, double x, double y, CallbackInfo ci) {
        if (Client.CLICKGUI.isOpened()) {
            int mouseX = (int)this.field_1779.field_1729.method_68879(this.field_1779.method_22683());
            int mouseY = (int)this.field_1779.field_1729.method_68883(this.field_1779.method_22683());
            Client.CLICKGUI.mouseDragged(mouseX, mouseY);
        }
    }
}

