/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBuffer$MappedView
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.block.enums.CameraSubmersionType
 *  net.minecraft.client.gl.MappableRingBuffer
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.render.RenderTickCounter
 *  net.minecraft.client.render.fog.FogData
 *  net.minecraft.client.render.fog.FogModifier
 *  net.minecraft.client.render.fog.FogRenderer
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector4f
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import im.leet.base.modules.impl.render.CustomWorld;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={FogRenderer.class})
public abstract class FogRendererMixin {
    @Shadow
    @Final
    private MappableRingBuffer field_60098;
    @Shadow
    @Final
    private static List<FogModifier> field_60586;

    @Shadow
    protected abstract void method_71110(ByteBuffer var1, int var2, Vector4f var3, float var4, float var5, float var6, float var7, float var8, float var9);

    @Shadow
    protected abstract Vector4f method_62185(Camera var1, float var2, ClientWorld var3, int var4, float var5, boolean var6);

    @Shadow
    protected abstract CameraSubmersionType method_71652(Camera var1, boolean var2);

    @Inject(method={"getFogColor"}, at={@At(value="TAIL")}, cancellable=true)
    private void leet$getFogColor(Camera camera, float tickProgress, ClientWorld world, int viewDistance, float skyDarkness, boolean thick, CallbackInfoReturnable<Vector4f> cir) {
        if (CustomWorld.INSTANCE.isEnabled() && CustomWorld.INSTANCE.useFog.get()) {
            Color c = CustomWorld.INSTANCE.fogColor.get();
            cir.setReturnValue((Object)new Vector4f((float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, (float)c.getAlpha() / 255.0f));
        }
    }

    @Inject(method={"applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;"}, at={@At(value="HEAD")}, cancellable=true)
    private void leet$applyFog(Camera camera, int viewDistance, boolean thick, RenderTickCounter tickCounter, float skyDarkness, ClientWorld world, CallbackInfoReturnable<Vector4f> cir) {
        if (!CustomWorld.INSTANCE.useFog.get() || !CustomWorld.INSTANCE.isEnabled()) {
            return;
        }
        cir.cancel();
        float fac = CustomWorld.INSTANCE.fogDistance.get();
        float f = tickCounter.method_60637(false);
        Vector4f vector4f = this.method_62185(camera, f, world, viewDistance, skyDarkness, thick);
        float g = viewDistance * 16;
        CameraSubmersionType cameraSubmersionType = this.method_71652(camera, thick);
        Entity entity = camera.method_19331();
        FogData fogData = new FogData();
        for (FogModifier fogModifier : field_60586) {
            if (!fogModifier.method_42593(cameraSubmersionType, entity)) continue;
            fogModifier.method_42591(fogData, entity, camera.method_19328(), world, g, tickCounter);
            break;
        }
        float h = MathHelper.method_15363((float)(g / 10.0f), (float)4.0f, (float)64.0f);
        fogData.field_60583 = g - h;
        fogData.field_60585 = g;
        try (GpuBuffer.MappedView mappedView = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.field_60098.method_71119(), false, true);){
            this.method_71110(mappedView.data(), 0, vector4f, fogData.field_60582 * fac, fogData.field_60584 * fac, fogData.field_60583 * fac, fogData.field_60585 * fac, fogData.field_60099 * fac, fogData.field_60100 * fac);
        }
        cir.setReturnValue((Object)vector4f);
    }
}

