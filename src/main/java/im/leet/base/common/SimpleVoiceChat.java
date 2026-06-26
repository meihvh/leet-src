/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.common;

import im.leet.utils.LogUtility;
import java.util.UUID;

public final class SimpleVoiceChat {
    static boolean hooked = false;

    public static boolean isTalking(UUID uuid) {
        if (hooked) {
            try {
                Object instance = Class.forName("de.maxhenkel.voicechat.voice.client.ClientManager").getDeclaredMethod("instance", new Class[0]).invoke(null, new Object[0]);
                Object client = instance.getClass().getDeclaredMethod("getClient", new Class[0]).invoke(instance, new Object[0]);
                Object talkCache = client.getClass().getDeclaredMethod("getTalkCache", new Class[0]).invoke(client, new Object[0]);
                return (Boolean)talkCache.getClass().getDeclaredMethod("isTalking", UUID.class).invoke(talkCache, uuid);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private SimpleVoiceChat() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        try {
            Class.forName("de.maxhenkel.voicechat.voice.client.ClientManager");
            hooked = true;
            LogUtility.debug("SimpleVoiceChat compatibility registered");
        }
        catch (ClassNotFoundException e) {
            hooked = false;
        }
    }
}

