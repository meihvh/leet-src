/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.movement;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventBoost;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventReceivePacket;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.ElytraAura;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.player.MoveUtility;
import java.util.function.Supplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SuperFirework
extends Module {
    public static final SuperFirework INSTANCE = new SuperFirework();
    public EnumSetting<Mode> mode = this.enumSetting("Boost mode", Mode.BravoHvH);
    public CheckBox distill = this.checkbox("Slow distill", true);
    public CheckBox boostOnDefensive = this.checkbox("Boost on Defensive", true);
    public EnumSetting<DefensiveMode> defensiveMode;
    public SliderSetting multiplier;
    public SliderSetting multiplierTimer;
    public SliderSetting multiplierMotion;
    private final TimeUtility speedtopTimer;
    private boolean activatedBooster;
    public float speedtop;
    EventBus<Event> events;

    private SuperFirework() {
        super("Super Firework", Category.MOVEMENT, "Increases the maximum power of fireworks", new Tag[0]);
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.boostOnDefensive::get;
        this.defensiveMode = (EnumSetting)this.enumSetting("Boost mode on Defensive", DefensiveMode.TargetSpeed).visible(supplierArray);
        this.multiplier = (SliderSetting)this.sliderSetting("Multiplier", 0.3f, 0.0f, 2.5f).increment(0.1f).visible(() -> this.mode.is(Mode.Engine));
        this.multiplierTimer = this.sliderSetting("Multiplier Timer", 1.0f, 1.0f, 1.2f).increment(0.01f);
        this.multiplierMotion = this.sliderSetting("Multiplier Motion", 1.0f, 1.0f, 1.2f).increment(0.01f);
        this.speedtopTimer = new TimeUtility();
        this.activatedBooster = false;
        this.speedtop = 0.0f;
        this.events = event -> {
            EventReceivePacket e;
            if (event instanceof EventBoost) {
                EventBoost ctx = (EventBoost)event;
                LivingEntity entity = TargetsUtility.getTarget();
                float pitch = Client.ROTATION.getRotate().getPitch();
                float yaw = Client.ROTATION.getRotate().getYaw();
                float normalizedYaw = yaw % 180.0f;
                if (normalizedYaw > 90.0f) {
                    normalizedYaw -= 180.0f;
                } else if (normalizedYaw < -90.0f) {
                    normalizedYaw += 180.0f;
                }
                if (SuperFirework.mc.field_1724.method_6128()) {
                    Client.TIMER = this.multiplierTimer.get();
                }
                if (SuperFirework.mc.field_1724.method_6128() && MoveUtility.getBPS((LivingEntity)SuperFirework.mc.field_1724) < 45.0f) {
                    SuperFirework.mc.field_1724.method_18799(SuperFirework.mc.field_1724.method_18798().method_1021((double)this.multiplierMotion.get()));
                }
                switch (this.mode.get().ordinal()) {
                    case 1: {
                        Vec3d look = Client.ROTATION.getRotate().toVector();
                        double ay = Math.max((double)0.2f, Math.abs(look.field_1351));
                        double angleOffsetFactor = 1.0f - Math.abs(MathHelper.method_15393((float)(45.0f - SuperFirework.mc.field_1724.method_36454() % 90.0f))) / 45.0f;
                        double boost = 1.5 + (double)this.multiplier.get() * Math.pow(angleOffsetFactor, 2.0);
                        ctx.speedz = ctx.speedx = boost + ay * 0.735;
                        ctx.speedy = ay * 1.3;
                        break;
                    }
                    case 0: {
                        ctx.speedz = ctx.speedx = Math.max((double)this.getSpeedForPitch(normalizedYaw) * 0.995, (double)(this.getSpeedForPitch1(pitch, normalizedYaw) * 0.985f));
                        ctx.speedy = Math.max(this.getSpeedYForPitch(pitch), this.getSpeedYForPitch1(pitch));
                    }
                }
                if (this.boostOnDefensive.get() && ElytraAura.INSTANCE.antiAimIsActive && ElytraAura.INSTANCE.defensive.isEnabled() && ElytraAura.INSTANCE.isEnabled() && TargetsUtility.getTarget() != null) {
                    switch (this.defensiveMode.get().ordinal()) {
                        case 0: {
                            float r = MathUtility.random(0.97f, 1.0f);
                            ctx.speedx *= (double)r;
                            ctx.speedz = ctx.speedx;
                            ctx.speedy *= (double)r;
                            break;
                        }
                        case 2: {
                            ctx.speedx = 1.5;
                            ctx.speedz = 1.5;
                            ctx.speedy = 1.5;
                            break;
                        }
                        case 1: {
                            float speedtarget = (float)Math.max((double)(SuperFirework.bpstarget(entity) / 20.0f), 1.6);
                            ctx.speedx = speedtarget;
                            ctx.speedz = speedtarget;
                            ctx.speedy = speedtarget;
                        }
                    }
                }
                LivingEntity target = TargetsUtility.getTarget();
                if (this.distill.get() && target != null && ElytraAura.INSTANCE.isEnabled() && ElytraAura.INSTANCE.targetIsLeave(target)) {
                    Vec3d distillPos = target.method_30950(mc.method_61966().method_60637(true)).method_1019(target.method_5828(mc.method_61966().method_60637(true)).method_1021(8.0));
                    if (SuperFirework.mc.field_1724.method_19538().method_1022(distillPos) < 1.5) {
                        ctx.speedx = 1.3f;
                        ctx.speedz = 1.3f;
                        ctx.speedy = 1.3f;
                    }
                }
            }
            if (event instanceof EventReceivePacket && (e = (EventReceivePacket)event).getPacket() instanceof PlayerPositionLookS2CPacket) {
                this.activatedBooster = true;
            }
            if (event instanceof EventGameTick) {
                EventGameTick gameTick = (EventGameTick)event;
                if (this.speedtopTimer.reached(40L) && SuperFirework.mc.field_1724.method_6128()) {
                    if (!this.activatedBooster) {
                        this.speedtop += 1.0E-4f;
                    }
                    if (SuperFirework.mc.field_1724.field_6012 % 10 == 0) {
                        this.speedtop -= 5.0E-4f;
                    }
                    if (this.speedtop > 0.0011f) {
                        this.speedtop = 0.0f;
                    }
                    if (this.activatedBooster) {
                        this.speedtop = 0.0f;
                    }
                    this.speedtopTimer.reset();
                }
            }
        };
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        Client.TIMER = 1.0f;
    }

    public static float bpstarget(LivingEntity entity) {
        if (entity == null) {
            return 1.47f;
        }
        double distance = Math.sqrt(Math.pow(entity.method_23317() - entity.field_6038, 2.0) + Math.pow(entity.method_23318() - entity.field_5971, 2.0) + Math.pow(entity.method_23321() - entity.field_5989, 2.0));
        float bps = (float)(distance * 20.0);
        return (float)Math.round(bps * 10.0f) / 10.2f;
    }

    private float getSpeedForPitch1(float pitch, float yaw) {
        if (Math.abs(pitch) >= 38.0f && Math.abs(pitch) <= 52.0f) {
            return 1.99f;
        }
        if (Math.abs(pitch) >= 32.0f && Math.abs(pitch) <= 58.0f) {
            return 1.97f;
        }
        if (Math.abs(pitch) >= 28.0f && Math.abs(pitch) <= 62.0f) {
            return 1.95f;
        }
        if (Math.abs(yaw) >= 29.0f && Math.abs(yaw) <= 61.0f || Math.abs(pitch) >= 29.0f && Math.abs(pitch) <= 61.0f) {
            return 1.961f;
        }
        if (!(Math.abs(yaw) >= 27.0f && Math.abs(yaw) <= 63.0f || Math.abs(pitch) >= 27.0f && Math.abs(pitch) <= 63.0f)) {
            if (!(Math.abs(yaw) >= 22.0f && Math.abs(yaw) <= 68.0f || Math.abs(pitch) >= 22.0f && Math.abs(pitch) <= 68.0f)) {
                if (Math.abs(yaw) >= 15.0f && Math.abs(yaw) <= 75.0f || Math.abs(pitch) >= 15.0f && Math.abs(pitch) <= 75.0f) {
                    return 1.7f;
                }
                if (!(Math.abs(yaw) >= 13.0f && Math.abs(yaw) <= 77.0f || Math.abs(pitch) >= 13.0f && Math.abs(pitch) <= 77.0f)) {
                    if (!(Math.abs(yaw) >= 12.0f && Math.abs(yaw) <= 78.0f || Math.abs(pitch) >= 12.0f && Math.abs(pitch) <= 78.0f)) {
                        if (Math.abs(yaw) >= 8.0f && Math.abs(yaw) <= 82.0f || Math.abs(pitch) >= 11.0f && Math.abs(pitch) <= 79.0f) {
                            return 1.66f;
                        }
                        return !(Math.abs(yaw) >= 5.0f && Math.abs(yaw) <= 85.0f || Math.abs(pitch) >= 8.0f && Math.abs(pitch) <= 82.0f) ? 1.621f : 1.63f;
                    }
                    return 1.68f;
                }
                return 1.68f;
            }
            return 1.77f;
        }
        return 1.87f;
    }

    private float getSpeedForPitch(float yaw) {
        if ((yaw = Math.abs(yaw)) > 0.0f && yaw < 5.0f) {
            return 1.65f;
        }
        if (yaw < 90.0f && yaw > 85.0f) {
            return 1.65f;
        }
        if (yaw > 5.0f && yaw < 10.0f) {
            return 1.66f;
        }
        if (yaw > 80.0f && yaw < 85.0f) {
            return 1.66f;
        }
        if (yaw > 10.0f && yaw < 15.0f) {
            return 1.67f;
        }
        if (yaw > 75.0f && yaw < 80.0f) {
            return 1.67f;
        }
        if (yaw > 15.0f && yaw < 20.0f) {
            return 1.75f;
        }
        if (yaw > 70.0f && yaw < 75.0f) {
            return 1.75f;
        }
        if (yaw > 20.0f && yaw < 25.0f) {
            return 1.76f;
        }
        if (yaw > 65.0f && yaw < 70.0f) {
            return 1.76f;
        }
        if (yaw > 25.0f && yaw < 30.0f) {
            return 1.83f;
        }
        if (yaw > 60.0f && yaw < 65.0f) {
            return 1.83f;
        }
        if (yaw > 30.0f && yaw < 35.0f) {
            return 1.93f;
        }
        if (yaw > 55.0f && yaw < 60.0f) {
            return 1.93f;
        }
        if (yaw > 35.0f && yaw < 40.0f) {
            return 1.95f;
        }
        if (yaw > 50.0f && yaw < 55.0f) {
            return 1.95f;
        }
        if (yaw > 40.0f && yaw < 45.0f) {
            return 1.99f;
        }
        if (yaw > 45.0f && yaw < 50.0f) {
            return 1.99f;
        }
        return 1.6f;
    }

    private float getSpeedYForPitch1(float yaw) {
        if ((yaw = Math.abs(yaw)) > 0.0f && yaw < 5.0f) {
            return 1.59f;
        }
        if (yaw > 5.0f && yaw < 10.0f) {
            return 1.61f;
        }
        if (yaw > 10.0f && yaw < 15.0f) {
            return 1.7f;
        }
        if (yaw > 15.0f && yaw < 20.0f) {
            return 1.68f;
        }
        if (yaw > 20.0f && yaw < 25.0f) {
            return 1.8f;
        }
        if (yaw > 25.0f && yaw < 30.0f) {
            return 1.82f;
        }
        if (yaw > 30.0f && yaw < 35.0f) {
            return 1.95f;
        }
        if (yaw > 35.0f && yaw < 40.0f) {
            return 1.99f;
        }
        if (yaw > 40.0f && yaw < 45.0f) {
            return 1.98f;
        }
        return 1.6f;
    }

    private float getSpeedYForPitch(float pitch) {
        if (Math.abs(pitch) >= 37.0f && Math.abs(pitch) <= 38.0f) {
            return 1.98f;
        }
        if (Math.abs(pitch) >= 20.0f && Math.abs(pitch) <= 37.0f) {
            return 1.95f;
        }
        if (Math.abs(pitch) >= 25.0f && Math.abs(pitch) <= 30.0f) {
            return 1.95f;
        }
        if (Math.abs(pitch) >= 35.0f && Math.abs(pitch) <= 45.0f) {
            return 1.98f;
        }
        if (Math.abs(pitch) >= 40.0f && Math.abs(pitch) <= 50.0f) {
            return 1.96f;
        }
        if (Math.abs(pitch) >= 50.0f && Math.abs(pitch) <= 60.0f) {
            return 1.95f;
        }
        if (Math.abs(pitch) >= 51.0f && Math.abs(pitch) <= 61.0f) {
            return 1.86f;
        }
        if (Math.abs(pitch) >= 52.0f && Math.abs(pitch) <= 65.0f) {
            return 1.7f;
        }
        return 1.62f;
    }

    public static enum Mode {
        BravoHvH,
        Engine;

    }

    public static enum DefensiveMode {
        Randomize,
        TargetSpeed,
        Disable;

    }
}

