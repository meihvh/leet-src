/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings.impl.range;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.UIStyle;
import im.leet.api.ui.parts.impl.UIRange;
import im.leet.api.ui.parts.impl.UITextField;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.range.RangeSetting;
import im.leet.utils.client.ClientColors;

public class RangeRenderer
extends SettingRenderer<RangeSetting> {
    private final UITextField textMin = new UITextField();
    private final UITextField textMax = new UITextField();
    private final UIRange range;

    public RangeRenderer(RangeSetting setting) {
        super(setting);
        this.range = new UIRange(setting.minimumValue, setting.maximumValue, setting.increment, setting::setMin, setting::setMax, setting::get);
        this.textMin.setCallback(s -> {
            try {
                setting.setMin(Float.parseFloat(s.replace(",", ".")));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        });
        this.textMin.setFinishCallback(s -> this.updateText(this.textMin, setting.getMin()));
        this.textMax.setCallback(s -> {
            try {
                setting.setMax(Float.parseFloat(s.replace(",", ".")));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        });
        this.textMax.setFinishCallback(s -> this.updateText(this.textMax, setting.getMax()));
    }

    private void updateText(UITextField textField, float v) {
        if (((RangeSetting)this.setting).increment % 1.0f == 0.0f) {
            textField.setText(String.format("%.0f", Float.valueOf(v)));
        } else {
            String s = String.valueOf(((RangeSetting)this.setting).increment);
            int dot = s.indexOf(46);
            int decimals = s.length() - 1 - dot;
            textField.setText(String.format("%." + decimals + "f", Float.valueOf(v)));
        }
        textField.style = UIStyle.TRANSPARENT;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = Math.max(12.0f, 12.0f + this.drawDesc(12.0f, 8.0f, 40));
        Client.RENDERER.text(((RangeSetting)this.setting).getName(), this.x + 2.0f, this.y + 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        float textMinWidth = Math.max(Client.RENDERER.textWidth(this.textMin.getText(), TextureUse.SFMEDIUM, 8.0f), 12.0f);
        float textMaxWidth = Math.max(Client.RENDERER.textWidth(this.textMax.getText(), TextureUse.SFMEDIUM, 8.0f), 12.0f);
        float textStart = this.width - textMaxWidth - 26.0f - textMinWidth - 12.0f;
        this.textMin.bound(this.x + textStart, this.y + 2.0f, Math.max(26.0f, textMinWidth + 12.0f), 15.0f).render(mouseX, mouseY);
        Client.RENDERER.text("\u2014", this.x + textStart + textMinWidth + 14.0f, this.y + 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        this.textMax.bound(this.x + textStart + textMaxWidth + 26.0f, this.y + 2.0f, Math.max(26.0f, textMaxWidth + 12.0f), 15.0f).render(mouseX, mouseY);
        this.range.bound(this.x + 3.0f, this.y + 10.0f, this.width - 10.0f, 15.0f).render(mouseX, mouseY);
        if (!this.textMin.writing) {
            this.updateText(this.textMin, ((RangeSetting)this.setting).getMin());
        }
        if (!this.textMax.writing) {
            this.updateText(this.textMax, ((RangeSetting)this.setting).getMax());
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        return this.range.click(mouseX, mouseY, button) || this.textMin.click(mouseX, mouseY, button) || this.textMax.click(mouseX, mouseY, button);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        this.textMin.chartyped(ch, keyCode);
        this.textMax.chartyped(ch, keyCode);
        super.chartyped(ch, keyCode);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        this.textMin.keyPressed(keyCode, scanCode, modifiers);
        this.textMax.keyPressed(keyCode, scanCode, modifiers);
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void release(int button) {
        this.range.release(button);
        this.textMin.release(button);
        this.textMax.release(button);
    }
}

