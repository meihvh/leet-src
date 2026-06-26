/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector4f
 */
package im.leet.api.ui.parts.impl;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.base.settings.impl.range.FloatRange;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;

public class UIRange
extends RendererObject {
    public float min;
    public float max;
    public float increment;
    public Consumer<Float> setMin;
    public Consumer<Float> setMax;
    public Supplier<FloatRange> get;
    private float animation1;
    private float animation2;
    private boolean dragging;

    public UIRange(float min, float max, float increment, Consumer<Float> setMin, Consumer<Float> setMax, Supplier<FloatRange> get) {
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.setMin = setMin;
        this.setMax = setMax;
        this.get = get;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.dragging) {
            this.update(mouseX);
        }
        FloatRange value = this.get.get();
        float v1 = value.min;
        float v2 = value.max;
        float progress1 = MathHelper.method_15363((float)((v1 - this.min) / (this.max - this.min)), (float)0.0f, (float)1.0f);
        float progress2 = MathHelper.method_15363((float)((v2 - this.min) / (this.max - this.min)), (float)0.0f, (float)1.0f);
        this.animation1 = MathUtility.linearFps(this.animation1, progress1, 10.0f);
        this.animation2 = MathUtility.linearFps(this.animation2, progress2, 10.0f);
        Color body = ClientColors.DISABLED;
        Color progressColor1 = ClientSettings.INSTANCE.getColor(0);
        Color progressColor2 = ClientSettings.INSTANCE.getColor(90);
        float progressOffset = this.width * this.animation1;
        float progressWidth = this.width * (this.animation2 - this.animation1);
        Client.RENDERER.rect(this.x, this.y + this.height / 2.0f - 1.5f, this.width, 3.0f, new Vector4f(1.5f), 1.0f, body, body, body, body);
        Client.RENDERER.rect(this.x + progressOffset, this.y + this.height / 2.0f - 1.5f, progressWidth, 3.0f, new Vector4f(1.5f), 1.0f, progressColor1, progressColor1, progressColor2, progressColor2);
        Client.RENDERER.rect(this.x + progressOffset - 2.0f, this.y + this.height / 2.0f - 2.0f, 4.0f, 4.0f, new Vector4f(3.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.rect(this.x + this.width * this.animation2 - 2.0f, this.y + this.height / 2.0f - 2.0f, 4.0f, 4.0f, new Vector4f(3.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (MathUtility.mouseIn(this.x, this.y - this.height * 0.5f, this.width, this.height * 1.2f, mouseX, mouseY) && button == 0) {
            this.dragging = true;
            return true;
        }
        return super.click(mouseX, mouseY, button);
    }

    @Override
    public void release(int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }

    private void update(int mx) {
        float distMax;
        float distMin;
        float mouseX = mx;
        FloatRange current = this.get.get();
        float progress = MathUtility.clamp((mouseX - this.x) / this.width, 0.0f, 1.0f);
        float newValue = this.min + progress * (this.max - this.min);
        if (this.increment > 0.0f) {
            newValue = (float)Math.round(newValue / this.increment) * this.increment;
        }
        if ((distMin = Math.abs(newValue - current.min)) <= (distMax = Math.abs(newValue - current.max))) {
            this.setMin.accept(Float.valueOf(newValue));
        } else {
            this.setMax.accept(Float.valueOf(newValue));
        }
    }
}

