/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 */
package im.leet.api.render.system.sys2d;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class TextureState {
    private final int[] boundTextures;
    private final int originalActiveTexture;
    private final int maxTextureUnits = this.getInteger(35661);

    public TextureState() {
        this.boundTextures = new int[this.maxTextureUnits];
        this.originalActiveTexture = this.getInteger(34016);
        for (int i = 0; i < this.maxTextureUnits; ++i) {
            GL13.glActiveTexture((int)(33984 + i));
            this.boundTextures[i] = this.getInteger(32873);
        }
        GL13.glActiveTexture((int)this.originalActiveTexture);
    }

    public void restore() {
        for (int i = 0; i < this.maxTextureUnits; ++i) {
            GL13.glActiveTexture((int)(33984 + i));
            GL11.glBindTexture((int)3553, (int)this.boundTextures[i]);
        }
        GL13.glActiveTexture((int)this.originalActiveTexture);
    }

    private int getInteger(int parameter) {
        IntBuffer buf = BufferUtils.createIntBuffer((int)1);
        GL11.glGetIntegerv((int)parameter, (IntBuffer)buf);
        return buf.get(0);
    }
}

