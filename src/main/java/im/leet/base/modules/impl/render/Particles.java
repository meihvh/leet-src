/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.render;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.render.renderer.ParticleRenderer;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.math.Vec3d;

public final class Particles
extends Module {
    public static final Particles INSTANCE = new Particles();
    public final SliderSetting spawnRate = this.sliderSetting("Spawn Rate", 3.0f, 1.0f, 10.0f);
    public final SliderSetting lifetime = this.sliderSetting("Lifetime", 2000.0f, 500.0f, 5000.0f).increment(100.0f);
    public final SliderSetting size = this.sliderSetting("Size", 0.15f, 0.05f, 0.5f).increment(0.01f);
    public final SliderSetting radius = this.sliderSetting("Spawn Radius", 2.5f, 1.0f, 5.0f).increment(0.1f);
    public final SliderSetting gravity = this.sliderSetting("Gravity", 0.5f, 0.0f, 2.0f).increment(0.01f);
    public final SliderSetting airResistance = this.sliderSetting("Air Resistance", 0.98f, 0.0f, 1.0f).increment(0.01f);
    public final SliderSetting velocityMagnitude = this.sliderSetting("Velocity Magnitude", 0.5f, 0.1f, 2.0f).increment(0.01f);
    public final EnumSetting<Mode> mode = this.enumSetting("Mode", Mode.Default);
    private final List<Particle> particles = new CopyOnWriteArrayList<Particle>();
    private final EventBus<Event> events = e -> {
        if (e instanceof EventGameTick) {
            if (Particles.mc.field_1724 == null) {
                return;
            }
            this.particles.removeIf(p -> !p.alive());
            if (this.particles.size() < 200) {
                Vec3d center = Particles.mc.field_1724.method_19538().method_1031(0.0, 1.0, 0.0);
                double angle = Math.random() * Math.PI * 2.0;
                double dist = Math.random() * (double)this.radius.get() * 2.0;
                Vec3d offset = new Vec3d(Math.cos(angle) * dist, Math.random() * 20.0, Math.sin(angle) * dist);
                Vec3d vel = new Vec3d((Math.random() - 0.5) * (double)this.velocityMagnitude.get(), Math.random() * (double)this.velocityMagnitude.get(), (Math.random() - 0.5) * (double)this.velocityMagnitude.get());
                this.particles.add(new Particle(center.method_1019(offset), vel, (long)this.lifetime.get(), this));
            }
        }
        if (e instanceof Event3D) {
            Event3D ev = (Event3D)e;
            ParticleRenderer.renderAll(ev, this.particles, this.size.get());
        }
    };

    private Particles() {
        super("Particles", Category.RENDER, "Adds particles to the world", new Tag[0]);
    }

    public static enum Mode {
        Default,
        Cross;

    }

    public static final class Particle {
        public Vec3d pos;
        public Vec3d vel;
        public final long start = System.currentTimeMillis();
        public final long lifetime;
        public final Particles particles;

        public Particle(Vec3d pos, Vec3d vel, long lifetime, Particles particles) {
            this.pos = pos;
            this.vel = vel;
            this.lifetime = lifetime;
            this.particles = particles;
        }

        public boolean alive() {
            return System.currentTimeMillis() - this.start <= this.lifetime;
        }

        public float alpha() {
            return 1.0f - (float)(System.currentTimeMillis() - this.start) / (float)this.lifetime;
        }

        public Vec3d position() {
            float deltaTime = (float)(System.currentTimeMillis() - this.start) / 1000.0f;
            Vec3d gravityForce = new Vec3d(0.0, (double)(-this.particles.gravity.get()), 0.0);
            Vec3d airResistanceForce = this.vel.method_1021((double)(-this.particles.airResistance.get()));
            Vec3d acceleration = gravityForce.method_1019(airResistanceForce);
            Vec3d currentVel = this.vel.method_1019(acceleration.method_1021((double)deltaTime));
            return this.pos.method_1019(currentVel.method_1021((double)deltaTime));
        }
    }
}

