/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfilerUtility {
    static final Map<String, Long> times = new HashMap<String, Long>();
    static final Map<String, Long> results = new HashMap<String, Long>();

    public static void push(String name) {
        times.put(name, System.nanoTime());
    }

    public static void pop(String name) {
        results.put(name, (System.nanoTime() - times.get(name)) / 1000000L);
    }

    public static long get(String name) {
        return results.getOrDefault(name, -1L);
    }

    public static Set<Map.Entry<String, Long>> getAll() {
        return results.entrySet();
    }
}

