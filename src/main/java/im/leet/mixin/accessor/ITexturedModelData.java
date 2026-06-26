/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelData
 *  net.minecraft.client.model.TexturedModelData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package im.leet.mixin.accessor;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.TexturedModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={TexturedModelData.class})
public interface ITexturedModelData {
    @Accessor(value="data")
    public ModelData getData();
}

