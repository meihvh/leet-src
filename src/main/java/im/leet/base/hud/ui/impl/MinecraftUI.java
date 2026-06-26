/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.client.ClientColors;
import java.awt.Color;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MinecraftUI
extends HudElement {
    public static final MinecraftUI INSTANCE = new MinecraftUI();
    static final float round = 13.0f;
    static final float smooth = 1.0f;
    static final float thickness = 1.0f;
    static final float roundSmall = 7.0f;
    static final float alpha = 200.0f;

    private MinecraftUI() {
        super("Minecraft UI", null);
    }

    @Override
    public void render(int mouseX, int mouseY) {
    }

    public static void renderButton(float x, float y, float width, float height, boolean enabled, boolean hover, Text message) {
        CRenderSystem.RenderLayer layer = Client.RENDERER.getCrenderSystem().layer();
        Client.RENDERER.getCrenderSystem().layerTex(Client.RENDERER.getCrenderSystem().getAtlas().getGlId()).layer(CRenderSystem.RenderLayer.OVERLAY);
        Client.RENDERER.getCrenderSystem().push(x, y, width, height);
        Color bg = ClientColors.BACK_COLOR;
        if (!enabled) {
            bg = ClientColors.DISABLED;
        }
        if (hover && enabled) {
            Color outline = ClientColors.FORE_COLOR;
            Client.RENDERER.outline(x, y, width, height, 1.0f, new Vector4f(13.0f), new Vector2f(1.0f), outline, outline, outline, outline);
        }
        Client.RENDERER.rect(x, y, width, height, new Vector4f(13.0f), 1.0f, bg, bg, bg, bg);
        if (message != null) {
            float textWidth = Client.RENDERER.textWidth(message.getString(), TextureUse.SFMEDIUM, 9.0f);
            float textHeight = Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 9.0f);
            Client.RENDERER.text(message, x + MinecraftUI.scrollText(width, textWidth) + width / 2.0f - Math.min(width, textWidth) / 2.0f, y + height / 2.0f - textHeight / 2.0f, TextureUse.SFMEDIUM, 9.0f);
        }
        Client.RENDERER.getCrenderSystem().pop();
        Client.RENDERER.getCrenderSystem().layer(layer);
    }

    public static float scrollText(float width, float textWidth) {
        if (textWidth > width) {
            return MathHelper.method_15363((float)((float)Math.sin((double)System.currentTimeMillis() / 10000.0) * textWidth), (float)(-textWidth * 0.8f), (float)10.0f);
        }
        return 0.0f;
    }

    public static void renderSlider(float x, float y, float width, float height, boolean enabled, boolean hover, double value, Text message) {
        CRenderSystem.RenderLayer layer = Client.RENDERER.getCrenderSystem().layer();
        Client.RENDERER.getCrenderSystem().layer(CRenderSystem.RenderLayer.OVERLAY);
        Color bg = ClientColors.BACK_COLOR;
        if (!enabled) {
            bg = ClientColors.DISABLED;
        }
        if (hover && enabled) {
            Color outline = ClientColors.FORE_COLOR;
            Client.RENDERER.outline(x, y, width, height, 1.0f, new Vector4f(13.0f), new Vector2f(1.0f), outline, outline, outline, outline);
        }
        Client.RENDERER.rect(x, y, width, height, new Vector4f(13.0f), 1.0f, bg, bg, bg, bg);
        float vx = (float)value * (width - 8.0f);
        Color outline = ClientColors.DARK_GRAY_COLOR;
        Client.RENDERER.rect(x + vx, y + height / 2.0f - 4.0f, 8.0f, 8.0f, new Vector4f(7.0f), 1.0f, outline, outline, outline, outline);
        if (message != null) {
            float textWidth = Client.RENDERER.textWidth(message.getString(), TextureUse.SFMEDIUM, 9.0f);
            float textHeight = Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 9.0f);
            Client.RENDERER.text(message, x + MinecraftUI.scrollText(width, textWidth) + width / 2.0f - Math.min(width, textWidth) / 2.0f, y + height / 2.0f - textHeight / 2.0f, TextureUse.SFMEDIUM, 9.0f);
        }
        Client.RENDERER.getCrenderSystem().layer(layer);
    }
}

