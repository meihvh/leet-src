/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.widget.PressableWidget
 *  net.minecraft.client.gui.widget.TextIconButtonWidget
 *  net.minecraft.text.Text
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.screen;

import im.leet.base.hud.ui.impl.MinecraftUI;
import im.leet.base.modules.impl.render.Interface;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PressableWidget.class})
public abstract class PressableWidgetMixin
extends ClickableWidget {
    public PressableWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Inject(method={"renderWidget"}, at={@At(value="HEAD")}, cancellable=true)
    private void leet$hookRender(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        if (Interface.INSTANCE.isEnabled() && MinecraftUI.INSTANCE.isEnabled()) {
            ci.cancel();
            MinecraftUI.renderButton(this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), this.field_22763, this.method_25367(), this instanceof TextIconButtonWidget ? null : this.method_25369());
        }
    }
}

