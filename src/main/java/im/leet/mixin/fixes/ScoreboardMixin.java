/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.scoreboard.Team
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package im.leet.mixin.fixes;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Scoreboard.class})
public abstract class ScoreboardMixin {
    @Shadow
    @Final
    private Object2ObjectMap<String, Team> field_1427;

    @Shadow
    @Nullable
    public abstract Team method_1164(String var1);

    @Inject(method={"removeScoreHolderFromTeam"}, at={@At(value="HEAD")}, cancellable=true)
    private void removeScoreHolderFromTeam(String scoreHolderName, Team team, CallbackInfo ci) {
        ci.cancel();
        if (this.method_1164(scoreHolderName) == team) {
            this.field_1427.remove((Object)scoreHolderName);
            team.method_1204().remove(scoreHolderName);
        }
    }
}

