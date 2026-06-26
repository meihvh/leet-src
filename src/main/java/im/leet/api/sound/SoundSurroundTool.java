/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package im.leet.api.sound;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SoundSurroundTool {
    private ClientPlayerEntity player;
    private boolean rtxDebug = false;
    private boolean tooPerfomance = false;
    private final List<Vec3d> listOfTestVecs = new ArrayList<Vec3d>();

    public static SoundSurroundTool build() {
        return new SoundSurroundTool();
    }

    public float[] getGainArgsFromWorld() {
        if (this.player == null || this.player.method_37908() == null) {
            return new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        }
        World world = this.player.method_37908();
        BlockPos playerPos = this.player.method_24515();
        int scanRadius = this.tooPerfomance ? 3 : 5;
        int scanHeight = this.tooPerfomance ? 2 : 3;
        int solidBlocks = 0;
        int totalBlocks = 0;
        int airBlocks = 0;
        for (int x = -scanRadius; x <= scanRadius; ++x) {
            for (int y = -scanHeight; y <= scanHeight; ++y) {
                for (int z = -scanRadius; z <= scanRadius; ++z) {
                    BlockPos checkPos = playerPos.method_10069(x, y, z);
                    BlockState state = world.method_8320(checkPos);
                    ++totalBlocks;
                    if (!state.method_26215()) {
                        ++solidBlocks;
                        if (!this.rtxDebug || this.listOfTestVecs.size() >= 100) continue;
                        this.listOfTestVecs.add(new Vec3d((double)checkPos.method_10263() + 0.5, (double)checkPos.method_10264() + 0.5, (double)checkPos.method_10260() + 0.5));
                        continue;
                    }
                    ++airBlocks;
                }
            }
        }
        float enclosureRatio = totalBlocks > 0 ? (float)solidBlocks / (float)totalBlocks : 0.0f;
        float echoLevel = MathHelper.method_15363((float)(enclosureRatio * 2.0f), (float)0.0f, (float)1.5f);
        float reflectionLevel = MathHelper.method_15363((float)(enclosureRatio * 1.5f), (float)0.0f, (float)1.0f);
        float lowPassGain = MathHelper.method_15363((float)(1.0f - enclosureRatio * 0.3f), (float)0.5f, (float)1.0f);
        float lowPassGainHF = MathHelper.method_15363((float)(1.0f - enclosureRatio * 0.5f), (float)0.3f, (float)1.0f);
        if (this.player.method_5869()) {
            echoLevel *= 0.3f;
            reflectionLevel *= 0.5f;
            lowPassGain *= 0.6f;
            lowPassGainHF *= 0.4f;
        }
        if (this.player.method_5771()) {
            echoLevel *= 0.2f;
            reflectionLevel *= 0.3f;
            lowPassGain *= 0.5f;
            lowPassGainHF *= 0.3f;
        }
        return new float[]{echoLevel, reflectionLevel, lowPassGain, lowPassGainHF};
    }

    public ClientPlayerEntity getPlayer() {
        return this.player;
    }

    public void setPlayer(ClientPlayerEntity player) {
        this.player = player;
    }

    public boolean isRtxDebug() {
        return this.rtxDebug;
    }

    public void setRtxDebug(boolean rtxDebug) {
        this.rtxDebug = rtxDebug;
    }

    public boolean isTooPerfomance() {
        return this.tooPerfomance;
    }

    public void setTooPerfomance(boolean tooPerfomance) {
        this.tooPerfomance = tooPerfomance;
    }

    public List<Vec3d> getListOfTestVecs() {
        return this.listOfTestVecs;
    }
}

