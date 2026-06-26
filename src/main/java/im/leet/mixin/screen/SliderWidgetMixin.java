/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.widget.ClickableWidget
 *  net.minecraft.client.gui.widget.SliderWidget
 *  net.minecraft.text.Text
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.screen;

import im.leet.base.hud.ui.impl.MinecraftUI;
import im.leet.base.modules.impl.render.Interface;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={SliderWidget.class})
public abstract class SliderWidgetMixin
extends ClickableWidget {
    @Shadow
    protected double field_22753;

    public SliderWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Inject(method={"renderWidget"}, at={@At(value="HEAD")}, cancellable=true)
    private void leet$hookRender(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        if (Interface.INSTANCE.isEnabled() && MinecraftUI.INSTANCE.isEnabled()) {
            ci.cancel();
            MinecraftUI.renderSlider(this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), this.field_22763, this.method_25367(), this.field_22753, this.method_25369());
        }
    }
}

