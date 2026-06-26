/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.font;

import java.util.ArrayList;

public final class EmojiUtility {
    private static final ArrayList<Integer> cached = new ArrayList();

    public static boolean isEmoji(int code) {
        if (cached.contains(code)) {
            return true;
        }
        if (EmojiUtility.isIn(code, 127744, 128511) || EmojiUtility.isIn(code, 128512, 128591) || EmojiUtility.isIn(code, 128640, 128767) || EmojiUtility.isIn(code, 128768, 128895) || EmojiUtility.isIn(code, 128896, 129023) || EmojiUtility.isIn(code, 129024, 129279) || EmojiUtility.isIn(code, 129280, 129535) || EmojiUtility.isIn(code, 129648, 129791) || code == 11088) {
            cached.add(code);
            return true;
        }
        return false;
    }

    private static boolean isIn(int codePoint, int min, int max) {
        return codePoint >= min && codePoint <= max;
    }

    private EmojiUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

