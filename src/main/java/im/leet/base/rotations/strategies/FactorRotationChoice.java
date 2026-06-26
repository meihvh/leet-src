/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.rotations.strategies;

import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.RotationChoice;

public abstract class FactorRotationChoice
extends RotationChoice {
    protected FactorRotationChoice(String name) {
        super(name);
    }

    public abstract Angle getFactors(Angle var1, Angle var2);

    @Override
    public Angle calculate(Angle current, Angle target) {
        Angle factors = this.getFactors(current, target);
        return current.towardsLinear(target, factors.getYaw(), factors.getPitch());
    }
}

