/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.Text
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.screen;

import im.leet.Client;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ChatScreen.class})
public class ChatScreenMixin
extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")})
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Client.HUD.click((int)mouseX, (int)mouseY, button);
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
        Client.HUD.release(button);
        return super.method_25406(mouseX, mouseY, button);
    }
}

