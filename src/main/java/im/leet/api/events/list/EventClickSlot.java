/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.screen.slot.SlotActionType
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import net.minecraft.screen.slot.SlotActionType;

public class EventClickSlot
extends Event {
    private static final EventClickSlot instance = new EventClickSlot();
    public int syncId;
    public int slotId;
    public int button;
    public SlotActionType actionType;

    public static EventClickSlot build(int syncId, int slotId, int button, SlotActionType actionType) {
        EventClickSlot.instance.syncId = syncId;
        EventClickSlot.instance.slotId = slotId;
        EventClickSlot.instance.button = button;
        EventClickSlot.instance.actionType = actionType;
        instance.reset();
        return instance;
    }

    public String toString() {
        return "EventClickSlot(syncId=" + this.syncId + ", slotId=" + this.slotId + ", button=" + this.button + ", actionType=" + String.valueOf(this.actionType) + ")";
    }
}

