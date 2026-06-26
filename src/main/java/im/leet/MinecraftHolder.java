/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.Window
 */
package im.leet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public interface MinecraftHolder {
    public static final MinecraftClient mc = MinecraftClient.method_1551();
    public static final Window window = MinecraftClient.method_1551().method_22683();
}

