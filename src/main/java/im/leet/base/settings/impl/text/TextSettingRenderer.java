/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 */
package im.leet.base.settings.impl.text;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.parts.impl.UITextField;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.text.TextSetting;
import im.leet.utils.client.ClientColors;
import net.minecraft.client.MinecraftClient;

public class TextSettingRenderer
extends SettingRenderer<TextSetting> {
    public static boolean hovered = false;
    private boolean hoveredDesc = false;
    private boolean sas = false;
    int MAX_SYMBOLS = 100;
    private int cursor = -1;
    private int selectionEnd = -1;
    private boolean dragging = false;
    private int cursorPosition = 0;
    private float scrollOffset = 0.0f;
    private long lastClickTime = 0L;
    private long lastInputTime = System.currentTimeMillis();
    private final MinecraftClient mc = MinecraftClient.method_1551();
    private UITextField textFieldObject = new UITextField();
    private boolean loaded = false;

    public TextSettingRenderer(TextSetting setting) {
        super(setting);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (!this.loaded) {
            this.textFieldObject.setText(((TextSetting)this.setting).getText());
            this.textFieldObject.setDesc(((TextSetting)this.setting).getDesc());
            this.textFieldObject.setCallback(((TextSetting)this.setting)::setText);
            this.textFieldObject.setHideMask(((TextSetting)this.setting).getHideMask());
            this.loaded = true;
        }
        this.height = Math.max(20.0f, 20.0f + this.drawDesc(Client.RENDERER.textLined(((TextSetting)this.setting).getName(), 20, this.x + 2.0f, this.y + 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR)));
        this.textFieldObject.bound(this.x + this.width / 2.0f, this.y + this.height / 2.0f - 6.5f, this.width / 2.0f, 15.0f).render(mouseX, mouseY);
        if (!this.textFieldObject.isWriting()) {
            this.textFieldObject.setText(((TextSetting)this.setting).getText());
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        return this.textFieldObject.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        this.textFieldObject.release(button);
        super.release(button);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        this.textFieldObject.chartyped(ch, keyCode);
        super.chartyped(ch, keyCode);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.textFieldObject.keyPressed(keyCode, scanCode, modifiers);
        super.keyPressed(keyCode, scanCode, modifiers);
    }
}

