/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventPriority;
import im.leet.utils.JavaUtility;
import im.leet.utils.LogUtility;
import im.leet.utils.math.ProfilerUtility;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Events {
    public static final CopyOnWriteArrayList<Invoker> events = new CopyOnWriteArrayList();

    public void register(Object owner) {
        for (Method method : owner.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0]) || !Void.TYPE.isAssignableFrom(method.getReturnType())) continue;
            EventPriority eventPriority = method.getAnnotation(EventPriority.class);
            int priority = eventPriority != null ? eventPriority.value() : 0;
            method.setAccessible(true);
            JavaUtility.sortedInsert(events, new Invoker(method, method.getParameterTypes()[0], owner, priority), Comparator.comparingInt(it -> -it.priority));
            LogUtility.debug("registered event: " + owner.getClass().getName() + "#" + method.getName());
        }
    }

    public void unregister(Object owner) {
        events.removeIf(invoker -> invoker.owner == owner);
    }

    public <E extends Event> E post(E event) {
        if (Client.IS_PANIC) {
            return event;
        }
        ProfilerUtility.push("event-post");
        for (Invoker invoker : events) {
            if (!invoker.event.isAssignableFrom(event.getClass())) continue;
            try {
                invoker.method.invoke(invoker.owner, event);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ProfilerUtility.pop("event-post");
        return event;
    }

    public record Invoker(Method method, Class<?> event, Object owner, int priority) {
    }
}

