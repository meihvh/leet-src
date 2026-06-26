/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.events.list;

import im.leet.api.events.Event;
import ru.aloweeed.perfect.Ignored;

@Ignored
public class EventInput
extends Event {
    static EventInput instance = new EventInput();
    private float strafe;
    private float forward;
    private boolean jump;
    private boolean sprint;
    private boolean sneak;

    public static EventInput build(float forward, float strafe, boolean jump, boolean shift, boolean sprint) {
        instance.setStrafe(strafe);
        instance.setForward(forward);
        instance.setJump(jump);
        instance.setSneak(shift);
        instance.setSprint(sprint);
        instance.reset();
        return instance;
    }

    public float getStrafe() {
        return this.strafe;
    }

    public float getForward() {
        return this.forward;
    }

    public boolean isJump() {
        return this.jump;
    }

    public boolean isSprint() {
        return this.sprint;
    }

    public boolean isSneak() {
        return this.sneak;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }

    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }
}

