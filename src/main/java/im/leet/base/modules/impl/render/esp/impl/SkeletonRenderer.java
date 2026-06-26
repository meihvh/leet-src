/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  org.joml.Vector4f
 */
package im.leet.base.modules.impl.render.esp.impl;

import im.leet.Client;
import im.leet.base.modules.impl.render.esp.ESPRenderer;
import im.leet.utils.client.ClientSettings;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.joml.Vector4f;

public class SkeletonRenderer
extends ESPRenderer {
    public SkeletonRenderer(ESPRenderer.Align align) {
        super(align);
    }

    @Override
    public void render(float minX, float minY, float maxX, float maxY, float width, Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        float w = maxX - minX;
        float h = maxY - minY;
        if (w <= 0.0f || h <= 0.0f) {
            return;
        }
        float cx = minX + w / 2.0f;
        float headTopY = minY;
        float neckY = minY + h * 0.15f;
        float shouldersY = minY + h * 0.22f;
        float chestY = minY + h * 0.3f;
        float pelvisY = minY + h * 0.58f;
        float kneesY = minY + h * 0.8f;
        float feetY = maxY;
        float shoulderOffsetX = w * 0.25f;
        float handOffsetX = w * 0.33f;
        float hipOffsetX = w * 0.18f;
        float leftShoulderX = minX + shoulderOffsetX;
        float rightShoulderX = maxX - shoulderOffsetX;
        float leftHandX = minX + handOffsetX;
        float rightHandX = maxX - handOffsetX;
        float leftHipX = cx - hipOffsetX;
        float rightHipX = cx + hipOffsetX;
        Color c0 = ClientSettings.INSTANCE.getColor(0);
        this.vline(cx, headTopY, neckY, width, c0);
        this.vline(cx, neckY, pelvisY, width, c0);
        this.hline(leftShoulderX, rightShoulderX, shouldersY, width, c0);
        this.vline(leftShoulderX, shouldersY, chestY, width, c0);
        this.vline(rightShoulderX, shouldersY, chestY, width, c0);
        this.vline(leftHandX, chestY, chestY + h * 0.12f, width, c0);
        this.vline(rightHandX, chestY, chestY + h * 0.12f, width, c0);
        this.hline(leftHipX, rightHipX, pelvisY, width, c0);
        this.vline(leftHipX, pelvisY, kneesY, width, c0);
        this.vline(rightHipX, pelvisY, kneesY, width, c0);
        this.vline(leftHipX, kneesY, feetY, width, c0);
        this.vline(rightHipX, kneesY, feetY, width, c0);
    }

    private void hline(float x1, float x2, float y, float thickness, Color color) {
        if (x2 < x1) {
            float t = x1;
            x1 = x2;
            x2 = t;
        }
        Client.RENDERER.rect(x1, y - thickness / 2.0f, x2 - x1, thickness, new Vector4f(0.0f), 0.0f, color, color, color, color);
    }

    private void vline(float x, float y1, float y2, float thickness, Color color) {
        if (y2 < y1) {
            float t = y1;
            y1 = y2;
            y2 = t;
        }
        Client.RENDERER.rect(x - thickness / 2.0f, y1, thickness, y2 - y1, new Vector4f(0.0f), 0.0f, color, color, color, color);
    }
}

