/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventMove;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.slider.SliderSetting;
import net.minecraft.util.math.MathHelper;

public class FakeRot
extends Module {
    public static final FakeRot INSTANCE = new FakeRot();
    public SliderSetting yawValue = this.add(new SliderSetting("Yaw range", 3.0f, 0.0f, 90.0f).increment(1.0f));
    public SliderSetting pitchValue = this.add(new SliderSetting("Pitch range", 3.0f, 0.0f, 90.0f).increment(1.0f));
    EventBus<Event> events = event -> {
        if (event instanceof EventMove) {
            EventMove e = (EventMove)event;
            e.yaw = Client.ROTATION.getRotate().getYaw() + this.yawValue.get();
            e.pitch = MathHelper.method_15363((float)(Client.ROTATION.getRotate().getPitch() + this.pitchValue.get()), (float)-90.0f, (float)90.0f);
        }
    };

    public FakeRot() {
        super("Fake rot", Category.PLAYER, "Fake server rotation", new Tag[0]);
    }
}

