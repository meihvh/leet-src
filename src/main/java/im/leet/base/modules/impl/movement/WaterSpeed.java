/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.PlayerInput
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.Angle;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;

public class WaterSpeed
extends Module {
    public static final WaterSpeed INSTANCE = new WaterSpeed();
    public EnumSetting<Mode> mode = this.enumSetting("\u0420\u0435\u0436\u0438\u043c", Mode.FuntimeJump);
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (WaterSpeed.mc.field_1724 == null || WaterSpeed.mc.field_1687 == null) {
                return;
            }
            switch (this.mode.get().ordinal()) {
                case 0: {
                    if (!WaterSpeed.mc.field_1724.method_5799()) break;
                    Client.ROTATION.rotate(new Angle(WaterSpeed.mc.field_1724.method_36454(), 32.0f), false);
                    if (!WaterSpeed.mc.field_1724.method_24828()) break;
                    WaterSpeed.mc.field_1724.method_6043();
                    Vec3d vec = WaterSpeed.mc.field_1724.method_18798();
                    WaterSpeed.mc.field_1724.method_18800(vec.field_1352, 0.25, vec.field_1350);
                    PlayerInput playerInput = WaterSpeed.mc.field_1724.field_3913.field_54155;
                    NetworkUtility.sendInputPacket(!playerInput.comp_3159(), !playerInput.comp_3160(), playerInput.comp_3161(), playerInput.comp_3162(), false, true, playerInput.comp_3165());
                    NetworkUtility.sendInputPacket(playerInput.comp_3159(), playerInput.comp_3160(), playerInput.comp_3161(), playerInput.comp_3162(), false, false, playerInput.comp_3165());
                }
            }
        }
    };

    private WaterSpeed() {
        super("Water speed", Category.MOVEMENT, "Makes movement in water easier", new Tag[0]);
    }

    public static enum Mode {
        FuntimeJump,
        Legit;

    }
}

