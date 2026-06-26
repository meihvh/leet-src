/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector4f
 */
package im.leet.base.settings.impl.checkbox;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.base.settings.SettingRenderer;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import org.joml.Vector4f;

public class CheckBoxRenderer
extends SettingRenderer<CheckBox> {
    public boolean noName = false;
    float anim1 = 0.0f;
    float anim2 = 0.0f;

    public CheckBoxRenderer(CheckBox setting) {
        super(setting);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.height = Math.max(15.0f, 15.0f + this.drawDesc(this.noName ? 0.0f : Client.RENDERER.textLined(((CheckBox)this.setting).getName(), 25, this.x + 2.0f, this.y + 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR)));
        if (!((CheckBox)this.setting).get()) {
            this.anim1 = MathUtility.linearFps(this.anim1, 0.0f, 10.0f);
            this.anim2 = (double)this.anim1 < 0.5 ? MathUtility.linearFps(this.anim2, 0.0f, 10.0f) : MathUtility.linearFps(this.anim2, 1.0f, 10.0f);
        } else {
            this.anim1 = MathUtility.linearFps(this.anim1, 1.0f, 5.0f);
            this.anim2 = (double)this.anim1 > 0.5 ? MathUtility.linearFps(this.anim2, 0.0f, 10.0f) : MathUtility.linearFps(this.anim2, 1.0f, 10.0f);
        }
        Color thumbColor = ColorUtility.linear(ClientColors.DARK_GRAY_COLOR, ClientColors.GREEN, this.anim1);
        Color thumbBackColor = ClientColors.GUI_STROKE;
        Client.RENDERER.rect(this.x + this.width - 25.0f, this.y + this.height / 2.0f - 4.5f, 20.0f, 11.0f, new Vector4f(9.0f), 1.0f, thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
        Client.RENDERER.rect(this.x + this.width - 23.0f + 8.0f * this.anim1, this.y + this.height / 2.0f - 3.0f, 8.0f + 8.0f * this.anim2, 8.0f, new Vector4f(6.0f), 2.0f, thumbColor, thumbColor, thumbColor, thumbColor);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY) && button == 0) {
            ((CheckBox)this.setting).set(!((CheckBox)this.setting).get());
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }
}

