/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Quaternionfc
 *  org.joml.Vector4f
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.TextureUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionfc;
import org.joml.Vector4f;

public class CompassElement
extends HudElement {
    public CompassElement(Drag drag) {
        super("Compass", drag);
        this.enabled = false;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (CompassElement.mc.field_1724 == null) {
            return;
        }
        this.width = 50.0f;
        this.height = 50.0f;
        Color c = ColorUtility.injectAlpha(ClientColors.BACK_COLOR, 160.0f);
        Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f(this.width), 1.0f, c, c, c, c);
        Color color = ClientColors.FORE_COLOR;
        double zeroYaw = -Math.atan2(-CompassElement.mc.field_1724.method_23317(), -CompassElement.mc.field_1724.method_23321());
        double zeroCos = Math.cos(zeroYaw);
        double zeroSin = Math.sin(zeroYaw);
        float zeroX = this.x + this.width / 2.0f + (float)zeroCos * this.width / 2.0f;
        float zeroY = this.y + this.height / 2.0f + (float)zeroSin * this.height / 2.0f;
        Client.RENDERER.textCentered("O", zeroX, zeroY, TextureUse.SFMEDIUM, 9.0f, color);
        Client.RENDERER.textCentered("+X", this.x + this.width / 2.0f, this.y - 10.0f, TextureUse.SFMEDIUM, 9.0f, color);
        Client.RENDERER.textCentered("-X", this.x + this.width / 2.0f, this.y + this.height - 1.0f, TextureUse.SFMEDIUM, 9.0f, color);
        Client.RENDERER.text("-Z", this.x - 12.0f, this.y + this.height / 2.0f - 6.0f, TextureUse.SFMEDIUM, 9.0f, color);
        Client.RENDERER.text("+Z", this.x + this.width - 1.0f, this.y + this.height / 2.0f - 6.0f, TextureUse.SFMEDIUM, 9.0f, color);
        MatrixStack stack = Client.RENDERER.getStack();
        stack.method_22903();
        stack.method_46416(this.x * 2.0f + this.width, this.y * 2.0f + this.height, 0.0f);
        stack.method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees(CompassElement.mc.field_1724.method_36454()));
        stack.method_46416(-5.0f, -5.0f, 0.0f);
        Client.RENDERER.rect(0.0f, 0.0f, 27.5f, 5.0f, new Vector4f(3.0f), 1.0f, color, color, color, color);
        stack.method_22909();
    }
}

