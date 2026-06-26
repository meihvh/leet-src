/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.TextureUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.rotations.Angle;
import im.leet.utils.client.ClientColors;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class InfoElement
extends HudElement {
    public InfoElement(Drag drag) {
        super("Info", drag);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        ClientPlayerEntity player = InfoElement.mc.field_1724;
        Vec3d pos = player.method_19538();
        Angle rotate = Client.ROTATION.getRotate();
        if (rotate == null) {
            rotate = Angle.fromPlayer();
        }
        Client.RENDERER.text(String.format("XYZ: %.1f, %.1f, %.1f; Yaw: %.1f, Pitch: %.1f", pos.field_1352, pos.field_1351, pos.field_1350, Float.valueOf(rotate.getYaw()), Float.valueOf(rotate.getPitch())), this.x, this.y, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
    }
}

