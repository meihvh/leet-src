/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.themes;

import java.awt.Color;

public enum ThemePresets {
    DEFAULT("Default", new Color(201, 31, 210, 255), new Color(101, 0, 110, 255)),
    PURPLE("Purple", new Color(138, 43, 226), new Color(75, 0, 130)),
    OCEAN("Ocean", new Color(0, 119, 182), new Color(0, 180, 216)),
    FIRE("Fire", new Color(255, 99, 71), new Color(255, 69, 0)),
    NATURE("Nature", new Color(34, 139, 34), new Color(50, 205, 50)),
    CUSTOM("Custom", new Color(0), new Color(0));

    private final String name;
    private final Color main;
    private final Color secondary;

    private ThemePresets(String name, Color main, Color secondary) {
        this.name = name;
        this.main = main;
        this.secondary = secondary;
    }

    public String getName() {
        return this.name;
    }

    public Color getMain() {
        return this.main;
    }

    public Color getSecondary() {
        return this.secondary;
    }
}

