/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.render;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.hud.ui.impl.ArrowsElement;
import im.leet.base.hud.ui.impl.BpsElement;
import im.leet.base.hud.ui.impl.CompassElement;
import im.leet.base.hud.ui.impl.DebugElement;
import im.leet.base.hud.ui.impl.EffectsElement;
import im.leet.base.hud.ui.impl.HotbarElement;
import im.leet.base.hud.ui.impl.KeybindsElement;
import im.leet.base.hud.ui.impl.MinecraftUI;
import im.leet.base.hud.ui.impl.NotificationElement;
import im.leet.base.hud.ui.impl.StaffElement;
import im.leet.base.hud.ui.impl.TargetHudElement;
import im.leet.base.hud.ui.impl.TpsElement;
import im.leet.base.hud.ui.impl.WatermarkElement;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;

public class Interface
extends Module {
    public static final Interface INSTANCE = new Interface();
    public final CheckBox blur = this.checkbox("Blur", false);
    public final CheckBox arrows3d = this.checkbox("Arrows 3D", false);
    public MultiEnumSetting<HudElements> hudElements = this.add(new MultiEnumSetting<HudElements>("Hud elements", HudElements.class));

    private Interface() {
        super("Interface", Category.RENDER, "Managing HUD elements", new Tag[0]);
        for (HudElements element : HudElements.values()) {
            Client.HUD.register(element.getElement());
            this.hudElements.onChange((mode, value1) -> HudElements.values()[mode.ordinal()].getElement().setEnabled(value1));
        }
    }

    public static enum HudElements implements EnumChoice
    {
        Watermark("Watermark", true, new WatermarkElement(new Drag("Watermark", () -> true).bound(10.0f, 10.0f, 100.0f, 20.0f))),
        Keybinds("Hotkeys", true, new KeybindsElement(new Drag("KeyBinds", () -> true).bound(10.0f, 35.0f, 100.0f, 20.0f))),
        Bps("BPS", true, new BpsElement(new Drag("Bps", () -> true).bound(5.0f, 85.0f, 45.0f, 20.0f))),
        Targethud("TargetHud", true, new TargetHudElement(new Drag("TargetHud", () -> true).bound(5.0f, 110.0f, 100.0f, 40.0f))),
        Compass("Compass", false, new CompassElement(new Drag("Compass", () -> true).bound(5.0f, 150.0f, 100.0f, 100.0f))),
        Hotbar("Hotbar", false, HotbarElement.INSTANCE),
        Minecraftui("Minecraft UI", false, MinecraftUI.INSTANCE),
        Arrows("Arrows", true, new ArrowsElement("Arrows", null)),
        Effects("Effects", true, EffectsElement.INSTANCE),
        TPS("TPS", true, new TpsElement(new Drag("TPS", () -> true).bound(5.0f, 150.0f, 45.0f, 20.0f))),
        Notify("Notifications", true, new NotificationElement(new Drag("Notify", () -> true).bound(5.0f, 170.0f, 45.0f, 20.0f))),
        StaffList("Staff list", true, new StaffElement(new Drag("Staff", () -> true).bound(5.0f, 170.0f, 100.0f, 20.0f))),
        Debug("Debug", false, new DebugElement(new Drag("Debug", () -> true).bound(5.0f, 170.0f, 200.0f, 100.0f)));

        final String renderName;
        final boolean defaultEnabled;
        final HudElement element;

        private HudElements(String renderName, boolean defaultEnabled, HudElement element) {
            this.renderName = renderName;
            this.defaultEnabled = defaultEnabled;
            this.element = element;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }

        @Override
        public boolean isDefaultEnabled() {
            return this.defaultEnabled;
        }

        public HudElement getElement() {
            return this.element;
        }
    }
}

