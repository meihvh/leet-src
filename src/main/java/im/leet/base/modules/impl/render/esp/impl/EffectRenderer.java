/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.text.Text
 *  org.joml.Vector4f
 */
package im.leet.base.modules.impl.render.esp.impl;

import im.leet.Client;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.base.modules.impl.render.esp.ESPRenderer;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.TextUtility;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import org.joml.Vector4f;

public class EffectRenderer
extends ESPRenderer {
    public EffectRenderer(ESPRenderer.Align align) {
        super(align);
    }

    @Override
    public void render(float minX, float minY, float maxX, float maxY, float width, Entity entity) {
        float off = 5.0f;
        if (entity instanceof LivingEntity) {
            LivingEntity e = (LivingEntity)entity;
            for (StatusEffectInstance s : e.method_6026()) {
                if (s.method_5584() == 0) continue;
                String text = Text.method_43471((String)s.method_5586()).getString();
                String duration = TextUtility.ticksToTime(s.method_5584());
                float textWidth = Client.RENDERER.textWidth(text, TextureUse.SFMEDIUM, 7.0f);
                float durWidth = Client.RENDERER.textWidth(duration, TextureUse.SFMEDIUM, 7.0f);
                float wd = maxX - minX;
                float f = Client.RENDERER.getCrenderSystem().alpha();
                float alwd = textWidth + durWidth + 24.0f;
                Client.RENDERER.blur(minX + wd / 2.0f - alwd / 2.0f - 2.5f, maxY + off - 1.0f, alwd, 10.5f, new Vector4f(0.0f), 5.0f, 0.0f);
                Client.RENDERER.getCrenderSystem().alpha(0.3f);
                Client.RENDERER.rect(minX + wd / 2.0f - alwd / 2.0f - 2.5f, maxY + off - 1.0f, alwd, 10.5f, new Vector4f(0.0f), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
                Client.RENDERER.getCrenderSystem().alpha(f);
                Client.RENDERER.textCentered(String.format("%s %s", text, s.method_5578() + 1), minX + wd / 2.0f - durWidth / 2.0f - 2.0f, maxY + off, TextureUse.SFMEDIUM, 7.0f, new Color(((StatusEffect)s.method_5579().comp_349()).method_5556()));
                Client.RENDERER.text(duration, minX + wd / 2.0f + textWidth / 2.0f - 6.0f, maxY + off, TextureUse.SFMEDIUM, 7.0f, ClientColors.FORE_COLOR);
                boolean beneficial = ((StatusEffect)s.method_5579().comp_349()).method_5573();
                Client.RENDERER.text(beneficial ? IconUse.UP : IconUse.DOWN, minX + wd / 2.0f - textWidth / 2.0f - 12.0f - durWidth / 2.0f, maxY + off, TextureUse.ICONS, 7.0f, beneficial ? ClientColors.GREEN : ClientColors.UI_RED);
                off += 10.0f;
            }
        }
    }
}

