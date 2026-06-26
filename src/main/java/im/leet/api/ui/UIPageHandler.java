/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui;

import im.leet.api.render.RendererObject;
import im.leet.api.ui.UIPage;
import java.util.ArrayList;
import java.util.Arrays;

public class UIPageHandler
extends RendererObject {
    private ArrayList<UIPage> pages = new ArrayList();

    public UIPageHandler register(UIPage ... pages) {
        this.pages.addAll(Arrays.asList(pages));
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        for (UIPage page : this.pages) {
            page.bound(this.x, this.y, this.width, this.height).render(mouseX, mouseY);
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        for (UIPage page : this.pages) {
            if (!page.click(mouseX, mouseY, button)) continue;
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (UIPage page : this.pages) {
            page.keyPressed(keyCode, scanCode, modifiers);
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (UIPage page : this.pages) {
            page.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }

    @Override
    public void release(int button) {
        for (UIPage page : this.pages) {
            page.release(button);
        }
        super.release(button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (UIPage page : this.pages) {
            page.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public ArrayList<UIPage> getPages() {
        return this.pages;
    }
}

