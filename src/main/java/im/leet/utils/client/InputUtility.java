/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.InputUtil
 */
package im.leet.utils.client;

import im.leet.MinecraftHolder;
import net.minecraft.client.util.InputUtil;

public final class InputUtility
implements MinecraftHolder {
    public static boolean isKeyPressed(int key) {
        return InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)key);
    }

    private InputUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

