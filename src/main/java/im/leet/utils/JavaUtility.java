/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils;

import im.leet.utils.LogUtility;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class JavaUtility {
    public static <T> void sortedInsert(List<T> list, T item, Comparator<T> cmp) {
        int index = Collections.binarySearch(list, item, cmp);
        if (index < 0) {
            index ^= 0xFFFFFFFF;
        }
        list.add(index, item);
    }

    public static <T> T join(Future<T> future) {
        try {
            return future.get();
        }
        catch (InterruptedException | ExecutionException e) {
            LogUtility.error(e, "joining future");
            return null;
        }
    }

    public static void joinAll(CompletableFuture<?> ... futures) {
        JavaUtility.join(CompletableFuture.allOf(futures));
    }

    private JavaUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

