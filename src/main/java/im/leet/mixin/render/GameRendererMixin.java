/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.render.GameRenderer
 *  net.minecraft.client.render.RenderTickCounter
 *  net.minecraft.client.render.fog.FogRenderer
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.joml.Vector4f
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import com.mojang.blaze3d.systems.RenderSystem;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventGetFov;
import im.leet.base.modules.impl.render.AspectRatio;
import im.leet.base.modules.impl.render.CustomWorld;
import im.leet.base.modules.impl.render.Removals;
import im.leet.base.modules.impl.render.Zoom;
import im.leet.utils.math.MathUtility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GameRenderer.class})
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private MinecraftClient field_4015;
    @Shadow
    @Final
    private FogRenderer field_60793;

    @Shadow
    public abstract float method_32796();

    @Shadow
    public abstract float method_3195(float var1);

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V", shift=At.Shift.AFTER)})
    public void renderOverlay(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.method_1551();
        int mouseX = (int)client.field_1729.method_68879(client.method_22683());
        int mouseY = (int)client.field_1729.method_68883(client.method_22683());
        Client.CLICKGUI.render(mouseX, mouseY);
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    public void render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        Client.RENDERER.getCrenderSystem().postRender();
    }

    @Inject(method={"showFloatingItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        if (floatingItem.method_7909() == Items.field_8288 && Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.TotemPop)) {
            info.cancel();
        }
    }

    @Inject(method={"getBasicProjectionMatrix"}, at={@At(value="HEAD")}, cancellable=true)
    public void getBasicProjectionMatrix(float fovDegrees, CallbackInfoReturnable<Matrix4f> cir) {
        fovDegrees = Client.EVENTS.post(new EventGetFov((float)fovDegrees)).fov;
        Matrix4f matrix4f = new Matrix4f();
        if (Client.IS_PANIC) {
            return;
        }
        cir.cancel();
        float aspect = AspectRatio.INSTANCE.isEnabled() ? AspectRatio.INSTANCE.widthSlider.get() : (float)this.field_4015.method_22683().method_4489() / (float)this.field_4015.method_22683().method_4506();
        cir.setReturnValue((Object)matrix4f.perspective(fovDegrees * ((float)Math.PI / 180), aspect, 0.05f, this.method_32796()));
    }

    @Inject(method={"tiltViewWhenHurt"}, at={@At(value="HEAD")}, cancellable=true)
    public void getBasicProjectionMatrix(MatrixStack matrices, float tickProgress, CallbackInfo ci) {
        if (Removals.INSTANCE.isEnabled() && Removals.INSTANCE.removals.get(Removals.Removal.HurtView)) {
            ci.cancel();
        }
    }

    @Inject(method={"renderWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V", shift=At.Shift.AFTER)})
    private void renderWorld(RenderTickCounter renderTickCounter, CallbackInfo ci) {
        if (MinecraftHolder.mc.field_1724 == null || MinecraftHolder.mc.field_1687 == null) {
            return;
        }
        Camera camera = MinecraftHolder.mc.field_1773.method_19418();
        MatrixStack matrixStack = new MatrixStack();
        RenderSystem.getModelViewStack().pushMatrix().mul((Matrix4fc)matrixStack.method_23760().method_23761());
        matrixStack.method_22907((Quaternionfc)RotationAxis.field_40714.rotationDegrees(camera.method_19329()));
        matrixStack.method_22907((Quaternionfc)RotationAxis.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        MathUtility.lastProjMat.set((Matrix4fc)MinecraftHolder.mc.field_1773.method_22973(MinecraftHolder.mc.field_1773.method_3196(camera, MinecraftHolder.mc.method_61966().method_60637(true), true)));
        MathUtility.lastModMat.set((Matrix4fc)RenderSystem.getModelViewMatrix());
        MathUtility.lastWorldSpaceMatrix.set((Matrix4fc)matrixStack.method_23760().method_23761());
        MathUtility.worldStack = matrixStack;
        RenderSystem.getModelViewStack().popMatrix();
    }

    @Redirect(method={"renderWorld"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;"))
    private Vector4f leet$applyFog(FogRenderer instance, Camera camera, int viewDistance, boolean thick, RenderTickCounter tickCounter, float skyDarkness, ClientWorld world) {
        boolean bl2;
        boolean bl = bl2 = this.field_4015.field_1687.method_28103().method_28110(camera.method_19328().method_10263(), camera.method_19328().method_10260()) || this.field_4015.field_1705.method_1740().method_1800();
        if (!CustomWorld.INSTANCE.isEnabled() || !CustomWorld.INSTANCE.useFog.get()) {
            return this.field_60793.method_3211(camera, this.field_4015.field_1690.method_38521(), bl2, tickCounter, this.method_3195(skyDarkness), this.field_4015.field_1687);
        }
        return this.field_60793.method_3211(camera, (int)((float)this.field_4015.field_1690.method_38521() * CustomWorld.INSTANCE.fogDistance.get() * 5.0f), true, tickCounter, 10.0f, this.field_4015.field_1687);
    }

    @Inject(method={"renderHand"}, at={@At(value="HEAD")}, cancellable=true)
    private void leet$hookHand(float tickProgress, boolean sleeping, Matrix4f positionMatrix, CallbackInfo ci) {
        if (Zoom.INSTANCE.isEnabled() && !Zoom.INSTANCE.hands.get()) {
            ci.cancel();
        }
    }
}

