/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.client.world.ClientWorld$Properties
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package im.leet.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import im.leet.base.modules.impl.render.CustomWorld;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value={ClientWorld.Properties.class})
public class ClientWorldPropertiesMixin {
    @ModifyReturnValue(method={"getTimeOfDay"}, at={@At(value="RETURN")})
    private long hookGetTime(long original) {
        return CustomWorld.INSTANCE.getTime(original);
    }
}

