/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.text.Text
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.base.hud.notification.Notify;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.animations.Direction;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;

public class NotificationElement
extends HudElement {
    Notify demo = new Notify(Text.method_30163((String)"This is an example of a notification"), "", -1L).markDemo();

    public NotificationElement(Drag drag) {
        super("Notify", drag);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        CopyOnWriteArrayList<Notify> notifies = Client.NOTIFIES.getNotifies();
        if (notifies.isEmpty()) {
            this.demo.bound((float)window.method_4486() / 2.0f, (float)window.method_4502() / 2.0f + 56.0f, 0.0f, 15.0f).render(0, 0);
        }
        if (NotificationElement.mc.field_1755 instanceof ChatScreen && notifies.isEmpty()) {
            this.demo.getAnimation().setDirection(Direction.FORWARDS);
        } else {
            this.demo.getAnimation().setDirection(Direction.BACKWARDS);
        }
        float i = 0.0f;
        for (Notify notif : notifies) {
            notif.bound((float)window.method_4486() / 2.0f, (float)window.method_4502() / 2.0f - i + (float)(notifies.size() * 16) + 40.0f, 0.0f, 15.0f).render(0, 0);
            i += 16.0f;
        }
        notifies.removeIf(Notify::shouldRemove);
    }
}

