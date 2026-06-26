/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.base.screens.main.widgets;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.TextureUse;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class CustomButton
extends RendererObject {
    private final Runnable onPress;
    private final String text;
    private final String icon;
    private final Color iconcolor;
    private final Color textcolor;
    private final CustomButtonBuilder.ButtonType type;
    private Color hoverColor = new Color(150, 150, 150);
    private float anim = 0.0f;

    public CustomButton(Runnable onPress, String icon, String text, Color iconcolor, Color textcolor, CustomButtonBuilder.ButtonType type) {
        this.onPress = onPress;
        this.text = text;
        this.icon = icon;
        this.type = type;
        this.iconcolor = iconcolor;
        this.textcolor = textcolor;
        switch (type.ordinal()) {
            case 0: {
                this.hoverColor = ColorUtility.injectAlpha(ClientColors.MAIN_COLOR, 255.0f);
                break;
            }
            case 2: {
                this.hoverColor = new Color(255, 63, 63, 255);
                break;
            }
            case 1: {
                this.hoverColor = new Color(13, 54, 148, 255);
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.anim = MathUtility.fastAnim(this.anim, MathUtility.mouseIn(this.x, this.y, this.width, this.height, mouseX, mouseY) ? 1.0f : 0.0f, 10.0f);
        Color color5 = ColorUtility.injectAlpha(ClientColors.DISABLED, 4.0f);
        Color color6 = ColorUtility.injectAlpha(ClientColors.ENABLED, 20.0f);
        Color average = ColorUtility.linear(color5, color6, this.anim);
        Color color3 = ColorUtility.injectAlpha(ClientColors.DISABLED, 12.0f);
        Color color4 = ColorUtility.injectAlpha(ClientColors.ENABLED, 20.0f);
        Color average2 = ColorUtility.linear(color3, color4, this.anim);
        Color color1 = ColorUtility.injectAlpha(ClientColors.FORE_COLOR, 120.0f);
        Color color2 = ColorUtility.injectAlpha(ClientColors.ENABLED, 200.0f);
        Color average3 = ColorUtility.linear(color1, color2, this.anim);
        Vector4f round = new Vector4f(8.0f, 8.0f, 8.0f, 8.0f);
        Vector2f smooth = new Vector2f(1.0f, 1.0f);
        Client.RENDERER.blur(this.x, this.y, this.width, this.height, round, 16.0f, 1.0f);
        Client.RENDERER.outline(this.x, this.y, this.width, this.height, 0.0f, round, smooth, average, average, average, average);
        Client.RENDERER.textCentered(this.icon, this.x + this.width / 2.0f - 1.0f - Client.RENDERER.textWidth(this.text, TextureUse.SFMEDIUM, 9.0f) / 2.0f - 3.0f, this.y + this.height / 2.0f - 4.5f, TextureUse.ICONS, 9.0f, average3);
        Client.RENDERER.textCentered(this.text, this.x + this.width / 2.0f + 4.0f, this.y + this.height / 2.0f - 5.5f, TextureUse.SFMEDIUM, 9.0f, ColorUtility.injectAlpha(ClientColors.FORE_COLOR, 120.0f + 130.0f * this.anim));
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.onPress != null && MathUtility.mouseIn(this.text.equals("") ? this.x - 39.0f : this.x, this.text.equals("") ? this.y - 1.0f : this.y, this.width, this.height, mouseX, mouseY)) {
            this.onPress.run();
        }
        return super.click(mouseX, mouseY, button);
    }

    public static class CustomButtonBuilder {
        public static CustomButton build(float x, float y, float w, float h, String icon, String text, Color iconcolor, Color textcolor, ButtonType type, Runnable onPress) {
            CustomButton button = new CustomButton(onPress, icon, text, iconcolor, textcolor, type);
            button.x = x;
            button.y = y;
            button.width = w;
            button.height = h;
            return button;
        }

        public static enum ButtonType {
            MAIN,
            ALT,
            RED;

        }
    }
}

