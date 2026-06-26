/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.registry.Registries
 *  net.minecraft.registry.Registry
 *  net.minecraft.sound.SoundEvent
 *  net.minecraft.util.Identifier
 */
package im.leet.utils.stupidity;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class MySoundEvents {
    public static final SoundEvent moduleOn = MySoundEvents.register("module_on");
    public static final SoundEvent moduleOff = MySoundEvents.register("module_off");

    public static void init() {
    }

    private static SoundEvent register(String name) {
        return (SoundEvent)Registry.method_10230((Registry)Registries.field_41172, (Identifier)Identifier.method_60655((String)"leet", (String)name), (Object)SoundEvent.method_47908((Identifier)Identifier.method_60655((String)"leet", (String)name)));
    }

    private MySoundEvents() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

