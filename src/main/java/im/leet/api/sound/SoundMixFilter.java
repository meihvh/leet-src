/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 */
package im.leet.api.sound;

import im.leet.api.sound.SoundMixerBase;
import im.leet.api.sound.SoundSurroundTool;
import im.leet.api.sound.filters.FilterLowPass;
import im.leet.api.sound.filters.FilterReverb;
import im.leet.base.modules.impl.other.RTXSounds;
import net.minecraft.client.MinecraftClient;

public class SoundMixFilter {
    private final SoundMixerBase mixer = SoundMixerBase.loadMixer();
    private final SoundSurroundTool surround = SoundSurroundTool.build();
    private boolean state = false;

    private SoundMixFilter() {
    }

    public static SoundMixFilter makeDistorterMixer() {
        return new SoundMixFilter();
    }

    public void updateMixer() {
        float[] args = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        this.setState(RTXSounds.INSTANCE.isEnabled());
        if (this.state) {
            MinecraftClient mc = MinecraftClient.method_1551();
            this.surround.setRtxDebug(false);
            this.surround.setPlayer(mc.field_1724);
            this.surround.setTooPerfomance(RTXSounds.INSTANCE.performancePriority.is(RTXSounds.SoundMode.Performance));
            args = this.surround.getGainArgsFromWorld();
        } else if (!this.surround.getListOfTestVecs().isEmpty()) {
            int sz = this.surround.getListOfTestVecs().size();
            int i = 0;
            while ((float)i < (float)sz / 2.0f) {
                if (!this.surround.getListOfTestVecs().isEmpty()) {
                    this.surround.getListOfTestVecs().remove(0);
                }
                ++i;
            }
        } else {
            this.surround.setRtxDebug(false);
        }
        float performanceMod = this.surround.isTooPerfomance() ? 0.75f : 1.0f;
        this.mixer.setEchoEffect(args[0] * performanceMod, args[1] * (this.surround.isTooPerfomance() ? 0.4f : 0.8f));
        this.mixer.setLowPass(args[2], args[3]);
        this.updateFiltersData();
    }

    private void updateFiltersData() {
        SoundMixerBase mixer = this.getMixer();
        FilterReverb reverbFilter = mixer.getReverbFilter();
        FilterLowPass lowPassFilter = mixer.getLowPassFilter();
        float echoDelay = mixer.getEchoPercent();
        float echoRev = echoDelay + mixer.getReflectPercent() * 8.000011f;
        reverbFilter.decayTime = echoDelay;
        reverbFilter.reflectionsGain = echoRev * (0.05f + 0.05f * echoDelay);
        reverbFilter.reflectionsDelay = 0.125f * echoDelay;
        reverbFilter.lateReverbGain = echoRev * (1.26f + 0.2f * echoDelay);
        reverbFilter.lateReverbDelay = 0.01f * echoDelay;
        reverbFilter.checkParameters();
        float lLevelGain = mixer.getLowPassGain();
        float lLevelGainHF = mixer.getLowPassGainHF();
        lowPassFilter.gain = this.lerp(lowPassFilter.gain, lLevelGain, lowPassFilter.gainHF > lLevelGainHF ? 0.05f : 0.1f);
        lowPassFilter.gainHF = this.lerp(lowPassFilter.gainHF, lLevelGainHF, lowPassFilter.gainHF > lLevelGainHF ? 0.3f : 0.12f);
        lowPassFilter.checkParameters();
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public boolean getHasMixerLoaded() {
        return this.mixer != null;
    }

    public void init() {
    }

    public void unload() {
        if (this.mixer != null) {
            this.mixer.cleanupEffects();
        }
    }

    public SoundMixerBase getMixer() {
        return this.mixer;
    }

    public SoundSurroundTool getSurround() {
        return this.surround;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}

