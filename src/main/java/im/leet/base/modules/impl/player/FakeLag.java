/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.player;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventKey;
import im.leet.api.events.list.EventSendPacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.BlinkUtility;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class FakeLag
extends Module {
    public static final FakeLag INSTANCE = new FakeLag();
    public EnumSetting<Mode> mode = this.enumSetting("Mode", Mode.Packets);
    public SliderSetting ppt = (SliderSetting)this.sliderSetting("Packets per tick", 100.0f, 0.0f, 100.0f).visible(() -> this.mode.is(Mode.Packets));
    public KeybindSetting release = this.keybindSetting("Release lag", -1);
    public KeybindSetting releaseInstant = this.keybindSetting("Release lag instantly", -1);
    private Vec3d lastSent = Vec3d.field_1353;
    private final TimeUtility time = new TimeUtility();
    private final BlinkUtility blink = new BlinkUtility();
    EventBus<Event> events = event -> {
        EventKey e;
        if (event instanceof EventKey && (e = (EventKey)event).getKey() == this.releaseInstant.getBind() && e.action == 1) {
            this.sendAll();
        }
        if (event instanceof EventSendPacket) {
            EventSendPacket p = (EventSendPacket)event;
            this.blink.queue(p);
        }
        if (event instanceof EventGameTick) {
            if (this.release.getBind() != -1 && InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)this.release.getBind())) {
                for (int i = 0; i < FakeLag.mc.field_1724.field_6012 % 20; ++i) {
                    this.sendFirst();
                }
            } else if (this.mode.is(Mode.Packets)) {
                for (int i = 0; i < this.ppt.getInt(); ++i) {
                    this.sendFirst();
                }
            } else if (FakeLag.mc.field_1724.field_6017 > 0.0 && FakeLag.mc.field_1724.field_6017 < 0.1) {
                this.sendAll();
            }
        }
        if (event instanceof Event3D) {
            Event3D e2 = (Event3D)event;
            e2.stack.method_22903();
            Client.RENDERER.toCamera(e2.stack);
            VertexRendering.method_62295((MatrixStack)e2.stack, (VertexConsumer)e2.buffer.getBuffer((RenderLayer)RenderLayer.field_21695), (Box)FakeLag.mc.field_1724.method_5829().method_997(this.lastSent.method_1020(FakeLag.mc.field_1724.method_19538())), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            e2.stack.method_22909();
        }
    };

    private FakeLag() {
        super("Fake Lag", Category.PLAYER, "Artificially increases lags", new Tag[0]);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.sendAll();
    }

    private void sendFirst() {
        if (!this.blink.queue.isEmpty()) {
            PlayerMoveC2SPacket p;
            Packet<?> packet = this.blink.queue.poll().packet();
            if (packet instanceof PlayerMoveC2SPacket && (p = (PlayerMoveC2SPacket)packet).method_36171()) {
                this.lastSent = new Vec3d(p.method_12269(FakeLag.mc.field_1724.method_23317()), p.method_12268(FakeLag.mc.field_1724.method_23318()), p.method_12274(FakeLag.mc.field_1724.method_23321()));
            }
            NetworkUtility.sendWithoutEvent(packet);
        }
    }

    private void sendAll() {
        this.blink.flush(snap -> {
            PlayerMoveC2SPacket p;
            Packet<?> patt0$temp = snap.packet();
            if (patt0$temp instanceof PlayerMoveC2SPacket && (p = (PlayerMoveC2SPacket)patt0$temp).method_36171()) {
                this.lastSent = new Vec3d(p.method_12269(FakeLag.mc.field_1724.method_23317()), p.method_12268(FakeLag.mc.field_1724.method_23318()), p.method_12274(FakeLag.mc.field_1724.method_23321()));
                System.out.println(p.method_12268(FakeLag.mc.field_1724.method_23318()) + " " + p.method_12273());
            }
            return true;
        });
    }

    public static enum Mode {
        Packets,
        Time;

    }
}

