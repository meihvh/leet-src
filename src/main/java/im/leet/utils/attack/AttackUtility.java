/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.attack;

import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.range.RangeSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.mixin.accessor.ILivingEntity;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import java.util.function.Supplier;

public class AttackUtility
extends Group {
    final TimeUtility time = new TimeUtility();
    static final float BASE_TIME = 1.5f;
    static final float MIN_COOLDOWN = 0.944f;
    static final float MIN_COOLDOWN_ELYTRA = 0.99f;
    int falloff;
    final SliderSetting attackDelay = (SliderSetting)this.sliderSetting("Attack delay", 0.0f, 0.0f, 1000.0f).desc("Minimal attack delay");
    final SliderSetting attackDelayRandom = (SliderSetting)this.sliderSetting("Attack delay random", 0.0f, 0.0f, 1000.0f).desc("Random attack delay");
    final CheckBox hurtTime = (CheckBox)this.checkbox("Hurt time", false).desc("Attack relative to target hurt time");
    final SliderSetting maxHurtTime;
    final EnumSetting<Mode> mode;
    final SliderSetting ticks;
    final RangeSetting ppt;
    final EnumSetting<Distribution> distribution;

    private void reset() {
        this.falloff = 0;
    }

    public AttackUtility() {
        super("Attack settings");
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.hurtTime::get;
        this.maxHurtTime = (SliderSetting)((SliderSetting)this.sliderSetting("Max hurt time", 6.0f, 0.0f, 10.0f).visible(supplierArray)).desc("Target hurt time to attack");
        this.mode = (EnumSetting)this.enumSetting("Mode", Mode.Modern).desc("Mode of clicking");
        this.ticks = (SliderSetting)((SliderSetting)this.sliderSetting("Min attack ticks", 9.0f, 0.0f, 20.0f).increment(1.0f).visible(() -> this.mode.is(Mode.Modern))).desc("Minimal required ticks since last attack");
        this.ppt = (RangeSetting)((RangeSetting)((RangeSetting)this.rangeSetting("Packets", 1.0f, 1.0f, 0.0f, 100.0f, 1.0f).visible(() -> this.mode.is(Mode.Old))).onChanged(ignored -> this.reset())).desc("How many attack packets to send");
        this.distribution = (EnumSetting)((EnumSetting)((EnumSetting)this.enumSetting("Distribution", Distribution.Linear).visible(() -> this.mode.is(Mode.Old))).onChanged(ignored -> this.reset())).desc("Clicks distribution");
    }

    @Override
    public String getLangKey() {
        return "leet.AttackUtility";
    }

    public int getClicks() {
        long delay = this.attackDelay.getLong() + MathUtility.random(0L, this.attackDelayRandom.getLong());
        if (!this.time.reached(delay, true)) {
            return 0;
        }
        if (this.hurtTime.get() && TargetsUtility.getTarget() != null && TargetsUtility.getTarget().field_6235 > this.maxHurtTime.getInt()) {
            return 0;
        }
        return switch (this.mode.get().ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> {
                float v0 = AttackUtility.mc.field_1724.method_7261(1.5f);
                float v1 = AttackUtility.mc.field_1724.method_6128() ? 0.99f : 0.944f;
                if (v0 > v1 && ((ILivingEntity)AttackUtility.mc.field_1724).client$lastAttackedTicks() > this.ticks.getInt()) {
                    yield 1;
                }
                yield 0;
            }
            case 1 -> {
                int min = (int)this.ppt.getMin();
                int max = (int)this.ppt.getMax();
                if (max == 0) {
                    yield 0;
                }
                switch (this.distribution.get().ordinal()) {
                    default: {
                        throw new MatchException(null, null);
                    }
                    case 0: {
                        yield MathUtility.random(min, max);
                    }
                    case 1: 
                }
                if (this.falloff <= 0) {
                    yield this.falloff = MathUtility.random(min, max);
                }
                yield this.falloff = this.falloff - 1;
            }
        };
    }

    public float getProgress() {
        return switch (this.mode.get().ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> {
                int attackTicks = ((ILivingEntity)AttackUtility.mc.field_1724).client$lastAttackedTicks();
                if (this.ticks.get() > 0.0f) {
                    yield (float)attackTicks / this.ticks.get();
                }
                yield AttackUtility.mc.field_1724.method_7261(1.5f) - (1.0f - (AttackUtility.mc.field_1724.method_6128() ? 0.99f : 0.944f));
            }
            case 1 -> this.ppt.getMax() > 0.0f ? 1.0f : 0.0f;
        };
    }

    static enum Mode implements EnumChoice
    {
        Modern,
        Old;


        @Override
        public String getLangClassName() {
            return "AttackUtility.Mode";
        }
    }

    static enum Distribution implements EnumChoice
    {
        Linear,
        Falloff;


        @Override
        public String getLangClassName() {
            return "AttackUtility.Distribution";
        }
    }
}

