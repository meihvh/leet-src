/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector4f
 */
package im.leet.base.settings.impl.keybind;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.TextUtility;
import im.leet.utils.math.Rectangle;
import java.awt.Color;
import org.joml.Vector4f;

public class KeybindRenderer
extends SettingRenderer<KeybindSetting> {
    private boolean binding = false;
    Rectangle bindbound = new Rectangle();

    public KeybindRenderer(KeybindSetting setting) {
        super(setting);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = Math.max(20.0f, 20.0f + this.drawDesc(Client.RENDERER.textLined(((KeybindSetting)this.setting).getName(), 20, this.x + 2.0f, this.y + 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR)));
        String bind = TextUtility.keyToString(((KeybindSetting)this.setting).getBind());
        float bWidth = this.binding ? 30.0f : Math.max(14.0f, Client.RENDERER.textWidth(bind, TextureUse.SFMEDIUM, 8.0f)) + 6.0f;
        this.bindbound.bound(this.x + this.width - bWidth - 4.0f, this.y + 1.0f, bWidth, 15.0f);
        Client.RENDERER.drawModuleRect(this.bindbound);
        if (!this.binding) {
            Client.RENDERER.textCentered(bind, this.bindbound.getX() + this.bindbound.getWidth() / 2.0f - 1.0f, this.bindbound.getY() + 3.5f, TextureUse.SFMEDIUM, 6.0f, ClientColors.DARK_GRAY_COLOR);
        } else {
            float pad = 5.0f;
            for (int i = 0; i < 3; ++i) {
                float sin = (float)Math.sin((double)System.currentTimeMillis() / 300.0 - (double)i);
                float alpha = Client.RENDERER.getCrenderSystem().alpha();
                Client.RENDERER.getCrenderSystem().alpha(alpha * sin);
                Client.RENDERER.rect(this.bindbound.getX() + (float)i * pad + this.bindbound.getWidth() / 2.0f - pad * 3.0f / 2.0f + 0.5f, this.bindbound.getY() + this.bindbound.getHeight() / 2.0f - 2.0f, 4.0f, 4.0f, new Vector4f(3.0f), 1.0f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
                Client.RENDERER.getCrenderSystem().alpha(alpha);
            }
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.binding && button > 1) {
            ((KeybindSetting)this.setting).bind(button);
            this.binding = false;
            return true;
        }
        if (this.bindbound.hovered(mouseX, mouseY)) {
            this.binding = true;
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (this.binding) {
            if (keyCode == 256 || keyCode == 259 || keyCode == 261) {
                keyCode = -1;
            }
            ((KeybindSetting)this.setting).bind(keyCode);
            this.binding = false;
        }
    }

    public boolean isBinding() {
        return this.binding;
    }
}

