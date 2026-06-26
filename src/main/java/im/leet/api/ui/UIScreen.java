/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.text.Text
 */
package im.leet.api.ui;

import im.leet.api.ui.parts.UIPart;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class UIScreen
extends Screen {
    final ArrayList<UIPart> parts = new ArrayList();

    public UIScreen() {
        super((Text)Text.method_43473());
    }

    public void method_25394(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        for (UIPart part : this.parts) {
            part.render(mouseX, mouseY);
        }
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        for (UIPart part : this.parts) {
            if (!part.click((int)mouseX, (int)mouseY, button)) continue;
            return true;
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
        for (UIPart part : this.parts) {
            part.release(button);
        }
        return super.method_25406(mouseX, mouseY, button);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (UIPart part : this.parts) {
            if (!part.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) continue;
            return true;
        }
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (UIPart part : this.parts) {
            part.mouseDragged((int)mouseX, (int)mouseY);
        }
        return super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        for (UIPart part : this.parts) {
            part.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25400(char chr, int modifiers) {
        for (UIPart part : this.parts) {
            part.chartyped(chr, modifiers);
        }
        return super.method_25400(chr, modifiers);
    }

    public void addPart(UIPart part) {
        this.parts.add(part);
    }

    public void removePart(UIPart part) {
        this.parts.remove(part);
    }
}

