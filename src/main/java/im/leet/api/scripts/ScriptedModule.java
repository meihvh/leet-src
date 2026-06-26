/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.scripts;

import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;

public class ScriptedModule
extends Module {
    private String[] authors;

    public ScriptedModule(String name, Category category, int key, String[] authors, String desc) {
        super(name.concat(".js"), category, desc, new Tag[0]);
        this.setKey(key);
        this.authors = authors;
    }

    public String[] getAuthors() {
        return this.authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }
}

