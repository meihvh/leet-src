/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.ui.widgets.impl;

import im.leet.api.ui.UIWidget;
import im.leet.api.ui.parts.impl.UITextField;

public class TextFieldWidget
extends UIWidget {
    private final UITextField textField;

    public TextFieldWidget(UITextField textField) {
        this.textField = textField;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.textField.bound(this.x, this.y, this.width, this.height).render(mouseX, mouseY);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.textField.keyPressed(keyCode, scanCode, modifiers);
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        this.textField.chartyped(ch, keyCode);
        super.chartyped(ch, keyCode);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        return this.textField.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        this.textField.release(button);
        super.release(button);
    }
}

