/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Language
 */
package im.leet.utils.lang;

import im.leet.base.settings.EnumChoice;
import net.minecraft.util.Language;

public final class LangUtility {
    public static String get(String key) {
        return Language.method_10517().method_48307(key);
    }

    public static String get(String key, String fallback) {
        return Language.method_10517().method_4679(key, fallback);
    }

    public static String getEnumChoiceName(Enum<?> value) {
        String string;
        if (value instanceof EnumChoice) {
            EnumChoice enumChoice = (EnumChoice)((Object)value);
            string = LangUtility.get("leet.enum." + enumChoice.getLangClassName() + "." + value.name(), enumChoice.getRenderName());
        } else {
            string = value.name();
        }
        return string;
    }

    private LangUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

