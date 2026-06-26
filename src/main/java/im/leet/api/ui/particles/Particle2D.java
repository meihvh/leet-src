/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package im.leet.api.ui.particles;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.utils.client.ClientSettings;
import java.awt.Color;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Particle2D
extends RendererObject {
    private static final Logger log = LoggerFactory.getLogger(Particle2D.class);
    public Vector2f vel;
    public float dir;
    public float roll;
    public long alive;
    public long deleteIn;

    public Particle2D(Vector2f pos, Vector2f vel, float dir, float roll, long alive) {
        this.x = pos.x;
        this.y = pos.y;
        this.vel = vel;
        this.dir = dir;
        this.roll = roll;
        this.alive = alive;
        this.deleteIn = System.currentTimeMillis() + alive;
    }

    public boolean removed() {
        return System.currentTimeMillis() > this.deleteIn;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.dir += this.roll;
        this.x += (float)(Math.sin((double)this.dir * (Math.PI / 180)) * (double)this.vel.x);
        this.y += (float)(Math.cos((double)this.dir * (Math.PI / 180)) * (double)this.vel.y);
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float alpha = system.alpha();
        float f = (float)Math.min(255L, this.deleteIn - System.currentTimeMillis()) / 255.0f;
        system.alpha(f * alpha);
        Color c = ClientSettings.INSTANCE.getColor((int)this.x * 4);
        Client.RENDERER.textureRaw(Identifier.method_60655((String)"leet", (String)"images/world/ghost-glow.png"), this.x - 5.0f, this.y - 5.0f, 10.0f, 10.0f, 1.0f, new Vector4f(0.0f), c, c, c, c);
        system.alpha(alpha);
    }
}

