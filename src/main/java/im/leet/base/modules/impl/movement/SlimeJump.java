/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.movement;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.slider.SliderSetting;

public class SlimeJump
extends Module {
    public static final SlimeJump INSTANCE = new SlimeJump();
    public SliderSetting multiplier = this.add(new SliderSetting("Multiplier", 1.5f, 1.0f, 3.0f).increment(0.1f));

    private SlimeJump() {
        super("SlimeJump", Category.MOVEMENT, "Increases the bounce power of slime blocks", new Tag[0]);
    }
}

