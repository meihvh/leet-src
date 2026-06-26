/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.api.sound.filters;

import net.minecraft.util.math.MathHelper;

public class FilterReverb {
    public float density = 1.0f;
    public float diffusion = 1.0f;
    public float gain = 0.32f;
    public float gainHF = 0.89f;
    public float decayTime = 1.49f;
    public float decayHFRatio = 0.83f;
    public float reflectionsGain = 0.05f;
    public float reflectionsDelay = 0.007f;
    public float lateReverbGain = 1.26f;
    public float lateReverbDelay = 0.011f;
    public float airAbsorptionGainHF = 0.994f;
    public float roomRolloffFactor = 0.0f;
    private static final float MIN_DENSITY = 0.0f;
    private static final float MAX_DENSITY = 1.0f;
    private static final float MIN_DIFFUSION = 0.0f;
    private static final float MAX_DIFFUSION = 1.0f;
    private static final float MIN_GAIN = 0.0f;
    private static final float MAX_GAIN = 1.0f;
    private static final float MIN_GAIN_HF = 0.0f;
    private static final float MAX_GAIN_HF = 1.0f;
    private static final float MIN_DECAY_TIME = 0.1f;
    private static final float MAX_DECAY_TIME = 20.0f;
    private static final float MIN_DECAY_HF_RATIO = 0.1f;
    private static final float MAX_DECAY_HF_RATIO = 2.0f;
    private static final float MIN_REFLECTIONS_GAIN = 0.0f;
    private static final float MAX_REFLECTIONS_GAIN = 3.16f;
    private static final float MIN_REFLECTIONS_DELAY = 0.0f;
    private static final float MAX_REFLECTIONS_DELAY = 0.3f;
    private static final float MIN_LATE_REVERB_GAIN = 0.0f;
    private static final float MAX_LATE_REVERB_GAIN = 10.0f;
    private static final float MIN_LATE_REVERB_DELAY = 0.0f;
    private static final float MAX_LATE_REVERB_DELAY = 0.1f;
    private static final float MIN_AIR_ABSORPTION_GAIN_HF = 0.892f;
    private static final float MAX_AIR_ABSORPTION_GAIN_HF = 1.0f;
    private static final float MIN_ROOM_ROLLOFF_FACTOR = 0.0f;
    private static final float MAX_ROOM_ROLLOFF_FACTOR = 10.0f;

    public void checkParameters() {
        this.density = MathHelper.method_15363((float)this.density, (float)0.0f, (float)1.0f);
        this.diffusion = MathHelper.method_15363((float)this.diffusion, (float)0.0f, (float)1.0f);
        this.gain = MathHelper.method_15363((float)this.gain, (float)0.0f, (float)1.0f);
        this.gainHF = MathHelper.method_15363((float)this.gainHF, (float)0.0f, (float)1.0f);
        this.decayTime = MathHelper.method_15363((float)this.decayTime, (float)0.1f, (float)20.0f);
        this.decayHFRatio = MathHelper.method_15363((float)this.decayHFRatio, (float)0.1f, (float)2.0f);
        this.reflectionsGain = MathHelper.method_15363((float)this.reflectionsGain, (float)0.0f, (float)3.16f);
        this.reflectionsDelay = MathHelper.method_15363((float)this.reflectionsDelay, (float)0.0f, (float)0.3f);
        this.lateReverbGain = MathHelper.method_15363((float)this.lateReverbGain, (float)0.0f, (float)10.0f);
        this.lateReverbDelay = MathHelper.method_15363((float)this.lateReverbDelay, (float)0.0f, (float)0.1f);
        this.airAbsorptionGainHF = MathHelper.method_15363((float)this.airAbsorptionGainHF, (float)0.892f, (float)1.0f);
        this.roomRolloffFactor = MathHelper.method_15363((float)this.roomRolloffFactor, (float)0.0f, (float)10.0f);
    }

    public void reset() {
        this.density = 1.0f;
        this.diffusion = 1.0f;
        this.gain = 0.32f;
        this.gainHF = 0.89f;
        this.decayTime = 1.49f;
        this.decayHFRatio = 0.83f;
        this.reflectionsGain = 0.05f;
        this.reflectionsDelay = 0.007f;
        this.lateReverbGain = 1.26f;
        this.lateReverbDelay = 0.011f;
        this.airAbsorptionGainHF = 0.994f;
        this.roomRolloffFactor = 0.0f;
    }
}

