/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.settings;

public interface EnumChoice {
    default public String getLangClassName() {
        return this.getClass().getName();
    }

    default public String getRenderName() {
        return ((Enum)((Object)this)).name();
    }

    default public boolean isDefaultEnabled() {
        return false;
    }
}

