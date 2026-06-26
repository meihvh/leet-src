/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.OtherClientPlayerEntity
 *  net.minecraft.entity.player.PlayerEntity
 */
package im.leet.base.modules.impl.combat.resolver;

import im.leet.base.modules.impl.combat.Resolver;
import im.leet.base.modules.impl.combat.resolver.ResolverMode;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.mixin.IOtherClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class BackTrackPosResolver
extends ResolverMode {
    public static final BackTrackPosResolver INSTANCE = new BackTrackPosResolver();
    public EnumSetting<Resolve> type = this.enumSetting("Type Resolve", Resolve.BackTrack);
    public SliderSetting backTicks = (SliderSetting)this.sliderSetting("Ticks", 15.0f, 1.0f, 50.0f).increment(1.0f).visible(() -> this.type.is(Resolve.BackTrack));

    private BackTrackPosResolver() {
        super("BackTrack Pos");
        this.toggleable(false);
    }

    public void resolvePlayers() {
        if (Resolver.INSTANCE.isEnabled() && this.isEnabled()) {
            for (PlayerEntity player : BackTrackPosResolver.mc.field_1687.method_18456()) {
                if (!(player instanceof OtherClientPlayerEntity)) continue;
                ((IOtherClientPlayerEntity)player).resolve(this.type.get());
            }
        }
    }

    public void restorePlayers() {
        if (Resolver.INSTANCE.isEnabled() && this.isEnabled()) {
            for (PlayerEntity player : BackTrackPosResolver.mc.field_1687.method_18456()) {
                if (!(player instanceof OtherClientPlayerEntity)) continue;
                ((IOtherClientPlayerEntity)player).releaseResolver();
            }
        }
    }

    public static enum Resolve {
        BackTrack,
        Advantage,
        Predictive;

    }

    public static class Position {
        private double x;
        private double y;
        private double z;
        private int ticks;

        public Position(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean shouldRemove() {
            return (float)this.ticks++ > BackTrackPosResolver.INSTANCE.backTicks.get();
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }

        public int getTicks() {
            return this.ticks;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Position)) {
                return false;
            }
            Position other = (Position)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (Double.compare(this.getX(), other.getX()) != 0) {
                return false;
            }
            if (Double.compare(this.getY(), other.getY()) != 0) {
                return false;
            }
            if (Double.compare(this.getZ(), other.getZ()) != 0) {
                return false;
            }
            return this.getTicks() == other.getTicks();
        }

        protected boolean canEqual(Object other) {
            return other instanceof Position;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            long $x = Double.doubleToLongBits(this.getX());
            result = result * 59 + (int)($x >>> 32 ^ $x);
            long $y = Double.doubleToLongBits(this.getY());
            result = result * 59 + (int)($y >>> 32 ^ $y);
            long $z = Double.doubleToLongBits(this.getZ());
            result = result * 59 + (int)($z >>> 32 ^ $z);
            result = result * 59 + this.getTicks();
            return result;
        }

        public String toString() {
            return "BackTrackPosResolver.Position(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", ticks=" + this.getTicks() + ")";
        }
    }
}

