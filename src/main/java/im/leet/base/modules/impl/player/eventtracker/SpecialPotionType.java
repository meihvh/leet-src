/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffects
 */
package im.leet.base.modules.impl.player.eventtracker;

import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

public enum SpecialPotionType {
    FLASH(0xFFFFFF, "\u00a76\u00a7l[\u2605] \u00a7e\u00a7l\u0412\u0441\u043f\u044b\u0448\u043a\u0430", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5919.comp_349(), 20, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5912.comp_349(), 240, 0)), Arrays.asList(0xFCFFF6, 0xFFFFF5, 0xFFFFFC, 0xFFF7FF, 0xF8FFF5, 0xFFFEFF, 0xF7FFF8, 0xFBFCFF, 0xFFF9FF)),
    KILLER(0xCC0000, "\u00a74\u00a7l[\u2605] \u00a7c\u00a7l\u0417\u0435\u043b\u044c\u0435 \u041a\u0438\u043b\u043b\u0435\u0440\u0430", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5907.comp_349(), 180, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5910.comp_349(), 90, 3)), Arrays.asList(0xC00000, 0xE50000, 0xD60000)),
    KILLER2(0xB40000, "\u00a74\u00a7l[\u2605] \u00a7c\u00a7l\u0417\u0435\u043b\u044c\u0435 \u041a\u0438\u043b\u043b\u0435\u0440\u0430", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5907.comp_349(), 180, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5910.comp_349(), 180, 3)), Arrays.asList(0xC00000, 0xE50000, 0xD60000)),
    BURP(0xFF6600, "\u00a7c\u00a7l[\u2605] \u00a76\u00a7l\u0417\u0435\u043b\u044c\u0435 \u041e\u0442\u0440\u044b\u0436\u043a\u0438", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5919.comp_349(), 10, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5912.comp_349(), 180, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5903.comp_349(), 90, 9), new PotionEffectData((StatusEffect)StatusEffects.field_5909.comp_349(), 180, 2), new PotionEffectData((StatusEffect)StatusEffects.field_5920.comp_349(), 30, 4)), Arrays.asList(16727040, 16733184, 16739072)),
    SULFURIC_ACID(0x99FF33, "\u00a72\u00a7l[\u2605] \u00a7a\u00a7l\u0421\u0435\u0440\u043d\u0430\u044f \u043a\u0438\u0441\u043b\u043e\u0442\u0430", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5899.comp_349(), 50, 1), new PotionEffectData((StatusEffect)StatusEffects.field_5909.comp_349(), 90, 3), new PotionEffectData((StatusEffect)StatusEffects.field_5911.comp_349(), 90, 2), new PotionEffectData((StatusEffect)StatusEffects.field_5920.comp_349(), 30, 4)), Arrays.asList(0x980000, 10223411, 0x98FFFF)),
    MEDIC(0xFF00FF, "\u00a75\u00a7l[\u2605] \u00a7d\u00a7l\u0417\u0435\u043b\u044c\u0435 \u041c\u0435\u0434\u0438\u043a\u0430", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5914.comp_349(), 45, 2), new PotionEffectData((StatusEffect)StatusEffects.field_5924.comp_349(), 45, 2)), Arrays.asList(0xFF11FF, 0xFEEFFF, 0xDFFFFF)),
    AGENT(0xFFFF00, "\u00a76\u00a7l[\u2605] \u00a7e\u00a7l\u0417\u0435\u043b\u044c\u0435 \u0430\u0433\u0435\u043d\u0442\u0430", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5918.comp_349(), 900, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5917.comp_349(), 180, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5905.comp_349(), 900, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5904.comp_349(), 900, 2), new PotionEffectData((StatusEffect)StatusEffects.field_5910.comp_349(), 300, 2)), Arrays.asList(0xFFEE00, 0xFFF100, 16775892, 0xEF0000)),
    WINNER(65280, "\u00a72\u00a7l[\u2605] \u00a7a\u00a7l\u0417\u0435\u043b\u044c\u0435 \u041f\u043e\u0431\u0435\u0434\u0438\u0442\u0435\u043b\u044f", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5914.comp_349(), 180, 1), new PotionEffectData((StatusEffect)StatusEffects.field_5905.comp_349(), 900, 0), new PotionEffectData((StatusEffect)StatusEffects.field_5924.comp_349(), 60, 1), new PotionEffectData((StatusEffect)StatusEffects.field_5907.comp_349(), 60, 0)), Arrays.asList(59136, 57088, 63232, 65310, 65350)),
    URINE(65280, "\u00a73\u00a7l[\u2605] \u00a7b\u00a7l\u041c\u043e\u0447\u0430 \u0424\u043b\u0435\u0448\u0430", Arrays.asList(new PotionEffectData((StatusEffect)StatusEffects.field_5913.comp_349(), 120, 1), new PotionEffectData((StatusEffect)StatusEffects.field_5904.comp_349(), 120, 2)), List.of(Integer.valueOf(65535)));

    private final Integer baseColor;
    private final String displayName;
    private final List<PotionEffectData> effects;
    private final List<Integer> colorVariations;

    public Integer getBaseColor() {
        return this.baseColor;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<PotionEffectData> getEffects() {
        return this.effects;
    }

    public List<Integer> getColorVariations() {
        return this.colorVariations;
    }

    private SpecialPotionType(Integer baseColor, String displayName, List<PotionEffectData> effects, List<Integer> colorVariations) {
        this.baseColor = baseColor;
        this.displayName = displayName;
        this.effects = effects;
        this.colorVariations = colorVariations;
    }

    public static class PotionEffectData {
        private final StatusEffect effect;
        private final Integer durationSeconds;
        private final Integer amplifier;

        public Integer getDurationTicks() {
            return this.durationSeconds * 20;
        }

        public StatusEffect getEffect() {
            return this.effect;
        }

        public Integer getDurationSeconds() {
            return this.durationSeconds;
        }

        public Integer getAmplifier() {
            return this.amplifier;
        }

        public PotionEffectData(StatusEffect effect, Integer durationSeconds, Integer amplifier) {
            this.effect = effect;
            this.durationSeconds = durationSeconds;
            this.amplifier = amplifier;
        }
    }
}

