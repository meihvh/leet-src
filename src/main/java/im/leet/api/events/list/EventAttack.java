/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.entity.Entity;

public class EventAttack
extends Event {
    private static EventAttack instance = new EventAttack();
    public Entity target;

    public static EventAttack build(Entity target) {
        EventAttack.instance.target = target;
        instance.reset();
        return instance;
    }
}

