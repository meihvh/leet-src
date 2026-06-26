/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui.parts.impl;

import im.leet.Client;
import im.leet.api.ui.parts.UIPart;
import im.leet.utils.math.MathUtility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrollPart
extends UIPart {
    private final List<UIPart> children = new ArrayList<UIPart>();
    private float scroll;
    private float scrollAnim;

    public ScrollPart(UIPart ... parts) {
        if (parts != null) {
            this.children.addAll(Arrays.asList(parts));
        }
    }

    public ScrollPart add(UIPart part) {
        this.children.add(part);
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 10.0f);
        Client.RENDERER.getCrenderSystem().push(this.x, this.y, this.width, this.height);
        float offY = 0.0f;
        for (UIPart child : this.children) {
            float ph = child.getPreferredHeight(this.width - 8.0f);
            child.bound(this.x + 4.0f, this.y + 4.0f + this.scrollAnim + offY, this.width - 8.0f, ph);
            child.render(mouseX, mouseY);
            offY += ph + 4.0f;
        }
        Client.RENDERER.getCrenderSystem().pop();
        float contentHeight = Math.max(0.0f, offY - 4.0f);
        this.scroll = contentHeight > this.height ? MathUtility.clamp(this.scroll, -(contentHeight - this.height), 0.0f) : 0.0f;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!this.hover((int)mouseX, (int)mouseY)) {
            return false;
        }
        this.scroll += (float)verticalAmount * 10.0f;
        return true;
    }

    @Override
    public void mouseDragged(int mouseX, int mouseY) {
        for (int i = this.children.size() - 1; i >= 0; --i) {
            this.children.get(i).mouseDragged(mouseX, mouseY);
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        for (int i = this.children.size() - 1; i >= 0; --i) {
            if (!this.children.get(i).click(mouseX, mouseY, button)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void release(int button) {
        for (UIPart child : this.children) {
            child.release(button);
        }
    }
}

