/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.text.Text
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.slider.SliderSetting;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class AutoLeave
extends Module {
    public static final AutoLeave INSTANCE = new AutoLeave();
    public SliderSetting radius = this.sliderSetting("Radius", 16.0f, 1.0f, 64.0f);
    public CheckBox ignoreFriends = this.checkbox("Ignore friends", true);
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (AutoLeave.mc.field_1724 == null || AutoLeave.mc.field_1687 == null) {
                return;
            }
            float maxDistSq = this.radius.get() * this.radius.get();
            for (AbstractClientPlayerEntity p : AutoLeave.mc.field_1687.method_18456()) {
                if (p == AutoLeave.mc.field_1724 || this.ignoreFriends.get() && Client.FRIENDS.isFriend((PlayerEntity)p)) continue;
                if (AutoLeave.mc.field_1724.method_5858((Entity)p) <= (double)maxDistSq) {
                    mc.method_1562().method_48296().method_10747((Text)Text.method_43473());
                }
                return;
            }
        }
    };

    private AutoLeave() {
        super("Auto Leave", Category.PLAYER, "\u041b\u0438\u0432\u0430\u0435\u0442 \u043e\u0442 \u043a\u043e\u0433\u043e-\u043b\u0438\u0431\u043e \u043f\u0440\u0438 \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0451\u043d\u043d\u044b\u0445 \u0443\u0441\u043b\u043e\u0432\u0438\u044f\u0445", new Tag[0]);
    }
}

