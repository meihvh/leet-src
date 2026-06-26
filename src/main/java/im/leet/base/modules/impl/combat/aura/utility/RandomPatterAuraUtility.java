/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.modules.impl.combat.aura.utility;

import im.leet.base.modules.impl.combat.aura.utility.CalcRotUtility;
import im.leet.base.rotations.Angle;
import java.security.SecureRandom;
import net.minecraft.util.math.MathHelper;

public final class RandomPatterAuraUtility {
    public static float random(Angle target, float min, float max) {
        SecureRandom random = new SecureRandom();
        float pat = RandomPatterAuraUtility.randomPattern(target) == 0.0f ? 0.01f : RandomPatterAuraUtility.randomPattern(target);
        random.setSeed((long)((double)System.currentTimeMillis() * random.nextGaussian() * (double)pat));
        return (float)((double)min + random.nextGaussian() * (double)(max - min));
    }

    public static float randomPattern(Angle targetAngle) {
        float[] deltaS = CalcRotUtility.getDeltas(targetAngle);
        float normalizedYaw = MathHelper.method_15363((float)(Math.abs(deltaS[0]) / 180.0f), (float)0.0f, (float)1.0f);
        float normalizedPitch = MathHelper.method_15363((float)(Math.abs(deltaS[1]) / 90.0f), (float)0.0f, (float)1.0f);
        float combinedDeviation = normalizedYaw * 0.5f + normalizedPitch * 0.5f;
        float legitimacyFactor = 1.0f - combinedDeviation;
        return MathHelper.method_15363((float)legitimacyFactor, (float)0.1f, (float)1.0f);
    }

    private RandomPatterAuraUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

