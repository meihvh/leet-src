/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.RotationSettings;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.RotationUtility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class AimBot
extends Module {
    public static final AimBot INSTANCE = new AimBot();
    private final Group general = this.group("General");
    private final SliderSetting range = (SliderSetting)this.general.sliderSetting("Range", 50.0f, 5.0f, 100.0f).increment(0.5f).desc("Maximal distance to find target");
    private final SliderSetting fov = (SliderSetting)this.general.sliderSetting("FOV", 120.0f, 5.0f, 180.0f).increment(1.0f).desc("Find targets relative to FOV");
    private final EnumSetting<TargetsUtility.Sort> sortMode = (EnumSetting)this.general.enumSetting("Sort", TargetsUtility.Sort.Adaptive).desc("Target sorting");
    private final RotationSettings rotations = this.add(new RotationSettings("Rotations", 33));
    private LivingEntity currentTarget;
    private final EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            this.onTick();
        }
    };

    private AimBot() {
        super("AimBot", Category.COMBAT, "Automatically aims ranged weapons at targets", new Tag[0]);
    }

    private void onTick() {
        if (AimBot.nullCheck()) {
            this.currentTarget = null;
            return;
        }
        ItemStack stack = AimBot.mc.field_1724.method_6047();
        if (!this.isRangedWeapon(stack)) {
            this.currentTarget = null;
            return;
        }
        LivingEntity target = TargetsUtility.find(this.range.get(), this.sortMode.get());
        if (target == null) {
            this.currentTarget = null;
            return;
        }
        if (RotationUtility.calculateFOVFromCamera(target) > (double)this.fov.get()) {
            this.currentTarget = null;
            return;
        }
        Angle targetAngle = this.computeAngle(target);
        this.rotations.rotate(targetAngle);
        this.currentTarget = target;
    }

    private boolean isRangedWeapon(ItemStack stack) {
        return stack.method_31574(Items.field_8102) || stack.method_31574(Items.field_8399) || stack.method_31574(Items.field_8547);
    }

    private Angle computeAngle(LivingEntity target) {
        Vec3d point = target.method_19538().method_1031(0.0, (double)target.method_18381(target.method_18376()) * 0.75, 0.0);
        return RotationUtility.calculate(point);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.currentTarget = null;
        TargetsUtility.reset();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.currentTarget = null;
        TargetsUtility.reset();
    }

    public LivingEntity getCurrentTarget() {
        return this.currentTarget;
    }
}

