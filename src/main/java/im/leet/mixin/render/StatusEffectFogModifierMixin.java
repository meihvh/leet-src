/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.client.render.fog.StatusEffectFogModifier
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.registry.entry.RegistryEntry
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 */
package im.leet.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import im.leet.base.modules.impl.render.Removals;
import net.minecraft.client.render.fog.StatusEffectFogModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value={StatusEffectFogModifier.class})
public abstract class StatusEffectFogModifierMixin {
    @Shadow
    public abstract RegistryEntry<StatusEffect> method_42590();

    @ModifyReturnValue(method={"shouldApply"}, at={@At(value="RETURN")})
    private boolean hookApply(boolean original) {
        Removals removals = Removals.INSTANCE;
        if (!removals.isEnabled()) {
            return original;
        }
        if (this.method_42590() == StatusEffects.field_5919) {
            return original && !removals.removals.get(Removals.Removal.Blindness);
        }
        if (this.method_42590() == StatusEffects.field_38092) {
            return original && !removals.removals.get(Removals.Removal.Darkness);
        }
        return original;
    }
}

