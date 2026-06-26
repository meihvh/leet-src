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
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;

public class UISlider
extends RendererObject {
    public float min;
    public float max;
    private float animation;
    public Consumer<Float> callback;
    public Supplier<Float> get;
    public Supplier<Float> increment;
    private boolean dragging;

    public UISlider(Supplier<Float> get, float min, float max, Supplier<Float> increment, Consumer<Float> callback) {
        this.get = get;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.callback = callback;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.dragging) {
            this.updateValue(mouseX);
        }
        float value = this.get.get().floatValue();
        float progress = MathHelper.method_15363((float)((value - this.min) / (this.max - this.min)), (float)0.0f, (float)1.0f);
        if (this.animation == -999.0f) {
            this.animation = progress;
        }
        this.animation = MathUtility.linearFps(this.animation, progress, 10.0f);
        Color thumb = ClientColors.DISABLED;
        Color progressColor1 = ClientSettings.INSTANCE.getColor(0);
        Color progressColor2 = ClientSettings.INSTANCE.getColor(90);
        float progressWidth = this.width * this.animation;
        Client.RENDERER.rect(this.x, this.y + this.height / 2.0f - 1.5f, this.width, 3.0f, new Vector4f(1.5f), 1.0f, thumb, thumb, thumb, thumb);
        Client.RENDERER.rect(this.x, this.y + this.height / 2.0f - 1.5f, progressWidth, 3.0f, new Vector4f(1.5f), 1.0f, progressColor1, progressColor1, progressColor2, progressColor2);
        Client.RENDERER.rect(this.x + progressWidth - 3.0f, this.y + this.height / 2.0f - 2.0f, 4.0f, 4.0f, new Vector4f(3.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (MathUtility.mouseIn(this.x, this.y - this.height * 0.5f, this.width, this.height * 1.5f, mouseX, mouseY) && button == 0) {
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

    private void updateValue(int mouseX) {
        float sliderX = this.x + 4.0f;
        float sliderWidth = this.width - 8.0f;
        float progress = MathUtility.clamp(((float)mouseX - sliderX) / sliderWidth, 0.0f, 1.0f);
        float newValue = this.min + progress * (this.max - this.min);
        float increment = this.increment.get().floatValue();
        if (increment > 0.0f) {
            newValue = (float)Math.round(newValue / increment) * increment;
        }
        this.callback.accept(Float.valueOf(newValue));
    }
}

