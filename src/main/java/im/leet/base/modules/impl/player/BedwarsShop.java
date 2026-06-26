/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  net.minecraft.client.gui.hud.ClientBossBar
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.ingame.GenericContainerScreen
 *  net.minecraft.client.gui.screen.ingame.HandledScreen
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.Potions
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.screen.sync.ItemStackHash
 *  net.minecraft.util.collection.DefaultedList
 */
package im.leet.base.modules.impl.player;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventClickSlot;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventKey;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.mixin.accessor.IBossBarHud;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.network.NetworkUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;
import net.minecraft.util.collection.DefaultedList;

public class BedwarsShop
extends Module {
    public static final BedwarsShop INSTANCE = new BedwarsShop();
    public final KeybindSetting open = this.keybindSetting("Open", 90);
    public final CheckBox buttons = this.checkbox("Buttons", false);
    final Group autobuy = this.group("Auto buy").toggleable(false);
    final SliderSetting clickDelay = this.autobuy.sliderSetting("Click delay", 12.0f, 0.0f, 20.0f);
    private HandledScreen<?> savedScreen;
    static final Int2ObjectMap<ItemStackHash> NOTHING = new Int2ObjectOpenHashMap();
    int delay;
    private final BuyItem[] SEQUENCE_1_8 = new BuyItem[]{BuyItem.Elytra, BuyItem.Totem, BuyItem.DiamondAxe, BuyItem.DiamondPickaxe};
    private final BuyItem[] SEQUENCE_1_12 = new BuyItem[]{BuyItem.Elytra, BuyItem.Totem, BuyItem.DiamondSword, BuyItem.DiamondAxe, BuyItem.DiamondPickaxe};
    private BuyItem[] sequence;
    private int index;
    BuyItem buying;
    EventBus<Event> events = event -> {
        Event e;
        if (event instanceof EventClickSlot) {
            e = (EventClickSlot)event;
            ChatUtility.sendDebug("clickslot " + String.valueOf(e));
        }
        if (event instanceof EventKey) {
            e = (EventKey)event;
            if (((EventKey)e).action == 0 && this.open.matches((EventKey)e)) {
                this.loadScreen(false);
            }
        }
        if (event instanceof EventGameTick) {
            if (this.delay > 0) {
                ChatUtility.sendDebug("wait " + this.delay);
                --this.delay;
                return;
            }
            if (this.savedScreen == null || !this.autobuy.isEnabled()) {
                return;
            }
            if (this.buying != null) {
                this.click(this.buying.slot);
                this.buying = null;
                return;
            }
            BuyItem item = this.getNextItem();
            if (item == null) {
                this.loadScreen(true);
                this.sequence = null;
                return;
            }
            ChatUtility.sendDebug("Item: " + String.valueOf((Object)item));
            if (!item.resources.check()) {
                ChatUtility.sendDebug("Item " + String.valueOf((Object)item) + " failed resource check!");
                return;
            }
            ++this.index;
            if (BedwarsShop.mc.field_1724.method_31548().method_55753(item.itemCheck)) {
                return;
            }
            this.click(item.category);
            this.buying = item;
        }
    };
    public static final Set<Item> WOOL_BLOCKS = Set.of(Items.field_19059, Items.field_19055, Items.field_19056, Items.field_19053, Items.field_19051, Items.field_19057, Items.field_19047, Items.field_19052, Items.field_19049, Items.field_19046, Items.field_19045, Items.field_19050, Items.field_19054, Items.field_19058, Items.field_19044, Items.field_19048);

    private BedwarsShop() {
        super("Bedwars Shop", Category.PLAYER, "Utility for bedwars minigame", new Tag[0]);
    }

    public boolean saveScreen(Screen screen) {
        if (screen instanceof GenericContainerScreen) {
            GenericContainerScreen s;
            this.savedScreen = s = (GenericContainerScreen)screen;
            mc.method_1507(null);
            this.setEnabled(true);
            return true;
        }
        return false;
    }

    @Override
    protected void onDisable() {
        this.loadScreen(true);
    }

    public void loadScreen(boolean remove) {
        if (this.savedScreen != null) {
            mc.method_1507(this.savedScreen);
            if (remove) {
                this.savedScreen = null;
                this.setEnabled(false);
            }
        }
    }

    private void click(int slot) {
        ScreenHandler handler = this.savedScreen.method_17577();
        NetworkUtility.send(new ClickSlotC2SPacket(handler.field_7763, handler.method_37421(), (short)slot, 0, SlotActionType.field_7790, NOTHING, ItemStackHash.field_58176));
        this.delay = this.clickDelay.getInt();
    }

    private BuyItem getNextItem() {
        if (this.sequence == null) {
            Map<UUID, ClientBossBar> bars = ((IBossBarHud)BedwarsShop.mc.field_1705.method_1740()).client$getBossBars();
            String text = bars.values().stream().map(it -> it.method_5414().getString()).filter(s -> s.contains("1.")).findFirst().orElse(null);
            this.sequence = text.contains("1.8") ? this.SEQUENCE_1_8 : this.SEQUENCE_1_12;
        }
        if (this.index >= this.sequence.length) {
            this.sequence = null;
            this.index = 0;
            return null;
        }
        BuyItem item = this.sequence[this.index];
        return item;
    }

    static enum BuyItem {
        Elytra(3, 22, BuyItem.item(Items.field_8833), new Resources(1000, Resources.Kind.Emerald, 6)),
        DiamondArmor(3, 21, BuyItem.item(Items.field_8285), new Resources(750, Resources.Kind.Emerald, 6)),
        DiamondSword(2, 22, BuyItem.item(Items.field_8802), new Resources(500, Resources.Kind.Emerald, 4)),
        IronAxe(4, 29, BuyItem.item(Items.field_8475), new Resources(600, Resources.Kind.Gold, 6)),
        DiamondPickaxe(4, 38, BuyItem.item(Items.field_8377), new Resources(600, Resources.Kind.Emerald, 6)),
        DiamondAxe(4, 38, BuyItem.item(Items.field_8556), new Resources(1000, Resources.Kind.Emerald, 10)),
        Strength(6, 22, BuyItem.potion((RegistryEntry<Potion>)Potions.field_8978), new Resources(250, Resources.Kind.Emerald, 1)),
        GoldenApple(8, 21, BuyItem.item(Items.field_8463), new Resources(100, Resources.Kind.Gold, 3)),
        FireAura(8, 40, BuyItem.item(Items.field_8183), new Resources(150, Resources.Kind.Emerald, 1)),
        Totem(8, 28, BuyItem.item(Items.field_8288), new Resources(1000, Resources.Kind.Emerald, 6));

        final int category;
        final int slot;
        final Predicate<ItemStack> itemCheck;
        final Resources resources;

        static Predicate<ItemStack> item(Item item) {
            return it -> it.method_7909() == item;
        }

        static Predicate<ItemStack> potion(RegistryEntry<Potion> potion) {
            return it -> {
                PotionContentsComponent contents = (PotionContentsComponent)it.method_58694(DataComponentTypes.field_49651);
                return contents != null && contents.method_57401(potion);
            };
        }

        private BuyItem(int category, int slot, Predicate<ItemStack> itemCheck, Resources resources) {
            this.category = category;
            this.slot = slot;
            this.itemCheck = itemCheck;
            this.resources = resources;
        }

        public String toString() {
            return "BedwarsShop.BuyItem." + this.name() + "(category=" + this.category + ", slot=" + this.slot + ", itemCheck=" + String.valueOf(this.itemCheck) + ", resources=" + String.valueOf(this.resources) + ")";
        }
    }

    record Resources(int exp, Kind kind, int count) {
        boolean check() {
            if (mc.field_1724.field_7520 > 0) {
                return mc.field_1724.field_7520 >= this.exp;
            }
            int c = Math.toIntExact(mc.field_1724.method_31548().method_67533().stream().filter(it -> it.method_7909() == this.kind.item).count());
            DefaultedList inv = mc.field_1724.method_31548().method_67533();
            return c >= this.count;
        }

        static enum Kind {
            Iron(Items.field_8620),
            Gold(Items.field_8695),
            Emerald(Items.field_8687);

            final Item item;

            private Kind(Item item) {
                this.item = item;
            }
        }
    }
}

