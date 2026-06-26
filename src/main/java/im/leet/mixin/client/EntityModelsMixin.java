/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelData
 *  net.minecraft.client.model.ModelPartData
 *  net.minecraft.client.model.TexturedModelData
 *  net.minecraft.client.render.entity.model.EntityModelLayer
 *  net.minecraft.client.render.entity.model.EntityModels
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.mixin.accessor.IModelPartData;
import im.leet.mixin.accessor.ITexturedModelData;
import java.util.Map;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityModels.class})
public class EntityModelsMixin {
    @Inject(method={"getModels"}, at={@At(value="TAIL")})
    private static void getModels(CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> cir) {
        for (Map.Entry entry : ((Map)cir.getReturnValue()).entrySet()) {
            EntityModelLayer layer = (EntityModelLayer)entry.getKey();
            ModelData data = ((ITexturedModelData)entry.getValue()).getData();
            ModelPartData root = data.method_32111();
            Map<String, ModelPartData> s = ((IModelPartData)root).getChildren();
            for (Map.Entry<String, ModelPartData> entry2 : s.entrySet()) {
                ModelPartData modelPartData = entry2.getValue();
            }
        }
    }
}

