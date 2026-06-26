/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils;

import java.util.function.Supplier;

public record MultiBoolSupplier(Supplier<Boolean>[] suppliers) implements Supplier<Boolean>
{
    @Override
    public Boolean get() {
        for (Supplier<Boolean> s : this.suppliers) {
            if (s.get().booleanValue()) continue;
            return false;
        }
        return true;
    }
}

