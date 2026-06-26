/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventChatMessage;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.multitext.MultiTextSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AutoEZ
extends Module {
    public static final AutoEZ INSTANCE = new AutoEZ();
    private final String[] defaultMessages = new String[]{"\\, \u0442\u044b \u0431\u0443\u0434\u0435\u0448\u044c \u043d\u0438\u0436\u043d\u0438\u043c \u0432 \u043f\u0430\u0440\u0430\u0432\u043e\u0437\u0438\u043a\u0435!", "\\ \u043f\u0435\u0440\u0435\u0434\u0430\u044e \u043f\u0440\u0438\u0432\u0435\u0442 \u0442\u0432\u043e\u0435\u0439 \u043c\u0430\u043c\u0435", "\\ \u044d\u0442\u043e \u043f\u0440\u043e\u0441\u0442\u043e \u043a\u043b\u043e\u0443\u043d\u0430\u0434\u0430", "\\ \u0447\u0451 \u043e\u043d \u043d\u0435\u0441\u0451\u0442", "\\ \u0431\u0440\u043e, \u0442\u0435\u0431\u0435 \u043d\u0430\u0434\u043e \u0442\u0440\u0435\u043d\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f", "\\ \u043c\u0438\u043d\u0443\u0442\u0430 \u043f\u043e\u0448\u043b\u0430!", "\\ \u0442\u044b \u0448\u043e \u0434\u043e\u043b\u0431\u0430\u0435\u0431?", "\\ your mom owned by leet client"};
    private final String[] killString = new String[]{"\u0443\u0431\u0438\u043b", "\u043f\u043e\u0431\u0435\u0434\u0438\u043b", "killed"};
    private final MultiTextSetting messages = this.multiTextSetting("Messages", this.defaultMessages);
    private final EnumSetting<Prefix> prefix = this.enumSetting("Prefix", Prefix.Global);
    private final CheckBox noFriends = this.checkbox("Ignore friends", true);
    private final SliderSetting delay = this.sliderSetting("Delay (s)", 5.0f, 0.0f, 10.0f);
    private final EnumSetting<Trigger> trigger = this.enumSetting("Trigger", Trigger.VanillaDeath);
    private final SliderSetting deathThreshold = this.sliderSetting("Death threshold", 0.0f, 0.0f, 1.0f).increment(0.01f);
    final TimeUtility time = new TimeUtility();
    EventBus<Event> events = event -> {
        LivingEntity patt1$temp;
        Event e;
        if (event instanceof EventAttack) {
            Entity patt0$temp;
            e = (EventAttack)event;
            if (this.trigger.is(Trigger.Hit) && (patt0$temp = e.target) instanceof PlayerEntity) {
                PlayerEntity target = (PlayerEntity)patt0$temp;
                this.send(target);
            }
        }
        if (event instanceof EventChatMessage) {
            e = (EventChatMessage)event;
            if (this.trigger.is(Trigger.ChatKill)) {
                String my = AutoEZ.mc.field_1724.method_5820();
                LivingEntity patt1$temp2 = TargetsUtility.getTarget();
                if (!(patt1$temp2 instanceof PlayerEntity)) {
                    return;
                }
                PlayerEntity target = (PlayerEntity)patt1$temp2;
                String targets = target.method_5820();
                if (Arrays.stream(this.killString).anyMatch(arg_0 -> AutoEZ.lambda$new$0((EventChatMessage)e, arg_0)) && ((EventChatMessage)e).message.contains(my) && ((EventChatMessage)e).message.contains(targets)) {
                    this.send(target);
                }
            }
        }
        if (event instanceof EventGameTick && this.trigger.is(Trigger.VanillaDeath) && (patt1$temp = TargetsUtility.getTarget()) instanceof PlayerEntity) {
            PlayerEntity target = (PlayerEntity)patt1$temp;
            if (target.field_6213 > 0 || target.method_6032() <= this.deathThreshold.get()) {
                this.send(target);
            }
        }
    };

    private AutoEZ() {
        super("AutoEZ", Category.PLAYER, "", new Tag[0]);
    }

    void send(PlayerEntity target) {
        if (this.noFriends.get() && Client.FRIENDS.isFriend(target)) {
            return;
        }
        if (!this.time.reached(this.delay.getLong() * 1000L, true)) {
            return;
        }
        List<String> messages = this.messages.get();
        String msg = messages.get(MathUtility.random(0, messages.size()));
        msg = msg.replace("\\", target.method_7334().getName());
        mc.method_1562().method_45729(this.prefix.get().prefix + msg);
    }

    private static /* synthetic */ boolean lambda$new$0(EventChatMessage e, String it) {
        return e.message.contains(it);
    }

    static enum Prefix {
        Global("!"),
        None("");

        final String prefix;

        private Prefix(String prefix) {
            this.prefix = prefix;
        }
    }

    static enum Trigger {
        VanillaDeath,
        Hit,
        ChatKill;

    }
}

