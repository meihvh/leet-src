/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.NativeImage
 *  net.minecraft.client.util.Window
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.glfw.GLFWImage
 *  org.lwjgl.glfw.GLFWImage$Buffer
 *  org.lwjgl.system.MemoryStack
 *  org.lwjgl.system.MemoryUtil
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.client;

import im.leet.Client;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Window.class})
public class WindowMixin {
    @Shadow
    @Final
    private long field_5187;

    @Inject(method={"setIcon"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetIcon(CallbackInfo ci) {
        if (Client.IS_PANIC) {
            return;
        }
        ci.cancel();
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            GLFWImage.Buffer buffer = GLFWImage.malloc((int)1, (MemoryStack)memoryStack);
            try (NativeImage nativeImage = NativeImage.method_4309((InputStream)WindowMixin.class.getResourceAsStream("/assets/leet/images/ui/logo.png"));){
                ByteBuffer byteBuffer = MemoryUtil.memAlloc((int)(nativeImage.method_4307() * nativeImage.method_4323() * 4));
                byteBuffer.asIntBuffer().put(nativeImage.method_48463());
                buffer.position(0);
                buffer.width(nativeImage.method_4307());
                buffer.height(nativeImage.method_4323());
                buffer.pixels(byteBuffer);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            GLFW.glfwSetWindowIcon((long)this.field_5187, (GLFWImage.Buffer)((GLFWImage.Buffer)buffer.position(0)));
        }
    }

    @Inject(method={"logGlError"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGlError(int error, long description, CallbackInfo ci) {
        ci.cancel();
    }
}

