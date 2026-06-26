/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.screens.ingame;

import im.leet.api.render.RendererObject;
import im.leet.api.ui.UIWidget;
import java.util.concurrent.CopyOnWriteArrayList;

public class Widgets
extends RendererObject {
    private int lastId;
    private final CopyOnWriteArrayList<UIWidget> UIWidgets = new CopyOnWriteArrayList();

    public int genId() {
        return ++this.lastId;
    }

    public void register(UIWidget uiWidget) {
        if (!this.UIWidgets.contains(uiWidget)) {
            uiWidget.shouldRemove = false;
            uiWidget.opened();
            this.UIWidgets.add(uiWidget);
        }
    }

    public void unregister(UIWidget UIWidget2) {
        this.UIWidgets.remove(UIWidget2);
    }

    public CopyOnWriteArrayList<UIWidget> get() {
        return this.UIWidgets;
    }

    public boolean has(UIWidget UIWidget2) {
        return this.UIWidgets.contains(UIWidget2);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        for (UIWidget UIWidget2 : this.UIWidgets) {
            UIWidget2.render(mouseX, mouseY);
        }
        this.UIWidgets.removeIf(UIWidget::isShouldRemove);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        for (UIWidget UIWidget2 : this.UIWidgets) {
            UIWidget2.click(mouseX, mouseY, button);
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        for (UIWidget UIWidget2 : this.UIWidgets) {
            UIWidget2.release(button);
        }
        super.release(button);
    }
}

