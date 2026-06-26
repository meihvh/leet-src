/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.movement.flight.DamageFly;
import im.leet.base.modules.impl.movement.flight.GrimGlideFly;
import im.leet.base.modules.impl.movement.flight.MatrixClip;
import im.leet.base.modules.impl.movement.flight.MatrixFallFly;
import im.leet.base.modules.impl.movement.flight.MatrixGlide;
import im.leet.base.modules.impl.movement.flight.NCPFly;
import im.leet.base.modules.impl.movement.flight.RWFly;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Flight
extends Module {
    public static final Flight INSTANCE = new Flight();
    public ChoiceSetting<Choice> mode = this.choiceSetting("Mode", 0, new Choice[]{new MatrixGlide(), new MatrixClip(), new DamageFly(), new MatrixFallFly(), new GrimGlideFly(), new NCPFly(), new RWFly()});
    int ticks = 0;
    long handle = 0L;
    final TimeUtility time = new TimeUtility();
    final BlinkUtility blink = new BlinkUtility();
    boolean shouldSpoofPing = false;
    Vec3d savedPos = Vec3d.field_1353;
    ArrayList<BlockPos> sentPlaces = new ArrayList();
    EventBus<Event> events = event -> this.mode.onEvent(event);

    private Flight() {
        super("Flight", Category.MOVEMENT, "Allows flight under certain conditions", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.ticks = 0;
        this.shouldSpoofPing = false;
        this.handle = 0L;
        this.savedPos = Flight.mc.field_1724.method_19538();
        this.blink.clear();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        Client.TIMER = 1.0f;
    }
}

