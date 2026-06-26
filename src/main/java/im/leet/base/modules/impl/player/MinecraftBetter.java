/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.DeathScreen
 *  net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket$Mode
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;

public class MinecraftBetter
extends Module {
    public static final MinecraftBetter INSTANCE = new MinecraftBetter();
    public CheckBox cameraNoClip = this.checkbox("No camera clip", true);
    public CheckBox autoRespawn = this.checkbox("Auto respawn", true);
    public CheckBox noFluidSlowdown = this.checkbox("No slowdown breaking in liquids", true);
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (MinecraftBetter.mc.field_1724 == null || MinecraftBetter.mc.field_1687 == null) {
                return;
            }
            if (this.autoRespawn.get() && MinecraftBetter.mc.field_1755 instanceof DeathScreen) {
                NetworkUtility.sendWithoutEvent(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.field_12774));
            }
        }
    };

    private MinecraftBetter() {
        super("Minecraft Better", Category.PLAYER, "\u0423\u043b\u0443\u0447\u0448\u0430\u0435\u0442 \u0438\u0433\u0440\u0443", new Tag[0]);
    }
}

