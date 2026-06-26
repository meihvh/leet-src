/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventAttack;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.events.list.EventInput;
import im.leet.api.events.list.EventPostMotion;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.Resolver;
import im.leet.base.modules.impl.combat.ShieldUtility;
import im.leet.base.modules.impl.combat.aura.attack.AttackHandler;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.RotationSettings;
import im.leet.base.rotations.RotationTiming;
import im.leet.base.rotations.point.PointTracker;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.mixin.accessor.ILocalPlayer;
import im.leet.utils.attack.AttackUtility;
import im.leet.utils.client.ChatUtility;
import im.leet.utils.client.DebugUtility;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.RayTraceUtility;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.network.NetworkUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AuraModule
extends Module {
    public static final AuraModule INSTANCE = new AuraModule();
    private final Group mainSettings = this.group("Main Settings");
    public SliderSetting attackRangeSetting = (SliderSetting)this.mainSettings.sliderSetting("Attack Range", 3.0f, 0.0f, 6.0f).increment(0.1f).desc("How far target should be attacked");
    public SliderSetting preAttackRangeSetting = (SliderSetting)this.mainSettings.sliderSetting("Target Range", 5.0f, 0.0f, 7.0f).increment(0.1f).desc("How far target should be detected");
    public EnumSetting<TargetsUtility.Sort> sortTargetSetting = (EnumSetting)this.mainSettings.enumSetting("Sort Target", TargetsUtility.Sort.Adaptive).desc("How targets will be sorted");
    private final RotationSettings rotations = this.add(new RotationSettings("Rotation Settings", 35));
    private final EnumSetting<RotationTiming> rotationTiming = this.rotations.enumSetting("Timing", RotationTiming.Normal);
    private final SliderSetting snapProgress = (SliderSetting)this.rotations.sliderSetting("Snap progress", 0.8f, 0.0f, 1.0f).increment(0.01f).visible(() -> this.rotationTiming.is(RotationTiming.Snap));
    private final CheckBox snapFollow = (CheckBox)this.rotations.checkbox("Snap follow target", true).visible(() -> this.rotationTiming.is(RotationTiming.Snap));
    private final PointTracker point = this.add(new PointTracker());
    private final Group attackSettings = this.group("Attack Settings");
    public CheckBox checkCrit = (CheckBox)this.attackSettings.checkbox("Check critical", true).desc("Only critical attacks");
    public CheckBox smartCrits = (CheckBox)((CheckBox)this.attackSettings.checkbox("Smart Crit", true).visible(() -> this.checkCrit.get())).desc("Allows attack on ground when possible");
    public MultiEnumSetting<Feature> auraSetting = (MultiEnumSetting)this.attackSettings.multiEnumSetting("Setting", Feature.class).desc("Additional module features");
    public EnumSetting<ResetSprint> resetSprint = (EnumSetting)this.attackSettings.enumSetting("Type Reset Sprint", ResetSprint.Legit).desc("How sprint will be reseted");
    private final Group groupAdvancedSettings = this.group("Advanced Settings").expanded(false);
    public CheckBox sensitivityBypass = (CheckBox)this.groupAdvancedSettings.checkbox("Sensitivity Bypass", false).desc("Super low sensivity value in rotations");
    public EnumSetting<CritType> typeCritSetting = (EnumSetting)this.groupAdvancedSettings.enumSetting("Type Crit", CritType.Default).desc("Method of crit (Packet hit earlier)");
    public CheckBox logMiss = (CheckBox)((CheckBox)this.groupAdvancedSettings.checkbox("Log misses", Client.IS_DEBUG).visible(() -> Client.IS_DEBUG)).desc("Developer only setting");
    public CheckBox randomFallDistanceSetting = (CheckBox)this.groupAdvancedSettings.checkbox("Random Fall Distance", false).desc("Randomizing crit timing, heuristic anticheat sucks");
    public final AttackUtility attack = this.groupAdvancedSettings.add(new AttackUtility());
    public final Group grimReduce = this.groupAdvancedSettings.group("Grim reduce").toggleable(false);
    public final SliderSetting grimReduceFactor = this.grimReduce.sliderSetting("Factor", 0.6f, 0.0f, 1.0f).increment(0.01f);
    private boolean shouldRecoverSprint = false;
    private Angle angle;
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            if (AuraModule.nullCheck()) {
                return;
            }
            this.updateTarget();
            if (TargetsUtility.getTarget() == null) {
                return;
            }
            if (this.auraSetting.get(Feature.DisableOnDeath) && !AuraModule.mc.field_1724.method_5805()) {
                this.toggle();
                return;
            }
            LivingEntity target = TargetsUtility.getTarget();
            Vec3d p = this.point.getPoint((Entity)target);
            DebugUtility.trace("Aura point", p.toString());
            this.angle = RotationUtility.calcRotate(p, true);
            DebugUtility.trace("Aura angle", this.angle.toString());
            DebugUtility.trace("Aura attack progress", Float.valueOf(this.attack.getProgress()));
            if (this.rotationTiming.is(RotationTiming.Tick)) {
                if (!Client.ROTATION.getRotate().equals(this.angle)) {
                    NetworkUtility.sendAngle(this.angle);
                }
            } else if (!this.rotationTiming.is(RotationTiming.Snap) || this.attack.getProgress() >= this.snapProgress.get()) {
                this.rotations.rotate(this.angle);
            }
            if (this.shieldBreaker() || this.isCheckWeapon()) {
                return;
            }
            if (this.typeCritSetting.is(CritType.Default) && !this.updateAttack(Client.ROTATION.getRotate())) {
                ShieldUtility.INSTANCE.startBlocking();
            }
            if (this.rotationTiming.is(RotationTiming.Tick) && !Client.ROTATION.getRotate().equals(this.angle)) {
                NetworkUtility.sendAngle(Client.ROTATION.getRotate());
            }
        }
        if (event instanceof EventPostMotion && TargetsUtility.getTarget() != null && this.typeCritSetting.is(CritType.Packet)) {
            this.updateAttack(Client.ROTATION.getRotate());
        }
        if (event instanceof EventInput) {
            boolean resetSprint;
            EventInput eventInput = (EventInput)event;
            if (this.angle != null && this.rotationTiming.is(RotationTiming.Snap) && this.snapFollow.get() && this.attack.getProgress() < this.snapProgress.get()) {
                MoveUtility.silentCorrectRaw(eventInput, this.angle.getYaw());
            }
            if (TargetsUtility.getTarget() != null && (resetSprint = AttackHandler.shouldResetSprinting()) && this.resetSprint.is(ResetSprint.Legit) && !AuraModule.mc.field_1724.method_5799() && eventInput.getForward() > 0.0f) {
                eventInput.setForward(0.0f);
                eventInput.setSprint(false);
                this.shouldRecoverSprint = true;
                return;
            }
            if (this.shouldRecoverSprint) {
                eventInput.setForward(1.0f);
                eventInput.setSprint(true);
                this.shouldRecoverSprint = false;
            }
        }
    };

    private AuraModule() {
        super("Aura", Category.COMBAT, "Automatically hits the target", Tag.RAGE);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        TargetsUtility.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        TargetsUtility.reset();
    }

    public void updateTarget() {
        TargetsUtility.find(this.attackRangeSetting.get() + this.preAttackRangeSetting.get(), this.sortTargetSetting.get());
    }

    public boolean isCheckWeapon() {
        return this.auraSetting.get(Feature.OnlyWeapon) && !(AuraModule.mc.field_1724.method_6047().method_7909() instanceof AxeItem) && !AuraModule.mc.field_1724.method_6047().method_7909().equals(Items.field_22022) && !AuraModule.mc.field_1724.method_6047().method_7909().equals(Items.field_8802) && !AuraModule.mc.field_1724.method_6047().method_7909().equals(Items.field_8371) && !AuraModule.mc.field_1724.method_6047().method_7909().equals(Items.field_8528) && !AuraModule.mc.field_1724.method_6047().method_7909().equals(Items.field_8845) && !AuraModule.mc.field_1724.method_6047().method_7909().equals(Items.field_8091);
    }

    public boolean updateAttack(Angle angle) {
        if (!AttackHandler.shouldAttack() || TargetsUtility.getTarget() == null || RotationUtility.getStrictDistance(TargetsUtility.getTarget()) > (double)this.attackRange()) {
            return false;
        }
        if (!this.raycast(angle)) {
            if (this.logMiss.get()) {
                ChatUtility.send("[Aura] Miss!");
            }
            return false;
        }
        if (this.resetSprint.is(ResetSprint.Legit) && ((ILocalPlayer)AuraModule.mc.field_1724).serverSprintState()) {
            return false;
        }
        int clicks = this.attack.getClicks();
        if (clicks == 0) {
            return false;
        }
        ShieldUtility.INSTANCE.stopBlocking(true);
        boolean sprint = NetworkUtility.serverSprinting();
        if (this.resetSprint.is(ResetSprint.Packet) && sprint) {
            mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)AuraModule.mc.field_1724, ClientCommandC2SPacket.Mode.field_12985));
            AuraModule.mc.field_1724.method_5728(false);
        }
        Client.EVENTS.post(EventAttack.build((Entity)TargetsUtility.getTarget()));
        for (int i = 0; i < clicks; ++i) {
            AttackHandler.attackEntity(TargetsUtility.getTarget());
            if (!this.grimReduce.isEnabled()) continue;
            Vec3d velocity = AuraModule.mc.field_1724.method_18798();
            velocity.field_1352 *= (double)this.grimReduceFactor.get();
            velocity.field_1350 *= (double)this.grimReduceFactor.get();
        }
        if (this.resetSprint.is(ResetSprint.Packet) && sprint) {
            mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)AuraModule.mc.field_1724, ClientCommandC2SPacket.Mode.field_12981));
            AuraModule.mc.field_1724.method_5728(true);
        }
        return true;
    }

    public float attackRange() {
        if (Resolver.INSTANCE.isFakeLagging()) {
            return 12.0f;
        }
        return this.attackRangeSetting.get();
    }

    public boolean raycast(Angle angle) {
        if (TargetsUtility.getTarget() == null) {
            return false;
        }
        return RayTraceUtility.rayTrace(angle.toVector(), (double)this.attackRange(), TargetsUtility.getTarget().method_5829());
    }

    private boolean shieldBreaker() {
        if (TargetsUtility.getTarget().method_6079().method_7909().equals(Items.field_8255) && TargetsUtility.getTarget().method_6039() && Math.abs(MathHelper.method_15393((float)(AuraModule.mc.field_1724.method_36454() - TargetsUtility.getTarget().method_36454() - 180.0f))) < 90.0f) {
            return ShieldUtility.INSTANCE.breakShield();
        }
        return false;
    }

    public static enum Feature implements EnumChoice
    {
        DisableOnDeath("Turn off on death", true),
        OnlyWeapon("Only Weapon", false),
        KeepSprint("Keep Sprint", false);

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

    public static enum ResetSprint {
        Legit,
        Packet,
        None;

    }

    public static enum CritType {
        Default,
        Packet;

    }
}

