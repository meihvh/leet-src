/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.font.TextRenderer
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.hud.InGameHud
 *  net.minecraft.client.gui.hud.InGameHud$BarType
 *  net.minecraft.client.gui.hud.SpectatorHud
 *  net.minecraft.client.gui.hud.bar.Bar
 *  net.minecraft.client.gui.render.state.SimpleGuiElementRenderState
 *  net.minecraft.client.gui.screen.DownloadingTerrainScreen
 *  net.minecraft.client.render.RenderTickCounter
 *  net.minecraft.text.Text
 *  net.minecraft.world.GameMode
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.Event2D;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.render.system.sys2d.ClientLayer;
import im.leet.base.hud.ui.impl.EffectsElement;
import im.leet.base.hud.ui.impl.HotbarElement;
import im.leet.base.modules.impl.render.Interface;
import im.leet.base.modules.impl.render.Removals;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameHud.class})
public abstract class InGameHudMixin
implements MinecraftHolder {
    @Shadow
    private int field_2041;
    @Shadow
    @Nullable
    private Text field_2018;
    @Shadow
    private int field_2040;
    @Shadow
    @Final
    private MinecraftClient field_2035;
    @Shadow
    @Final
    private SpectatorHud field_2025;
    @Shadow
    private Pair<InGameHud.BarType, Bar> field_59817;
    @Shadow
    @Final
    private Map<InGameHud.BarType, Supplier<Bar>> field_59818;

    @Shadow
    public abstract void method_1753(DrawContext var1, RenderTickCounter var2);

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (Client.IS_PANIC) {
            return;
        }
        ci.cancel();
        if (this.field_2035.field_1755 == null || !(this.field_2035.field_1755 instanceof DownloadingTerrainScreen)) {
            Client.RENDERER.setDrawContext(context);
            Client.RENDERER.getCrenderSystem().layer(CRenderSystem.RenderLayer.HUD);
            context.field_59826.method_70919((SimpleGuiElementRenderState)new ClientLayer());
            Client.EVENTS.post(Event2D.build());
            float statusYOffset = 0.0f;
            if (!this.field_2035.field_1690.field_1842) {
                this.method_55798(context, tickCounter);
                this.method_1736(context, tickCounter);
                context.method_71048();
                if (this.field_2035.field_1761.method_2920() == GameMode.field_9219) {
                    this.field_2025.method_1978(context);
                } else if (Interface.INSTANCE.isEnabled() && HotbarElement.INSTANCE.isEnabled()) {
                    HotbarElement.renderHotbar(context, tickCounter);
                } else {
                    this.method_1759(context, tickCounter);
                }
                if (this.field_2035.field_1761.method_2908()) {
                    if (Interface.INSTANCE.isEnabled() && HotbarElement.INSTANCE.isEnabled()) {
                        statusYOffset = HotbarElement.renderStatusBars(context);
                    } else {
                        this.method_1760(context);
                    }
                }
                this.method_1741(context);
                InGameHud.BarType barType = this.method_70842();
                if (barType != this.field_59817.getKey()) {
                    this.field_59817 = Pair.of((Object)barType, (Object)this.field_59818.get(barType).get());
                }
                if (Interface.INSTANCE.isEnabled() && HotbarElement.INSTANCE.isEnabled()) {
                    HotbarElement.renderBar((Bar)this.field_59817.getValue(), context, tickCounter);
                } else {
                    ((Bar)this.field_59817.getValue()).method_70865(context, tickCounter);
                }
                if (!(!this.field_2035.field_1761.method_2913() || this.field_2035.field_1724.field_7520 <= 0 || Interface.INSTANCE.isEnabled() && HotbarElement.INSTANCE.isEnabled())) {
                    Bar.method_70866((DrawContext)context, (TextRenderer)this.field_2035.field_1772, (int)this.field_2035.field_1724.field_7520);
                }
                ((Bar)this.field_59817.getValue()).method_70868(context, tickCounter);
                if (this.field_2035.field_1761.method_2920() != GameMode.field_9219) {
                    if (!Interface.INSTANCE.isEnabled() || !HotbarElement.INSTANCE.isEnabled()) {
                        this.method_1749(context);
                    } else {
                        HotbarElement.renderTooltipAndOverlay(statusYOffset, this.field_2040, this.field_2018, this.field_2041, tickCounter);
                    }
                } else if (this.field_2035.field_1724.method_7325()) {
                    this.field_2025.method_71054(context);
                }
                if (!Interface.INSTANCE.isEnabled() || !EffectsElement.INSTANCE.isEnabled()) {
                    this.method_1765(context, tickCounter);
                }
                this.method_70837(context, tickCounter);
            }
            this.method_55799(context, tickCounter);
            if (!this.field_2035.field_1690.field_1842) {
                this.method_1766(context, tickCounter);
                this.method_70838(context, tickCounter);
                if (!Removals.INSTANCE.isEnabled() || !Removals.INSTANCE.removals.get(Removals.Removal.Scoreboard)) {
                    this.method_55803(context, tickCounter);
                }
                if (!Interface.INSTANCE.isEnabled() || !HotbarElement.INSTANCE.isEnabled()) {
                    this.method_55800(context, tickCounter);
                }
                this.method_55801(context, tickCounter);
                this.method_55802(context, tickCounter);
                this.method_55804(context, tickCounter);
                this.method_70839(context, tickCounter);
            }
        }
        Client.RENDERER.runTasks();
    }

    @Shadow
    protected abstract void method_55798(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_1736(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_1759(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_1760(DrawContext var1);

    @Shadow
    protected abstract void method_1741(DrawContext var1);

    @Shadow
    protected abstract InGameHud.BarType method_70842();

    @Shadow
    protected abstract void method_1749(DrawContext var1);

    @Shadow
    protected abstract void method_1765(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_70837(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_55799(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_1766(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_70838(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_55803(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_55800(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_55801(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_55802(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_55804(DrawContext var1, RenderTickCounter var2);

    @Shadow
    protected abstract void method_70839(DrawContext var1, RenderTickCounter var2);

    @Inject(method={"renderNauseaOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookNausea(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        if (Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.Nausea)) {
            ci.cancel();
        }
    }
}

