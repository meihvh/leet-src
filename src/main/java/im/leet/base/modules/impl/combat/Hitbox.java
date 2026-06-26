/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.combat;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.slider.SliderSetting;

public class Hitbox
extends Module {
    public static final Hitbox INSTANCE = new Hitbox();
    public final SliderSetting expand = this.sliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440", 0.2f, 0.0f, 2.0f).increment(0.05f);
    public final CheckBox ignFr = this.checkbox("\u0418\u0433\u043d\u043e\u0440 \u0434\u0440\u0443\u0437\u0435\u0439", true);

    private Hitbox() {
        super("Hitbox", Category.COMBAT, "Increases players' hitboxes", new Tag[0]);
    }
}

