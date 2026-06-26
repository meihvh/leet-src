/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.player;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.slider.SliderSetting;

public class ClientSounds
extends Module {
    public static final ClientSounds INSTANCE = new ClientSounds();
    public CheckBox toggleFunction = this.checkbox("Module sound", true);
    public SliderSetting volume = this.sliderSetting("Volume", 0.5f, 0.1f, 2.0f).increment(0.1f);

    private ClientSounds() {
        super("Client sounds", Category.PLAYER, "", new Tag[0]);
    }
}

