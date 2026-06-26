/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui.parts;

import im.leet.api.render.RendererObject;
import im.leet.api.ui.UIStyle;

public abstract class UIPart
extends RendererObject {
    public UIStyle style;

    public float getPreferredHeight(float availableWidth) {
        return 20.0f;
    }
}

