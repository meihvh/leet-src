/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement.speed;

import im.leet.api.events.Event;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventMove;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SpeedSpartan4043
extends Choice {
    public SpeedSpartan4043() {
        super("Spartan 4.0.43");
    }

    @Override
    public void onEvent(Event event) {
        Event e;
        if (event instanceof EventInput) {
            e = (EventInput)event;
            if (MoveUtility.hasElytra()) {
                SpeedSpartan4043.mc.field_1724.method_23669();
                NetworkUtility.send(new ClientCommandC2SPacket((Entity)SpeedSpartan4043.mc.field_1724, ClientCommandC2SPacket.Mode.field_12982));
                if (((EventInput)e).isJump() && SpeedSpartan4043.mc.field_1724.method_24828()) {
                    NetworkUtility.sendOnlySneak(!((EventInput)e).isSneak());
                    NetworkUtility.sendOnlySneak(((EventInput)e).isSneak());
                    ((EventInput)e).setJump(false);
                    SpeedSpartan4043.mc.field_1724.method_6043();
                    Vec3d vec3d = SpeedSpartan4043.mc.field_1724.method_18798();
                    SpeedSpartan4043.mc.field_1724.method_18800(vec3d.field_1352, (double)0.35f, vec3d.field_1350);
                    float g = SpeedSpartan4043.mc.field_1724.method_36454() * ((float)Math.PI / 180);
                    float m = 0.3f;
                    SpeedSpartan4043.mc.field_1724.method_60491(new Vec3d((double)(-MathHelper.method_15374((float)g) * m), 0.0, (double)(MathHelper.method_15362((float)g) * m)));
                    NetworkUtility.sendOnlySneak(((EventInput)e).isSneak());
                } else if (!SpeedSpartan4043.mc.field_1724.method_6128() || SpeedSpartan4043.mc.field_1724.field_6012 % 2 != 0 || SpeedSpartan4043.mc.field_1724.method_24828()) {
                    // empty if block
                }
            }
        }
        if (event instanceof EventMove) {
            e = (EventMove)event;
            if (SpeedSpartan4043.mc.field_1724.method_6128() && SpeedSpartan4043.mc.field_1724.field_6017 > 3.25) {
                ((EventMove)e).y += (double)0.01f;
                SpeedSpartan4043.mc.field_1724.field_6017 = 0.0;
            }
        }
    }
}

