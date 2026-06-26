/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.math;

import im.leet.utils.math.MathUtility;

public class Rectangle {
    private float x;
    private float y;
    private float width;
    private float height;

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle() {
    }

    public Rectangle center(float x, float y) {
        this.x = x - this.width / 2.0f;
        this.y = y - this.height / 2.0f;
        return this;
    }

    public Rectangle bound(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public boolean hovered(int mouseX, int mouseY) {
        return MathUtility.mouseIn(this.x, this.y, this.width, this.height, mouseX, mouseY);
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}

