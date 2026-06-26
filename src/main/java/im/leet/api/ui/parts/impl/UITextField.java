/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.util.InputUtil
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.parts.impl;

import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.UICallback;
import im.leet.api.ui.UIStyle;
import im.leet.api.ui.parts.UIPart;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class UITextField
extends UIPart {
    public boolean writing = false;
    private boolean hoveredDesc = false;
    private boolean sas = false;
    int limit = 100;
    private int cursor = -1;
    private int selectionEnd = -1;
    private boolean dragging = false;
    private int cursorPosition = 0;
    private float scrollOffset = 0.0f;
    private long lastClickTime = 0L;
    private long lastInputTime = System.currentTimeMillis();
    private final MinecraftClient mc = MinecraftClient.method_1551();
    private static UITextField active;
    private String hideMask = null;
    private IconUse icon = null;
    private String text = "";
    private String desc = "";
    private UICallback callback = str -> {};
    private UICallback finishCallback = str -> {};
    private int lastText = -1;
    private float lastMouseX = -999.0f;
    private float lastMouseY = -999.0f;

    @Override
    public void render(int mouseX, int mouseY) {
        float iconX;
        boolean wasHovered = this.writing;
        if (!wasHovered && this.writing) {
            this.lastInputTime = System.currentTimeMillis();
        }
        if (this.style != UIStyle.TRANSPARENT) {
            Client.RENDERER.drawModuleRect(this.x, this.y, this.width, this.height);
        }
        if (this.style == UIStyle.OUTLINED) {
            Client.RENDERER.outline(this.x, this.y, this.width, this.height, 0.0f, new Vector4f(7.0f), new Vector2f(1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        }
        Client.RENDERER.getCrenderSystem().push(this.x + 6.0f, this.y, this.width - 12.0f, 24.0f);
        this.updateScrollOffset();
        float f = iconX = this.icon != null ? 18.0f : 6.0f;
        if (this.icon != null) {
            Client.RENDERER.text(this.icon, this.x + 6.0f, this.y + this.height / 2.0f - 4.5f, TextureUse.ICONS, 7.0f, ClientColors.DARK_GRAY_COLOR);
        }
        if (!this.text.isEmpty()) {
            Client.RENDERER.text(this.hideMask != null ? this.hideMask.repeat(this.text.length()) : this.text, this.x + iconX + this.scrollOffset, this.y + this.height / 2.0f - 4.5f, TextureUse.SFMEDIUM, 7.0f, this.style == UIStyle.TRANSPARENT && this.hover(mouseX, mouseY) ? ColorUtility.injectAlpha(ClientColors.FORE_COLOR, 155.0f) : ClientColors.FORE_COLOR);
        } else if (!this.writing && this.desc != null && !this.desc.isEmpty()) {
            Client.RENDERER.text(this.desc, this.x + iconX + this.scrollOffset, this.y + this.height / 2.0f - 4.5f, TextureUse.SFMEDIUM, 7.0f, ClientColors.DARK_GRAY_COLOR);
        }
        if (this.writing && this.hasSelection()) {
            int start = Math.min(this.cursor, this.selectionEnd);
            int end = Math.max(this.cursor, this.selectionEnd);
            String textBefore = this.safeSubstring(this.hideMask != null ? this.hideMask.repeat(this.text.length()) : this.text, 0, start);
            String selectedText = this.safeSubstring(this.hideMask != null ? this.hideMask.repeat(this.text.length()) : this.text, start, end);
            float startX = this.x + iconX + this.scrollOffset + Client.RENDERER.textWidth(textBefore, TextureUse.SFMEDIUM, 7.0f);
            float widthX = Client.RENDERER.textWidth(selectedText, TextureUse.SFMEDIUM, 7.0f) + 2.0f;
            Client.RENDERER.rect(startX, this.y + this.height / 2.0f - 5.5f, widthX, 11.0f, new Vector4f(0.0f), 0.0f, new Color(85, 133, 232, 155), new Color(85, 133, 232, 155), new Color(85, 133, 232, 155), new Color(85, 133, 232, 155));
        }
        if (this.writing && active == this) {
            boolean shouldBlink = System.currentTimeMillis() / 500L % 2L == 0L;
            String beforeCursor = this.safeSubstring(this.hideMask != null ? this.hideMask.repeat(this.text.length()) : this.text, 0, this.cursorPosition);
            float cursorX = this.x + iconX + this.scrollOffset + Client.RENDERER.textWidth(beforeCursor, TextureUse.SFMEDIUM, 7.0f);
            if (cursorX >= this.x + iconX && cursorX <= this.x + this.width - iconX && shouldBlink) {
                Client.RENDERER.text("|", cursorX, this.y + this.height / 2.0f - 4.5f, TextureUse.SFMEDIUM, 7.0f, ClientColors.FORE_COLOR);
            }
        }
        Client.RENDERER.getCrenderSystem().pop();
        if (this.dragging) {
            int newCursorPos;
            this.selectionEnd = newCursorPos = this.getCursorIndexAt(mouseX);
            this.cursorPosition = newCursorPos;
            this.updateScrollOffset();
            this.lastInputTime = System.currentTimeMillis();
        }
    }

    private void updateScrollOffset() {
        if (this.text.isEmpty()) {
            this.scrollOffset = 0.0f;
            return;
        }
        float textWidth = Client.RENDERER.textWidth(this.text, TextureUse.SFMEDIUM, 7.0f);
        float visibleWidth = this.width - 12.0f;
        String beforeCursor = this.safeSubstring(this.text, 0, this.cursorPosition);
        float cursorX = Client.RENDERER.textWidth(beforeCursor, TextureUse.SFMEDIUM, 7.0f);
        if (textWidth <= visibleWidth) {
            this.scrollOffset = 0.0f;
            return;
        }
        float leftBound = -this.scrollOffset;
        float rightBound = leftBound + visibleWidth;
        if (cursorX < leftBound) {
            this.scrollOffset = -cursorX;
        } else if (cursorX > rightBound - 5.0f) {
            this.scrollOffset = -(cursorX - (visibleWidth - 10.0f));
        }
        float maxOffset = -(textWidth - visibleWidth + 5.0f);
        this.scrollOffset = Math.max(maxOffset, Math.min(0.0f, this.scrollOffset));
    }

    private boolean hasSelection() {
        return this.cursor != -1 && this.selectionEnd != -1 && this.cursor != this.selectionEnd;
    }

    private String safeSubstring(String str, int start, int end) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        int len = str.length();
        start = Math.max(0, Math.min(start, len));
        end = Math.max(start, Math.min(end, len));
        return str.substring(start, end);
    }

    private int getCursorIndexAt(int mouseX) {
        float textStartX = this.x + (float)(this.icon != null ? 18 : 6) + this.scrollOffset;
        if ((float)mouseX <= textStartX) {
            return 0;
        }
        if (this.text.isEmpty()) {
            return 0;
        }
        float relativeX = (float)mouseX - textStartX;
        for (int i = 0; i <= this.text.length(); ++i) {
            String substr = this.safeSubstring(this.text, 0, i);
            float w = Client.RENDERER.textWidth(substr, TextureUse.SFMEDIUM, 7.0f);
            if (!(w >= relativeX)) continue;
            return Math.min(i, this.text.length());
        }
        return this.text.length();
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.lastMouseX == -999.0f || this.lastMouseY == -999.0f) {
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
        if (this.hover(mouseX, mouseY)) {
            int clickPos;
            active = this;
            long currentTime = System.currentTimeMillis();
            this.writing = true;
            Client.CLICKGUI.WRITING = true;
            this.resetCursor();
            this.selectionEnd = clickPos = this.getCursorIndexAt(mouseX);
            this.cursorPosition = clickPos;
            if (currentTime - this.lastClickTime < 250L) {
                this.cursor = 0;
                this.cursorPosition = this.selectionEnd = this.text.length();
            } else {
                this.cursor = clickPos;
            }
            this.dragging = true;
            this.lastClickTime = currentTime;
            this.lastInputTime = currentTime;
            return true;
        }
        if (MathUtility.delta(this.lastMouseX, mouseX) + MathUtility.delta(this.lastMouseY, mouseY) < 10.0f || !this.hover(mouseX, mouseY)) {
            this.writing = false;
            Client.CLICKGUI.WRITING = false;
            active = null;
            this.resetCursor();
            this.dragging = false;
            if (this.finishCallback != null) {
                this.finishCallback.onChanged(this.text);
            }
        }
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        if (this.writing && active == this && this.text.length() < this.limit) {
            this.deleteSelectedText();
            String left = this.safeSubstring(this.text, 0, this.cursorPosition);
            String right = this.safeSubstring(this.text, this.cursorPosition);
            this.text = left + ch + right;
            this.callback.onChanged(this.text);
            ++this.cursorPosition;
            this.resetCursor();
            this.lastInputTime = System.currentTimeMillis();
        }
        super.chartyped(ch, keyCode);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.writing || active != this) {
            return;
        }
        if (UITextField.hasControlDown()) {
            switch (keyCode) {
                case 65: {
                    this.cursor = 0;
                    this.cursorPosition = this.selectionEnd = this.text.length();
                    break;
                }
                case 67: {
                    this.copy();
                    break;
                }
                case 86: {
                    this.paste();
                    break;
                }
                case 88: {
                    this.copy();
                    this.deleteSelectedText();
                    break;
                }
                case 259: 
                case 261: {
                    this.deleteAll();
                }
            }
        } else {
            switch (keyCode) {
                case 259: {
                    if (this.hasSelection()) {
                        this.deleteSelectedText();
                        break;
                    }
                    if (this.cursorPosition <= 0) break;
                    this.text = this.safeSubstring(this.text, 0, this.cursorPosition - 1) + this.safeSubstring(this.text, this.cursorPosition);
                    this.callback.onChanged(this.text);
                    --this.cursorPosition;
                    this.resetCursor();
                    break;
                }
                case 261: {
                    if (this.hasSelection()) {
                        this.deleteSelectedText();
                        break;
                    }
                    if (this.cursorPosition >= this.text.length()) break;
                    this.text = this.safeSubstring(this.text, 0, this.cursorPosition) + this.safeSubstring(this.text, this.cursorPosition + 1);
                    this.callback.onChanged(this.text);
                    this.resetCursor();
                    break;
                }
                case 262: 
                case 263: {
                    this.moveCursor(keyCode);
                    break;
                }
                case 256: 
                case 257: {
                    this.writing = false;
                    Client.CLICKGUI.WRITING = false;
                    this.resetCursor();
                    if (this.finishCallback == null) break;
                    this.finishCallback.onChanged(this.text);
                }
            }
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void release(int button) {
        this.dragging = false;
        super.release(button);
    }

    private void resetCursor() {
        this.cursor = -1;
        this.selectionEnd = -1;
    }

    private void moveCursor(int keyCode) {
        boolean shift = Screen.method_25442();
        if (keyCode == 263) {
            if (this.cursorPosition > 0) {
                --this.cursorPosition;
                if (shift) {
                    if (this.cursor == -1) {
                        this.cursor = this.cursorPosition + 1;
                    }
                    this.selectionEnd = this.cursorPosition;
                } else {
                    this.resetCursor();
                }
            } else if (!shift) {
                this.resetCursor();
            }
        } else if (keyCode == 262) {
            if (this.cursorPosition < this.text.length()) {
                ++this.cursorPosition;
                if (shift) {
                    if (this.cursor == -1) {
                        this.cursor = this.cursorPosition - 1;
                    }
                    this.selectionEnd = this.cursorPosition;
                } else {
                    this.resetCursor();
                }
            } else if (!shift) {
                this.resetCursor();
            }
        }
        this.updateScrollOffset();
        this.lastInputTime = System.currentTimeMillis();
    }

    private void deleteSelectedText() {
        if (this.hasSelection()) {
            int start = Math.min(this.cursor, this.selectionEnd);
            int end = Math.max(this.cursor, this.selectionEnd);
            this.replaceText(start, end, "");
        }
    }

    private void replaceText(int start, int end, String replacement) {
        start = Math.max(0, Math.min(start, this.text.length()));
        end = Math.max(start, Math.min(end, this.text.length()));
        this.text = this.safeSubstring(this.text, 0, start) + replacement + this.safeSubstring(this.text, end);
        this.callback.onChanged(this.text);
        this.cursorPosition = start + replacement.length();
        this.resetCursor();
        this.lastInputTime = System.currentTimeMillis();
    }

    private String safeSubstring(String str, int start) {
        return this.safeSubstring(str, start, str.length());
    }

    private void copy() {
        if (this.hasSelection()) {
            int start = Math.min(this.cursor, this.selectionEnd);
            int end = Math.max(this.cursor, this.selectionEnd);
            String selected = this.safeSubstring(this.text, start, end);
            this.mc.field_1774.method_1455(selected);
        } else {
            this.mc.field_1774.method_1455(this.text);
        }
    }

    private void paste() {
        String clip = this.mc.field_1774.method_1460();
        if (clip == null || clip.isEmpty()) {
            return;
        }
        if ((clip = clip.replaceAll("[^a-zA-Z0-9_]", "")).isEmpty()) {
            return;
        }
        this.deleteSelectedText();
        String left = this.safeSubstring(this.text, 0, this.cursorPosition);
        String right = this.safeSubstring(this.text, this.cursorPosition);
        this.text = left + clip + right;
        this.callback.onChanged(this.text);
        this.cursorPosition += clip.length();
        this.resetCursor();
        this.lastInputTime = System.currentTimeMillis();
    }

    private void deleteAll() {
        this.text = "";
        this.callback.onChanged(this.text);
        this.cursorPosition = 0;
        this.resetCursor();
    }

    public static boolean hasControlDown() {
        return InputUtil.method_15987((long)MinecraftClient.method_1551().method_22683().method_4490(), (int)341) || InputUtil.method_15987((long)MinecraftClient.method_1551().method_22683().method_4490(), (int)344);
    }

    public static boolean hasShiftDown() {
        return Screen.method_25442();
    }

    public static boolean hasAltDown() {
        return InputUtil.method_15987((long)MinecraftClient.method_1551().method_22683().method_4490(), (int)342) || InputUtil.method_15987((long)MinecraftClient.method_1551().method_22683().method_4490(), (int)346);
    }

    public boolean isWriting() {
        return this.writing;
    }

    public boolean isHoveredDesc() {
        return this.hoveredDesc;
    }

    public boolean isSas() {
        return this.sas;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getCursor() {
        return this.cursor;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public float getScrollOffset() {
        return this.scrollOffset;
    }

    public long getLastClickTime() {
        return this.lastClickTime;
    }

    public long getLastInputTime() {
        return this.lastInputTime;
    }

    public MinecraftClient getMc() {
        return this.mc;
    }

    public IconUse getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public String getDesc() {
        return this.desc;
    }

    public UICallback getCallback() {
        return this.callback;
    }

    public UICallback getFinishCallback() {
        return this.finishCallback;
    }

    public int getLastText() {
        return this.lastText;
    }

    public float getLastMouseX() {
        return this.lastMouseX;
    }

    public float getLastMouseY() {
        return this.lastMouseY;
    }

    public void setWriting(boolean writing) {
        this.writing = writing;
    }

    public void setHoveredDesc(boolean hoveredDesc) {
        this.hoveredDesc = hoveredDesc;
    }

    public void setSas(boolean sas) {
        this.sas = sas;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public void setScrollOffset(float scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public void setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
    }

    public void setLastInputTime(long lastInputTime) {
        this.lastInputTime = lastInputTime;
    }

    public void setIcon(IconUse icon) {
        this.icon = icon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCallback(UICallback callback) {
        this.callback = callback;
    }

    public void setFinishCallback(UICallback finishCallback) {
        this.finishCallback = finishCallback;
    }

    public void setLastText(int lastText) {
        this.lastText = lastText;
    }

    public void setLastMouseX(float lastMouseX) {
        this.lastMouseX = lastMouseX;
    }

    public void setLastMouseY(float lastMouseY) {
        this.lastMouseY = lastMouseY;
    }

    public String getHideMask() {
        return this.hideMask;
    }

    public void setHideMask(String hideMask) {
        this.hideMask = hideMask;
    }
}

