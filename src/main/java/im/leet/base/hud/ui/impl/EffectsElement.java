/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.hud.InGameHud
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.text.Text
 *  org.joml.Vector4f
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.client.TextUtility;
import im.leet.utils.client.mixin.IStatusEffectInstance;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.joml.Vector4f;

public class EffectsElement
extends HudElement {
    public static final EffectsElement INSTANCE = new EffectsElement(new Drag("Effects", () -> true).bound(20.0f, 20.0f, 100.0f, 100.0f));
    ArrayList<StatusEffectInstance> effects = new ArrayList();
    EventBus<Event> gameTick = event -> {
        if (event instanceof EventGameTick) {
            Collection localEffects = EffectsElement.mc.field_1724.method_6026();
            for (StatusEffectInstance effect : localEffects) {
                if (this.effects.contains(effect)) continue;
                this.effects.add(effect);
            }
        }
    };

    private EffectsElement(Drag drag) {
        super("Effects", drag);
        Client.EVENTS.register(this);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.animation.setDuration(300);
        Collection localEffects = EffectsElement.mc.field_1724.method_6026();
        boolean shown = EffectsElement.mc.field_1755 instanceof ChatScreen || !this.effects.isEmpty();
        float origin = this.animation.getOutput();
        float anim = 1.0f - origin;
        float lerpAll = 0.0f;
        float mx = 0.0f;
        if ((double)anim > 0.1) {
            MatrixStack stack = Client.RENDERER.getStack();
            CRenderSystem system = Client.RENDERER.getCrenderSystem();
            float alpha = system.alpha();
            system.alpha(anim);
            system.push(this.x, this.y, this.width, this.height);
            this.drawBase(IconUse.POTION, "Effects", 0.0f, 0.0f);
            Color white = Color.WHITE;
            float yy = this.y + 16.0f + 4.0f;
            for (StatusEffectInstance effect : this.effects) {
                stack.method_22903();
                IStatusEffectInstance ilerp = (IStatusEffectInstance)effect;
                float lerp = ilerp.getLerp();
                ilerp.setLerp(MathUtility.linearFps(lerp, localEffects.contains(effect) ? 1.0f : 0.0f, 10.0f));
                stack.method_46416(MathUtility.scaledX(this.x + this.width / 2.0f), MathUtility.scaledY(yy), 0.0f);
                stack.method_22905(1.0f, lerp, 1.0f);
                stack.method_46416(-MathUtility.scaledX(this.x + this.width / 2.0f), -MathUtility.scaledY(yy), 0.0f);
                lerpAll += lerp;
                float alpha2 = system.alpha();
                system.alpha(alpha2 * lerp);
                String maintext = Text.method_43471((String)effect.method_5586()).getString();
                float wd = Client.RENDERER.textWidth(maintext, TextureUse.SFMEDIUM, 8.0f);
                float off = (1.0f - lerp) * (wd + 21.0f);
                Client.RENDERER.textureRaw(InGameHud.method_71644((RegistryEntry)effect.method_5579()).method_45138("textures/").method_48331(".png"), this.x + 4.0f - off, yy, 12.0f, 12.0f, 0.0f, new Vector4f(0.0f), white, white, white, white);
                Client.RENDERER.text(maintext, this.x + 20.0f - off, yy + 1.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
                Client.RENDERER.text(String.valueOf(effect.method_5578() + 1), this.x + wd + 23.0f - off, yy + 1.0f, TextureUse.SFMEDIUM, 8.0f, ClientSettings.INSTANCE.getColor(0));
                String text = TextUtility.ticksToTime(effect.method_5584());
                float durwidth = Client.RENDERER.textWidth(text, TextureUse.SFMEDIUM, 8.0f);
                Client.RENDERER.text(text, this.x + this.width - 4.0f - durwidth + (1.0f - lerp) * (durwidth + 4.0f), yy + 1.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
                yy += 16.0f * lerp;
                mx = Math.max(durwidth + wd + 20.0f, mx);
                system.alpha(alpha2);
                stack.method_22909();
            }
            system.pop();
            system.alpha(alpha);
        }
        this.drag.height = this.height = Math.max(20.0f, MathUtility.linearFps(this.drag.height, 20.0f + 16.0f * lerpAll, 10.0f));
        this.drag.width = this.width = MathUtility.linearFps(this.drag.width, Math.max(mx + 20.0f, 100.0f), 10.0f);
        this.animation.setDirection(shown || (double)lerpAll > 0.1 ? Direction.BACKWARDS : Direction.FORWARDS);
        this.effects.removeIf(e -> ((IStatusEffectInstance)e).getLerp() <= 0.05f);
    }
}

