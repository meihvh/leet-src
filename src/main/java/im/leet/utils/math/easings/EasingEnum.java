/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.math.easings;

import im.leet.utils.math.easings.Easing;
import im.leet.utils.math.easings.Easings;

public enum EasingEnum implements Easing
{
    Linear(Easings.LINEAR),
    QuadIn(Easings.QUAD_IN),
    QuadOut(Easings.QUAD_OUT),
    QuadInOut(Easings.QUAD_IN_OUT),
    CubicIn(Easings.CUBIC_IN),
    CubicOut(Easings.CUBIC_OUT),
    CubicInOut(Easings.CUBIC_IN_OUT),
    QuartIn(Easings.QUART_IN),
    QuartOut(Easings.QUART_OUT),
    QuartInOut(Easings.QUART_IN_OUT),
    QuintIn(Easings.QUINT_IN),
    QuintOut(Easings.QUINT_OUT),
    QuintInOut(Easings.QUINT_IN_OUT),
    SineIn(Easings.SINE_IN),
    SineOut(Easings.SINE_OUT),
    SineInOut(Easings.SINE_IN_OUT),
    CircIn(Easings.CIRC_IN),
    CircOut(Easings.CIRC_OUT),
    CircInOut(Easings.CIRC_IN_OUT),
    ElasticIn(Easings.ELASTIC_IN),
    ElasticOut(Easings.ELASTIC_OUT),
    ElasticInOut(Easings.ELASTIC_IN_OUT);

    final Easing _easing;

    @Override
    public double ease(double value) {
        return this._easing.ease(value);
    }

    private EasingEnum(Easing _easing) {
        this._easing = _easing;
    }
}

