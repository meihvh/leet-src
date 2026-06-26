/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.projectile.FireworkRocketEntity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  org.joml.Vector3f
 */
package im.leet.base.modules.impl.combat.elytraaura.math;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.scripts.api.integrate.ScriptHook;
import im.leet.base.modules.impl.combat.ElytraAura;
import im.leet.base.modules.impl.combat.Resolver;
import im.leet.base.modules.impl.movement.SuperFirework;
import im.leet.utils.client.mixin.IShooterEntity;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.math.RotationUtility;
import im.leet.utils.player.MoveUtility;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public final class ElytraAuraResolve
implements MinecraftHolder {
    static ElytraAura elytraAura = ElytraAura.INSTANCE;

    public static Vec3d getFinalTargetVector(LivingEntity entity, boolean fromYou) {
        double d;
        Object o = Client.SCRIPTS.getScriptHook().solute(ScriptHook.HookValue.ELYTRA_RESOLVER, entity);
        if (o != null) {
            return new Vec3d((Vector3f)o);
        }
        Vec3d point = ElytraAuraResolve.getPointOnTarget(entity);
        if (!entity.method_6128() || entity.method_24828() || ElytraAuraResolve.isStoyak(entity)) {
            return entity.method_19538().method_1031(0.0, (double)entity.method_17682(), 0.0);
        }
        Vec3d getServerPos = ((ResolvedPositionEntity)entity).hachclientport$getResolvedPos();
        point = Resolver.INSTANCE.resolveElytra(point, ElytraAuraResolve.mc.field_1724.method_7261(1.5f) - 0.01f);
        Vec3d vec3d = getServerPos.method_1020(ElytraAuraResolve.getUnLerpedPos(entity));
        if (ElytraAuraResolve.shouldMovePredict(entity)) {
            if (entity instanceof AbstractClientPlayerEntity) {
                AbstractClientPlayerEntity pl = (AbstractClientPlayerEntity)entity;
                d = (float)mc.method_1562().method_2871(pl.method_5667()).method_2959() / 50.0f;
            } else {
                d = 1.5;
            }
        } else {
            d = 0.4f;
        }
        point = point.method_1019(vec3d.method_1021(d));
        if (fromYou) {
            point = point.method_1020(ElytraAuraResolve.mc.field_1724.method_33571());
        }
        return point;
    }

    public static Vec3d getPointOnTarget(LivingEntity entity) {
        return ElytraAuraResolve.getPoint(entity, switch (ElytraAuraResolve.elytraAura.typeAim.get()) {
            default -> throw new MatchException(null, null);
            case ElytraAura.TypeAim.Resolve, ElytraAura.TypeAim.Middle -> GETPOINT.MIDDLE;
            case ElytraAura.TypeAim.FireWork -> GETPOINT.FIREWORK;
            case ElytraAura.TypeAim.Default -> GETPOINT.DEFAULT;
        });
    }

    public static Vec3d getPoint(LivingEntity entity, GETPOINT getpoint) {
        return ElytraAuraResolve.getPoint(ElytraAuraResolve.mc.field_1724.method_33571(), entity, getpoint);
    }

    public static Vec3d getPoint(Vec3d pos, LivingEntity entity, GETPOINT getpoint) {
        if (pos == null) {
            return Vec3d.field_1353;
        }
        Vec3d vec3d = null;
        Vec3d resolve = ((ResolvedPositionEntity)entity).hachclientport$getResolvedPos() != null ? ((ResolvedPositionEntity)entity).hachclientport$getResolvedPos() : ElytraAuraResolve.getPoint(entity, GETPOINT.DEFAULT);
        float tick = mc.method_61966().method_60637(true);
        switch (getpoint.ordinal()) {
            case 0: {
                vec3d = new Vec3d(MathHelper.method_15350((double)pos.field_1352, (double)(entity.method_5829().field_1323 + (double)0.05f), (double)(entity.method_5829().field_1320 - (double)0.05f)), MathHelper.method_15350((double)pos.field_1351, (double)(entity.method_5829().field_1322 + (double)0.05f), (double)(entity.method_5829().field_1325 - (double)0.05f)), MathHelper.method_15350((double)pos.field_1350, (double)(entity.method_5829().field_1321 + (double)0.05f), (double)(entity.method_5829().field_1324 - (double)0.05f)));
                break;
            }
            case 2: {
                Vec3d interpolatedPos = ElytraAuraResolve.getPoint(entity, GETPOINT.MIDDLE);
                for (Entity e : ElytraAuraResolve.mc.field_1687.method_18112()) {
                    Vec3d fireworkResolved;
                    FireworkRocketEntity firework;
                    float maxDistance = 0.1f;
                    float md = maxDistance * maxDistance;
                    if (!(e instanceof FireworkRocketEntity) || ((IShooterEntity)(firework = (FireworkRocketEntity)e)).leet$getShooter() != entity || ElytraAura.INSTANCE.isLeave(entity) || !(RotationUtility.squaredBoxDistance((Entity)ElytraAuraResolve.mc.field_1724, fireworkResolved = MoveUtility.getResolvedPos((Entity)firework)) < (double)md)) continue;
                    interpolatedPos = fireworkResolved;
                }
                double minX = interpolatedPos.field_1352 - (double)entity.method_17681() / 2.0;
                double maxX = interpolatedPos.field_1352 + (double)entity.method_17681() / 2.0;
                double minY = interpolatedPos.field_1351;
                double maxY = interpolatedPos.field_1351 + (double)entity.method_17682();
                double minZ = interpolatedPos.field_1350 - (double)entity.method_17681() / 2.0;
                double maxZ = interpolatedPos.field_1350 + (double)entity.method_17681() / 2.0;
                vec3d = new Vec3d(MathHelper.method_15350((double)pos.field_1352, (double)(minX + (double)0.05f), (double)(maxX - (double)0.05f)), MathHelper.method_15350((double)pos.field_1351, (double)(minY + (double)0.05f), (double)(maxY - (double)0.05f)), MathHelper.method_15350((double)pos.field_1350, (double)(minZ + (double)0.05f), (double)(maxZ - (double)0.05f)));
                break;
            }
            case 1: {
                Vec3d interpolatedPos = ElytraAuraResolve.getPoint(entity, GETPOINT.DEFAULT).method_1019(resolve.method_1020(ElytraAuraResolve.getPoint(entity, GETPOINT.DEFAULT)));
                if (ElytraAuraResolve.elytraAura.typeAim.is(ElytraAura.TypeAim.Resolve)) {
                    interpolatedPos = resolve;
                }
                double minX = interpolatedPos.field_1352 - (double)entity.method_17681() / 2.0;
                double maxX = interpolatedPos.field_1352 + (double)entity.method_17681() / 2.0;
                double minY = interpolatedPos.field_1351;
                double maxY = interpolatedPos.field_1351 + (double)entity.method_17682();
                double minZ = interpolatedPos.field_1350 - (double)entity.method_17681() / 2.0;
                double maxZ = interpolatedPos.field_1350 + (double)entity.method_17681() / 2.0;
                vec3d = new Vec3d(MathHelper.method_15350((double)pos.field_1352, (double)(minX + (double)0.05f), (double)(maxX - (double)0.05f)), MathHelper.method_15350((double)pos.field_1351, (double)(minY + (double)0.05f), (double)(maxY - (double)0.05f)), MathHelper.method_15350((double)pos.field_1350, (double)(minZ + (double)0.05f), (double)(maxZ - (double)0.05f)));
            }
        }
        return vec3d;
    }

    public static Vec3d getUnLerpedPos(LivingEntity entity) {
        return new Vec3d(entity.field_6014, entity.field_6036, entity.field_5969);
    }

    public static boolean shouldMovePredict(LivingEntity entity) {
        return SuperFirework.bpstarget(entity) >= 25.0f && !ElytraAuraResolve.isStoyak(entity) && ElytraAura.INSTANCE.isLeave(entity) && entity.method_6128();
    }

    public static boolean isStoyak(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        double d = entity.field_6038;
        double e = entity.field_5971;
        double f = entity.field_5989;
        float offset = (float)entity.method_30950(mc.method_61966().method_60637(true)).method_1020(new Vec3d(d, e, f)).method_1033();
        return offset == 0.0f;
    }

    private ElytraAuraResolve() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static enum GETPOINT {
        DEFAULT,
        MIDDLE,
        FIREWORK;

    }
}

