/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.rotations;

import im.leet.Client;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.strategies.Rotation;
import im.leet.base.rotations.strategies.RotationChoice;
import im.leet.base.rotations.strategies.impl.AdaptiveRotation;
import im.leet.base.rotations.strategies.impl.CooldownRotation;
import im.leet.base.rotations.strategies.impl.InterpolationRotation;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.base.rotations.strategies.impl.process.CutRotation;
import im.leet.base.rotations.strategies.impl.process.JitterRotation;
import im.leet.base.rotations.strategies.impl.process.LimitRotation;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.ChoiceSetting;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;

public class RotationSettings
extends Group
implements Rotation {
    public final int rotationPriority;
    public final ChoiceSetting<RotationChoice> mode = this.choiceSetting("Mode", 0, new RotationChoice[]{LinearRotation.INSTANCE, new InterpolationRotation(), new CooldownRotation(), new AdaptiveRotation()});
    public final EnumSetting<MovementCorrection> correction = this.enumSetting("Correction", MovementCorrection.STRICT);
    public final SliderSetting timeout = this.sliderSetting("Timeout", 1.0f, 1.0f, 20.0f);
    public final CheckBox shouldLerp = this.checkbox("Smooth reset", false);
    public final JitterRotation jitter = this.add(new JitterRotation());
    public final CutRotation cut = this.add(new CutRotation());
    public final LimitRotation limit = this.add(new LimitRotation());

    public RotationSettings(String name, int priority) {
        super(name);
        this.rotationPriority = priority;
    }

    public void rotate(Angle targetAngle) {
        Client.ROTATION.rotate(this, targetAngle, this.timeout.getInt(), this.shouldLerp.get(), this.correction.get(), this.rotationPriority);
    }

    @Override
    public Angle calculate(Angle current, Angle target) {
        target = this.mode.get().calculate(current, target);
        if (this.cut.isEnabled()) {
            target = this.cut.calculate(current, target);
        }
        if (this.limit.isEnabled()) {
            target = this.limit.calculate(current, target);
        }
        if (this.jitter.isEnabled()) {
            target = this.jitter.calculate(current, target);
        }
        return target;
    }
}

