/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector2f
 */
package im.leet.api.ui.particles;

import im.leet.api.ui.particles.Particle2D;
import java.util.ArrayList;
import org.joml.Vector2f;

public class Particles2DEngine {
    ArrayList<Particle2D> particles = new ArrayList();

    public void render() {
        for (Particle2D p : this.particles) {
            p.render(0, 0);
        }
        this.particles.removeIf(Particle2D::removed);
    }

    public void addParticle(Particle2D p) {
        this.particles.add(p);
    }

    public void addParticle(Vector2f pos, Vector2f vel, float dir, float roll, long alive) {
        this.particles.add(new Particle2D(pos, vel, dir, roll, alive));
    }
}

