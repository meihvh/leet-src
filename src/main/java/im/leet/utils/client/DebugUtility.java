/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.client;

import im.leet.base.modules.impl.render.Interface;
import im.leet.utils.LogUtility;
import java.util.HashMap;
import java.util.Map;

public final class DebugUtility {
    public static final Map<String, Trace> traces = new HashMap<String, Trace>();

    public static void trace(String name, Object content) {
        if (!Interface.INSTANCE.isEnabled() || !Interface.INSTANCE.hudElements.get(Interface.HudElements.Debug)) {
            return;
        }
        LogUtility.debug(String.format("DebugUtility trace %s %s", name, content));
        traces.put(name, new Trace(content.toString(), System.currentTimeMillis()));
    }

    private DebugUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public record Trace(String content, long timestamp) {
    }
}

