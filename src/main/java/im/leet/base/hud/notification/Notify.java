/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  org.joml.Vector4f
 */
package im.leet.base.hud.notification;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.ShaderUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.render.system.sys2d.TextureAtlas;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector4f;

public class Notify
extends RendererObject {
    private final Text text;
    private IconUse icon;
    private final long duration;
    private final String item;
    private float lerpX = -999.0f;
    private float lerpY = -999.0f;
    private SmoothStepAnimation animation;
    private boolean isDemo = false;
    private IconUse[] demoIcons = new IconUse[]{IconUse.CROSS, IconUse.POTION, IconUse.PLAYER, IconUse.INFO, IconUse.CHECK, IconUse.CUBE, IconUse.FIGHT, IconUse.WARN, IconUse.GLOBE, IconUse.COMPASS};

    public Notify markDemo() {
        this.isDemo = true;
        return this;
    }

    public Notify(Text text, IconUse icon, long duration) {
        this.text = Text.method_43473().method_10852(text);
        this.icon = icon;
        this.duration = System.currentTimeMillis() + duration;
        this.item = null;
        this.animation = new SmoothStepAnimation(300, 1.0);
        this.animation.reset();
    }

    public Notify(Text text, String item, long duration) {
        this.text = Text.method_43473().method_10852(text);
        this.icon = null;
        this.item = item;
        this.duration = System.currentTimeMillis() + duration;
        this.animation = new SmoothStepAnimation(300, 1.0);
        this.animation.reset();
    }

    public boolean shouldRemove() {
        return System.currentTimeMillis() >= this.duration;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.lerpX == -999.0f || this.lerpY == -999.0f) {
            this.lerpX = this.x;
            this.lerpY = this.y;
        }
        this.lerpX = MathUtility.linearFps(this.lerpX, this.x, 15.0f);
        this.lerpY = MathUtility.linearFps(this.lerpY, this.y, 15.0f);
        if (this.isDemo) {
            this.icon = this.demoIcons[(int)(System.currentTimeMillis() / 1000L) % this.demoIcons.length];
        }
        if (!this.isDemo) {
            this.animation.setDirection(this.duration - System.currentTimeMillis() > (long)this.animation.getDuration() || this.isDemo ? Direction.FORWARDS : Direction.BACKWARDS);
        }
        boolean hasIcon = this.icon != null;
        boolean hasItem = this.item != null;
        boolean shouldOversize = hasIcon || hasItem;
        float nWidth = Client.RENDERER.textWidth(this.text.getString(), TextureUse.SFMEDIUM, 7.0f) + 12.0f + (float)(shouldOversize ? 18 : 0);
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float prevAlpha = system.alpha();
        system.alpha(this.animation.getOutput());
        Client.RENDERER.drawHudRect(this.lerpX - nWidth / 2.0f, this.lerpY, nWidth, this.height);
        Client.RENDERER.text(this.text, this.lerpX - nWidth / 2.0f + 5.0f + (float)(shouldOversize ? 18 : 0), this.lerpY + 3.0f, TextureUse.SFMEDIUM, 7.0f);
        if (hasIcon) {
            Client.RENDERER.text(this.icon, this.lerpX - nWidth / 2.0f + 2.0f, this.lerpY + 3.0f, TextureUse.ICONS, 8.0f, ClientColors.FORE_COLOR);
            Client.RENDERER.rect(this.lerpX - nWidth / 2.0f + 15.5f, this.lerpY + 6.0f, 4.0f, 4.0f, new Vector4f(3.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        } else if (hasItem) {
            Identifier identifier = Identifier.method_60656((String)("textures/item/" + this.item + ".png"));
            if (system.getAtlas().has(identifier.toString())) {
                TextureAtlas.UV uv = system.getAtlas().getUV(identifier.toString());
                Client.RENDERER.rect(this.lerpX - nWidth / 2.0f + 17.5f, this.lerpY + 6.0f, 4.0f, 4.0f, new Vector4f(3.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
                system.shader(ShaderUse.TEXTURE).alpha(this.animation.getOutput()).smoothness(1.0f, 1.0f).rect(this.lerpX - nWidth / 2.0f + 3.0f, this.lerpY + 1.0f, 13.0f, 13.0f).uv(uv.u0, uv.v0, uv.u1, uv.v1).color(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE).build();
            } else {
                system.addPrepare(identifier);
            }
        }
        system.alpha(prevAlpha);
    }

    public SmoothStepAnimation getAnimation() {
        return this.animation;
    }
}

