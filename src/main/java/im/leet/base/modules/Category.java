/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules;

import im.leet.api.render.system.IconUse;
import im.leet.utils.math.Rectangle;

public enum Category {
    COMBAT("Combat", IconUse.FIGHT.glyph),
    MOVEMENT("Movement", IconUse.MOVEMENT.glyph),
    PLAYER("Player", IconUse.PLAYER.glyph),
    RENDER("Render", IconUse.RENDER.glyph),
    OTHER("Other", IconUse.GEAR.glyph);

    private final String name;
    private final String icon;
    public float animation;
    public final Rectangle rect = new Rectangle(0.0f, 0.0f, 0.0f, 0.0f);

    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public float getAnimation() {
        return this.animation;
    }

    public Rectangle getRect() {
        return this.rect;
    }

    private Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
}

