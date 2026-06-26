/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.texture.AbstractTexture
 *  net.minecraft.client.texture.GlTextureView
 *  net.minecraft.util.Identifier
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL30
 */
package im.leet.api.render.system.sys2d;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.GlTextureView;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class TextureAtlas {
    public final int atlasWidth;
    public final int atlasHeight;
    private final int glId;
    private int currentX = 0;
    private int currentY = 0;
    private int rowHeight = 0;
    private final Map<String, UV> uvMap = new HashMap<String, UV>();

    public TextureAtlas(int width, int height) {
        this.atlasWidth = width;
        this.atlasHeight = height;
        this.glId = GL11.glGenTextures();
        GL11.glBindTexture((int)3553, (int)this.glId);
        GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)this.atlasWidth, (int)this.atlasHeight, (int)0, (int)6408, (int)5121, (ByteBuffer)null);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)33084, (int)0);
        GL11.glTexParameteri((int)3553, (int)33085, (int)0);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
    }

    public int getGlId() {
        return this.glId;
    }

    public UV addTextureFromGL(String name, int srcGlId) {
        int[] size = this.getSize(srcGlId);
        int width = size[0];
        int height = size[1];
        if (this.currentX + width > this.atlasWidth) {
            this.currentX = 0;
            this.currentY += this.rowHeight;
            this.rowHeight = 0;
        }
        if (this.currentY + height > this.atlasHeight) {
            throw new RuntimeException("Atlas overflow!");
        }
        this.rowHeight = Math.max(this.rowHeight, height);
        int readFbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer((int)36008, (int)readFbo);
        GL30.glFramebufferTexture2D((int)36008, (int)36064, (int)3553, (int)srcGlId, (int)0);
        int drawFbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer((int)36009, (int)drawFbo);
        GL30.glFramebufferTexture2D((int)36009, (int)36064, (int)3553, (int)this.glId, (int)0);
        if (GL30.glCheckFramebufferStatus((int)36008) != 36053) {
            GL30.glBindFramebuffer((int)36008, (int)0);
            GL30.glDeleteFramebuffers((int)readFbo);
            GL30.glDeleteFramebuffers((int)drawFbo);
            throw new RuntimeException("Read FBO incomplete");
        }
        if (GL30.glCheckFramebufferStatus((int)36009) != 36053) {
            GL30.glBindFramebuffer((int)36008, (int)0);
            GL30.glBindFramebuffer((int)36009, (int)0);
            GL30.glDeleteFramebuffers((int)readFbo);
            GL30.glDeleteFramebuffers((int)drawFbo);
            throw new RuntimeException("Draw FBO incomplete");
        }
        GL11.glReadBuffer((int)36064);
        GL11.glDrawBuffer((int)36064);
        GL30.glBlitFramebuffer((int)0, (int)0, (int)width, (int)height, (int)this.currentX, (int)this.currentY, (int)(this.currentX + width), (int)(this.currentY + height), (int)16384, (int)9728);
        GL30.glBindFramebuffer((int)36008, (int)0);
        GL30.glBindFramebuffer((int)36009, (int)0);
        GL30.glDeleteFramebuffers((int)readFbo);
        GL30.glDeleteFramebuffers((int)drawFbo);
        float u0 = (float)this.currentX / (float)this.atlasWidth;
        float v0 = (float)this.currentY / (float)this.atlasHeight;
        float u1 = (float)width / (float)this.atlasWidth;
        float v1 = (float)height / (float)this.atlasHeight;
        this.currentX += width;
        UV uv = new UV(u0, v0, u1, v1, width, height);
        this.uvMap.put(name, uv);
        return uv;
    }

    public void updateTextureFromGL(String name, int srcGlId) {
        UV uv = this.uvMap.get(name);
        if (uv == null) {
            return;
        }
        int destX = Math.round(uv.u0 * (float)this.atlasWidth);
        int destY = Math.round(uv.v0 * (float)this.atlasHeight);
        int destW = Math.round(uv.u1 * (float)this.atlasWidth);
        int destH = Math.round(uv.v1 * (float)this.atlasHeight);
        int[] srcSize = this.getSize(srcGlId);
        int srcW = srcSize[0];
        int srcH = srcSize[1];
        int blitW = Math.min(srcW, destW);
        int blitH = Math.min(srcH, destH);
        int readFbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer((int)36008, (int)readFbo);
        GL30.glFramebufferTexture2D((int)36008, (int)36064, (int)3553, (int)srcGlId, (int)0);
        if (GL30.glCheckFramebufferStatus((int)36008) != 36053) {
            GL30.glBindFramebuffer((int)36008, (int)0);
            GL30.glDeleteFramebuffers((int)readFbo);
            throw new RuntimeException("Read framebuffer incomplete when updating texture '" + name + "'");
        }
        int drawFbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer((int)36009, (int)drawFbo);
        GL30.glFramebufferTexture2D((int)36009, (int)36064, (int)3553, (int)this.glId, (int)0);
        if (GL30.glCheckFramebufferStatus((int)36009) != 36053) {
            GL30.glBindFramebuffer((int)36008, (int)0);
            GL30.glBindFramebuffer((int)36009, (int)0);
            GL30.glDeleteFramebuffers((int)readFbo);
            GL30.glDeleteFramebuffers((int)drawFbo);
            throw new RuntimeException("Draw framebuffer incomplete when updating texture '" + name + "'");
        }
        GL11.glReadBuffer((int)36064);
        GL11.glDrawBuffer((int)36064);
        GL30.glBlitFramebuffer((int)0, (int)0, (int)blitW, (int)blitH, (int)destX, (int)destY, (int)(destX + blitW), (int)(destY + blitH), (int)16384, (int)9728);
        GL30.glBindFramebuffer((int)36008, (int)0);
        GL30.glBindFramebuffer((int)36009, (int)0);
        GL30.glDeleteFramebuffers((int)readFbo);
        GL30.glDeleteFramebuffers((int)drawFbo);
    }

    public boolean has(String name) {
        return this.uvMap.containsKey(name);
    }

    public UV addTextureFromImage(String name, Identifier identifier) {
        AbstractTexture tex = MinecraftClient.method_1551().method_1531().method_4619(identifier);
        tex.method_4527(false, true);
        int id = ((GlTextureView)tex.method_71659()).method_71638().method_68427();
        return this.addTextureFromGL(name, id);
    }

    public UV getOrCreate(String name, Identifier identifier) {
        if (!this.has(name)) {
            try {
                AbstractTexture tex = MinecraftClient.method_1551().method_1531().method_4619(identifier);
                tex.method_4527(false, true);
                int id = ((GlTextureView)tex.method_71659()).method_71638().method_68427();
                return this.addTextureFromGL(name, id);
            }
            catch (Exception e) {
                Client.RENDERER.getCrenderSystem().addPrepare(identifier);
                return null;
            }
        }
        return this.getUV(name);
    }

    public UV add(TextureUse texUse, Identifier identifier) {
        AbstractTexture tex = MinecraftClient.method_1551().method_1531().method_4619(identifier);
        tex.method_4527(false, true);
        int id = ((GlTextureView)tex.method_71659()).method_71638().method_68427();
        return this.addTextureFromGL(texUse.name().toLowerCase(), id);
    }

    public int[] getSize(int texId) {
        int target = 3553;
        int prev = GL11.glGetInteger((int)32873);
        GL11.glBindTexture((int)target, (int)texId);
        int level = 0;
        int width = GL11.glGetTexLevelParameteri((int)target, (int)level, (int)4096);
        int height = GL11.glGetTexLevelParameteri((int)target, (int)level, (int)4097);
        GL11.glBindTexture((int)target, (int)prev);
        return new int[]{width, height};
    }

    public UV getUV(String name) {
        return this.uvMap.get(name);
    }

    public static class UV {
        public final float u0;
        public final float v0;
        public final float u1;
        public final float v1;
        public final float width;
        public final float height;

        public UV(float u0, float v0, float u1, float v1, float width, float height) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            this.width = width;
            this.height = height;
        }
    }
}

