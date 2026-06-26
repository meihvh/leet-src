/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package im.leet.base.modules.impl.render.esp;

import net.minecraft.entity.Entity;

public abstract class ESPRenderer {
    int index = 0;
    public float offsetX = 0.0f;
    public float offsetY = 0.0f;
    Align align = Align.TOP;

    public ESPRenderer(Align align) {
        this.align = align;
    }

    public abstract void render(float var1, float var2, float var3, float var4, float var5, Entity var6);

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public Align getAlign() {
        return this.align;
    }

    public static enum Align {
        TOP,
        LEFT,
        RIGHT,
        BOTTOM;

    }
}

