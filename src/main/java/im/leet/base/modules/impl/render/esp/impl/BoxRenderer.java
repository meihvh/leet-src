/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  org.joml.Vector4f
 */
package im.leet.base.modules.impl.render.esp.impl;

import im.leet.Client;
import im.leet.api.render.ClientRenderer;
import im.leet.base.modules.impl.player.countermine.SkinEntityUtility;
import im.leet.base.modules.impl.render.ESP;
import im.leet.base.modules.impl.render.esp.ESPRenderer;
import im.leet.utils.client.ClientSettings;
import java.awt.Color;
import net.minecraft.entity.Entity;
import org.joml.Vector4f;

public class BoxRenderer
extends ESPRenderer {
    public BoxRenderer(ESPRenderer.Align align) {
        super(align);
    }

    @Override
    public void render(float minX, float minY, float maxX, float maxY, float width, Entity entity) {
        Color color2;
        Color color1;
        float corner = ESP.INSTANCE.corners.get() ? ESP.INSTANCE.cornersLenght.get() : 1.0f;
        float acorner = 1.0f - corner;
        float w = maxX - minX;
        float h = maxY - minY;
        if (this.isCountermineEntity(entity)) {
            Color baseColor;
            color1 = baseColor = this.getCountermineColor(entity);
            color2 = baseColor;
        } else {
            color1 = ClientSettings.INSTANCE.getColor(0);
            color2 = ClientSettings.INSTANCE.getColor(90);
        }
        int a = (int)corner;
        int b = (int)(w / corner);
        ClientRenderer render = Client.RENDERER;
        render.rect(minX, minY, w * corner, width, new Vector4f(0.0f), 0.0f, color1, color1, color2, color2);
        render.rect(minX + w * acorner, minY, w - w * acorner, width, new Vector4f(0.0f), 0.0f, color1, color1, color2, color2);
        render.rect(minX, maxY, w * corner, width, new Vector4f(0.0f), 0.0f, color2, color2, color1, color1);
        render.rect(minX + w * acorner, maxY, w - w * acorner + width, width, new Vector4f(0.0f), 0.0f, color2, color2, color1, color1);
        render.rect(minX, minY, width, h * corner, new Vector4f(0.0f), 0.0f, color1, color2, color2, color1);
        render.rect(minX, minY + h * acorner, width, h - h * acorner, new Vector4f(0.0f), 0.0f, color1, color2, color2, color1);
        render.rect(maxX, minY, width, h * corner, new Vector4f(0.0f), 0.0f, color2, color1, color1, color2);
        render.rect(maxX, minY + h * acorner, width, h - h * acorner, new Vector4f(0.0f), 0.0f, color2, color1, color1, color2);
    }

    private boolean isCountermineEntity(Entity entity) {
        String skinModel = SkinEntityUtility.getEntitySkinModel(entity);
        return SkinEntityUtility.COUNTERMINE_SKINS.contains(skinModel);
    }

    private Color getCountermineColor(Entity entity) {
        boolean isTeammate = SkinEntityUtility.isTeammate(entity);
        if (isTeammate) {
            return ESP.INSTANCE.countermineTeamColor.get();
        }
        return ESP.INSTANCE.countermineEnemyColor.get();
    }
}

