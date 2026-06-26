/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.client;

import im.leet.api.themes.ThemePresets;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.ClientSpoof;
import im.leet.utils.TargetValidator;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.targetesp.TargetRenderer;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.network.ProxyUtility;
import java.awt.Color;

public final class ClientSettings
extends Group {
    public static final ClientSettings INSTANCE = new ClientSettings();
    public final KeybindSetting clickguiBind = this.keybindSetting("ClickGUI", 344);
    public final SliderSetting clickguiHeight = this.sliderSetting("ClickGUI height", 300.0f, 300.0f, 960.0f);
    public final CheckBox cloud = this.checkbox("Cloud", false);
    public final ClientSpoof clientSpoof = this.add(ClientSpoof.INSTANCE);
    public final TargetValidator targetValidator = this.add(new TargetValidator());
    public final ProxyUtility proxy = this.add(new ProxyUtility());
    public final TargetRenderer targetRenderer = this.add(new TargetRenderer());
    private final Group theme = this.group("Theme");
    public final EnumSetting<ColorType> colorType = this.theme.enumSetting("Color Type", ColorType.Gradient);
    public final SliderSetting colorSpeed = (SliderSetting)this.theme.sliderSetting("Color Speed", 9.0f, 1.0f, 30.0f).increment(0.5f).visible(() -> this.colorType.isAny(new ColorType[]{ColorType.Gradient, ColorType.Rainbow}));
    public final EnumSetting<ThemePresets> preset = (EnumSetting)((EnumSetting)this.theme.enumSetting("Preset", ThemePresets.DEFAULT).onChanged(this::applyPreset)).visible(() -> this.colorType.isAny(new ColorType[]{ColorType.Gradient, ColorType.Single}));
    private final ColorSetting mainColor = (ColorSetting)((ColorSetting)this.theme.colorSetting("Main Color", new Color(201, 31, 210, 255)).onChanged(c -> {
        ClientColors.MAIN_COLOR = c;
    })).visible(() -> this.preset.is(ThemePresets.CUSTOM) && this.colorType.isAny(new ColorType[]{ColorType.Gradient, ColorType.Single}));
    private final ColorSetting secondaryColor = (ColorSetting)((ColorSetting)this.theme.colorSetting("Secondary Color", new Color(101, 0, 110, 255)).onChanged(c -> {
        ClientColors.SECONDARY_COLOR = c;
    })).visible(() -> this.preset.is(ThemePresets.CUSTOM) && this.colorType.is(ColorType.Gradient));
    private final ColorSetting bgColor = (ColorSetting)this.theme.colorSetting("Background Color", new Color(12, 12, 12, 255)).onChanged(c -> {
        ClientColors.BACK_COLOR = c;
    });
    private final ColorSetting darkGrayColor = (ColorSetting)this.theme.colorSetting("Dark Gray Color", new Color(68, 68, 68, 255)).onChanged(c -> {
        ClientColors.DARK_GRAY_COLOR = c;
    });
    private final ColorSetting fgColor = (ColorSetting)this.theme.colorSetting("Foreground Color", new Color(255, 255, 255, 255)).onChanged(c -> {
        ClientColors.FORE_COLOR = c;
    });
    private final ColorSetting secondaryFgColor = (ColorSetting)this.theme.colorSetting("Secondary Foreground Color", new Color(255, 255, 255, 255)).onChanged(c -> {
        ClientColors.SECONDARY_FORE_COLOR = c;
    });
    private final ColorSetting enabledColor = (ColorSetting)this.theme.colorSetting("Enabled Color", new Color(185, 221, 216, 255)).onChanged(c -> {
        ClientColors.ENABLED = c;
    });
    private final ColorSetting disabledColor = (ColorSetting)this.theme.colorSetting("Disabled Color", new Color(36, 36, 36, 255)).onChanged(c -> {
        ClientColors.DISABLED = c;
    });
    private final ColorSetting guiBackgroundColor = (ColorSetting)this.theme.colorSetting("Gui Background Color", new Color(15, 15, 15, 255)).onChanged(c -> {
        ClientColors.GUI_BACKGROUND = c;
    });
    private final ColorSetting uiRedColor = (ColorSetting)this.theme.colorSetting("UI Red Color", new Color(255, 55, 55, 255)).onChanged(c -> {
        ClientColors.UI_RED = c;
    });
    private final ColorSetting friendColor = (ColorSetting)this.theme.colorSetting("Friend Color", new Color(50, 155, 50, 255)).onChanged(c -> {
        ClientColors.FRIEND_COLOR = c;
    });
    private final ColorSetting guiStrokeColor = (ColorSetting)this.theme.colorSetting("GUI Stroke Color", new Color(255, 255, 255, 10)).onChanged(c -> {
        ClientColors.GUI_STROKE = c;
    });
    private final ColorSetting goldenHpColor = (ColorSetting)this.theme.colorSetting("Golden HP Color", new Color(222, 177, 27, 255)).onChanged(c -> {
        ClientColors.GOLDEN_HP = c;
    });
    private final ColorSetting redColor = (ColorSetting)this.theme.colorSetting("Red Color", new Color(183, 0, 0, 255)).onChanged(c -> {
        ClientColors.RED = c;
    });
    private final ColorSetting iconForegroundColor = (ColorSetting)this.theme.colorSetting("Icon Foreground Color", new Color(255, 255, 255, 255)).onChanged(c -> {
        ClientColors.ICON_FOREGROUND_COLOR = c;
    });
    private final ColorSetting healthColor = (ColorSetting)this.theme.colorSetting("Health Color", new Color(183, 0, 0, 255)).onChanged(c -> {
        ClientColors.HEALTH_COLOR = c;
    });
    private final ColorSetting hungerColor = (ColorSetting)this.theme.colorSetting("Hunger Color", new Color(165, 105, 80, 255)).onChanged(c -> {
        ClientColors.HUNGER_COLOR = c;
    });
    private final ColorSetting armorColor = (ColorSetting)this.theme.colorSetting("Armor Color", new Color(100, 100, 100, 255)).onChanged(c -> {
        ClientColors.ARMOR_COLOR = c;
    });
    private final ColorSetting airColor = (ColorSetting)this.theme.colorSetting("Air Color", new Color(10, 100, 255, 255)).onChanged(c -> {
        ClientColors.AIR_COLOR = c;
    });

    private ClientSettings() {
        super("ClientSettings");
    }

    @Override
    public String getLangKey() {
        return "leet.ClientSettings";
    }

    private void applyPreset(ThemePresets preset) {
        if (preset == ThemePresets.CUSTOM) {
            ClientColors.MAIN_COLOR = this.mainColor.get();
            ClientColors.SECONDARY_COLOR = this.secondaryColor.get();
        } else {
            ClientColors.MAIN_COLOR = preset.getMain();
            ClientColors.SECONDARY_COLOR = preset.getSecondary();
        }
    }

    public Color getColor(int index) {
        return switch (this.colorType.get().ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> ClientColors.MAIN_COLOR;
            case 1 -> ColorUtility.transfusionEffect(this.colorSpeed.getInt(), index, ClientColors.MAIN_COLOR, ClientColors.SECONDARY_COLOR);
            case 2 -> ColorUtility.rainbowEffect(this.colorSpeed.getInt(), index);
        };
    }

    public Color getColorBright(int index) {
        return switch (this.colorType.get().ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> ClientColors.MAIN_COLOR;
            case 1 -> ColorUtility.transfusionEffect(this.colorSpeed.getInt(), index, ClientColors.MAIN_COLOR, ClientColors.SECONDARY_COLOR);
            case 2 -> ColorUtility.rainbowEffectBright(this.colorSpeed.getInt(), index);
        };
    }

    public static enum ColorType {
        Single,
        Gradient,
        Rainbow;

    }
}

