/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.decoration.ArmorStandEntity
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemStack
 */
package im.leet.utils;

import im.leet.Client;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.targets.ITargetValidator;
import im.leet.utils.client.targets.MatrixAntiBotUtility;
import im.leet.utils.client.targets.TargetsUtility;
import java.awt.Color;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class TargetValidator
extends Group
implements ITargetValidator {
    private final MultiEnumSetting<Target> target = this.multiEnumSetting("Targets", new Target[]{Target.Players});
    private final MultiEnumSetting<Target> visual = this.multiEnumSetting("Visual", new Target[]{Target.Players, Target.Naked, Target.Friends, Target.Invisible});
    private final Group antiBot = this.group("Anti-Bot");
    private final CheckBox matrixAntiBot = this.antiBot.checkbox("Matrix AntiBot", true);
    private final Group teams = this.group("Teams").toggleable(false);
    private final MultiEnumSetting<ArmorPart> teamsArmorParts = this.teams.multiEnumSetting("Check armor", new ArmorPart[]{ArmorPart.Helmet});
    public final CheckBox focus = this.checkbox("Focus", true);

    public TargetValidator() {
        super("Target validator");
    }

    @Override
    public boolean isValid(LivingEntity entity) {
        return this.isValid(entity, this.target);
    }

    public boolean isValidVisual(LivingEntity entity) {
        return this.isValid(entity, this.visual);
    }

    private boolean isValid(LivingEntity entity, MultiEnumSetting<Target> targets) {
        if (!entity.method_5805() || entity.method_6032() <= 0.0f) {
            return false;
        }
        if (entity.equals((Object)TargetValidator.mc.field_1724)) {
            return false;
        }
        if (entity instanceof ArmorStandEntity) {
            return false;
        }
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)entity;
            if (!targets.get(Target.Players)) {
                return false;
            }
            if (Client.FRIENDS.isFriend(playerEntity) && !targets.get(Target.Friends)) {
                return false;
            }
            if (this.matrixAntiBot.get() && MatrixAntiBotUtility.isMatrixBot(playerEntity)) {
                return false;
            }
            if ((playerEntity.method_5767() || playerEntity.method_6059(StatusEffects.field_5905)) && !targets.get(Target.Invisible)) {
                return false;
            }
            if (!TargetValidator.hasAnyArmor((LivingEntity)playerEntity) && !targets.get(Target.Naked)) {
                return false;
            }
            if (targets != this.visual && this.teams.isEnabled() && !this.teamsArmorParts.isEmpty()) {
                for (ArmorPart part : this.teamsArmorParts.get()) {
                    ItemStack me = TargetValidator.mc.field_1724.method_6118(part.slot);
                    ItemStack target = playerEntity.method_6118(part.slot);
                    Integer meColor = TargetsUtility.getArmorColor(me);
                    Integer targetColor = TargetsUtility.getArmorColor(target);
                    if (meColor == null || targetColor == null || meColor.intValue() != targetColor.intValue()) continue;
                    return false;
                }
            }
            return true;
        }
        if (entity instanceof LivingEntity) {
            return targets.get(Target.Mobs);
        }
        return targets.get(Target.Other);
    }

    public Color getColor(LivingEntity entity) {
        PlayerEntity player;
        if (this.teams.isEnabled() && !this.teamsArmorParts.isEmpty()) {
            for (ArmorPart part : this.teamsArmorParts.get()) {
                ItemStack target = entity.method_6118(part.slot);
                Integer targetColor = TargetsUtility.getArmorColor(target);
                if (targetColor == null) continue;
                return new Color(targetColor);
            }
        }
        if (entity instanceof PlayerEntity && Client.FRIENDS.isFriend(player = (PlayerEntity)entity)) {
            return ClientColors.FRIEND_COLOR;
        }
        return null;
    }

    private static boolean hasAnyArmor(LivingEntity entity) {
        if (entity == null || TargetValidator.mc.field_1724 == null) {
            return false;
        }
        return !entity.method_6118(EquipmentSlot.field_6169).method_7960() || !entity.method_6118(EquipmentSlot.field_6174).method_7960() || !entity.method_6118(EquipmentSlot.field_6172).method_7960() || !entity.method_6118(EquipmentSlot.field_6166).method_7960();
    }

    public static enum Target {
        Players,
        Invisible,
        Friends,
        Naked,
        Mobs,
        Other;

    }

    private static enum ArmorPart {
        Helmet(EquipmentSlot.field_6169),
        Chestplate(EquipmentSlot.field_6174),
        Leggings(EquipmentSlot.field_6172),
        Boots(EquipmentSlot.field_6166);

        final EquipmentSlot slot;

        private ArmorPart(EquipmentSlot slot) {
            this.slot = slot;
        }
    }
}

