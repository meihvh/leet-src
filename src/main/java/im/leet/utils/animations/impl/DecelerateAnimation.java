/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.animations.impl;

import im.leet.utils.animations.Direction;
import im.leet.utils.animations.OldAnimation;

public class DecelerateAnimation
extends OldAnimation {
    public DecelerateAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DecelerateAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        double x1 = x / (double)this.duration;
        return 1.0 - (x1 - 1.0) * (x1 - 1.0);
    }
}

