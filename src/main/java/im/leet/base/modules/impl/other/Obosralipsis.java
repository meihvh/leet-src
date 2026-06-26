/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 */
package im.leet.base.modules.impl.other;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.render.system.ClientPipelines;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public class Obosralipsis
extends Module {
    public static final Obosralipsis INSTANCE = new Obosralipsis();
    private final List<Perdun> updatedPerunsList = new CopyOnWriteArrayList<Perdun>();
    private final List<SplashAnimatedVec3dColored> shitAnimationsList = new CopyOnWriteArrayList<SplashAnimatedVec3dColored>();
    private final List<PhysicalParticleOfShit> splashParticlesList = new CopyOnWriteArrayList<PhysicalParticleOfShit>();
    private Vec3d cameraPosUpdated = Vec3d.field_1353;
    EventBus<Event> events = e -> {
        if (e instanceof EventGameTick) {
            this.updateSplashAnimationsList(false);
            this.updateSpreadsList(false);
            List<Perdun> perduns = this.controlPerdunList(false, 1600L, 7000L);
            if (!perduns.isEmpty()) {
                perduns.forEach(Perdun::processing);
            }
        }
        if (e instanceof Event3D) {
            Event3D event3D = (Event3D)e;
            this.cameraPosUpdated = this.cameraPos();
            this.drawSplashAnimations(event3D);
            this.drawSpreads(event3D);
        }
    };

    private Obosralipsis() {
        super("Obosralipsis", Category.RENDER, "\u0418\u0433\u0440\u043e\u043a\u0438 \u043d\u0430\u0447\u0438\u043d\u0430\u044e\u0442 \u0441\u0440\u0430\u0442\u044c \u043f\u043e\u043d\u043e\u0441\u043e\u043c", new Tag[0]);
    }

    private int getFartTicksDuration() {
        return 4;
    }

    private int getMaxShitFlyingDistance(PlayerEntity player) {
        return 10;
    }

    private boolean addSpreadsChancedTemp() {
        return Math.random() > 0.3;
    }

    private int getShitCount(PlayerEntity player) {
        return 550 / this.getFartTicksDuration();
    }

    private int getRandomShitColor() {
        float lrpPC = (float)Math.random();
        Color c1 = new Color(35, 15, 0);
        Color c2 = new Color(116, 50, 0);
        return ColorUtility.linear(c1, c2, lrpPC * lrpPC).getRGB();
    }

    private List<Perdun> controlPerdunList(boolean clear, long minFartDelay, long maxFartDelay) {
        if (clear) {
            this.updatedPerunsList.clear();
            return this.updatedPerunsList;
        }
        this.updatedPerunsList.removeIf(Perdun::isToRemove);
        if (Obosralipsis.mc.field_1687 == null) {
            return this.updatedPerunsList;
        }
        int fartDuration = this.getFartTicksDuration();
        for (PlayerEntity player : Obosralipsis.mc.field_1687.method_18456()) {
            Perdun findAnyPerdun;
            if (player == null || !player.method_5805() || Obosralipsis.mc.field_1724.method_5739((Entity)player) > 10.0f) {
                Perdun findAnyPerdun2 = this.updatedPerunsList.stream().filter(perdun -> perdun.getPlayer() == player).findAny().orElse(null);
                if (findAnyPerdun2 == null) continue;
                this.updatedPerunsList.remove(findAnyPerdun2);
                continue;
            }
            long minDelay = minFartDelay;
            long maxDelay = maxFartDelay;
            if (player == Obosralipsis.mc.field_1724 && Obosralipsis.mc.field_1724.method_5715()) {
                minDelay = 200L;
                maxDelay = 300L;
            }
            if ((findAnyPerdun = (Perdun)this.updatedPerunsList.stream().filter(perdun -> perdun.getPlayer() == player).findAny().orElse(null)) == null) {
                this.updatedPerunsList.add(new Perdun(player, minDelay, maxDelay, fartDuration));
                continue;
            }
            findAnyPerdun.setData(minDelay, maxDelay, fartDuration);
        }
        return this.updatedPerunsList;
    }

    private Vec3d getAssPositionVector(PlayerEntity player) {
        float yaw = player.method_43078();
        double yawRad = Math.toRadians(yaw);
        return player.method_19538().method_1031(Math.sin(yawRad) * (double)player.method_17681() / 5.0, (double)player.method_18381(player.method_18376()) / 2.5, -Math.cos(yawRad) * (double)player.method_17681() / 5.0);
    }

    private float[] getRandFartRadiansYaw$Pitch(PlayerEntity player, float randYaw, float randPitch, float downValue) {
        float yawRandom = (float)Math.random() * 360.0f;
        float randRadian = (float)Math.toRadians(yawRandom);
        float randomDistancePC = yawRandom * 100.0f % 1.0f;
        randomDistancePC *= randomDistancePC;
        float yawAdditionPC01 = (float)(-Math.sin(randRadian)) * randomDistancePC;
        float pitchAdditionPC01 = (float)Math.cos(randRadian) * randomDistancePC;
        float finalYaw = player.method_43078() + 180.0f + randYaw * yawAdditionPC01;
        float finalPitch = -MathUtility.clamp(downValue + randPitch * pitchAdditionPC01, -90.0f, 90.0f);
        return new float[]{(float)Math.toRadians(finalYaw), (float)Math.toRadians(finalPitch)};
    }

    private Vec3d[] startEndBlowPosesRand(PlayerEntity player, float randYaw, float randPitch, float downValue, float maxRange) {
        Vec3d start = this.getAssPositionVector(player);
        float[] radians = this.getRandFartRadiansYaw$Pitch(player, randYaw, randPitch, downValue);
        Vec3d end = start.method_1031(-Math.sin(radians[0]) * (double)maxRange, Math.sin(radians[1]) * (double)maxRange, Math.cos(radians[0]) * (double)maxRange);
        if (Obosralipsis.mc.field_1687 == null) {
            return null;
        }
        RaycastContext context = new RaycastContext(start, end, RaycastContext.ShapeType.field_17559, RaycastContext.FluidHandling.field_1348, (Entity)player);
        BlockHitResult ray = Obosralipsis.mc.field_1687.method_17742(context);
        if (ray.method_17783() == HitResult.Type.field_1333) {
            return null;
        }
        end = ray.method_17784().method_1031(0.0, 0.01, 0.0);
        return new Vec3d[]{start, end};
    }

    private void addSplashAnim(PlayerEntity player, int countAdd) {
        float maxDistance = this.getMaxShitFlyingDistance(player);
        for (int iteration = 0; iteration < countAdd; ++iteration) {
            float dstDEPC;
            int shitColor = this.getRandomShitColor();
            int timeAnimation = 100 + (int)(250.0 * Math.random());
            int timeAlive = timeAnimation + 2700 + (int)(700.0 * Math.random());
            Vec3d[] animationPoses = this.startEndBlowPosesRand(player, 28.0f, 26.0f, 70.0f, maxDistance);
            if (animationPoses == null || (animationPoses = this.startEndBlowPosesRand(player, 30.0f + 90.0f * (dstDEPC = 1.0f - Math.min((float)animationPoses[0].method_1022(animationPoses[1]), 1.0f)), 25.0f + 70.0f * dstDEPC, 45.0f, maxDistance)) == null) continue;
            this.shitAnimationsList.add(new SplashAnimatedVec3dColored(animationPoses[0], animationPoses[1], timeAnimation, timeAlive, shitColor));
        }
    }

    private void updateSplashAnimationsList(boolean clear) {
        if (clear) {
            this.shitAnimationsList.clear();
            return;
        }
        if (this.shitAnimationsList.isEmpty()) {
            return;
        }
        this.shitAnimationsList.removeIf(SplashAnimatedVec3dColored::onIsToRemove);
    }

    private void drawSplashAnimations(Event3D event) {
        if (this.shitAnimationsList.isEmpty()) {
            return;
        }
        VertexConsumer consumer = event.buffer.getBuffer((RenderLayer)ClientPipelines.POINTS);
        Vec3d camPos = Obosralipsis.mc.field_1773.method_19418().method_19326();
        Quaternionf camRot = Obosralipsis.mc.field_1773.method_19418().method_23767();
        float size = 0.035f;
        for (SplashAnimatedVec3dColored splashAnim : this.shitAnimationsList) {
            float timePC = splashAnim.getTimePC();
            int renderColor = splashAnim.getAlphedPointColor(timePC);
            Vec3d renderPos = splashAnim.getAnimatedVec3d(timePC);
            event.stack.method_22903();
            event.stack.method_22904(renderPos.field_1352 - camPos.field_1352, renderPos.field_1351 - camPos.field_1351, renderPos.field_1350 - camPos.field_1350);
            event.stack.method_22907((Quaternionfc)camRot);
            consumer.method_56824(event.stack.method_23760(), -size, -size, 0.0f).method_39415(renderColor);
            consumer.method_56824(event.stack.method_23760(), -size, size, 0.0f).method_39415(renderColor);
            consumer.method_56824(event.stack.method_23760(), size, size, 0.0f).method_39415(renderColor);
            consumer.method_56824(event.stack.method_23760(), size, -size, 0.0f).method_39415(renderColor);
            event.stack.method_22909();
        }
    }

    private void addSpread(SplashAnimatedVec3dColored splash) {
        this.splashParticlesList.add(new PhysicalParticleOfShit(this, splash));
    }

    private void updateSpreadsList(boolean clear) {
        if (clear) {
            this.splashParticlesList.clear();
            return;
        }
        if (this.splashParticlesList.isEmpty()) {
            return;
        }
        this.splashParticlesList.removeIf(PhysicalParticleOfShit::isToRemove);
        if (this.splashParticlesList.isEmpty()) {
            return;
        }
        this.splashParticlesList.forEach(part -> part.updatePhysics(0.02f));
    }

    private void drawSpreads(Event3D event) {
        if (this.splashParticlesList.isEmpty()) {
            return;
        }
        VertexConsumer consumer = event.buffer.getBuffer((RenderLayer)ClientPipelines.POINTS);
        Vec3d camPos = Obosralipsis.mc.field_1773.method_19418().method_19326();
        Quaternionf camRot = Obosralipsis.mc.field_1773.method_19418().method_23767();
        float size = 0.045f;
        for (PhysicalParticleOfShit spreadPart : this.splashParticlesList) {
            int renderColor = spreadPart.getRenderColor(0.8f);
            Vec3d renderPos = spreadPart.getRenderVec3d();
            event.stack.method_22903();
            event.stack.method_22904(renderPos.field_1352 - camPos.field_1352, renderPos.field_1351 - camPos.field_1351, renderPos.field_1350 - camPos.field_1350);
            event.stack.method_22907((Quaternionfc)camRot);
            consumer.method_56824(event.stack.method_23760(), -size, -size, 0.0f).method_39415(renderColor);
            consumer.method_56824(event.stack.method_23760(), -size, size, 0.0f).method_39415(renderColor);
            consumer.method_56824(event.stack.method_23760(), size, size, 0.0f).method_39415(renderColor);
            consumer.method_56824(event.stack.method_23760(), size, -size, 0.0f).method_39415(renderColor);
            event.stack.method_22909();
        }
    }

    private Vec3d cameraPos() {
        if (Obosralipsis.mc.field_1687 != null && Obosralipsis.mc.field_1724 != null) {
            return Obosralipsis.mc.field_1773.method_19418().method_19326();
        }
        return Vec3d.field_1353;
    }

    @Override
    protected void onDisable() {
        this.updateSplashAnimationsList(true);
        this.updateSpreadsList(true);
        this.controlPerdunList(true, 0L, 0L);
    }

    private class Perdun {
        private long minFartDelay;
        private long maxFartDelay;
        private long fartDelay;
        private final TimeUtility fartTimer = new TimeUtility();
        private final PlayerEntity player;
        private int fartTicksBackward = Integer.MAX_VALUE;
        private int fartTicksBackwardToSet;

        public Perdun(PlayerEntity player, long minFartDelay, long maxFartDelay, int fartTicksBackwardToSet) {
            this.player = player;
            this.fartTicksBackwardToSet = fartTicksBackwardToSet;
            this.setData(minFartDelay, maxFartDelay, fartTicksBackwardToSet);
            this.setRandFartDelay();
            this.fartTimer.setTime((long)MathUtility.linear(0.0f, this.fartDelay, (float)Math.random()));
        }

        public void setData(long minFartDelay, long maxFartDelay, int fartTicksBackwardToSet) {
            this.minFartDelay = minFartDelay;
            this.maxFartDelay = maxFartDelay;
            this.fartTicksBackwardToSet = fartTicksBackwardToSet;
        }

        public void setRandFartDelay() {
            this.fartDelay = (long)MathUtility.linear(this.minFartDelay, this.maxFartDelay, (float)Math.random());
        }

        public PlayerEntity getPlayer() {
            return this.player;
        }

        public void processing() {
            if (this.fartTimer.reached(this.fartDelay)) {
                this.setRandFartDelay();
                this.fartTicksBackward = 0;
                this.fartTimer.reset();
            }
            if (this.fartTicksBackward < this.fartTicksBackwardToSet && this.player != null) {
                Obosralipsis.this.addSplashAnim(this.player, Obosralipsis.this.getShitCount(this.player));
                ++this.fartTicksBackward;
            }
        }

        public boolean isToRemove() {
            return this.player == null || mc.field_1687 == null;
        }
    }

    private class SplashAnimatedVec3dColored {
        private final int maxTimeAnim;
        private final int maxTimeAlive;
        private final int baseColor;
        private final TimeUtility timerHelper = new TimeUtility();
        private final Vec3d start;
        private final Vec3d end;
        private boolean spawnSpreadWaiting = Obosralipsis.this.addSpreadsChancedTemp();

        public SplashAnimatedVec3dColored(Vec3d start, Vec3d end, int maxTimeAnim, int maxTimeAlive, int color) {
            this.start = start;
            this.end = end;
            this.maxTimeAnim = maxTimeAnim;
            this.maxTimeAlive = maxTimeAlive;
            this.baseColor = color;
            this.timerHelper.reset();
        }

        public float getTimePC() {
            return Math.min((float)this.timerHelper.getTime() / (float)this.maxTimeAnim, 1.0f);
        }

        public int getAlphedPointColor(float timePC) {
            float aPCAnim = this.valWave01(timePC);
            float alphaDecay = 1.0f - Math.min((float)this.timerHelper.getTime() / (float)this.maxTimeAlive, 1.0f);
            float wave = (float)Math.pow(aPCAnim, 7.0);
            aPCAnim = MathUtility.linear(aPCAnim, alphaDecay, 1.0f - wave);
            int alpha = this.baseColor >> 24 & 0xFF;
            return ColorUtility.injectAlpha(new Color(this.baseColor), (int)((float)alpha * aPCAnim)).getRGB();
        }

        public Vec3d getAnimatedVec3d(float timePC) {
            return new Vec3d((double)MathUtility.linear((float)this.start.field_1352, (float)this.end.field_1352, timePC), (double)MathUtility.linear((float)this.start.field_1351, (float)this.end.field_1351, timePC), (double)MathUtility.linear((float)this.start.field_1350, (float)this.end.field_1350, timePC));
        }

        public boolean onIsToRemove() {
            if (this.spawnSpreadWaiting && this.timerHelper.reached(this.maxTimeAnim)) {
                Obosralipsis.this.addSpread(this);
                this.spawnSpreadWaiting = false;
            }
            return this.timerHelper.reached(this.maxTimeAlive) || this.start == null || this.end == null;
        }

        private float valWave01(float value) {
            return value > 0.5f ? 1.0f - value : value;
        }
    }

    private class PhysicalParticleOfShit {
        private Vec3d pos;
        private Vec3d lastPos;
        private Vec3d motion;
        private final int maxTimeAlive;
        private final int color;
        private final TimeUtility timeAlive = new TimeUtility();

        public PhysicalParticleOfShit(Obosralipsis obosralipsis, SplashAnimatedVec3dColored splash) {
            this.maxTimeAlive = splash.maxTimeAlive;
            float speedMul = 185.0f;
            this.motion = new Vec3d((splash.end.field_1352 - splash.start.field_1352) / (double)this.maxTimeAlive * (double)speedMul, (splash.end.field_1351 - splash.start.field_1351) / (double)this.maxTimeAlive * (double)speedMul, (splash.end.field_1350 - splash.start.field_1350) / (double)this.maxTimeAlive * (double)speedMul);
            this.pos = new Vec3d(splash.end.field_1352 - this.motion.field_1352, splash.end.field_1351 - this.motion.field_1351, splash.end.field_1350 - this.motion.field_1350);
            this.lastPos = new Vec3d(this.pos.field_1352, this.pos.field_1351, this.pos.field_1350);
            this.color = splash.baseColor;
            this.timeAlive.reset();
        }

        public void updatePhysics(float emulateCollisionBox) {
            this.lastPos = new Vec3d(this.pos.field_1352, this.pos.field_1351, this.pos.field_1350);
            if (mc.field_1687 != null) {
                Box boxZ;
                Box boxY;
                float predicate = 2.0f;
                float halfBox = emulateCollisionBox / 2.0f;
                Box boxX = new Box(this.pos.field_1352 + this.motion.field_1352 * (double)predicate - (double)halfBox, this.pos.field_1351 - (double)halfBox, this.pos.field_1350 - (double)halfBox, this.pos.field_1352 + this.motion.field_1352 * (double)predicate + (double)halfBox, this.pos.field_1351 + (double)halfBox, this.pos.field_1350 + (double)halfBox);
                if (!(!mc.field_1687.method_20812(null, boxX).iterator().hasNext())) {
                    this.motion = new Vec3d(0.0, this.motion.field_1351, this.motion.field_1350 * 0.95);
                }
                this.motion = new Vec3d(this.motion.field_1352 * 0.93, this.motion.field_1351, this.motion.field_1350);
                if (this.motion.field_1351 > -0.05) {
                    this.motion = this.motion.method_1031(0.0, -0.005, 0.0);
                }
                if (!(!mc.field_1687.method_20812(null, boxY = new Box(this.pos.field_1352 - (double)halfBox, this.pos.field_1351 + this.motion.field_1351 * (double)predicate - (double)halfBox, this.pos.field_1350 - (double)halfBox, this.pos.field_1352 + (double)halfBox, this.pos.field_1351 + this.motion.field_1351 * (double)predicate + (double)halfBox, this.pos.field_1350 + (double)halfBox)).iterator().hasNext())) {
                    this.motion = new Vec3d(this.motion.field_1352, -this.motion.field_1351 * 0.1, this.motion.field_1350);
                }
                if (!(!mc.field_1687.method_20812(null, boxZ = new Box(this.pos.field_1352 - (double)halfBox, this.pos.field_1351 - (double)halfBox, this.pos.field_1350 + this.motion.field_1350 * (double)predicate - (double)halfBox, this.pos.field_1352 + (double)halfBox, this.pos.field_1351 + (double)halfBox, this.pos.field_1350 + this.motion.field_1350 * (double)predicate + (double)halfBox)).iterator().hasNext())) {
                    this.motion = new Vec3d(this.motion.field_1352 * 0.95, this.motion.field_1351, 0.0);
                }
                this.motion = new Vec3d(this.motion.field_1352, this.motion.field_1351, this.motion.field_1350 * 0.93);
            }
            this.pos = this.pos.method_1019(this.motion);
        }

        public Vec3d getRenderVec3d() {
            return this.pos;
        }

        private float getTimePC() {
            return Math.min((float)this.timeAlive.getTime() / (float)this.maxTimeAlive, 1.0f);
        }

        public int getRenderColor(float aPC) {
            float timePC = this.getTimePC();
            aPC *= 1.0f - timePC;
            Color darkened = ColorUtility.brightness(new Color(this.color), 153);
            int alpha = this.color >> 24 & 0xFF;
            return ColorUtility.injectAlpha(darkened, (int)((float)alpha * (aPC *= Math.min(timePC / 0.075f, 1.0f)))).getRGB();
        }

        public boolean isToRemove() {
            return this.getTimePC() >= 1.0f || mc.field_1687 == null;
        }
    }
}

