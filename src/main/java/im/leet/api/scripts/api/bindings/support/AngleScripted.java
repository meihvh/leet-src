/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3f
 */
package im.leet.api.scripts.api.bindings.support;

import im.leet.base.rotations.Angle;
import org.joml.Vector3f;

public class AngleScripted {
    private Angle origin;

    public AngleScripted(float yaw, float pitch) {
        this.origin = new Angle(yaw, pitch);
    }

    public Vector3f toVector() {
        return this.origin.toVector().method_46409();
    }

    public float yaw() {
        return this.origin.getYaw();
    }

    public float pitch() {
        return this.origin.getPitch();
    }

    public void yaw(float v) {
        this.origin.setYaw(v);
    }

    public void pitch(float v) {
        this.origin.setPitch(v);
    }
}

