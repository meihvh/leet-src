/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils;

import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.text.TextSetting;

public class ClientSpoof
extends Group {
    public static ClientSpoof INSTANCE = new ClientSpoof();
    public CheckBox clientSpoof;
    public EnumSetting<Mode> type;
    public TextSetting customName;

    private ClientSpoof() {
        super("Client Spoof", false);
        this.clientSpoof = this.enabled;
        this.type = this.enumSetting("Client Mode", Mode.LUNAR);
        this.customName = (TextSetting)this.text("Client Name", "1337").visible(() -> this.type.is(Mode.CUSTOM));
    }

    public String getClientName() {
        if (this.type.is(Mode.CUSTOM)) {
            return this.customName.getText();
        }
        return this.type.get().text;
    }

    public static enum Mode implements EnumChoice
    {
        LUNAR("Lunar 1.21.4", "lunarclient:1.21.4"),
        LABYMOD("LabyMod4 1.21.4", "labymod4:1.21.4"),
        DEFAULT("Vanilla", "vanilla"),
        CUSTOM("Custom", "");

        final String renderName;
        final String text;

        private Mode(String renderName, String text) {
            this.renderName = renderName;
            this.text = text;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }

        public String getText() {
            return this.text;
        }
    }
}

