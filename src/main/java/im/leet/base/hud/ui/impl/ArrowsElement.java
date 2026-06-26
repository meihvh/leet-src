/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RotationAxis
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Quaternionfc
 *  org.joml.Vector4f
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.modules.impl.render.Interface;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionfc;
import org.joml.Vector4f;

public class ArrowsElement
extends HudElement {
    public float ANGLE = 0.0f;

    public ArrowsElement(String name, Drag drag) {
        super(name, drag);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float tick = mc.method_61966().method_60637(true);
        this.ANGLE = MathHelper.method_17821((float)tick, (float)this.ANGLE, (float)(this.ANGLE + MathHelper.method_15393((float)(ArrowsElement.mc.field_1773.method_19418().method_19330() - this.ANGLE)) * 0.1f));
        Vec3d lLerp = ArrowsElement.mc.field_1724.method_30950(tick);
        CRenderSystem cRenderSystem = Client.RENDERER.getCrenderSystem();
        MatrixStack stack = Client.RENDERER.getStack();
        List players = ArrowsElement.mc.field_1687.method_18456();
        boolean showDistance = players.size() < 20;
        float F = ArrowsElement.mc.field_1773.method_19418().method_19329();
        for (PlayerEntity player : players) {
            if (!(player instanceof AbstractClientPlayerEntity)) continue;
            AbstractClientPlayerEntity p = (AbstractClientPlayerEntity)player;
            if (player instanceof ClientPlayerEntity) continue;
            stack.method_22903();
            Vec3d pLerp = p.method_30950(tick);
            Color color = ClientSettings.INSTANCE.targetValidator.getColor((LivingEntity)player);
            if (color == null) {
                color = ClientSettings.INSTANCE.getColor(0);
            }
            float fY = Interface.INSTANCE.arrows3d.get() ? F / 90.0f : 1.0f;
            double dist = Interface.INSTANCE.arrows3d.get() ? pLerp.method_1022(lLerp) : 1.0;
            dist = Math.max(1.0, dist / 50.0);
            double d = lLerp.field_1352 - pLerp.field_1352;
            double e = lLerp.field_1350 - pLerp.field_1350;
            double f = lLerp.field_1351 - pLerp.field_1351;
            double angle = Math.atan2(d, e) + (double)this.ANGLE * (Math.PI / 180);
            float x = (float)((double)((float)mc.method_22683().method_4486() / 2.0f) + Math.sin(angle) * 90.0);
            float y = (float)((double)((float)mc.method_22683().method_4502() / 2.0f) + Math.cos(angle) * 90.0 * (double)fY);
            stack.method_46416(MathUtility.scaledX(x), MathUtility.scaledY(y), 0.0f);
            stack.method_22907((Quaternionfc)RotationAxis.field_40717.rotation((float)(angle - 1.5707963267948966)));
            stack.method_46416(-MathUtility.scaledX(x), -MathUtility.scaledY(y), 0.0f);
            Client.RENDERER.texture(Identifier.method_60655((String)"leet", (String)"images/ui/triangle.png"), x - 21.0f, y - 21.0f + fY, 42.0f, 42.0f, 0.0f, new Vector4f(0.0f), color, color, color, color);
            stack.method_22909();
            if (!showDistance) continue;
            String text = String.format("%s", Math.ceil(pLerp.method_1022(lLerp) * 10.0) / 10.0);
            Client.RENDERER.textCentered(text, x, y + 4.0f, TextureUse.SFMEDIUM, 6.0f, ClientColors.FORE_COLOR);
            if (!(MathUtility.delta((float)lLerp.field_1351, (float)pLerp.field_1351) > 10.0f)) continue;
            Client.RENDERER.text(lLerp.field_1351 > pLerp.field_1351 ? IconUse.DOWN : IconUse.UP, x - Client.RENDERER.textWidth(text, TextureUse.SFMEDIUM, 6.0f), y + 3.0f, TextureUse.ICONS, 8.0f, ClientColors.FORE_COLOR);
        }
    }
}

