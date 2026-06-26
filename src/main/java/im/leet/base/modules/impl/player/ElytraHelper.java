/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Items
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventKey;
import im.leet.api.render.system.IconUse;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.player.elytrahelper.ElytraSwapper;
import im.leet.base.modules.impl.player.elytrahelper.FireworkUser;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.utils.client.ClientColors;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ElytraHelper
extends Module {
    public static final ElytraHelper INSTANCE = new ElytraHelper();
    public KeybindSetting swap = this.keybindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0441\u0432\u0430\u043f\u0430", -1);
    public KeybindSetting firework = this.keybindSetting("\u041a\u043d\u043e\u043f\u043a\u0430 \u0444\u0435\u0439\u0435\u0440\u0432\u0435\u0440\u043a\u0430", -1);
    public CheckBox legitFirework = this.checkbox("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u043b\u0435\u0433\u0438\u0442\u043d\u043e", false);
    ElytraSwapper swapper = new ElytraSwapper();
    FireworkUser user = new FireworkUser();
    EventBus<Event> events = event -> {
        if (event instanceof EventKey) {
            EventKey e = (EventKey)event;
            if (ElytraHelper.mc.field_1724 == null || e.action != 1) {
                return;
            }
            if (e.key == this.swap.getBind()) {
                Text elytra = ElytraHelper.mc.field_1724.method_31548().method_5438(38).method_7909().equals(Items.field_8833) ? Text.method_30163((String)"\u041d\u0430\u0433\u0440\u0443\u0434\u043d\u0438\u043a") : Text.method_30163((String)"\u042d\u043b\u0438\u0442\u0440\u0443");
                elytra = elytra.method_27661().method_54663(elytra.method_44745(Text.method_30163((String)"\u041d\u0430\u0433\u0440\u0443\u0434\u043d\u0438\u043a")) ? ClientColors.RED.getRGB() : ClientColors.MAIN_COLOR.getRGB());
                Text mainText = Text.method_30163((String)"\u0421\u0432\u0430\u043f\u043d\u0443\u043b \u043d\u0430 ");
                MutableText mutable = mainText.method_27661().method_10852(elytra);
                Client.NOTIFIES.add((Text)mutable, IconUse.INFO, 3000L);
                this.swapper.swap();
            } else if (e.key == this.firework.getBind()) {
                this.user.useItemOnHotbar(Items.field_8639);
            }
        }
    };

    private ElytraHelper() {
        super("Elytra Helper", Category.PLAYER, "Simplifies the use of Elytra", new Tag[0]);
    }
}

