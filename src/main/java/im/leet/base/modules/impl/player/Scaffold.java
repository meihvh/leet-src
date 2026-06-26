/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 *  net.minecraft.util.Hand
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.BlockView
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventPacketTick;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.point.PointTracker;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;
import im.leet.utils.network.NetworkUtility;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;

public class Scaffold
extends Module {
    public static final Scaffold INSTANCE = new Scaffold();
    private final List<Vec3i> offsets = this.makeOffsets(0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5);
    final MultiEnumSetting<Feature> features = this.multiEnumSetting("Features", new Feature[]{Feature.Swap});
    final SliderSetting placeDelay = this.sliderSetting("Delay", 0.0f, 0.0f, 20.0f);
    final CheckBox noConsume = this.checkbox("No consume", false);
    final EnumSetting<SameY> sameY = this.enumSetting("Same Y", SameY.Off);
    int startY;
    int placeY;
    final Group rotations = this.group("Rotations");
    final EnumSetting<PointTracker.Mode> aimMode = this.rotations.enumSetting("Aim Mode", PointTracker.Mode.Random);
    final EnumSetting<MovementCorrection> correction = this.rotations.enumSetting("Correction", MovementCorrection.NONE);
    final CheckBox tick = this.rotations.checkbox("Tick rotation", false);
    final Group telly = (Group)this.group("Telly").visible(() -> this.features.get(Feature.Telly));
    final SliderSetting tellyAirTicks = this.telly.sliderSetting("Air Ticks", 0.0f, 0.0f, 10.0f);
    int airTicks;
    final Group blockFly = (Group)this.group("BlockFly").visible(() -> this.features.get(Feature.BlockFly));
    final EnumSetting<BlockFlyMode> blockFlyMode = this.blockFly.enumSetting("Mode", BlockFlyMode.GrimSwap);
    final SliderSetting bfPacketDelay = (SliderSetting)this.blockFly.sliderSetting("Delay", 500.0f, 0.0f, 1000.0f).visible(() -> this.blockFlyMode.is(BlockFlyMode.GrimSwap));
    final SliderSetting bfBlink = (SliderSetting)this.blockFly.sliderSetting("Blink", 1345.0f, 0.0f, 3000.0f).visible(() -> this.blockFlyMode.is(BlockFlyMode.GrimBlink));
    final BlinkUtility blink = new BlinkUtility();
    final TimeUtility time = new TimeUtility();
    int ticks;
    EventBus<Event> events = event -> {
        if (Scaffold.mc.field_1724 == null) {
            return;
        }
        if (event instanceof EventGameTick) {
            ItemStack stack;
            if (Scaffold.mc.field_1724.method_6047().method_7960() && Scaffold.mc.field_1724.method_6079().method_7960()) {
                return;
            }
            if (this.ticks > 0) {
                --this.ticks;
                return;
            }
            if (!Scaffold.mc.field_1724.method_24828()) {
                ++this.airTicks;
            } else {
                this.airTicks = 0;
                if (this.features.get(Feature.Telly)) {
                    Scaffold.mc.field_1724.method_6043();
                    return;
                }
            }
            if (this.features.get(Feature.Telly) && this.tellyAirTicks.get() > 0.0f && (float)this.airTicks < this.tellyAirTicks.get() && Scaffold.mc.field_1724.method_23318() - (double)this.startY > 0.49) {
                return;
            }
            BlockHitResult ray = this.find();
            if (ray == null) {
                return;
            }
            Angle rotation = RotationUtility.calcRotate(ray.method_17784(), true).fix();
            if (this.tick.get()) {
                if (Client.ROTATION.getRotate() != rotation) {
                    NetworkUtility.sendAngle(rotation);
                }
            } else {
                Client.ROTATION.rotate(LinearRotation.INSTANCE, rotation, 1, false, this.correction.get(), 40);
            }
            Hand hand = Scaffold.mc.field_1724.method_6047().method_7960() ? Hand.field_5810 : Hand.field_5808;
            ItemStack itemStack = stack = hand == Hand.field_5810 ? Scaffold.mc.field_1724.method_6079() : Scaffold.mc.field_1724.method_6047();
            if (this.features.get(Feature.BlockFly) && this.blockFlyMode.is(BlockFlyMode.GrimSwap)) {
                mc.method_1562().method_52787((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.field_12969, BlockPos.field_10980, Direction.field_11033));
            }
            Scaffold.mc.field_1761.method_2896(Scaffold.mc.field_1724, hand, ray);
            if (this.features.get(Feature.BlockFly) && this.blockFlyMode.is(BlockFlyMode.GrimSwap)) {
                mc.method_1562().method_52787((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.field_12969, BlockPos.field_10980, Direction.field_11033));
            }
            if (this.noConsume.get()) {
                stack.method_7939(stack.method_7947() + 1);
            }
            Scaffold.mc.field_1724.method_6104(hand);
            if (this.tick.get() && Client.ROTATION.getRotate() != rotation) {
                NetworkUtility.sendAngle(Client.ROTATION.getRotate());
            }
            if (Scaffold.mc.field_1724.method_24828()) {
                this.placeY = Scaffold.mc.field_1724.method_31478() - 1;
            }
            if (Scaffold.mc.field_1690.field_1903.method_1434()) {
                this.startY = Scaffold.mc.field_1724.method_31478();
            }
            this.ticks = Math.max(this.ticks, this.placeDelay.getInt());
        }
        if (this.features.get(Feature.BlockFly)) {
            switch (this.blockFlyMode.get().ordinal()) {
                case 0: {
                    EventSendPacket e;
                    if (event instanceof EventSendPacket) {
                        e = (EventSendPacket)event;
                        this.blink.queue(e);
                    }
                    if (!(event instanceof EventPacketTick)) break;
                    this.blink.flushTime(this.bfPacketDelay.getLong());
                    break;
                }
                case 1: {
                    if (!(event instanceof EventSendPacket)) break;
                    EventSendPacket e = (EventSendPacket)event;
                    if (Scaffold.mc.field_1724.method_24828() || this.time.reached(this.bfBlink.getLong(), true)) {
                        this.blink.flush();
                        break;
                    }
                    this.blink.queue(e);
                }
            }
        }
    };

    private Scaffold() {
        super("Scaffold", Category.PLAYER, "Puts blocks under itself", new Tag[0]);
    }

    private List<Vec3i> makeOffsets(int ... values) {
        ArrayList<Vec3i> list = new ArrayList<Vec3i>(values.length * values.length * 2);
        for (int x : values) {
            for (int z : values) {
                list.add(new Vec3i(x, 0, z));
                list.add(new Vec3i(x, -1, z));
            }
        }
        return list;
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (Scaffold.mc.field_1724 == null) {
            return;
        }
        this.placeY = Scaffold.mc.field_1724.method_31478() - 1;
        this.startY = Scaffold.mc.field_1724.method_31478();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.blink.flush();
    }

    private BlockHitResult find() {
        BlockPos blockPos = Scaffold.mc.field_1724.method_24515().method_10069(0, -1, 0);
        switch (this.sameY.get().ordinal()) {
            case 1: {
                blockPos = blockPos.method_33096(this.placeY);
                break;
            }
            case 2: {
                if (Scaffold.mc.field_1690.field_1903.method_1434()) break;
                blockPos = blockPos.method_33096(this.placeY);
                break;
            }
        }
        if (!Scaffold.mc.field_1687.method_8320(blockPos).method_45474()) {
            return null;
        }
        BlockPosWithDirection next = this.tryScanExtend(blockPos);
        if (next == null) {
            return null;
        }
        Vec3d point = this.aimMode.get().getPoint(new Box(next.pos).method_1014(-0.2), Scaffold.mc.field_1724.method_33571());
        return new BlockHitResult(point, next.direction, next.pos, false);
    }

    private BlockPosWithDirection tryScanExtend(BlockPos blockPos) {
        BlockPosWithDirection ret = Scaffold.tryExtend(blockPos);
        if (ret != null) {
            return ret;
        }
        List<BlockPos> offsets = this.offsets.stream().map(arg_0 -> ((BlockPos)blockPos).method_10081(arg_0)).sorted(Comparator.comparingDouble(pos -> Scaffold.mc.field_1724.method_5649((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260()))).toList();
        for (BlockPos offset : offsets) {
            ret = Scaffold.tryExtend(offset);
            if (ret == null) continue;
            return ret;
        }
        return null;
    }

    public static BlockPosWithDirection tryExtend(BlockPos pos) {
        BlockPos[] attempt = new BlockPos[]{pos.method_10069(0, -1, 0), pos.method_10069(-1, 0, 0), pos.method_10069(1, 0, 0), pos.method_10069(0, 0, 1), pos.method_10069(0, 0, -1)};
        Direction[] direction = new Direction[]{Direction.field_11036, Direction.field_11034, Direction.field_11039, Direction.field_11043, Direction.field_11035};
        for (int i = 0; i < attempt.length; ++i) {
            BlockPos next = attempt[i];
            if (!Scaffold.mc.field_1687.method_8320(next).method_26212((BlockView)Scaffold.mc.field_1687, next)) continue;
            return new BlockPosWithDirection(next, direction[i]);
        }
        return null;
    }

    static enum Feature {
        Telly,
        BlockFly,
        Swap;

    }

    static enum SameY {
        Off,
        On,
        Jump;

    }

    static enum BlockFlyMode {
        GrimSwap,
        GrimBlink;

    }

    public record BlockPosWithDirection(BlockPos pos, Direction direction) {
    }
}

