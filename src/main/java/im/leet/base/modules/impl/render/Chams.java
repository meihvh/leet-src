/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package im.leet.base.modules.impl.render;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import net.minecraft.entity.Entity;

public class Chams
extends Module {
    public static final Chams INSTANCE = new Chams();

    private Chams() {
        super("Chams", Category.RENDER, "Draws players through walls", Tag.TEST);
    }

    public boolean shouldRender(Entity entity) {
        return true;
    }
}

