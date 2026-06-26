/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.component.type.DyedColorComponent
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.registry.tag.ItemTags
 */
package im.leet.utils.client.targets;

import im.leet.MinecraftHolder;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.client.HealthUtility;
import im.leet.utils.client.targets.ITargetValidator;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.player.PlayerUtility;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public class TargetsUtility {
    private static LivingEntity target;
    private static LivingEntity lastTarget;
    private static boolean targetLocked;

    public static boolean isValid() {
        return target != null && ClientSettings.INSTANCE.targetValidator.isValid(target);
    }

    public static LivingEntity find(float range, Sort sort) {
        TargetsUtility.find(range, sort, ClientSettings.INSTANCE.targetValidator);
        return TargetsUtility.isValid() ? target : null;
    }

    private static void find(float range, Sort sort, ITargetValidator validator) {
        lastTarget = target;
        boolean targetLocked = ClientSettings.INSTANCE.targetValidator.focus.get();
        if (targetLocked && target != null && target.method_5805() && !target.method_31481() && MinecraftHolder.mc.field_1724.method_5858((Entity)target) <= (double)(range * range) && validator.isValid(target)) {
            return;
        }
        if (targetLocked && (target == null || !target.method_5805() || target.method_31481() || MinecraftHolder.mc.field_1724.method_5858((Entity)target) > (double)(range * range) || !validator.isValid(target))) {
            TargetsUtility.unlockTarget();
        }
        Optional<LivingEntity> foundTarget = TargetsUtility.getTargets(range, validator).stream().min(sort.getComparator().get());
        target = foundTarget.orElse(null);
    }

    public static void unlockTarget() {
        targetLocked = false;
    }

    private static ArrayList<LivingEntity> getTargets(float range, ITargetValidator validator) {
        ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>();
        for (Entity entity : MinecraftHolder.mc.field_1687.method_18112()) {
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity living = (LivingEntity)entity;
            if (entity instanceof ClientPlayerEntity || !(MinecraftHolder.mc.field_1724.method_5858((Entity)living) <= (double)(range * range)) || !living.method_5805() || living.method_31481() || !validator.isValid(living)) continue;
            entities.add(living);
        }
        return entities;
    }

    public static void reset() {
        target = null;
        targetLocked = false;
    }

    public static void lockTarget() {
        if (target != null) {
            targetLocked = true;
        }
    }

    public static void toggleTargetLock() {
        targetLocked = target != null && !targetLocked;
    }

    public static Integer getArmorColor(ItemStack stack) {
        if (stack.method_31573(ItemTags.field_48803)) {
            return DyedColorComponent.method_57470((ItemStack)stack, (int)-6265536);
        }
        return null;
    }

    public static LivingEntity getTarget() {
        return target;
    }

    public static void setLastTarget(LivingEntity lastTarget) {
        TargetsUtility.lastTarget = lastTarget;
    }

    public static LivingEntity getLastTarget() {
        return lastTarget;
    }

    public static boolean isTargetLocked() {
        return targetLocked;
    }

    static {
        targetLocked = false;
    }

    public static enum Sort {
        Distance(() -> Comparator.comparingDouble(arg_0 -> ((ClientPlayerEntity)MinecraftHolder.mc.field_1724).method_5858(arg_0))),
        Health(() -> Comparator.comparingDouble(HealthUtility::get)),
        Angle(() -> Comparator.comparingDouble(RotationUtility::calculateFOVFromCamera)),
        Adaptive(() -> Comparator.comparingDouble(PlayerUtility::compareArmor).thenComparingDouble(RotationUtility::calculateFOVFromCamera).thenComparingDouble(arg_0 -> ((ClientPlayerEntity)MinecraftHolder.mc.field_1724).method_5858(arg_0)).thenComparingDouble(HealthUtility::get));

        final Supplier<Comparator<LivingEntity>> comparator;

        private Sort(Supplier<Comparator<LivingEntity>> comparator) {
            this.comparator = comparator;
        }

        public Supplier<Comparator<LivingEntity>> getComparator() {
            return this.comparator;
        }
    }
}

