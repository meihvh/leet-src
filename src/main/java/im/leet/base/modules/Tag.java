/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules;

import java.awt.Color;

public enum Tag {
    RAGE("Rage", new Color(220, 20, 60)),
    TEST("Test", new Color(14, 72, 148)),
    EXPLOIT("Exploit", new Color(255, 128, 0)),
    DEV("Dev", new Color(113, 84, 255)),
    MATRIX("Matrix", new Color(0, 116, 255)),
    FUNTIME("Femtime", new Color(255, 0, 6)),
    HOLYWORLD("HolyWorld", new Color(0, 150, 255)),
    REALLYWORLD("ReallyWorld", new Color(255, 198, 0)),
    VANILLA("Vanilla", new Color(114, 114, 114)),
    v1_8("1.8", new Color(154, 136, 80));

    private final String name;
    private final Color color;

    private Tag(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }
}

