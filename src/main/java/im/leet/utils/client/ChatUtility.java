/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.hud.ChatHudLine
 *  net.minecraft.client.gui.hud.MessageIndicator
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 */
package im.leet.utils.client;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.mixin.screen.InsertChatMixin;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.client.TextUtility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ChatUtility
implements MinecraftHolder {
    public static void sendDebug(String message) {
        if (Client.IS_DEBUG) {
            ChatUtility.send(Text.method_30163((String)message));
        }
    }

    public static void send(String message) {
        ChatUtility.send(Text.method_30163((String)message));
    }

    public static void send(Text message) {
        if (ChatUtility.mc.field_1705 == null) {
            return;
        }
        Text prefix = TextUtility.applyGradient("[LEET] ", ClientSettings.INSTANCE.getColor(0), ClientSettings.INSTANCE.getColor(90));
        MutableText combinedText = prefix.method_27661().method_10852(message);
        ChatHudLine line = new ChatHudLine(MinecraftClient.method_1551().field_1705.method_1738(), (Text)combinedText, null, MessageIndicator.method_44751());
        ((InsertChatMixin)ChatUtility.mc.field_1705.method_1743()).invokeAddMessage(line);
        ((InsertChatMixin)ChatUtility.mc.field_1705.method_1743()).invokeAddVisibleMessage(line);
    }
}

