/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 */
package im.leet.base.modules.impl.movement.elytrafly;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventKey;
import im.leet.api.events.list.EventMoveVelocity;
import im.leet.api.render.system.IconUse;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.movement.elytrafly.ElytraFlyGrim;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.choice.ChoiceSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.InputUtility;
import im.leet.utils.player.MoveUtility;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ElytraFly
extends Module {
    public static final ElytraFly INSTANCE = new ElytraFly();
    final Group mainSettings = this.group("Main");
    final CheckBox start = this.mainSettings.checkbox("Instant start", true);
    final Group stop = this.mainSettings.group("Instant stop").toggleable(true);
    final KeybindSetting stopBind = this.stop.keybindSetting("Stop bind", 341);
    final CheckBox stopFreeze = this.stop.checkbox("Freeze", true);
    boolean cruise;
    final CheckBox cruiseControl = (CheckBox)this.mainSettings.checkbox("Cruise control", true).onChanged(ignored -> {
        this.cruise = false;
    });
    final KeybindSetting cruiseKey;
    final CheckBox checkGround;
    final Group speedLimiter;
    final SliderSetting maxBps;
    final ChoiceSetting<Choice> mode;
    public float forceForward;
    public float forceStrafe;
    public boolean forceStart;
    public boolean forceStop;
    public boolean forceUp;
    EventBus<Event> events;

    private ElytraFly() {
        super("ElytraFly", Category.MOVEMENT, "Hypersonic elytra flight", new Tag[0]);
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.cruiseControl::get;
        this.cruiseKey = (KeybindSetting)this.mainSettings.keybindSetting("Cruise key", 342).visible(supplierArray);
        this.checkGround = this.mainSettings.checkbox("Check Ground", true);
        this.speedLimiter = this.group("Speed limiter").toggleable(false);
        this.maxBps = this.speedLimiter.sliderSetting("Max BPS", 100.0f, 0.0f, 1000.0f);
        this.mode = this.choiceSetting("Mode", 0, new Choice[]{new ElytraFlyGrim()});
        this.events = event -> {
            Event e;
            if (event instanceof EventGameTick) {
                if (!MoveUtility.hasElytra()) {
                    return;
                }
                if (this.stop.isEnabled() && ElytraFly.mc.field_1724.method_6128() && ElytraFly.mc.field_1755 == null && (this.forceStop || InputUtility.isKeyPressed(this.stopBind.getBind()))) {
                    MoveUtility.stopGliding();
                    this.forceStop = false;
                    if (this.stopFreeze.get()) {
                        ElytraFly.mc.field_1724.method_18800(0.0, 0.0, 0.0);
                    }
                    return;
                }
                if (this.start.get() && !ElytraFly.mc.field_1724.method_6128() && !ElytraFly.mc.field_1724.method_24828() && (ElytraFly.mc.field_1724.field_3913.field_54155.comp_3163() || this.forceStart) && ElytraFly.mc.field_1724.method_18798().field_1351 != 0.0) {
                    MoveUtility.startGliding();
                    this.forceStart = false;
                }
            }
            if (event instanceof EventMoveVelocity) {
                e = (EventMoveVelocity)event;
                if (!MoveUtility.hasElytra() || ElytraFly.mc.field_1724.method_5765() || ElytraFly.mc.field_1724.method_31549().field_7477 || ElytraFly.mc.field_1724.method_6059(StatusEffects.field_5902) || this.speedLimiter.isEnabled() && MoveUtility.getBPS((LivingEntity)ElytraFly.mc.field_1724) > this.maxBps.get() || !ElytraFly.mc.field_1724.method_6128() || ElytraFly.mc.field_1724.method_24828() && this.checkGround.get()) {
                    return;
                }
                this.mode.onEvent(e);
            }
            if (event instanceof EventKey) {
                e = (EventKey)event;
                if (this.cruiseControl.get() && ((EventKey)e).action == 0 && ((EventKey)e).key == this.cruiseKey.getBind()) {
                    this.cruise = !this.cruise;
                    MutableText module = Text.method_30163((String)"\u041a\u0440\u0443\u0438\u0437 \u043a\u043e\u043d\u0442\u0440\u043e\u043b\u044c ").method_27661().method_54663(ClientColors.MAIN_COLOR.getRGB());
                    MutableText enableOrdisable = Text.method_30163((String)(this.cruise ? "\u0432\u043a\u043b\u044e\u0447\u0435\u043d!" : "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d!")).method_27661().method_54663(this.cruise ? new Color(0, 200, 0, 255).getRGB() : new Color(200, 0, 0, 255).getRGB());
                    MutableText mainText = Text.method_30163((String)"[ElytraFly] ").method_27661().method_10852((Text)module).method_27661().method_10852((Text)enableOrdisable);
                    Client.NOTIFIES.add((Text)mainText, IconUse.INFO, 2000L);
                }
            }
        };
    }

    public static enum Mode {
        Grim,
        None;

    }
}

