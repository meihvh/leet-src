/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.api.sound.filters;

import net.minecraft.util.math.MathHelper;

public class FilterLowPass {
    public float gain = 1.0f;
    public float gainHF = 1.0f;
    private static final float MIN_GAIN = 0.0f;
    private static final float MAX_GAIN = 1.0f;
    private static final float MIN_GAIN_HF = 0.0f;
    private static final float MAX_GAIN_HF = 1.0f;

    public void checkParameters() {
        this.gain = MathHelper.method_15363((float)this.gain, (float)0.0f, (float)1.0f);
        this.gainHF = MathHelper.method_15363((float)this.gainHF, (float)0.0f, (float)1.0f);
    }

    public void reset() {
        this.gain = 1.0f;
        this.gainHF = 1.0f;
    }
}

