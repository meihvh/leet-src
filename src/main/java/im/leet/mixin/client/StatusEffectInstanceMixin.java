/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package im.leet.mixin.client;

import im.leet.utils.client.mixin.IStatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={StatusEffectInstance.class})
public class StatusEffectInstanceMixin
implements IStatusEffectInstance {
    @Unique
    float lerped = 0.0f;

    @Override
    public float getLerp() {
        return this.lerped;
    }

    @Override
    public void setLerp(float value) {
        this.lerped = value;
    }
}

