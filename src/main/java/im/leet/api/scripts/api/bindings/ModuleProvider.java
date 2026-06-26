/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.scripts.api.bindings;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;

public class ModuleProvider {
    private Module module;

    public ModuleProvider(Module module) {
        this.module = module;
    }

    public String getName() {
        return this.module.getName();
    }

    public int getKey() {
        return this.module.getKey();
    }

    public Category getCategory() {
        return this.module.getCategory();
    }

    public boolean isEnabled() {
        return this.module.isEnabled();
    }
}

