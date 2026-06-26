/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket
 *  net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket$Status
 *  net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.api.events.list.EventSetRotation;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;

public class CancelAction
extends Module {
    public static final CancelAction INSTANCE = new CancelAction();
    public MultiEnumSetting<Mode> setting = this.multiEnumSetting("Settings", Mode.class);
    EventBus<Event> onEvent = event -> {
        if (event instanceof EventAttack) {
            EventAttack eventAttack = (EventAttack)event;
            Entity patt0$temp = eventAttack.target;
            if (patt0$temp instanceof PlayerEntity) {
                PlayerEntity target = (PlayerEntity)patt0$temp;
                if (CancelAction.mc.field_1724 == null || CancelAction.mc.field_1687 == null || Client.FRIENDS.getFriendList().isEmpty()) {
                    return;
                }
                if (Client.FRIENDS.isFriend(target.method_7334().getName()) && this.setting.get(Mode.FriendDamage)) {
                    eventAttack.cancel();
                }
            }
        }
        if (event instanceof EventReceivePacket) {
            Packet<?> patt1$temp;
            EventReceivePacket eventReceivePacket = (EventReceivePacket)event;
            if (CancelAction.mc.field_1724 == null || CancelAction.mc.field_1687 == null || mc.method_1562() == null) {
                return;
            }
            if (this.setting.get(Mode.ServerResourcePack) && (patt1$temp = eventReceivePacket.getPacket()) instanceof ResourcePackSendS2CPacket) {
                ResourcePackSendS2CPacket packet = (ResourcePackSendS2CPacket)patt1$temp;
                event.cancel();
                mc.method_1562().method_52787((Packet)new ResourcePackStatusC2SPacket(packet.comp_2158(), ResourcePackStatusC2SPacket.Status.field_13016));
                mc.method_1562().method_52787((Packet)new ResourcePackStatusC2SPacket(packet.comp_2158(), ResourcePackStatusC2SPacket.Status.field_47704));
                mc.method_1562().method_52787((Packet)new ResourcePackStatusC2SPacket(packet.comp_2158(), ResourcePackStatusC2SPacket.Status.field_13017));
                if (CancelAction.mc.field_1755 != null) {
                    CancelAction.mc.field_1724.method_7346();
                }
            }
        }
        if (event instanceof EventSetRotation) {
            EventSetRotation e = (EventSetRotation)event;
            if (this.setting.get(Mode.ServerRotate)) {
                e.cancel();
            }
        }
    };

    private CancelAction() {
        super("Cancel Action", Category.PLAYER, "Cancels the selected actions", new Tag[0]);
    }

    public static enum Mode implements EnumChoice
    {
        FriendDamage("Damage for Friends", true),
        ServerRotate("Server Rotate", true),
        ServerResourcePack("Server Resource Pack", false);

        final String renderName;
        final boolean defaultEnabled;

        private Mode(String renderName, boolean defaultEnabled) {
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

