/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.hud.BossBarHud
 *  net.minecraft.client.gui.hud.ClientBossBar
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package im.leet.mixin.accessor;

import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={BossBarHud.class})
public interface IBossBarHud {
    @Accessor(value="bossBars")
    public Map<UUID, ClientBossBar> client$getBossBars();
}

