/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.api.ui.UIWidget;
import im.leet.base.settings.impl.point.PointRenderer;
import im.leet.base.settings.impl.point.PointSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class PointSettingWidget
extends UIWidget {
    private PointRenderer renderer;
    boolean dragging = false;
    float lerpX = -999.0f;
    float lerpY = -999.0f;
    public boolean expanded = false;

    public PointSettingWidget(PointRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float pAnim = 1.0f - this.animation.getOutput();
        float alpha = Client.RENDERER.getCrenderSystem().alpha();
        Client.RENDERER.getCrenderSystem().alpha(pAnim * alpha);
        Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f(6.0f), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
        Client.RENDERER.outline(this.x, this.y, this.width, this.height, 0.0f, new Vector4f(6.0f), new Vector2f(1.0f), ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.rect(this.x + this.width / 2.0f, this.y + 0.5f, 1.0f, this.height - 1.0f, new Vector4f(0.0f), 0.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.rect(this.x + 0.5f, this.y + this.height / 2.0f, this.height - 1.0f, 1.0f, new Vector4f(0.0f), 0.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        Color themeColor = ClientSettings.INSTANCE.getColor(0);
        Client.RENDERER.rect(this.x + this.width / 2.0f + this.width / 2.0f * this.lerpX - 3.5f, this.y + this.height / 2.0f + this.height / 2.0f * this.lerpY - 3.5f, 8.0f, 8.0f, new Vector4f(7.0f), 1.0f, themeColor, themeColor, themeColor, themeColor);
        Vector2f vec = ((PointSetting)this.renderer.getSetting()).get();
        float dx = MathHelper.method_15363((float)(((float)mouseX - (this.x + this.width / 2.0f)) / (this.width / 2.0f)), (float)-1.0f, (float)1.0f);
        float dy = MathHelper.method_15363((float)(((float)mouseY - (this.y + this.height / 2.0f)) / (this.height / 2.0f)), (float)-1.0f, (float)1.0f);
        if (this.lerpX == -999.0f || this.lerpY == -999.0f) {
            this.lerpX = dx;
            this.lerpY = dy;
        }
        if (this.dragging) {
            this.lerpX = MathUtility.linearFps(this.lerpX, dx, 15.0f);
            this.lerpY = MathUtility.linearFps(this.lerpY, dy, 15.0f);
            vec.x = this.lerpX;
            vec.y = this.lerpY;
        }
        Client.RENDERER.getCrenderSystem().alpha(alpha);
        if ((double)pAnim < 0.1) {
            this.shouldRemove = true;
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.hover(mouseX, mouseY)) {
            if (button == 0) {
                this.dragging = true;
            } else {
                this.lerpX = 0.0f;
                this.lerpY = 0.0f;
                ((PointSetting)this.renderer.getSetting()).set(new Vector2f());
            }
            return true;
        }
        if (button < 2) {
            this.animation.setDirection(Direction.FORWARDS);
            return false;
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        this.dragging = false;
        super.release(button);
    }
}

