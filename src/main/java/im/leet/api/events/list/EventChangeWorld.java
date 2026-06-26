/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.world.ClientWorld
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.client.world.ClientWorld;

public class EventChangeWorld
extends Event {
    public final ClientWorld world;

    public EventChangeWorld(ClientWorld world) {
        this.world = world;
    }
}

