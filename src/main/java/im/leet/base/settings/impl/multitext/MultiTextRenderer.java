/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.multitext;

import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.parts.impl.UITextField;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.multitext.MultiTextSetting;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import java.util.List;

public class MultiTextRenderer
extends SettingRenderer<MultiTextSetting> {
    Rectangle addRect = new Rectangle();
    UITextField[] fields = new UITextField[0];

    public MultiTextRenderer(MultiTextSetting setting) {
        super(setting);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float nameheight = Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f) + Client.RENDERER.textLined(((MultiTextSetting)this.setting).getName(), 25, this.x + 2.0f, this.y, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        this.addRect.bound(this.x, this.y + nameheight + 2.0f, this.width, 15.0f);
        Client.RENDERER.drawModuleRect(this.addRect);
        Client.RENDERER.text(IconUse.ADD.glyph, this.x + this.width / 2.0f - 3.0f, this.y + this.addRect.getHeight() / 2.0f + nameheight - 3.0f, TextureUse.ICONS, 9.0f, ClientColors.ICON_FOREGROUND_COLOR);
        float offsetY = nameheight + 2.0f + this.addRect.getHeight() + 2.0f;
        for (UITextField field : this.fields) {
            field.bound(this.x, this.y + offsetY, this.width - 14.0f, 15.0f).render(mouseX, mouseY);
            Client.RENDERER.text(IconUse.CROSS.glyph, this.x + this.width - 13.0f, this.y + offsetY, TextureUse.ICONS, 13.0f, ClientColors.ICON_FOREGROUND_COLOR);
            offsetY += 17.0f;
        }
        this.height = offsetY;
        List<String> strings = ((MultiTextSetting)this.setting).get();
        if (this.fields.length != strings.size()) {
            this.fields = new UITextField[strings.size()];
            int i = 0;
            while (i < this.fields.length) {
                UITextField field = this.fields[i] = new UITextField();
                field.setText(strings.get(i));
                int move_i = i++;
                field.setCallback(n -> strings.set(move_i, n));
            }
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.addRect.hovered(mouseX, mouseY)) {
            ((MultiTextSetting)this.setting).add();
            return true;
        }
        for (int i = 0; i < this.fields.length; ++i) {
            UITextField field = this.fields[i];
            if (MathUtility.mouseIn(this.x + this.width - 13.0f, field.getY() + 4.0f, 13.0f, 15.0f, mouseX, mouseY)) {
                ((MultiTextSetting)this.setting).remove(i);
                return true;
            }
            if (!field.click(mouseX, mouseY, button)) continue;
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        for (UITextField field : this.fields) {
            field.release(button);
        }
        super.release(button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (UITextField field : this.fields) {
            field.keyPressed(keyCode, scanCode, modifiers);
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (UITextField field : this.fields) {
            field.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }
}

