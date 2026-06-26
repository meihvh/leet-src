/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package im.leet.base.rotations.strategies.impl.process;

import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.RotationChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.range.RangeSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import net.minecraft.entity.Entity;

public class JitterRotation
extends RotationChoice {
    public final RangeSetting yawJitter = this.rangeSetting("Yaw", 0.0f, 5.0f, 0.0f, 180.0f, 0.1f);
    public final RangeSetting pitchJitter = this.rangeSetting("Pitch", 0.0f, 5.0f, 0.0f, 90.0f, 0.1f);
    public final SliderSetting multiplier = this.sliderSetting("Multiplier", 1.0f, 0.0f, 10.0f).increment(0.1f);
    public final CheckBox multiplyNonDistance = this.checkbox("Multiply by 1/dist", true);

    public JitterRotation() {
        super("Jitter");
        this.toggleable(false);
    }

    @Override
    public Angle calculate(Angle current, Angle target) {
        float jyaw = this.yawJitter.getMax() <= 0.0f ? 0.0f : MathUtility.random(this.yawJitter.getMin(), this.yawJitter.getMax());
        jyaw = MathUtility.random(-jyaw, jyaw);
        float jpitch = this.pitchJitter.getMax() <= 0.0f ? 0.0f : MathUtility.random(this.pitchJitter.getMin(), this.pitchJitter.getMax());
        jpitch = MathUtility.random(-jpitch, jpitch);
        if (this.multiplyNonDistance.get() && TargetsUtility.getTarget() != null) {
            float dist = 1.0f / TargetsUtility.getTarget().method_5739((Entity)JitterRotation.mc.field_1724);
            jyaw *= dist;
            jpitch *= dist;
        }
        return new Angle(target.getYaw() + (jyaw *= this.multiplier.get()), target.getPitch() + (jpitch *= this.multiplier.get()));
    }
}

