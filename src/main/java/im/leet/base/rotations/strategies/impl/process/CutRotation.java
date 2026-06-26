/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.rotations.strategies.impl.process;

import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.RotationChoice;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.MathUtility;
import net.minecraft.util.math.MathHelper;

public class CutRotation
extends RotationChoice {
    public final SliderSetting yawLeft = this.sliderSetting("Yaw%", 100.0f, 0.0f, 100.0f).increment(0.1f);
    public final SliderSetting pitchLeft = this.sliderSetting("Pitch%", 100.0f, 0.0f, 100.0f).increment(0.1f);

    public CutRotation() {
        super("Cut");
        this.toggleable(false);
    }

    @Override
    public Angle calculate(Angle current, Angle target) {
        float yawleft = this.yawLeft.get();
        float pitchleft = this.pitchLeft.get();
        Angle delta = current.delta(target);
        float yawSign = Math.signum(delta.getYaw());
        float pitchSign = Math.signum(delta.getPitch());
        float dYaw = Math.abs(delta.getYaw());
        float dPitch = Math.abs(delta.getPitch());
        float yaw = yawleft >= 1.0f ? target.getYaw() : current.getYaw() + MathUtility.random(dYaw * yawleft, dYaw) * yawSign;
        float pitch = pitchleft >= 1.0f ? target.getPitch() : current.getPitch() + MathUtility.random(dPitch * pitchleft, dPitch) * pitchSign;
        return new Angle(MathHelper.method_15393((float)yaw), MathHelper.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }
}

