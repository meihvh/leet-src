/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.render;

import im.leet.MinecraftHolder;
import im.leet.utils.math.MathUtility;

public abstract class RendererObject
implements MinecraftHolder {
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    public RendererObject bound(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        if (height >= 0.0f) {
            this.height = height;
        }
        return this;
    }

    public boolean hover(int mouseX, int mouseY) {
        return MathUtility.mouseIn(this.x, this.y, this.width, this.height, mouseX, mouseY);
    }

    public abstract void render(int var1, int var2);

    public boolean click(int mouseX, int mouseY, int button) {
        return false;
    }

    public void chartyped(char ch, int keyCode) {
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public void release(int button) {
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return false;
    }

    public void mouseDragged(int mouseX, int mouseY) {
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }
}

