/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.rotations.strategies.impl;

import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.RotationChoice;
import net.minecraft.util.math.MathHelper;

public class LinearRotation
extends RotationChoice {
    public static final LinearRotation INSTANCE = new LinearRotation();

    private LinearRotation() {
        super("Linear");
    }

    @Override
    public Angle calculate(Angle current, Angle target) {
        return new Angle(current.getYaw() + MathHelper.method_15393((float)(target.getYaw() - current.getYaw())), target.getPitch());
    }
}

