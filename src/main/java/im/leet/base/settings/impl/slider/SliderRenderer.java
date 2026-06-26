/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.slider;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.UIStyle;
import im.leet.api.ui.parts.impl.UISlider;
import im.leet.api.ui.parts.impl.UITextField;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ClientColors;

public class SliderRenderer
extends SettingRenderer<SliderSetting> {
    private final UITextField textField = new UITextField();
    private final UISlider slider;

    private void updateText() {
        if (((SliderSetting)this.setting).getIncrement() % 1.0f == 0.0f) {
            this.textField.setText(String.format("%.0f", Float.valueOf(((SliderSetting)this.setting).get())));
        } else {
            String s = String.valueOf(((SliderSetting)this.setting).getIncrement());
            int dot = s.indexOf(46);
            int decimals = s.length() - 1 - dot;
            this.textField.setText(String.format("%." + decimals + "f", Float.valueOf(((SliderSetting)this.setting).get())));
        }
        this.textField.style = UIStyle.TRANSPARENT;
    }

    public SliderRenderer(SliderSetting setting) {
        super(setting);
        this.slider = new UISlider(setting::get, setting.getMin(), setting.getMax(), setting::getIncrement, value -> {
            setting.setValue(value.floatValue());
            this.updateText();
        });
        this.textField.setCallback(s -> {
            try {
                setting.setValue(Float.parseFloat(s.replace(",", ".")));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        });
        this.textField.setFinishCallback(s -> this.updateText());
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = Math.max(16.0f, 16.0f + this.drawDesc(Client.RENDERER.textLined(((SliderSetting)this.setting).getName(), 20, this.x + 2.0f, this.y + 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR)));
        float nameWidth = Math.max(Client.RENDERER.textWidth(((SliderSetting)this.setting).getName(), TextureUse.SFMEDIUM, 8.0f), Client.RENDERER.wrappedTextWidth(((SliderSetting)this.setting).getDesc(), TextureUse.SFMEDIUM, 7.0f, 20));
        this.textField.bound(this.x + nameWidth + 3.0f, this.y + this.height / 2.0f - 4.0f, Math.max(13.0f, Client.RENDERER.textWidth(this.textField.getText(), TextureUse.SFMEDIUM, 8.0f) + 14.0f), 8.0f).render(mouseX, mouseY);
        float sw = Math.min(this.width / 2.0f, this.width - this.textField.getWidth() - nameWidth - 7.0f);
        this.slider.bound(this.x + (this.width - sw), this.y + this.height / 2.0f - 7.0f, sw, 15.0f).render(mouseX, mouseY);
        if (!this.textField.writing) {
            this.updateText();
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        return this.slider.click(mouseX, mouseY, button) || this.textField.click(mouseX, mouseY, button);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        this.textField.chartyped(ch, keyCode);
        super.chartyped(ch, keyCode);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.textField.keyPressed(keyCode, scanCode, modifiers);
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void release(int button) {
        this.slider.release(button);
        this.textField.release(button);
    }
}

