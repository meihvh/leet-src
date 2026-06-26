/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.player.InventoryUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;

public class AutoTotem
extends Module {
    public static final AutoTotem INSTANCE = new AutoTotem();
    public final Group group = this.group("General");
    public final EnumSetting<Mode> mode = (EnumSetting)this.enumSetting("Mode", Mode.Health).desc("When should swap totem");
    public final CheckBox notInScreen = (CheckBox)this.checkbox("Not in screen", true).desc("Allow swap totem when no screen opened");
    public CheckBox smartCalc = (CheckBox)((CheckBox)this.checkbox("Calculate trigger health", false).visible(() -> this.mode.is(Mode.Health))).desc("Automatically calculating trigger health by calculation received damage");
    public SliderSetting trigger = (SliderSetting)((SliderSetting)this.sliderSetting("Trigger health", 3.0f, 1.0f, 19.0f).increment(0.1f).visible(() -> this.mode.is(Mode.Health) && !this.smartCalc.get())).desc("Health to swap totem");
    public CheckBox saveEnchanted = (CheckBox)this.checkbox("Save enchanted", false).desc("Should non enchanted totems be prioritized for swap");
    int lastSlot = -1;
    float prevHealth = -1.0f;
    int lastDamage = 0;
    TimeUtility swapDelay = new TimeUtility();
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            boolean shouldTake;
            if (this.notInScreen.get() && AutoTotem.mc.field_1755 != null) {
                return;
            }
            float health = AutoTotem.mc.field_1724.method_6032();
            float trigger = this.trigger.get();
            boolean bl = shouldTake = this.mode.is(Mode.Force) || !this.smartCalc.get() && health < trigger || this.smartCalc.get() && health - this.prevHealth <= 1.0f;
            if (!shouldTake) {
                if (!this.smartCalc.get() || this.lastDamage > 20) {
                    this.swapBack();
                }
            } else {
                this.takeTotem();
            }
            ++this.lastDamage;
            if (AutoTotem.mc.field_1724.field_6235 > 0) {
                this.lastDamage = 0;
            }
        }
        if (event instanceof EventReceivePacket) {
            HealthUpdateS2CPacket hp;
            EventReceivePacket p = (EventReceivePacket)event;
            if (AutoTotem.mc.field_1687 == null || AutoTotem.mc.field_1724 == null || !this.smartCalc.get() || this.notInScreen.get() && AutoTotem.mc.field_1755 != null) {
                return;
            }
            Packet<?> patt0$temp = p.getPacket();
            if (patt0$temp instanceof HealthUpdateS2CPacket && (hp = (HealthUpdateS2CPacket)patt0$temp).method_11833() < AutoTotem.mc.field_1724.method_6032()) {
                this.prevHealth = MathUtility.delta(hp.method_11833(), AutoTotem.mc.field_1724.method_6032());
            }
        }
    };

    private AutoTotem() {
        super("Auto totem", Category.COMBAT, "Automatically swaps totems to your left hand", new Tag[0]);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.lastSlot = -1;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.lastSlot = -1;
    }

    private void takeTotem() {
        if (!this.swapDelay.reached(100L, true)) {
            return;
        }
        if (!this.hasTotem()) {
            int slot = InventoryUtility.find(Items.field_8288, !this.saveEnchanted.get());
            if (slot == -1) {
                return;
            }
            if (this.lastSlot == -1) {
                this.lastSlot = slot;
            }
            InventoryUtility.swap(InventoryUtility.wrapHotbar(slot), 45);
        }
    }

    private void swapBack() {
        if (this.lastSlot != -1 && this.hasTotem()) {
            if (!this.swapDelay.reached(100L, true)) {
                return;
            }
            InventoryUtility.swap(InventoryUtility.wrapHotbar(this.lastSlot), 45);
            this.lastSlot = -1;
            this.swapDelay.reset();
        }
    }

    private boolean hasTotem() {
        ItemStack stack = AutoTotem.mc.field_1724.method_6079();
        return !stack.method_7960() && stack.method_7909() == Items.field_8288 && (!this.saveEnchanted.get() || !stack.method_7942());
    }

    public static enum Mode {
        Health,
        Force;

    }
}

