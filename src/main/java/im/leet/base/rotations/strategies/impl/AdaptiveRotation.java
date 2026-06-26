/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.rotations.strategies.impl;

import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.RotationChoice;
import im.leet.utils.math.MathUtility;
import net.minecraft.util.math.MathHelper;

public class AdaptiveRotation
extends RotationChoice {
    static float x;
    static float y;

    public AdaptiveRotation() {
        super("Adaptive");
    }

    @Override
    public Angle calculate(Angle current, Angle target) {
        if (AdaptiveRotation.mc.field_1724.field_6012 < 20) {
            x = 0.0f;
            y = 0.0f;
        }
        float yawDelta_raw = MathHelper.method_15393((float)(target.getYaw() - current.getYaw()));
        float pitchDelta_raw = target.getPitch() - current.getPitch();
        float speedX = (float)Math.max((double)MathUtility.gaussian(45.0f, 55.0f), Math.pow(1.0f - Math.abs(yawDelta_raw / 180.0f), 2.0) * (double)MathUtility.random(60, 70));
        float yawDelta = MathHelper.method_15363((float)yawDelta_raw, (float)(-speedX), (float)speedX);
        float pitchDelta = MathHelper.method_15363((float)pitchDelta_raw, (float)-5.0f, (float)5.0f);
        float tx = MathHelper.method_16439((float)0.35f, (float)x, (float)yawDelta);
        float ty = MathHelper.method_16439((float)0.35f, (float)y, (float)pitchDelta);
        x = tx;
        y = ty;
        return new Angle(current.getYaw() + (x += (float)((double)(pitchDelta_raw / 45.0f) * Math.sin((double)System.currentTimeMillis() / 300.0)) * (float)MathUtility.random(2, 5)), MathHelper.method_15363((float)(current.getPitch() + (y += (float)Math.min((double)MathUtility.gaussian(3.0f, 6.0f), Math.abs((double)(yawDelta_raw / 90.0f) * Math.cos((double)System.currentTimeMillis() / 150.0)) * (double)MathUtility.random(2, 3) * (double)0.1f))), (float)-90.0f, (float)90.0f));
    }
}

