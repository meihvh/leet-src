/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.macros.constructor;

import im.leet.base.settings.Setting;
import java.util.ArrayList;

public interface MacroBlock {
    default public ArrayList<Setting<?>> getSettings() {
        return new ArrayList();
    }

    public void execute();

    public String renderName();
}

