/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.rotations.strategies.impl;

import im.leet.Client;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.strategies.FactorRotationChoice;
import im.leet.base.settings.impl.range.RangeSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import net.minecraft.util.math.MathHelper;

public class InterpolationRotation
extends FactorRotationChoice {
    final RangeSetting yaw = this.rangeSetting("Yaw%", 80.0f, 85.0f, 1.0f, 100.0f, 0.1f);
    final RangeSetting pitch = this.rangeSetting("Pitch%", 20.0f, 25.0f, 1.0f, 100.0f, 0.1f);
    final RangeSetting direction = this.rangeSetting("Direction%", 95.0f, 100.0f, 0.0f, 100.0f, 0.1f);
    final SliderSetting mid = this.sliderSetting("Mid", 0.35f, 0.0f, 1.0f).increment(0.01f);

    public InterpolationRotation() {
        super("Interpolation");
    }

    private float sig(float x) {
        return 1.0f / (1.0f + (float)Math.exp(-0.5f * (x - 0.3f)));
    }

    private float bez(float a, float b, float x) {
        return (1.0f - x) * (1.0f - x) * a + 2.0f * (1.0f - x) * x * 1.0f + x * x * b;
    }

    @Override
    public Angle getFactors(Angle current, Angle target) {
        Angle delta = current.delta(target);
        Angle prev = Client.ROTATION.getPrevRotate();
        float dir = prev != null ? MathHelper.method_15363((float)prev.angleTo(target), (float)0.0f, (float)1.0f) * (this.direction.random() / 100.0f) : 0.0f;
        float yaws = this.yaw.random() / 100.0f;
        float pitchs = this.pitch.random() / 100.0f;
        float yawf = this.factor(Math.abs(delta.yaw), MathHelper.method_15363((float)yaws, (float)0.0f, (float)1.0f), dir);
        float pitchf = this.factor(Math.abs(delta.pitch), MathHelper.method_15363((float)pitchs, (float)0.0f, (float)1.0f), dir);
        return new Angle(yawf * Math.abs(delta.yaw), pitchf * Math.abs(delta.pitch));
    }

    private float factor(float delta, float speed, float dir) {
        float x = MathHelper.method_15363((float)(delta / 180.0f), (float)0.0f, (float)1.0f);
        return x > this.mid.get() ? this.bez(0.05f, 1.0f, 1.0f - x) * speed : MathHelper.method_15363((float)(speed + dir), (float)0.0f, (float)1.0f) * this.sig(x);
    }
}

