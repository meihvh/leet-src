/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.Items
 *  net.minecraft.registry.Registries
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
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
import im.leet.base.modules.impl.player.countermine.AntiRecoilHandler;
import im.leet.base.modules.impl.player.countermine.F5ArmorFix;
import im.leet.base.modules.impl.player.countermine.NoSmokeHandler;
import im.leet.base.settings.impl.binder.BinderSetting;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.utils.client.ClientColors;
import im.leet.utils.player.InventoryUtility;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AssistModule
extends Module {
    public static final AssistModule INSTANCE = new AssistModule();
    Group items = this.group("Binder");
    BinderSetting binder = this.items.binderSetting("Item bind", Map.of(Items.field_8634, -1, Items.field_8449, -1));
    CheckBox legitUse = this.items.checkbox("Legit use", false);
    Group general = this.group("General");
    public final CheckBox jumpDelay = this.general.checkbox("No jump delay", true);
    Group friends = this.group("Friends");
    KeybindSetting addFriendBind = this.friends.keybindSetting("Add friend bind", -1);
    CheckBox counterMineEnabled = this.checkbox("CounterMineAssist", false);
    Group counterMine;
    CheckBox fixF5;
    CheckBox antiRecoil;
    CheckBox noSmoke;
    AntiRecoilHandler antiRecoilHandler;
    NoSmokeHandler noSmokeHandler;
    EventBus<Event> events;

    private AssistModule() {
        super("Assist", Category.PLAYER, "Makes playing on different servers easier", Tag.VANILLA);
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.counterMineEnabled::get;
        this.counterMine = (Group)this.group("CounterMine").visible(supplierArray);
        this.fixF5 = this.counterMine.checkbox("Fix F5", true);
        this.antiRecoil = this.counterMine.checkbox("Anti-recoil", true);
        this.noSmoke = this.counterMine.checkbox("Remove smoke", true);
        this.antiRecoilHandler = new AntiRecoilHandler();
        this.noSmokeHandler = new NoSmokeHandler();
        this.events = event -> {
            if (event instanceof EventKey) {
                int friendKey;
                EventKey e = (EventKey)event;
                if (e.action != 1 || AssistModule.mc.field_1724 == null) {
                    return;
                }
                if (this.counterMineEnabled.get() && this.fixF5.get() && e.key == 294) {
                    F5ArmorFix.removeAllArmor(mc);
                }
                if ((friendKey = this.addFriendBind.getBind()) >= 0 && e.key == friendKey) {
                    PlayerEntity target;
                    if (AssistModule.mc.field_1724 == null) {
                        return;
                    }
                    Entity patt0$temp = AssistModule.mc.field_1692;
                    if (!(patt0$temp instanceof PlayerEntity) || (target = (PlayerEntity)patt0$temp) == AssistModule.mc.field_1724) {
                        Client.NOTIFIES.add(Text.method_30163((String)"\u041d\u0435\u0442 \u0446\u0435\u043b\u0438"), IconUse.CROSS, 1500L);
                        return;
                    }
                    String name = target.method_7334().getName();
                    if (Client.FRIENDS.isFriend(name)) {
                        Client.FRIENDS.removeFriend(name);
                        Client.NOTIFIES.add((Text)Text.method_30163((String)"\u0423\u0434\u0430\u043b\u0435\u043d \u0438\u0437 \u0434\u0440\u0443\u0437\u0435\u0439: ").method_27661().method_10852((Text)target.method_5477().method_27661()), IconUse.PERSONS, 2000L);
                    } else {
                        Client.FRIENDS.addFriend(target);
                        Client.NOTIFIES.add((Text)Text.method_30163((String)"\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u0432 \u0434\u0440\u0443\u0437\u044c\u044f: ").method_27661().method_10852((Text)target.method_5477().method_27661()), IconUse.PERSONS, 2000L);
                    }
                    return;
                }
                for (Map.Entry<Identifier, Integer> entry : this.binder.getEntry()) {
                    Item item = (Item)Registries.field_41178.method_63535(entry.getKey());
                    int bind = entry.getValue();
                    if (bind < 0 || bind != e.key) continue;
                    int slot = InventoryUtility.find(item);
                    if (slot == -1) {
                        Client.NOTIFIES.add((Text)Text.method_30163((String)"\u041d\u0435\u0442 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430 ").method_27661().method_10852((Text)item.method_63680().method_27661().method_54663(ClientColors.RED.getRGB())), IconUse.CROSS, 2000L);
                        return;
                    }
                    InventoryUtility.useItem(slot, true, false, this.legitUse.get());
                }
            }
            if (this.counterMineEnabled.get()) {
                if (this.antiRecoil.get()) {
                    this.antiRecoilHandler.handle(event);
                } else {
                    this.antiRecoilHandler.reset();
                }
                if (this.noSmoke.get()) {
                    this.noSmokeHandler.handle(event);
                }
            }
        };
    }
}

