/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.Message
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.Items
 *  net.minecraft.text.Text
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat;

import com.mojang.brigadier.Message;
import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventAddMessage;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventBoost;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventMove;
import im.leet.api.events.list.EventPostTick;
import im.leet.api.events.list.EventTravel;
import im.leet.api.render.system.IconUse;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.Resolver;
import im.leet.base.modules.impl.combat.elytraaura.attack.ElytraAuraAttackHandler;
import im.leet.base.modules.impl.combat.elytraaura.defensive.ElytraAuraDef;
import im.leet.base.modules.impl.combat.elytraaura.math.ElytraAuraResolve;
import im.leet.base.modules.impl.combat.elytraaura.utilities.ElytraAuraDefensive;
import im.leet.base.modules.impl.combat.elytraaura.utilities.ElytraAuraUtility;
import im.leet.base.modules.impl.combat.resolver.BackTrackPosResolver;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.keybind.KeybindSetting;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.base.settings.impl.text.TextSetting;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.player.PlayerUtility;
import java.awt.Color;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class ElytraAura
extends Module {
    public static final ElytraAura INSTANCE = new ElytraAura();
    private final Group range = this.group("Range Settings");
    public SliderSetting attackRangeSetting = (SliderSetting)this.range.sliderSetting("Attack Range", 3.2f, 3.0f, 4.0f).increment(0.1f).desc("Distance to attack target");
    public SliderSetting preAttackRangeSetting = (SliderSetting)this.range.sliderSetting("Pre Range", 50.0f, 15.0f, 60.0f).increment(1.0f).desc("Distance to find targets");
    private final Group main = this.group("Main Settings");
    public CheckBox autoStartSetting = (CheckBox)this.main.checkbox("Auto Start", true).desc("Automatically start gliding when elytra equiped");
    public CheckBox autoFireWorkSetting = (CheckBox)this.main.checkbox("Auto FireWork", true).desc("Automatically use firework");
    public CheckBox smartUseFireWorkSetting;
    public SliderSetting delayUseFireWorkSetting;
    private final Group aim;
    public EnumSetting<TypeAim> typeAim;
    public CheckBox predictYawandPitchSetting;
    private final Group airStack;
    public CheckBox isAirStack;
    public CheckBox autoAirStackSetting;
    public MultiEnumSetting<Feature> ifworkAitStack;
    public KeybindSetting bindAirStackSetting;
    public final Group defensive;
    public CheckBox smartDefensiveTimeSetting;
    public SliderSetting antiAimDelayDelaySetting;
    public CheckBox timer;
    public CheckBox aimWhenTargetAttack;
    public EnumSetting<DefensiveCondition> ifWorkAntiAimSetting;
    public EnumSetting<DefensiveType> defensiveType;
    public SliderSetting strenghtRollSetting;
    public CheckBox jitterOnDefensiveSetting;
    public TextSetting offsetsYOnDefensiveSetting;
    public CheckBox rangeByter;
    private final Group autoMessage;
    public CheckBox isAutoMessage;
    public TextSetting messageField;
    private final Group fakeRotationGroup;
    public SliderSetting yawValue;
    public SliderSetting pitchValue;
    public CheckBox randomValue;
    private final Group fakeLag;
    public CheckBox isFakeLag;
    public EnumSetting<FakeLagCondition> ifWorkitFakeLagSetting;
    TimeUtility sendDelay;
    TimeUtility leaveTime;
    TimeUtility hitTargetTime;
    public TimeUtility antiAimTimer;
    public Angle defVec;
    public float scale;
    public double prevY;
    public int missAmount;
    public boolean hit;
    public boolean isd;
    public boolean antiAimIsActive;
    public boolean lastAntiaim;
    public Vec3d lastPosTarget;
    EventBus<Event> events;

    private ElytraAura() {
        super("Elytra Aura", Category.COMBAT, "PvP Aura on Elytra", Tag.RAGE);
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.autoFireWorkSetting::get;
        this.smartUseFireWorkSetting = (CheckBox)((CheckBox)this.main.checkbox("Smart Use", true).desc("Automatically calculate timing to use fireworks")).visible(supplierArray);
        this.delayUseFireWorkSetting = (SliderSetting)this.main.sliderSetting("Delay Use", 400.0f, 200.0f, 800.0f).increment(50.0f).visible(() -> this.autoFireWorkSetting.get() && !this.smartUseFireWorkSetting.get());
        this.aim = this.group("Aim Settings");
        this.typeAim = (EnumSetting)this.aim.enumSetting("Type Aim", TypeAim.FireWork).desc("Where should target");
        this.predictYawandPitchSetting = this.aim.checkbox("Predict for Target Rotation", true);
        this.airStack = this.group("Air Stack Settings");
        this.isAirStack = (CheckBox)this.airStack.checkbox("Air Stuck", false).desc("Freeze local player");
        Supplier[] supplierArray2 = new Supplier[1];
        supplierArray2[0] = this.isAirStack::get;
        this.autoAirStackSetting = (CheckBox)this.airStack.checkbox("Auto Mode", false).visible(supplierArray2);
        Supplier[] supplierArray3 = new Supplier[1];
        supplierArray3[0] = this.autoAirStackSetting::get;
        this.ifworkAitStack = (MultiEnumSetting)((MultiEnumSetting)this.airStack.multiEnumSetting("Conditions", Feature.class).desc("When should freeze local player automatically")).visible(supplierArray3);
        Supplier[] supplierArray4 = new Supplier[2];
        supplierArray4[0] = this.isAirStack::get;
        supplierArray4[1] = () -> !this.autoAirStackSetting.get();
        this.bindAirStackSetting = (KeybindSetting)this.airStack.keybindSetting("Key on Active", -1).visible(supplierArray4);
        this.defensive = this.group("Defensive Settings").toggleable(false);
        this.smartDefensiveTimeSetting = (CheckBox)this.defensive.checkbox("Smart Time", false).desc("Automatically calculate time to fly in");
        this.antiAimDelayDelaySetting = (SliderSetting)((SliderSetting)this.defensive.sliderSetting("Defensive time", 250.0f, 150.0f, 500.0f).desc("Flying off time")).increment(25.0f).visible(() -> !this.smartDefensiveTimeSetting.get());
        this.timer = (CheckBox)this.defensive.checkbox("Timer", true).desc("Speeding up game to distill target");
        this.aimWhenTargetAttack = (CheckBox)this.defensive.checkbox("Attack when target pushing", false).desc("Starting aim at target when target trying to push");
        this.ifWorkAntiAimSetting = this.defensive.enumSetting("Defensive conditions", DefensiveCondition.TargetNotGliding);
        this.defensiveType = (EnumSetting)this.defensive.enumSetting("Defensive Type", DefensiveType.Horizontal).desc("Flying off mode");
        this.strenghtRollSetting = (SliderSetting)this.defensive.sliderSetting("Strength Roll", 24.0f, 8.0f, 32.0f).increment(1.0f).visible(() -> this.defensiveType.is(DefensiveType.Roll));
        this.jitterOnDefensiveSetting = this.defensive.checkbox("Jitter on Defensive", true);
        this.offsetsYOnDefensiveSetting = this.defensive.add((TextSetting)new TextSetting("Offsets on Y (separated by commas)", "-18").desc("Offsets"));
        this.rangeByter = this.defensive.checkbox("Range byter", false);
        this.autoMessage = this.group("Auto message");
        this.isAutoMessage = (CheckBox)this.autoMessage.checkbox("Auto Message", true).desc("Send chat message when target dead");
        Supplier[] supplierArray5 = new Supplier[1];
        supplierArray5[0] = this.isAutoMessage::get;
        this.messageField = this.autoMessage.add((TextSetting)((TextSetting)((TextSetting)new TextSetting("Message Field", "!- {name} fucked by LEET").desc("Message")).desc("Chat message when target dead")).visible(supplierArray5));
        this.fakeRotationGroup = this.group("Fake Rotation").toggleable(false);
        this.yawValue = this.fakeRotationGroup.sliderSetting("Yaw range", 3.0f, 0.0f, 90.0f).increment(1.0f);
        this.pitchValue = this.fakeRotationGroup.sliderSetting("Pitch range", 3.0f, 0.0f, 90.0f).increment(1.0f);
        this.randomValue = this.autoMessage.checkbox("Random Value", true);
        this.fakeLag = this.group("FakeLag Settings");
        this.isFakeLag = this.fakeLag.checkbox("FakeLag", true);
        this.ifWorkitFakeLagSetting = this.fakeLag.enumSetting("Conditions", FakeLagCondition.TargetIsntGliding);
        this.sendDelay = new TimeUtility();
        this.leaveTime = new TimeUtility();
        this.hitTargetTime = new TimeUtility();
        this.antiAimTimer = new TimeUtility();
        this.defVec = new Angle(0.0f, 0.0f);
        this.scale = 1.0f;
        this.missAmount = 0;
        this.hit = false;
        this.isd = false;
        this.antiAimIsActive = false;
        this.lastAntiaim = false;
        this.events = event -> {
            Event e;
            if (event instanceof EventGameTick) {
                if (ElytraAura.mc.field_1724 == null || ElytraAura.mc.field_1687 == null) {
                    return;
                }
                this.updateTarget();
                if (TargetsUtility.getTarget() == null) {
                    this.antiAimIsActive = false;
                    this.antiAimTimer.reset();
                } else {
                    LivingEntity patt0$temp;
                    this.prevY = ElytraAura.mc.field_1724.method_23318();
                    Vec3d getPosTarget = ElytraAuraResolve.getPointOnTarget(TargetsUtility.getTarget());
                    if (TargetsUtility.getTarget() != null && ElytraAura.mc.field_1724.method_6128() && (patt0$temp = TargetsUtility.getTarget()) instanceof AbstractClientPlayerEntity) {
                        boolean isWeapon;
                        AbstractClientPlayerEntity target = (AbstractClientPlayerEntity)patt0$temp;
                        boolean bl = isWeapon = target.method_6047().method_7909() instanceof AxeItem || target.method_6047().method_7909().equals(Items.field_22022) || target.method_6047().method_7909().equals(Items.field_8802) || target.method_6047().method_7909().equals(Items.field_8845) || target.method_6047().method_7909().equals(Items.field_8091) || target.method_6047().method_7909().equals(Items.field_8371) || target.method_6047().method_7909().equals(Items.field_8528);
                        if (target.field_6252) {
                            this.leaveTime.reset();
                        }
                        if (target != TargetsUtility.getLastTarget()) {
                            this.missAmount = 0;
                        }
                        if (target.field_6252 && isWeapon && ElytraAura.mc.field_1724.method_19538().method_1022(getPosTarget) < 4.0 && ElytraAura.mc.field_1724.field_6235 == 0 && this.hitTargetTime.reached(300L, true)) {
                            Client.NOTIFIES.add(Text.method_54155((Message)target.method_5477().method_27661().method_10852((Text)Text.method_30163((String)" missed hit due resolver issue").method_27661().method_54663(Color.RED.getRGB()))), IconUse.WARN, 5000L);
                            ++this.missAmount;
                        }
                    }
                    if (TargetsUtility.getTarget() != null && ElytraAura.mc.field_1724.method_6128()) {
                        LivingEntity entity = TargetsUtility.getTarget();
                        Angle vec2f = RotationUtility.calcRotateOnElytra(ElytraAuraResolve.getFinalTargetVector(entity, false), true);
                        float yaw = Client.ROTATION.getRotate().getYaw();
                        if (this.jitterOnDefensiveSetting.get() && this.antiAimIsActive) {
                            yaw += MathUtility.random(-20.0f, 20.0f);
                        }
                        if (this.defensiveType.is(DefensiveType.Roll) && this.antiAimIsActive) {
                            yaw += this.strenghtRollSetting.get() * (float)(this.hit ? -1 : 1);
                        }
                        Angle rotVe = this.antiAimIsActive ? new Angle(this.defensiveType.is(DefensiveType.Roll) ? yaw : this.defVec.getYaw(), this.defVec.getPitch()) : vec2f;
                        Client.ROTATION.rotate(LinearRotation.INSTANCE, rotVe, 1, false, MovementCorrection.STRICT, 30);
                    }
                    ElytraAuraUtility.useFireWork(TargetsUtility.getTarget());
                    if (TargetsUtility.getTarget() != null) {
                        if (Resolver.INSTANCE.isEnabled() && BackTrackPosResolver.INSTANCE.isEnabled()) {
                            BackTrackPosResolver.INSTANCE.resolvePlayers();
                        }
                        ElytraAuraAttackHandler.updateAttack(TargetsUtility.getTarget());
                        if (Resolver.INSTANCE.isEnabled() && BackTrackPosResolver.INSTANCE.isEnabled()) {
                            BackTrackPosResolver.INSTANCE.restorePlayers();
                        }
                    }
                    this.lastPosTarget = ((ResolvedPositionEntity)TargetsUtility.getTarget()).hachclientport$getResolvedPos();
                }
            }
            if (event instanceof EventMove) {
                EventMove move = (EventMove)event;
                if (this.fakeRotationGroup.isEnabled() && !this.antiAimIsActive && ElytraAura.mc.field_1724.method_6128() && TargetsUtility.getTarget() != null) {
                    move.yaw = Client.ROTATION.getRotate().getYaw() + this.yawValue.get() * (float)(this.randomValue.get() ? (this.hit ? 1 : -1) : 1);
                    move.pitch = MathHelper.method_15363((float)(Client.ROTATION.getRotate().getPitch() + this.pitchValue.get() * (float)(this.randomValue.get() ? (this.hit ? 1 : -1) : 1)), (float)-90.0f, (float)90.0f);
                }
            }
            if (event instanceof EventInput) {
                e = (EventInput)event;
                if (this.autoStartSetting.get() && TargetsUtility.getTarget() != null) {
                    ((EventInput)e).setJump(ElytraAura.mc.field_1724.field_6012 % 3 == 0);
                }
            }
            if (event instanceof EventAttack) {
                EventAttack attack = (EventAttack)event;
                if (ElytraAura.mc.field_1724 == null || ElytraAura.mc.field_1687 == null || TargetsUtility.getTarget() == null || attack.target != TargetsUtility.getTarget()) {
                    return;
                }
                this.hit = new Random().nextBoolean();
                if (!(!this.activeAntiAim(TargetsUtility.getTarget()) || this.isLeave(TargetsUtility.getTarget()) || attack.target != TargetsUtility.getTarget() || this.isAirStack.get() && InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)this.bindAirStackSetting.getBind()) || this.antiAimIsActive)) {
                    this.antiAimIsActive = true;
                    this.antiAimTimer.reset();
                    this.defVec.setYaw((float)ElytraAuraDef.updateAntiAimRotation((LivingEntity)TargetsUtility.getTarget(), (Vec2f[])this.antiAimVectorYaw(), (Vec2f[])ElytraAuraUtility.parsePitchOffsets((String)this.offsetsYOnDefensiveSetting.getText())).field_1352);
                    this.defVec.setPitch((float)ElytraAuraDef.updateAntiAimRotation((LivingEntity)TargetsUtility.getTarget(), (Vec2f[])this.antiAimVectorYaw(), (Vec2f[])ElytraAuraUtility.parsePitchOffsets((String)this.offsetsYOnDefensiveSetting.getText())).field_1351);
                    this.lastAntiaim = true;
                }
            }
            if (event instanceof EventPostTick) {
                EventPostTick postTick = (EventPostTick)event;
                if (ElytraAura.mc.field_1724 == null || ElytraAura.mc.field_1687 == null) {
                    return;
                }
                if (TargetsUtility.getTarget() == null) {
                    this.updateTarget();
                }
            }
            if (event instanceof EventTravel) {
                e = (EventTravel)event;
                if (this.isAirStack.get() && this.bindAirStackSetting.getBind() != -1) {
                    boolean key = InputUtil.method_15987((long)mc.method_22683().method_4490(), (int)this.bindAirStackSetting.getBind());
                    if (TargetsUtility.getTarget() != null && ElytraAura.mc.field_1724.method_6128() && ElytraAura.mc.field_1724.method_19538().method_10214() > TargetsUtility.getTarget().method_19538().method_10214()) {
                        boolean isWeapon;
                        float getDist = ElytraAura.mc.field_1724.method_5739((Entity)TargetsUtility.getTarget());
                        LivingEntity target = TargetsUtility.getTarget();
                        boolean bl = isWeapon = target.method_6047().method_7909() instanceof AxeItem || target.method_6047().method_7909().equals(Items.field_22022) || target.method_6047().method_7909().equals(Items.field_8802) || target.method_6047().method_7909().equals(Items.field_8845) || target.method_6047().method_7909().equals(Items.field_8091) || target.method_6047().method_7909().equals(Items.field_8371) || target.method_6047().method_7909().equals(Items.field_8528);
                        if (this.autoAirStackSetting.get()) {
                            if (this.ifworkAitStack.get(Feature.IF_TARGET_IS_RANGE)) {
                                if (getDist < ElytraAuraAttackHandler.attackDistance(TargetsUtility.getTarget()) && !ElytraAuraResolve.isStoyak(TargetsUtility.getTarget()) && !this.isLeave(target)) {
                                    this.isd = true;
                                    e.cancel();
                                } else {
                                    this.isd = false;
                                }
                            }
                            if (this.ifworkAitStack.get(Feature.IF_TARGET_ON_STOYAK)) {
                                if (getDist > 6.0f && isWeapon && this.lastPosTarget.method_1022(((ResolvedPositionEntity)target).hachclientport$getResolvedPos()) < 0.5 && ElytraAuraResolve.isStoyak(TargetsUtility.getTarget()) && !target.method_24828() && !this.isLeave(target)) {
                                    this.isd = true;
                                    e.cancel();
                                } else {
                                    this.isd = false;
                                }
                            }
                        } else if (getDist <= ElytraAuraAttackHandler.attackDistance(target)) {
                            if (key) {
                                e.cancel();
                                this.isd = true;
                            } else {
                                this.isd = false;
                            }
                        }
                    }
                }
            }
            if (event instanceof EventAddMessage) {
                e = (EventAddMessage)event;
                if (TargetsUtility.getTarget() != null && this.sendDelay.reached(1000L) && this.isAutoMessage.get() && ((EventAddMessage)e).text.getString().toLowerCase().contains("\u0443\u0431\u0438\u043b") && !((EventAddMessage)e).text.getString().toLowerCase().contains("\u0432\u0430\u0441") && ((EventAddMessage)e).text.getString().contains(TargetsUtility.getTarget().method_5477().getString())) {
                    mc.method_1562().method_45729(this.messageField.getText().replace("{name}", TargetsUtility.getTarget().method_5477().getString()));
                    this.sendDelay.reset();
                }
            }
            if (event instanceof Event3D) {
                boolean isCoolDowncompelte;
                e = (Event3D)event;
                LivingEntity target = TargetsUtility.getTarget();
                boolean hurt = target != null && target.field_6235 < 7;
                boolean bl = isCoolDowncompelte = ElytraAura.mc.field_1724.method_7261(1.5f) > 0.6f;
                if (target != null) {
                    boolean isDone;
                    boolean bl2 = this.aimWhenTargetAttack.get() ? (this.isLeave(target) || ElytraAuraResolve.isStoyak(target)) && this.antiAimTimer.reached(this.antiAimDelayDelaySetting.getLong()) || ((ResolvedPositionEntity)target).hachclientport$getResolvedPos().method_1022(ElytraAura.mc.field_1724.method_33571()) < target.method_30950(mc.method_61966().method_60637(true)).method_1022(ElytraAura.mc.field_1724.method_33571()) && target.method_6128() : (isDone = this.smartDefensiveTimeSetting.get() ? hurt & isCoolDowncompelte : this.antiAimTimer.reached(this.antiAimDelayDelaySetting.getLong()));
                    if (this.antiAimIsActive && isDone) {
                        this.antiAimIsActive = false;
                        this.antiAimTimer.reset();
                    }
                } else {
                    this.antiAimIsActive = false;
                }
                if (target != null) {
                    ((Event3D)e).stack.method_22903();
                    Client.RENDERER.toCamera(((Event3D)e).stack);
                    VertexConsumer consumer = ((Event3D)e).buffer.getBuffer((RenderLayer)RenderLayer.field_21695);
                    Vec3d pos = ElytraAuraResolve.getFinalTargetVector(target, false);
                    VertexRendering.method_62295((MatrixStack)((Event3D)e).stack, (VertexConsumer)consumer, (Box)new Box(pos.method_61888((double)0.1f), pos.method_61889((double)0.1f)), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    ((Event3D)e).stack.method_22909();
                }
            }
            if (event instanceof EventBoost) {
                e = (EventBoost)event;
                if (TargetsUtility.getTarget() == null) {
                    return;
                }
                if (this.rangeByter.get()) {
                    LivingEntity entity = TargetsUtility.getTarget();
                    double wrap = Math.atan2(ElytraAura.mc.field_1724.method_23321() - entity.method_23321(), ElytraAura.mc.field_1724.method_23317() - entity.method_23317());
                    double x = entity.method_23317() + 5.0 * Math.cos(wrap += 4.0 / ElytraAura.mc.field_1724.method_19538().method_1022(entity.method_19538()));
                    double z = entity.method_23321() + 5.0 * Math.sin(wrap);
                    double diffX = x - ElytraAura.mc.field_1724.method_23317();
                    double diffZ = z - ElytraAura.mc.field_1724.method_23321();
                    double d1 = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0;
                    ((EventBoost)e).yaw = (float)d1;
                }
            }
            if (!this.rangeByter.get()) {
                ElytraAuraDefensive.handleEvent(event);
            }
        };
    }

    public Vec2f[] antiAimVectorYaw() {
        if (TargetsUtility.getTarget() == null) {
            return new Vec2f[]{new Vec2f(0.0f, 0.0f)};
        }
        return this.getYawVectorByMode(this.defensiveType.get());
    }

    private Vec2f[] getYawVectorByMode(DefensiveType mode) {
        Vec2f[] vec2fArray;
        switch (mode.ordinal()) {
            default: {
                throw new MatchException(null, null);
            }
            case 0: {
                Vec2f[] vec2fArray2 = new Vec2f[8];
                vec2fArray2[0] = new Vec2f(180.0f, 0.0f);
                vec2fArray2[1] = new Vec2f(135.0f, 0.0f);
                vec2fArray2[2] = new Vec2f(90.0f, 0.0f);
                vec2fArray2[3] = new Vec2f(45.0f, 0.0f);
                vec2fArray2[4] = new Vec2f(-45.0f, 0.0f);
                vec2fArray2[5] = new Vec2f(-90.0f, 0.0f);
                vec2fArray2[6] = new Vec2f(-135.0f, 0.0f);
                vec2fArray = vec2fArray2;
                vec2fArray2[7] = new Vec2f(0.0f, 0.0f);
                break;
            }
            case 1: 
            case 3: {
                Vec2f[] vec2fArray3 = new Vec2f[1];
                vec2fArray = vec2fArray3;
                vec2fArray3[0] = new Vec2f(360.0f, 0.0f);
                break;
            }
            case 2: {
                Vec2f[] vec2fArray4 = new Vec2f[1];
                vec2fArray = vec2fArray4;
                vec2fArray4[0] = new Vec2f(180.0f, 0.0f);
            }
        }
        return vec2fArray;
    }

    public boolean activeAntiAim(LivingEntity target) {
        if (target == null) {
            return false;
        }
        if (this.ifWorkAntiAimSetting.is(DefensiveCondition.TargetNotGliding)) {
            return !target.method_6128() || target.method_24828() || ElytraAuraResolve.isStoyak(target) || PlayerUtility.isOnSolidGround(target, 1);
        }
        return true;
    }

    public boolean isLeave(LivingEntity entity) {
        return this.targetIsLeave(entity);
    }

    public boolean targetIsLeave(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        return this.leaveTime.reached(1500L) && entity.method_6128();
    }

    public void updateTarget() {
        if (TargetsUtility.getTarget() != null && TargetsUtility.getTarget().field_6213 > 0 && this.sendDelay.reached(1000L) && this.isAutoMessage.get()) {
            this.sendDelay.reset();
            mc.method_1562().method_45729(this.messageField.getText().replace("{name}", TargetsUtility.getTarget().method_5477().getString()));
        }
        TargetsUtility.find(this.attackRangeSetting.get() + this.preAttackRangeSetting.get(), TargetsUtility.Sort.Distance);
    }

    public void resetAll() {
        this.missAmount = 0;
        this.leaveTime.reset();
        this.hitTargetTime.reset();
        this.antiAimTimer.reset();
        this.antiAimIsActive = false;
        Client.TIMER = 1.0f;
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        TargetsUtility.reset();
        this.resetAll();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        TargetsUtility.reset();
        this.resetAll();
        ElytraAuraDefensive.onDisable();
        Client.TIMER = 1.0f;
    }

    public static enum TypeAim implements EnumChoice
    {
        Resolve("Resolve pos."),
        FireWork("FireWork Pos."),
        Middle("Middle Pos."),
        Default("Default Pos.");

        final String renderName;

        private TypeAim(String renderName) {
            this.renderName = renderName;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }

    public static enum Feature implements EnumChoice
    {
        IF_TARGET_ON_STOYAK("Target on Stoyak", true),
        IF_TARGET_IS_RANGE("Target is near", false);

        final String renderName;
        final boolean defaultEnabled;

        private Feature(String renderName, boolean defaultEnabled) {
            this.renderName = renderName;
            this.defaultEnabled = defaultEnabled;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }

        @Override
        public boolean isDefaultEnabled() {
            return this.defaultEnabled;
        }
    }

    public static enum DefensiveCondition implements EnumChoice
    {
        TargetNotGliding("If the target doesn't fly"),
        Always("Always");

        final String renderName;

        private DefensiveCondition(String renderName) {
            this.renderName = renderName;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }

    public static enum DefensiveType implements EnumChoice
    {
        Random("Random vector"),
        Horizontal("Horizontal"),
        Unilateral("Unilateral"),
        Roll("Roll");

        final String renderName;

        private DefensiveType(String renderName) {
            this.renderName = renderName;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }

    public static enum FakeLagCondition implements EnumChoice
    {
        Always("Always"),
        TargetIsntGliding("Target isn't elytra flying");

        final String renderName;

        private FakeLagCondition(String renderName) {
            this.renderName = renderName;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }
}

