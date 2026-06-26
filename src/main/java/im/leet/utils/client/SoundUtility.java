/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.sound.SoundEvent
 */
package im.leet.utils.client;

import im.leet.MinecraftHolder;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvent;

public class SoundUtility
implements MinecraftHolder {
    public static void playSound(SoundEvent sound, float volume) {
        if (SoundUtility.mc.field_1724 == null) {
            return;
        }
        SoundUtility.mc.field_1687.method_43128((Entity)SoundUtility.mc.field_1724, SoundUtility.mc.field_1724.method_23317(), SoundUtility.mc.field_1724.method_23318(), SoundUtility.mc.field_1724.method_23321(), sound, SoundUtility.mc.field_1724.method_5634(), volume, 1.0f);
    }
}

