/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBuffer
 *  com.mojang.blaze3d.buffers.GpuBufferSlice
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.systems.RenderPass
 *  com.mojang.blaze3d.vertex.VertexFormat$IndexType
 *  net.minecraft.client.gl.Framebuffer
 *  net.minecraft.client.gui.ScreenRect
 *  net.minecraft.client.gui.render.GuiRenderer
 *  net.minecraft.client.gui.render.GuiRenderer$Draw
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.VertexFormat;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.render.system.ClientPipelines;
import im.leet.api.render.system.sys2d.CRenderSystem;
import java.util.function.Supplier;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiRenderer.class})
public abstract class GuiRendererMixin
implements MinecraftHolder {
    @Shadow
    protected abstract void method_70884(ScreenRect var1, RenderPass var2);

    @Inject(method={"render(Lnet/minecraft/client/gui/render/GuiRenderer$Draw;Lcom/mojang/blaze3d/systems/RenderPass;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void client$render(GuiRenderer.Draw draw, RenderPass pass, GpuBuffer indexBuffer, VertexFormat.IndexType indexType, CallbackInfo ci) {
        ci.cancel();
        if (draw.comp_4049() == ClientPipelines.HUD) {
            Client.RENDERER.getCrenderSystem().render(CRenderSystem.RenderLayer.ESP);
            Client.HUD.render((int)GuiRendererMixin.mc.field_1729.method_68879(window), (int)GuiRendererMixin.mc.field_1729.method_68883(window));
            Client.RENDERER.getCrenderSystem().render(CRenderSystem.RenderLayer.POST);
            Client.RENDERER.getCrenderSystem().render(CRenderSystem.RenderLayer.BLUR);
            Client.RENDERER.getCrenderSystem().render(CRenderSystem.RenderLayer.HUD);
        } else {
            RenderPipeline renderPipeline = draw.comp_4049();
            pass.setPipeline(renderPipeline);
            pass.setVertexBuffer(0, draw.comp_4044());
            ScreenRect screenRect = draw.comp_4051();
            if (screenRect != null) {
                this.method_70884(screenRect, pass);
            } else {
                pass.disableScissor();
            }
            if (draw.comp_4050().comp_4052() != null) {
                pass.bindSampler("Sampler0", draw.comp_4050().comp_4052());
            }
            if (draw.comp_4050().comp_4053() != null) {
                pass.bindSampler("Sampler1", draw.comp_4050().comp_4053());
            }
            if (draw.comp_4050().comp_4054() != null) {
                pass.bindSampler("Sampler2", draw.comp_4050().comp_4054());
            }
            pass.setIndexBuffer(indexBuffer, indexType);
            pass.drawIndexed(draw.comp_4186(), 0, draw.comp_4048(), 1);
        }
    }

    @Inject(method={"render(Ljava/util/function/Supplier;Lnet/minecraft/client/gl/Framebuffer;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;II)V"}, at={@At(value="HEAD")})
    private void client$prepare(Supplier<String> nameSupplier, Framebuffer framebuffer, GpuBufferSlice fogBuffer, GpuBufferSlice dynamicTransformsBuffer, GpuBuffer buffer, VertexFormat.IndexType indexType, int from, int _to, CallbackInfo ci) {
        Client.RENDERER.prepare();
    }
}

