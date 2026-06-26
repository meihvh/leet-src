/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface EventPriority {
    public static final int LAST = -50;
    public static final int NORMAL = 0;
    public static final int FIRST = 50;

    public int value() default 0;
}

