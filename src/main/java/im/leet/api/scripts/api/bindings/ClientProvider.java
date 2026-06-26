/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  org.graalvm.polyglot.HostAccess$Export
 */
package im.leet.api.scripts.api.bindings;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.scripts.Script;
import im.leet.api.scripts.api.bindings.ModuleProvider;
import im.leet.api.scripts.api.bindings.support.AngleScripted;
import im.leet.base.modules.Module;
import im.leet.base.rotations.Angle;
import im.leet.base.rotations.MovementCorrection;
import im.leet.base.rotations.strategies.impl.LinearRotation;
import im.leet.base.settings.Setting;
import im.leet.utils.math.RayTraceUtility;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.graalvm.polyglot.HostAccess;

public class ClientProvider
implements MinecraftHolder {
    final ArrayList<ModuleProvider> modules = new ArrayList();
    Script script;

    public ClientProvider(Script script) {
        this.script = script;
    }

    @HostAccess.Export
    public ArrayList<ModuleProvider> getModules() {
        if (this.modules.size() != Client.MODULES.getModules().size()) {
            this.modules.clear();
            for (Module module : Client.MODULES.getModules()) {
                this.modules.add(new ModuleProvider(module));
            }
        }
        return this.modules;
    }

    @HostAccess.Export
    public void rotateTo(AngleScripted angle) {
        Client.ROTATION.rotate(LinearRotation.INSTANCE, new Angle(angle.yaw(), angle.pitch()), 1, true, MovementCorrection.STRICT, 0);
    }

    @HostAccess.Export
    public AngleScripted localRotation() {
        if (ClientProvider.mc.field_1724 == null) {
            return new AngleScripted(0.0f, 0.0f);
        }
        return new AngleScripted(ClientProvider.mc.field_1724.method_36454(), ClientProvider.mc.field_1724.method_36455());
    }

    @HostAccess.Export
    public AngleScripted rotation() {
        if (ClientProvider.mc.field_1724 == null) {
            return new AngleScripted(0.0f, 0.0f);
        }
        Angle client = Client.ROTATION.getRotate();
        return new AngleScripted(client.getYaw(), client.getPitch());
    }

    @HostAccess.Export
    public void settings(Setting<?> ... settings) {
        this.script.getScriptedModule().addAll(settings);
    }

    @HostAccess.Export
    public boolean traceEntity(AngleScripted vec, float distance, Entity entity, boolean walls) {
        if (walls && RayTraceUtility.raycast(ClientProvider.mc.field_1724.method_33571(), distance, new Angle(vec.yaw(), vec.pitch()), false).method_17783() == HitResult.Type.field_1332) {
            return false;
        }
        return RayTraceUtility.rayTrace(new Vec3d(vec.toVector()), (double)distance, entity.method_5829());
    }
}

