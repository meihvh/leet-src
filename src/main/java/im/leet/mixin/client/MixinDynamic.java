/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.floats.FloatUnaryOperator
 *  net.minecraft.client.render.RenderTickCounter$Dynamic
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package im.leet.mixin.client;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={RenderTickCounter.Dynamic.class})
public class MixinDynamic {
    @Shadow
    private float field_51958;
    @Shadow
    private long field_51962;
    @Shadow
    private float field_51959;
    @Shadow
    @Final
    private FloatUnaryOperator field_51965;
    @Shadow
    @Final
    private float field_51964;
}

