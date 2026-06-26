/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.render;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.slider.SliderSetting;

public class AspectRatio
extends Module {
    public static final AspectRatio INSTANCE = new AspectRatio();
    public SliderSetting widthSlider = this.sliderSetting("Stretch", 1.0f, 0.2f, 2.0f).increment(0.01f);

    private AspectRatio() {
        super("AspectRatio", Category.RENDER, "Changes the stretch of the game", new Tag[0]);
    }
}

