/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
 *  net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket
 *  net.minecraft.registry.Registries
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.text.Text
 *  net.minecraft.world.World
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventWorldEmit;
import im.leet.api.render.system.IconUse;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.player.eventtracker.SpecialPotionType;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class EventTracker
extends Module {
    public static final EventTracker INSTANCE = new EventTracker();
    MultiEnumSetting<TrackedEvents> track = this.add(new MultiEnumSetting<TrackedEvents>("Events to track", TrackedEvents.class));
    EventBus<Event> events = event -> {
        if (event instanceof EventReceivePacket) {
            Packet patt1$temp;
            EventReceivePacket p = (EventReceivePacket)event;
            if (EventTracker.mc.field_1687 == null || EventTracker.mc.field_1724 == null) {
                return;
            }
            Packet patt0$temp = p.packet;
            if (patt0$temp instanceof EntityStatusS2CPacket) {
                Entity entity;
                EntityStatusS2CPacket status = (EntityStatusS2CPacket)patt0$temp;
                if (this.track.get(TrackedEvents.TOTEM) && (entity = status.method_11469((World)EventTracker.mc.field_1687)) instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity)entity;
                    switch (status.method_11470()) {
                        case 35: {
                            ItemStack protect = living.method_6047();
                            if (protect.method_7909() != Items.field_8288) {
                                protect = living.method_6079();
                            }
                            Client.NOTIFIES.addItem((Text)Text.method_43473().method_10852((Text)Text.method_30163((String)living.method_5477().getString()).method_27661().method_54663(Client.FRIENDS.isFriend(living.method_5477().getString()) ? ClientColors.FRIEND_COLOR.getRGB() : Color.WHITE.getRGB())).method_27693(" \u043f\u043e\u0442\u0435\u0440\u044f\u043b \u0442\u043e\u0442\u0435\u043c ").method_10852(protect.method_7964()), "totem_of_undying", 4000L);
                        }
                    }
                }
            }
            if ((patt1$temp = p.packet) instanceof ScreenHandlerSlotUpdateS2CPacket) {
                ScreenHandlerSlotUpdateS2CPacket d = (ScreenHandlerSlotUpdateS2CPacket)patt1$temp;
                if (this.track.get(TrackedEvents.ITEMPICKUP) && d.method_11452() == 0 && EventTracker.mc.field_1724.field_7512.method_7611(d.method_11450()).method_7677().method_7960() && !d.method_11449().method_7960()) {
                    Client.NOTIFIES.add((Text)Text.method_30163((String)"\u041f\u043e\u0434\u043e\u0431\u0440\u0430\u043d \u043f\u0440\u0435\u0434\u043c\u0435\u0442 ").method_27661().method_10852(d.method_11449().method_63015()), IconUse.INFO, 4000L);
                }
            }
        }
        if (event instanceof EventWorldEmit) {
            EventWorldEmit e = (EventWorldEmit)event;
            if (e.eventId == 2002) {
                float range = 4.71f;
                for (Entity entity : EventTracker.mc.field_1687.method_18112()) {
                    if (entity.method_19538().method_1022(e.pos.method_46558()) > (double)range || !(entity instanceof PlayerEntity)) continue;
                    PlayerEntity pl = (PlayerEntity)entity;
                    StatusEffect def = this.find(e.data);
                    if (def != null) {
                        Client.NOTIFIES.add((Text)pl.method_5477().method_27661().method_27693(" \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043b \u0437\u0435\u043b\u044c\u0435 ").method_10852((Text)def.method_5560().method_27661().method_54663(def.method_5556())), IconUse.POTION, 4000L);
                        continue;
                    }
                    SpecialPotionType effects = this.findSpecial(e.data);
                    if (effects == null) continue;
                    Client.NOTIFIES.add((Text)pl.method_5477().method_27661().method_27693(" \u043f\u043e\u043b\u0443\u0447\u0438\u043b \u0431\u0430\u0444 ").method_10852((Text)Text.method_30163((String)effects.getDisplayName()).method_27661().method_54663(effects.getBaseColor().intValue())), IconUse.POTION, 4000L);
                }
            }
        }
    };

    private EventTracker() {
        super("Event Tracker", Category.PLAYER, "Notifies you of various events around you", new Tag[0]);
    }

    private StatusEffect find(int color) {
        float f5 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f = (float)(color >> 8 & 0xFF) / 255.0f;
        float f1 = (float)(color & 0xFF) / 255.0f;
        int parsed = new Color(f5, f, f1).getRGB();
        for (RegistryEntry effect : Registries.field_41174.method_40295()) {
            StatusEffect effect1 = (StatusEffect)effect.comp_349();
            int clr = new Color(effect1.method_5556()).getRGB();
            if (clr != parsed) continue;
            return effect1;
        }
        return null;
    }

    private SpecialPotionType findSpecial(int color) {
        float f5 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f = (float)(color >> 8 & 0xFF) / 255.0f;
        float f1 = (float)(color & 0xFF) / 255.0f;
        int parsed = new Color(f5, f, f1).getRGB();
        for (SpecialPotionType special : SpecialPotionType.values()) {
            if (!(ColorUtility.distance(special.getBaseColor(), parsed) < 30.0) && !special.getColorVariations().contains(parsed) && special.getBaseColor() != parsed) continue;
            return special;
        }
        return null;
    }

    public static enum TrackedEvents implements EnumChoice
    {
        TOTEM("Totem pop", true),
        POTION("Potion use", true),
        ITEMPICKUP("Pickup Item", true);

        final String renderName;
        final boolean defaultEnabled;

        private TrackedEvents(String renderName, boolean defaultEnabled) {
            this.renderName = renderName;
            this.defaultEnabled = defaultEnabled;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }

        @Override
        public boolean isDefaultEnabled() {
            return this.defaultEnabled;
        }
    }
}

