/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.util.InputUtil$Key
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package im.leet.mixin.accessor;

import java.util.Map;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={KeyBinding.class})
public interface IKeyBinding {
    @Accessor(value="boundKey")
    public InputUtil.Key client$boundKey();

    @Invoker(value="reset")
    public void client$reset();

    @Accessor(value="KEYS_BY_ID")
    public static Map<String, KeyBinding> client$getKeysById() {
        throw new AssertionError((Object)"ugh");
    }
}

