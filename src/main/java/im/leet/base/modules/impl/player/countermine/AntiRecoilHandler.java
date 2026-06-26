/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.modules.impl.player.countermine;

import im.leet.MinecraftHolder;
import im.leet.api.events.Event;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventMove;

public final class AntiRecoilHandler
implements MinecraftHolder {
    private float yaw;
    private float pitch;
    private boolean aiming;

    public void handle(Event event) {
        if (event instanceof EventMove) {
            EventMove move = (EventMove)event;
            if (this.aiming) {
                move.yaw = this.yaw;
                move.pitch = this.pitch;
            }
            return;
        }
        if (event instanceof EventGameTick) {
            if (AntiRecoilHandler.mc.field_1724 == null || AntiRecoilHandler.mc.field_1687 == null || AntiRecoilHandler.mc.field_1724.method_7325()) {
                this.aiming = false;
                return;
            }
            boolean using = AntiRecoilHandler.mc.field_1690.field_1886.method_1434();
            if (!using) {
                this.aiming = false;
                return;
            }
            if (!this.aiming) {
                this.yaw = AntiRecoilHandler.mc.field_1724.method_36454();
                this.pitch = AntiRecoilHandler.mc.field_1724.method_36455();
                this.aiming = true;
            }
            AntiRecoilHandler.mc.field_1724.method_36456(this.yaw);
            AntiRecoilHandler.mc.field_1724.method_36457(this.pitch);
        }
    }

    public void reset() {
        this.aiming = false;
    }
}

