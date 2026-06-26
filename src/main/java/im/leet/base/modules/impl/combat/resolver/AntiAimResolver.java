/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.Range
 */
package im.leet.base.modules.impl.combat.resolver;

import im.leet.Client;
import im.leet.base.modules.impl.combat.resolver.ResolverMode;
import im.leet.base.rotations.Angle;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Range;

public class AntiAimResolver
extends ResolverMode {
    private final CheckBox notGliding = this.checkbox("Target isn't gliding", true);
    private final SliderSetting maxDist = this.sliderSetting("Max distance", 4.0f, 1.0f, 8.0f).increment(0.1f);
    private final SliderSetting multiplyXZ = this.sliderSetting("Multiply XZ", 2.0f, 1.0f, 6.0f).increment(0.1f);
    private final EnumSetting<Mode> mode = this.enumSetting("Mode", Mode.Away);
    private final SliderSetting angle = (SliderSetting)this.sliderSetting("Away angle", 12.0f, 0.0f, 180.0f).visible(() -> this.mode.is(Mode.Away));
    private final SliderSetting pitch = (SliderSetting)this.sliderSetting("Straight pitch", 12.0f, -90.0f, 90.0f).visible(() -> this.mode.is(Mode.Straight));
    private boolean direction;
    private boolean switched;

    public AntiAimResolver() {
        super("Anti Aim");
        this.toggleable(false);
    }

    @Override
    public Vec3d resolveElytra(Vec3d origin, @Range(from=0L, to=1L) float cooldown) {
        if (AntiAimResolver.mc.field_1724.method_5707(origin) > (double)this.maxDist.getSquared()) {
            return origin;
        }
        LivingEntity target = TargetsUtility.getTarget();
        if (this.notGliding.get() && target != null && target.method_6128()) {
            return origin;
        }
        if (cooldown == 1.0f && !this.switched) {
            this.direction = !this.direction;
            this.switched = true;
            return origin;
        }
        this.switched = false;
        float mul = this.multiplyXZ.get();
        return switch (this.mode.get().ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> {
                Vec3d vector = new Angle(Client.ROTATION.getRotate().getYaw() + this.angle.get() * (float)(this.direction ? 1 : -1), 0.0f).toVector();
                vector.field_1351 = Math.random() - 0.5;
                yield origin.method_1019(vector.method_18805((double)mul, 1.0, (double)mul));
            }
            case 1 -> {
                Angle rotate = Client.ROTATION.getRotate();
                Vec3d vector = new Angle(rotate.getYaw() + (float)(this.direction ? 0 : 90), this.pitch.get()).toVector();
                yield origin.method_1019(vector.method_18805((double)mul, 1.0, (double)mul));
            }
        };
    }

    private static enum Mode {
        Away,
        Straight;

    }
}

