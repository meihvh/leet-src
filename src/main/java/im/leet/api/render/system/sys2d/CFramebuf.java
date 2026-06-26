/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL30C
 */
package im.leet.api.render.system.sys2d;

import org.lwjgl.opengl.GL30C;

public class CFramebuf {
    public int fboID = GL30C.glGenFramebuffers();
    public int textureID;
    public int depthRboID;
    public int width;
    public int height;

    public CFramebuf(int width, int height) {
        GL30C.glBindFramebuffer((int)36160, (int)this.fboID);
        this.textureID = GL30C.glGenTextures();
        GL30C.glBindTexture((int)3553, (int)this.textureID);
        GL30C.glTexImage2D((int)3553, (int)0, (int)32856, (int)width, (int)height, (int)0, (int)6408, (int)5121, (long)0L);
        GL30C.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL30C.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL30C.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)this.textureID, (int)0);
        this.depthRboID = GL30C.glGenRenderbuffers();
        GL30C.glBindRenderbuffer((int)36161, (int)this.depthRboID);
        GL30C.glRenderbufferStorage((int)36161, (int)35056, (int)width, (int)height);
        GL30C.glFramebufferRenderbuffer((int)36160, (int)33306, (int)36161, (int)this.depthRboID);
        GL30C.glBindFramebuffer((int)36160, (int)0);
        this.width = width;
        this.height = height;
    }

    public void resize(int width, int height) {
        GL30C.glBindFramebuffer((int)36160, (int)this.fboID);
        GL30C.glBindTexture((int)3553, (int)this.textureID);
        GL30C.glTexImage2D((int)3553, (int)0, (int)32856, (int)width, (int)height, (int)0, (int)6408, (int)5121, (long)0L);
        GL30C.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL30C.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL30C.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)this.textureID, (int)0);
        GL30C.glBindRenderbuffer((int)36161, (int)this.depthRboID);
        GL30C.glRenderbufferStorage((int)36161, (int)35056, (int)width, (int)height);
        GL30C.glFramebufferRenderbuffer((int)36160, (int)33306, (int)36161, (int)this.depthRboID);
        GL30C.glBindFramebuffer((int)36160, (int)0);
        this.width = width;
        this.height = height;
    }

    public void copyFrom(int id) {
        GL30C.glBindFramebuffer((int)36008, (int)id);
        GL30C.glBindFramebuffer((int)36009, (int)this.fboID);
        GL30C.glBlitFramebuffer((int)0, (int)0, (int)this.width, (int)this.height, (int)0, (int)0, (int)this.width, (int)this.height, (int)16384, (int)9728);
        GL30C.glBindFramebuffer((int)36160, (int)0);
    }
}

