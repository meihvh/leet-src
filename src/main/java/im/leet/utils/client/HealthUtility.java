/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.scoreboard.ReadableScoreboardScore
 *  net.minecraft.scoreboard.ScoreHolder
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.scoreboard.ScoreboardDisplaySlot
 *  net.minecraft.scoreboard.ScoreboardObjective
 */
package im.leet.utils.client;

import im.leet.MinecraftHolder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;

public class HealthUtility
implements MinecraftHolder {
    public static float get(LivingEntity e) {
        return HealthUtility.resolve(e);
    }

    public static float resolve(LivingEntity entity) {
        Float scoreboardHealth = HealthUtility.getHealthFromScoreboard(entity);
        if (scoreboardHealth != null) {
            return scoreboardHealth.floatValue();
        }
        return (float)(Math.floor(entity.method_6032() * 10.0f) / 10.0);
    }

    private static Float getHealthFromScoreboard(LivingEntity entity) {
        try {
            ReadableScoreboardScore score;
            ScoreboardObjective healthObjective;
            if (HealthUtility.mc.field_1687 == null) {
                return null;
            }
            Scoreboard scoreboard = HealthUtility.mc.field_1687.method_8428();
            if (scoreboard != null && (healthObjective = scoreboard.method_1189(ScoreboardDisplaySlot.field_45158)) != null && (score = scoreboard.method_55430((ScoreHolder)entity, healthObjective)) != null) {
                return Float.valueOf(score.method_55397());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }
}

