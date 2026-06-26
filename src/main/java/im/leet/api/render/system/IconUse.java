/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.render.system;

public enum IconUse {
    LOGO("a"),
    FIGHT("b"),
    MOVEMENT("c"),
    RENDER("d"),
    PLAYER("e"),
    MISC("f"),
    SCRIPT("g"),
    SEARCH("h"),
    CHECK("i"),
    DOWN("j"),
    UP("k"),
    CUBE("l"),
    GLOBE("m"),
    PERSONS("n"),
    GEAR("o"),
    EXIT("p"),
    ADD("q"),
    REFRESH("r"),
    MICROSOFT("s"),
    STAR("t"),
    CROSS("u"),
    HOME("v"),
    KEYBOARD("w"),
    COMPASS("x"),
    BACK("z"),
    INFO("A"),
    WARN("B"),
    POTION("C"),
    CLOCK("D"),
    SPUTNIK("E"),
    GROUP("F"),
    LINK("y");

    public final String glyph;

    private IconUse(String glyph) {
        this.glyph = glyph;
    }
}

