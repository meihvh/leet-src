/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui;

import im.leet.api.render.RendererObject;
import im.leet.utils.animations.impl.EaseInOutQuad;

public abstract class UIWidget
extends RendererObject {
    protected int id = System.identityHashCode(this);
    public boolean shouldRemove = false;
    protected EaseInOutQuad animation = new EaseInOutQuad(300, 1.0);

    public void opened() {
    }

    public boolean isShouldRemove() {
        return this.shouldRemove;
    }

    public EaseInOutQuad getAnimation() {
        return this.animation;
    }
}

