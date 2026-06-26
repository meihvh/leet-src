/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.other;

import im.leet.Client;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;

public class RTXSounds
extends Module {
    public static final RTXSounds INSTANCE = new RTXSounds();
    public final EnumSetting<SoundMode> performancePriority = this.enumSetting("Sound quality", SoundMode.Performance);
    public final CheckBox betterStereo = this.checkbox("3d stereo", true);
    public final CheckBox toneCompensation = this.checkbox("Tone compensation", true);

    private RTXSounds() {
        super("RTX Sounds", Category.RENDER, "Incredibly improves game immersion by enhancing sound quality", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        if (RTXSounds.mc.field_1724 != null && Client.INITIALIZED) {
            Client.RTX_ENGINE.updateMixer();
        }
    }

    @Override
    protected void onDisable() {
        if (Client.INITIALIZED) {
            Client.RTX_ENGINE.setState(false);
        }
    }

    public static enum SoundMode implements EnumChoice
    {
        Performance("Performance"),
        Quality("Quality");

        final String renderName;

        private SoundMode(String renderName) {
            this.renderName = renderName;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }
}

