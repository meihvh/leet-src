/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package im.leet.base.modules.impl.movement;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventMove;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.utils.player.MoveUtility;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class Jesus
extends Module {
    public static final Jesus INSTANCE = new Jesus();
    int ticks;
    EventBus<Event> events = event -> {
        if (event instanceof EventMove) {
            double posY;
            EventMove e = (EventMove)event;
            BlockPos playerPos = new BlockPos((int)Jesus.mc.field_1724.method_23317(), (int)(Jesus.mc.field_1724.method_23318() + 0.008), (int)Jesus.mc.field_1724.method_23321());
            Block playerBlock = Jesus.mc.field_1687.method_8320(playerPos).method_26204();
            if (playerBlock == Blocks.field_10382 && !Jesus.mc.field_1724.method_24828()) {
                boolean isUp = Jesus.mc.field_1687.method_8320(new BlockPos((int)Jesus.mc.field_1724.method_23317(), (int)(Jesus.mc.field_1724.method_23318() + 0.03), (int)Jesus.mc.field_1724.method_23321())).method_26204() == Blocks.field_10382;
                float yPort = (double)MoveUtility.getSpeed() > 0.1 ? 0.02f : 0.032f;
                Jesus.mc.field_1724.method_18800(Jesus.mc.field_1724.method_18798().field_1352, Jesus.mc.field_1724.field_6017 < 3.5 ? (double)(isUp ? yPort : -yPort) : -0.1, Jesus.mc.field_1724.method_18798().field_1350);
            }
            if ((posY = Jesus.mc.field_1724.method_23318()) > (double)((int)posY) + 0.89 && posY <= (double)((int)posY + 1) || Jesus.mc.field_1724.field_6017 > 3.5) {
                BlockPos waterBlockPos;
                Block waterBlock;
                Jesus.mc.field_1724.method_5814(Jesus.mc.field_1724.method_23317(), (double)((int)posY + 1) + 1.0E-45, Jesus.mc.field_1724.method_23321());
                if (!Jesus.mc.field_1724.method_5799() && (waterBlock = Jesus.mc.field_1687.method_8320(waterBlockPos = new BlockPos((int)Jesus.mc.field_1724.method_23317(), (int)(Jesus.mc.field_1724.method_23318() - 0.1), (int)Jesus.mc.field_1724.method_23321())).method_26204()) == Blocks.field_10382) {
                    e.ground = false;
                    if (this.ticks == 1) {
                        MoveUtility.setSpeed(1.1f);
                        this.ticks = 0;
                    } else {
                        this.ticks = 1;
                    }
                }
            }
        }
    };

    public Jesus() {
        super("Jesus", Category.MOVEMENT, "Allows you to walk on water", new Tag[0]);
    }
}

