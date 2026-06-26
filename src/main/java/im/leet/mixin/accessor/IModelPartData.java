/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelPartData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package im.leet.mixin.accessor;

import java.util.Map;
import net.minecraft.client.model.ModelPartData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ModelPartData.class})
public interface IModelPartData {
    @Accessor(value="children")
    public Map<String, ModelPartData> getChildren();
}

