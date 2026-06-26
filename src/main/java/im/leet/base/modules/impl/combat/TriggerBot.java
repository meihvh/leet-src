/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ShieldItem
 *  net.minecraft.util.Hand
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;

public class TriggerBot
extends Module {
    public static final TriggerBot INSTANCE = new TriggerBot();
    CheckBox onlycrit = this.checkbox("\u0422\u043e\u043b\u044c\u043a\u043e \u043a\u0440\u0438\u0442\u044b", false);
    CheckBox shieldcheck = this.checkbox("\u041f\u0440\u043e\u0432\u0435\u0440\u043a\u0430 \u043d\u0430 \u0449\u0438\u0442", true);
    CheckBox selfshieldcheck = this.checkbox("\u0411\u0438\u0442\u044c \u0441 \u0449\u0438\u0442\u043e\u043c", false);
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (TriggerBot.mc.field_1724 == null || TriggerBot.mc.field_1692 == null) {
                return;
            }
            if ((double)TriggerBot.mc.field_1724.method_7261(0.5f) > 0.9) {
                if (TriggerBot.mc.field_1724.field_6017 > 1.0) {
                    return;
                }
                if (TriggerBot.mc.field_1724.field_6017 == 0.0 && this.onlycrit.get()) {
                    return;
                }
                LivingEntity livingEntity = (LivingEntity)TriggerBot.mc.field_1692;
                if (livingEntity.method_6032() == 0.0f) {
                    return;
                }
                if (!TriggerBot.mc.field_1692.method_5709()) {
                    return;
                }
                if (TriggerBot.mc.field_1755 != null) {
                    return;
                }
                if (this.shieldcheck.get() && ((LivingEntity)TriggerBot.mc.field_1692).method_6030().method_7909() instanceof ShieldItem) {
                    return;
                }
                if (!this.selfshieldcheck.get() && TriggerBot.mc.field_1724.method_6030().method_7909() instanceof ShieldItem) {
                    return;
                }
                TriggerBot.mc.field_1761.method_2918((PlayerEntity)TriggerBot.mc.field_1724, TriggerBot.mc.field_1692);
                TriggerBot.mc.field_1724.method_6104(Hand.field_5808);
            }
        }
    };

    private TriggerBot() {
        super("TriggerBot", Category.COMBAT, "Automatically strikes when aiming at a target", new Tag[0]);
    }
}

