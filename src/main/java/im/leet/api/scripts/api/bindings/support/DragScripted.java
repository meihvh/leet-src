/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.scripts.api.bindings.support;

import im.leet.api.drags.Drag;

public class DragScripted {
    private Drag origin;

    public DragScripted(Drag origin) {
        this.origin = origin;
    }

    public float getX() {
        return this.origin.x;
    }

    public float getY() {
        return this.origin.y;
    }

    public float getWidth() {
        return this.origin.width;
    }

    public float getHeight() {
        return this.origin.height;
    }

    public void setX(float x) {
        this.origin.x = x;
    }

    public void setY(float y) {
        this.origin.y = y;
    }

    public void setWidth(float width) {
        this.origin.width = width;
    }

    public void setHeight(float height) {
        this.origin.height = height;
    }
}

