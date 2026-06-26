/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.base.modules.impl.render;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.EventPriority;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventGetFov;
import im.leet.api.events.list.EventScroll;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.slider.SliderSetting;
import net.minecraft.util.math.MathHelper;

public class Zoom
extends Module {
    public static final Zoom INSTANCE = new Zoom();
    final SliderSetting amount = this.sliderSetting("Amount", 6.0f, 1.0f, 100.0f);
    final SliderSetting scroll = this.sliderSetting("Scroll sensitivity", 1.0f, 0.0f, 100.0f).increment(0.1f);
    final CheckBox smooth = this.checkbox("Smooth", true);
    final CheckBox cinematic = this.checkbox("Cinematic", false);
    public final CheckBox hands = this.checkbox("Hands", false);
    private boolean prevCinematic;
    private double time;
    private double value;
    private double prevSensitivity;
    @EventPriority(value=-50)
    EventBus<Event> events = event -> {
        Event e;
        if (event instanceof EventGameTick) {
            Zoom.mc.field_1690.field_1914 = this.cinematic.get();
            if (!this.cinematic.get()) {
                Zoom.mc.field_1690.method_42495().method_41748((Object)(this.prevSensitivity / Math.max(this.getScaling() * 0.5, 1.0)));
            }
        }
        if (event instanceof EventGetFov) {
            e = (EventGetFov)event;
            ((EventGetFov)e).fov /= (float)this.getScaling();
        }
        if (event instanceof EventScroll) {
            e = (EventScroll)event;
            if (this.scroll.get() > 0.0f) {
                this.value += ((EventScroll)e).vertical * 0.25 * ((double)this.scroll.get() * this.value);
                if (this.value < 1.0) {
                    this.value = 1.0;
                }
                e.cancel();
            }
        }
        if (event instanceof Event3D) {
            if (!this.smooth.get()) {
                this.time = 1.0;
                return;
            }
            this.time += (double)mc.method_61966().method_60637(true);
            this.time = MathHelper.method_15350((double)this.time, (double)0.0, (double)1.0);
        }
    };

    private Zoom() {
        super("Zoom", Category.RENDER, "", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        this.value = this.amount.get();
        this.time = 0.001;
        this.prevCinematic = Zoom.mc.field_1690.field_1914;
        this.prevSensitivity = (Double)Zoom.mc.field_1690.method_42495().method_41753();
    }

    @Override
    protected void onDisable() {
        Zoom.mc.field_1690.field_1914 = this.prevCinematic;
        Zoom.mc.field_1690.method_42495().method_41748((Object)this.prevSensitivity);
    }

    double getScaling() {
        double delta = this.time < 0.5 ? 4.0 * this.time * this.time * this.time : 1.0 - Math.pow(-2.0 * this.time + 2.0, 3.0) / 2.0;
        return MathHelper.method_16436((double)delta, (double)1.0, (double)this.value);
    }
}

