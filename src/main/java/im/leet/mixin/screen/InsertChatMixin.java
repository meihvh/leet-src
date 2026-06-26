/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.hud.ChatHud
 *  net.minecraft.client.gui.hud.ChatHudLine
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package im.leet.mixin.screen;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={ChatHud.class})
public interface InsertChatMixin {
    @Invoker(value="addMessage")
    public void invokeAddMessage(ChatHudLine var1);

    @Invoker(value="addVisibleMessage")
    public void invokeAddVisibleMessage(ChatHudLine var1);
}

