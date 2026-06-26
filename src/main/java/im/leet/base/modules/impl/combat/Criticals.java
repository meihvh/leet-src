/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventAttack;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Criticals
extends Module {
    public static final Criticals INSTANCE = new Criticals();
    Group general = this.group("General");
    public EnumSetting<Mode> modes = this.general.enumSetting("Anticheat compatibility", Mode.None);
    EventBus<Event> events = event -> {
        if (event instanceof EventAttack) {
            EventAttack ev = (EventAttack)event;
            this.critPacket(1.0E-6, true);
            this.critPacket(-1.0E-6, false);
        }
    };

    private Criticals() {
        super("Criticals", Category.COMBAT, "Tries to make it easier to land a critical hit", new Tag[0]);
    }

    private void critPacket(double offset, boolean onGround) {
        boolean bl = Criticals.mc.field_1724.field_5976;
        float yaw = Criticals.mc.field_1724.method_36454();
        float pitch = Criticals.mc.field_1724.method_36455();
        mc.method_1562().method_52787((Packet)new PlayerMoveC2SPacket.Full(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + offset, Criticals.mc.field_1724.method_23321(), yaw, pitch, onGround, bl));
    }

    public static enum Mode {
        None,
        Grim,
        NCP;

    }
}

