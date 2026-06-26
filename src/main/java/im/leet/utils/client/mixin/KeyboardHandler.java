/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.gui.screen.ingame.InventoryScreen
 */
package im.leet.utils.client.mixin;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventKey;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public final class KeyboardHandler {
    public static boolean handleKey(int key, int scancode, int action, int modifiers) {
        EventKey eventKey = EventKey.build(key, action);
        if (!(MinecraftHolder.mc.field_1755 instanceof ChatScreen) && !(MinecraftHolder.mc.field_1755 instanceof InventoryScreen) && key != -1) {
            Client.EVENTS.post(eventKey);
        }
        if (action == 1 || action == 2) {
            if (Client.CLICKGUI.isOpened()) {
                Client.CLICKGUI.keyPressed(key, scancode, modifiers);
            } else if (MinecraftHolder.mc.field_1755 instanceof ChatScreen && Client.CLICKGUI.WRITING) {
                Client.HUD.keyPressed(key, scancode, modifiers);
                return true;
            }
        }
        return eventKey.isCancelled() || Client.CLICKGUI.WRITING && Client.CLICKGUI.isOpened();
    }

    public static boolean handleChar(char codePoint, int modifiers) {
        if (MinecraftHolder.mc.field_1755 instanceof ChatScreen && Client.CLICKGUI.WRITING) {
            Client.HUD.charTyped(codePoint, modifiers);
            return true;
        }
        if (Client.CLICKGUI.isOpened()) {
            Client.CLICKGUI.chartyped(codePoint, modifiers);
            return true;
        }
        return false;
    }

    private KeyboardHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

