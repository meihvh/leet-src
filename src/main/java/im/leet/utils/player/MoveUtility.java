/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.gui.screen.ingame.SignEditScreen
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 *  net.minecraft.util.PlayerInput
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.utils.player;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.list.EventInput;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.network.NetworkUtility;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public final class MoveUtility
implements MinecraftHolder {
    public static float getBPS(LivingEntity entity) {
        return (float)(Math.hypot(entity.method_23317() - entity.field_6014, Math.hypot(entity.method_23318() - entity.field_6036, entity.method_23321() - entity.field_5969)) * 20.0) * Client.TIMER;
    }

    public static boolean hasElytra() {
        return MoveUtility.mc.field_1724.method_6118(EquipmentSlot.field_6174).method_58694(DataComponentTypes.field_54197) != null;
    }

    public static void startGliding() {
        PlayerInput prevInput = MoveUtility.mc.field_1724.field_3913.field_54155;
        MoveUtility.mc.field_1724.field_3913.field_54155 = new PlayerInput(prevInput.comp_3159(), prevInput.comp_3160(), prevInput.comp_3161(), prevInput.comp_3162(), false, prevInput.comp_3164(), prevInput.comp_3165());
        MoveUtility.mc.field_1724.method_23669();
        mc.method_1562().method_52787((Packet)new ClientCommandC2SPacket((Entity)MoveUtility.mc.field_1724, ClientCommandC2SPacket.Mode.field_12982));
    }

    public static Vec2f getMovementInput() {
        PlayerInput input = MoveUtility.mc.field_1724.field_3913.field_54155;
        float x = 0.0f;
        float z = 0.0f;
        if (input.comp_3159()) {
            x = 1.0f;
        } else if (input.comp_3160()) {
            x = -1.0f;
        }
        if (input.comp_3161()) {
            z = -1.0f;
        } else if (input.comp_3162()) {
            z = 1.0f;
        }
        return new Vec2f(x, z);
    }

    public static Vec2f getMovementVector(Vec2f input, float yaw) {
        float x = input.field_1343;
        float z = input.field_1342;
        yaw = (yaw + 90.0f) * ((float)Math.PI / 180);
        float sin = MathHelper.method_15374((float)yaw);
        float cos = MathHelper.method_15362((float)yaw);
        return new Vec2f(x * cos - z * sin, z * cos + x * sin);
    }

    public static void stopGliding() {
        MoveUtility.mc.field_1724.method_66281();
        NetworkUtility.send(new ClientCommandC2SPacket((Entity)MoveUtility.mc.field_1724, ClientCommandC2SPacket.Mode.field_12982));
    }

    public static float getSpeed() {
        double dx = MoveUtility.mc.field_1724.method_23317() - MoveUtility.mc.field_1724.field_6014;
        double dz = MoveUtility.mc.field_1724.method_23321() - MoveUtility.mc.field_1724.field_5969;
        double dy = MoveUtility.mc.field_1724.method_23318() - MoveUtility.mc.field_1724.field_6036;
        return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static float getSpeedXZ() {
        double dx = MoveUtility.mc.field_1724.method_23317() - MoveUtility.mc.field_1724.field_6014;
        double dz = MoveUtility.mc.field_1724.method_23321() - MoveUtility.mc.field_1724.field_5969;
        return (float)Math.sqrt(dx * dx + dz * dz);
    }

    public static void setSpeed(double motion) {
        float strafe;
        float forward;
        PlayerInput playerInput = MoveUtility.mc.field_1724.field_3913.field_54155;
        float f = playerInput.comp_3159() ? 1.0f : (forward = playerInput.comp_3160() ? -1.0f : 0.0f);
        float f2 = playerInput.comp_3161() ? 1.0f : (strafe = playerInput.comp_3162() ? -1.0f : 0.0f);
        if (forward != 0.0f && strafe != 0.0f) {
            forward *= 0.7f;
            strafe *= 0.7f;
        }
        float yaw = Client.ROTATION.getRotate().getYaw();
        MoveUtility.mc.field_1724.method_18800((Math.sin((double)(-yaw) * (Math.PI / 180)) * (double)forward + Math.cos((double)yaw * (Math.PI / 180)) * (double)strafe) * motion, MoveUtility.mc.field_1724.method_18798().field_1351, (Math.cos((double)(-yaw) * (Math.PI / 180)) * (double)forward + Math.sin((double)yaw * (Math.PI / 180)) * (double)strafe) * motion);
    }

    public static boolean hasMovement(PlayerInput input) {
        return input.comp_3159() || input.comp_3160() || input.comp_3161() || input.comp_3162();
    }

    public static void DPSMUSORA(double x, double y, double z) {
        if (MoveUtility.mc.field_1724 == null) {
            return;
        }
        MoveUtility.mc.field_1724.method_5814(MoveUtility.mc.field_1724.method_23317() + x, MoveUtility.mc.field_1724.method_23318() + y, MoveUtility.mc.field_1724.method_23321() + z);
    }

    public static float getdir() {
        boolean willmove;
        boolean isr = MoveUtility.isRightDown();
        boolean isl = MoveUtility.isLeftDown();
        boolean isf = MoveUtility.isForDown();
        boolean isb = MoveUtility.isBackDown();
        float startfloat = Client.ROTATION.getRotate().getYaw();
        if (!(isr || isb || isl || isf)) {
            return -1.0f;
        }
        if (isb) {
            startfloat -= 180.0f;
        }
        boolean bl = willmove = !(!isb && !isf || isb && isf);
        if (!willmove) {
            if (isl) {
                startfloat -= 90.0f;
            }
            if (isr) {
                startfloat += 90.0f;
            }
        } else if (startfloat == MoveUtility.mc.field_1724.method_36454()) {
            if (isl) {
                startfloat -= 45.0f;
            }
            if (isr) {
                startfloat += 45.0f;
            }
        } else {
            if (isl) {
                startfloat += 45.0f;
            }
            if (isr) {
                startfloat -= 45.0f;
            }
        }
        if (!willmove) {
            willmove = !(!isl && !isr || isl && isr);
        }
        double yawt = Math.toRadians(startfloat);
        if (!willmove) {
            return -1.0f;
        }
        return startfloat;
    }

    public static boolean isForDown() {
        if (MoveUtility.weirdscreen()) {
            return false;
        }
        return MoveUtility.mc.field_1690.field_1894.method_1434();
    }

    public static boolean isBackDown() {
        if (MoveUtility.weirdscreen()) {
            return false;
        }
        return MoveUtility.mc.field_1690.field_1881.method_1434();
    }

    public static boolean isLeftDown() {
        if (MoveUtility.weirdscreen()) {
            return false;
        }
        return MoveUtility.mc.field_1690.field_1913.method_1434();
    }

    public static boolean isRightDown() {
        if (MoveUtility.weirdscreen()) {
            return false;
        }
        return MoveUtility.mc.field_1690.field_1849.method_1434();
    }

    public static boolean weirdscreen() {
        return MoveUtility.mc.field_1755 instanceof ChatScreen || MoveUtility.mc.field_1755 instanceof SignEditScreen;
    }

    public static double direction(float rotationYaw, double moveForward, double moveStrafing) {
        if (moveForward < 0.0) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (moveForward < 0.0) {
            forward = -0.5f;
        } else if (moveForward > 0.0) {
            forward = 0.5f;
        }
        if (moveStrafing > 0.0) {
            rotationYaw -= 90.0f * forward;
        }
        if (moveStrafing < 0.0) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static void silentCorrection(EventInput event, float yaw) {
        MoveUtility.silentCorrectRaw(event, MoveUtility.mc.field_1724.method_36454() - yaw);
    }

    public static void silentCorrectRaw(EventInput event, float deltaYaw) {
        float dYaw = deltaYaw * ((float)Math.PI / 180);
        float sin = MathHelper.method_15374((float)dYaw);
        float cos = MathHelper.method_15362((float)dYaw);
        float forward = event.getForward();
        float strafe = event.getStrafe();
        event.setStrafe(Math.round(strafe * cos - forward * sin));
        event.setForward(Math.round(forward * cos + strafe * sin));
    }

    public static Vec3d getResolvedPlayerPos() {
        return MoveUtility.getResolvedPos((Entity)MoveUtility.mc.field_1724);
    }

    public static Vec3d getResolvedPos(Entity entity) {
        return ((ResolvedPositionEntity)entity).hachclientport$getResolvedPos();
    }

    private MoveUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

