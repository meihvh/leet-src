/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.rotations.strategies.impl.process;

import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.FactorRotationChoice;
import im.leet.base.settings.impl.slider.SliderSetting;

public class LimitRotation
extends FactorRotationChoice {
    public final SliderSetting yawSpeed = this.sliderSetting("Yaw speed", 180.0f, 0.0f, 180.0f).increment(0.1f);
    public final SliderSetting pitchSpeed = this.sliderSetting("Pitch speed", 180.0f, 0.0f, 180.0f).increment(0.1f);

    public LimitRotation() {
        super("Limit");
    }

    @Override
    public Angle getFactors(Angle current, Angle target) {
        return new Angle(this.yawSpeed.get(), this.pitchSpeed.get());
    }
}

