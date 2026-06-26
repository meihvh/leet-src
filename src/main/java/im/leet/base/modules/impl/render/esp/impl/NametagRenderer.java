/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.text.Text
 *  net.minecraft.util.StringHelper
 *  org.joml.Vector4f
 */
package im.leet.base.modules.impl.render.esp.impl;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.base.modules.impl.render.esp.ESPRenderer;
import im.leet.utils.client.ClientColors;
import java.awt.Color;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import org.joml.Vector4f;

public class NametagRenderer
extends ESPRenderer {
    public NametagRenderer(ESPRenderer.Align align) {
        super(align);
    }

    @Override
    public void render(float minX, float minY, float maxX, float maxY, float width, Entity entity) {
        AbstractClientPlayerEntity p;
        Text text;
        Text text2 = text = entity.method_5797() == null ? entity.method_5476() : entity.method_5797();
        if (entity instanceof ItemEntity) {
            ItemEntity e = (ItemEntity)entity;
            text = e.method_6983().method_7964();
        }
        float textwidth = Client.RENDERER.textWidth(StringHelper.method_15440((String)text.getString()), TextureUse.SFMEDIUM, 6.5f);
        float textwidth2 = textwidth + 6.0f;
        float espwidth = maxX - minX;
        float f = Client.RENDERER.getCrenderSystem().alpha();
        Color color = entity instanceof AbstractClientPlayerEntity && Client.FRIENDS.isFriend((PlayerEntity)(p = (AbstractClientPlayerEntity)entity)) ? ClientColors.FRIEND_COLOR : ClientColors.BACK_COLOR;
        Client.RENDERER.blur(minX + espwidth / 2.0f - textwidth2 / 2.0f, minY - 16.0f, textwidth2, 10.0f, new Vector4f(0.0f), 5.0f, 0.0f);
        Client.RENDERER.getCrenderSystem().alpha(0.3f);
        Client.RENDERER.rect(minX + espwidth / 2.0f - textwidth2 / 2.0f, minY - 16.0f, textwidth2, 10.0f, new Vector4f(0.0f), 1.0f, color, color, color, color);
        Client.RENDERER.getCrenderSystem().alpha(f);
        Client.RENDERER.text(text, minX + espwidth / 2.0f - 1.0f - textwidth / 2.0f, minY - 15.0f, TextureUse.SFMEDIUM, 6.5f);
    }
}

