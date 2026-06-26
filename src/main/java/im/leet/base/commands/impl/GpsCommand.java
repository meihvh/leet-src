/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RotationAxis
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Quaternionfc
 *  org.joml.Vector4f
 */
package im.leet.base.commands.impl;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event2D;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.base.commands.Command;
import im.leet.base.modules.impl.render.Interface;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionfc;
import org.joml.Vector4f;

public class GpsCommand
extends Command {
    public static GpsCommand INSTANCE = new GpsCommand();
    private float ANGLE = 0.0f;
    private Vec3d current = null;
    EventBus<Event> events = event -> {
        if (event instanceof Event2D) {
            if (this.current == null) {
                return;
            }
            float tick = mc.method_61966().method_60637(true);
            this.ANGLE = MathHelper.method_17821((float)tick, (float)this.ANGLE, (float)(this.ANGLE + MathHelper.method_15393((float)(GpsCommand.mc.field_1773.method_19418().method_19330() - this.ANGLE)) * 0.1f));
            Vec3d lLerp = GpsCommand.mc.field_1724.method_30950(tick);
            CRenderSystem cRenderSystem = Client.RENDERER.getCrenderSystem();
            MatrixStack stack = Client.RENDERER.getStack();
            List players = GpsCommand.mc.field_1687.method_18456();
            boolean showDistance = players.size() < 20;
            float F = GpsCommand.mc.field_1773.method_19418().method_19329();
            stack.method_22903();
            Color color = ClientColors.FORE_COLOR;
            float fY = Interface.INSTANCE.arrows3d.get() ? F / 90.0f : 1.0f;
            double dist = Interface.INSTANCE.arrows3d.get() ? this.current.method_1022(lLerp) : 1.0;
            dist = Math.max(1.0, dist / 50.0);
            double d = lLerp.field_1352 - this.current.field_1352;
            double e = lLerp.field_1350 - this.current.field_1350;
            double angle = Math.atan2(d, e) + (double)this.ANGLE * (Math.PI / 180);
            float x = (float)((double)((float)mc.method_22683().method_4486() / 2.0f) + Math.sin(angle) * 45.0);
            float y = (float)(120.0 + Math.cos(angle) * 45.0 * (double)fY);
            stack.method_46416(MathUtility.scaledX(x), MathUtility.scaledY(y), 0.0f);
            stack.method_22907((Quaternionfc)RotationAxis.field_40717.rotation((float)(angle - 1.5707963267948966)));
            stack.method_46416(-MathUtility.scaledX(x), -MathUtility.scaledY(y), 0.0f);
            Client.RENDERER.texture(Identifier.method_60655((String)"leet", (String)"images/ui/triangle.png"), x - 21.0f, y - 21.0f + fY, 42.0f, 42.0f, 0.0f, new Vector4f(0.0f), color, color, color, color);
            stack.method_22909();
            if (showDistance) {
                String text = String.format("%s", Math.ceil(this.current.method_1022(lLerp) * 10.0) / 10.0);
                Client.RENDERER.textCentered(text, x, y + 4.0f, TextureUse.SFMEDIUM, 6.0f, ClientColors.FORE_COLOR);
            }
        }
    };

    public GpsCommand() {
        super("gps", "set gps to coords");
        Client.EVENTS.register(this);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            if (args[1].equals("off")) {
                ChatUtility.send("gps disabled.");
                this.current = null;
            }
        } else if (args.length == 3) {
            double x = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            this.current = new Vec3d(x, 0.0, z);
            ChatUtility.send(String.format("gps set to %s", this.current));
        } else {
            this.error("usage .gps <off | x, z>");
        }
    }
}

