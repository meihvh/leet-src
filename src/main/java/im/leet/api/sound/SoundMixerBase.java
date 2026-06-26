/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.sound.SoundInstance
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.openal.AL
 *  org.lwjgl.openal.AL10
 *  org.lwjgl.openal.AL11
 *  org.lwjgl.openal.ALCapabilities
 *  org.lwjgl.openal.EXTEfx
 */
package im.leet.api.sound;

import im.leet.api.sound.SoundHelper;
import im.leet.api.sound.filters.FilterLowPass;
import im.leet.api.sound.filters.FilterReverb;
import im.leet.base.modules.impl.other.RTXSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.EXTEfx;

public class SoundMixerBase {
    private final FilterReverb reverbFilter = new FilterReverb();
    private final FilterLowPass lowPassFilter = new FilterLowPass();
    private float echoPercent = 0.0f;
    private float reflectPercent = 0.0f;
    private float lowPassGain = 1.0f;
    private float lowPassGainHF = 1.0f;
    private int reverbEffectSlot = -1;
    private int reverbEffect = -1;
    private int lowPassFilter_AL = -1;
    private boolean initialized = false;
    private boolean efxSupported = false;

    public static SoundMixerBase loadMixer() {
        return new SoundMixerBase();
    }

    public void setEchoEffect(float echo, float reflect) {
        this.echoPercent = echo;
        this.reflectPercent = reflect;
    }

    public void setLowPass(float gain, float gainHF) {
        this.lowPassGain = gain;
        this.lowPassGainHF = gainHF;
    }

    public float getEchoPercent() {
        return this.echoPercent;
    }

    public float getReflectPercent() {
        return this.reflectPercent;
    }

    public float getLowPassGain() {
        return this.lowPassGain;
    }

    public float getLowPassGainHF() {
        return this.lowPassGainHF;
    }

    public void initializeEffects() {
        try {
            ALCapabilities capabilities = AL.getCapabilities();
            if (capabilities == null || !capabilities.ALC_EXT_EFX) {
                System.out.println("[RTX Sounds] EFX extension not supported, audio effects disabled");
                this.efxSupported = false;
                return;
            }
            this.efxSupported = true;
            System.out.println("[RTX Sounds] EFX extension supported, initializing audio effects...");
            this.reverbEffect = EXTEfx.alGenEffects();
            if (AL10.alGetError() != 0) {
                System.err.println("[RTX Sounds] Failed to create reverb effect");
                this.efxSupported = false;
                return;
            }
            EXTEfx.alEffecti((int)this.reverbEffect, (int)32769, (int)1);
            this.reverbEffectSlot = EXTEfx.alGenAuxiliaryEffectSlots();
            if (AL10.alGetError() != 0) {
                System.err.println("[RTX Sounds] Failed to create effect slot");
                this.efxSupported = false;
                return;
            }
            this.lowPassFilter_AL = EXTEfx.alGenFilters();
            if (AL10.alGetError() != 0) {
                System.err.println("[RTX Sounds] Failed to create lowpass filter");
                this.efxSupported = false;
                return;
            }
            EXTEfx.alFilteri((int)this.lowPassFilter_AL, (int)32769, (int)1);
            System.out.println("[RTX Sounds] Audio effects initialized successfully");
        }
        catch (Exception e) {
            System.err.println("[RTX Sounds] Failed to initialize OpenAL effects: " + e.getMessage());
            this.efxSupported = false;
        }
    }

    public void injectFiltersToChannel(SoundInstance sound, int sourceId) {
        if (sourceId <= 0) {
            return;
        }
        if (!this.initialized) {
            this.initializeEffects();
            this.initialized = true;
        }
        if (!this.efxSupported) {
            return;
        }
        try {
            if (this.reverbEffect != -1 && this.reverbEffectSlot != -1) {
                this.applyReverbEffect();
                EXTEfx.alAuxiliaryEffectSloti((int)this.reverbEffectSlot, (int)1, (int)this.reverbEffect);
                if (AL10.alGetError() == 0) {
                    AL11.alSource3i((int)sourceId, (int)131078, (int)this.reverbEffectSlot, (int)0, (int)0);
                }
            }
            if (this.lowPassFilter_AL != -1) {
                this.applyLowPassFilter();
                AL10.alSourcei((int)sourceId, (int)131077, (int)this.lowPassFilter_AL);
            }
            if (RTXSounds.INSTANCE.betterStereo.get()) {
                this.applyBetterStereo(sound, sourceId);
            }
            if (RTXSounds.INSTANCE.toneCompensation.get()) {
                this.applyToneCompensation(sound, sourceId);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void applyReverbEffect() {
        if (this.reverbEffect == -1) {
            return;
        }
        try {
            EXTEfx.alEffectf((int)this.reverbEffect, (int)1, (float)this.reverbFilter.density);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)2, (float)this.reverbFilter.diffusion);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)3, (float)this.reverbFilter.gain);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)4, (float)this.reverbFilter.gainHF);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)5, (float)this.reverbFilter.decayTime);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)6, (float)this.reverbFilter.decayHFRatio);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)7, (float)this.reverbFilter.reflectionsGain);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)8, (float)this.reverbFilter.reflectionsDelay);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)9, (float)this.reverbFilter.lateReverbGain);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)10, (float)this.reverbFilter.lateReverbDelay);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)11, (float)this.reverbFilter.airAbsorptionGainHF);
            EXTEfx.alEffectf((int)this.reverbEffect, (int)12, (float)this.reverbFilter.roomRolloffFactor);
        }
        catch (Exception e) {
            System.err.println("[RTX Sounds] Failed to apply reverb parameters: " + e.getMessage());
        }
    }

    private void applyLowPassFilter() {
        if (this.lowPassFilter_AL == -1) {
            return;
        }
        try {
            EXTEfx.alFilterf((int)this.lowPassFilter_AL, (int)1, (float)this.lowPassFilter.gain);
            EXTEfx.alFilterf((int)this.lowPassFilter_AL, (int)2, (float)this.lowPassFilter.gainHF);
        }
        catch (Exception e) {
            System.err.println("[RTX Sounds] Failed to apply lowpass parameters: " + e.getMessage());
        }
    }

    private void applyBetterStereo(SoundInstance sound, int sourceId) {
        MinecraftClient mc = MinecraftClient.method_1551();
        if (mc.field_1724 == null || sound.method_4787()) {
            return;
        }
        Vec3d playerPos = mc.field_1724.method_19538();
        Vec3d soundPos = new Vec3d(sound.method_4784(), sound.method_4779(), sound.method_4778());
        Vec2f angles = SoundHelper.calculate(playerPos, soundPos);
        float playerYaw = mc.field_1724.method_36454();
        float angleDiff = SoundHelper.getAngleDifference(angles.field_1343, playerYaw);
        float stereoModifier = 1.0f - angleDiff / 180.0f * 0.3f;
        stereoModifier = Math.max(0.7f, Math.min(1.0f, stereoModifier));
        float currentVolume = sound.method_4781();
        AL10.alSourcef((int)sourceId, (int)4106, (float)(currentVolume * stereoModifier));
    }

    private void applyToneCompensation(SoundInstance sound, int sourceId) {
        float basePitch = sound.method_4782();
        float toneModifier = 1.0f + this.echoPercent * 0.05f;
        toneModifier = Math.max(0.95f, Math.min(1.05f, toneModifier));
        AL10.alSourcef((int)sourceId, (int)4099, (float)(basePitch * toneModifier));
    }

    public void cleanupEffects() {
        try {
            if (this.reverbEffect != -1) {
                EXTEfx.alDeleteEffects((int)this.reverbEffect);
                this.reverbEffect = -1;
            }
            if (this.reverbEffectSlot != -1) {
                EXTEfx.alDeleteAuxiliaryEffectSlots((int)this.reverbEffectSlot);
                this.reverbEffectSlot = -1;
            }
            if (this.lowPassFilter_AL != -1) {
                EXTEfx.alDeleteFilters((int)this.lowPassFilter_AL);
                this.lowPassFilter_AL = -1;
            }
        }
        catch (Exception e) {
            System.err.println("[RTX Sounds] Failed to cleanup OpenAL effects: " + e.getMessage());
        }
    }

    public FilterReverb getReverbFilter() {
        return this.reverbFilter;
    }

    public FilterLowPass getLowPassFilter() {
        return this.lowPassFilter;
    }
}

