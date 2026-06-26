/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.text.Text;

public class EventAddMessage
extends Event {
    private static final EventAddMessage instance = new EventAddMessage();
    public Text text;

    public static EventAddMessage build(Text text) {
        EventAddMessage.instance.text = text;
        return instance;
    }
}

