/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 */
package im.leet.base.hud.notification;

import im.leet.MinecraftHolder;
import im.leet.api.render.system.IconUse;
import im.leet.base.hud.notification.Notify;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.text.Text;

public class Notifies
implements MinecraftHolder {
    final CopyOnWriteArrayList<Notify> notifies = new CopyOnWriteArrayList();

    public void add(Text text, IconUse icon, long duration) {
        this.notifies.add(new Notify(text, icon, duration));
    }

    public void addItem(Text text, String item, long duration) {
        this.notifies.add(new Notify(text, item, duration));
    }

    public CopyOnWriteArrayList<Notify> getNotifies() {
        return this.notifies;
    }
}

