/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexRendering
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.combat;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event3D;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.combat.resolver.AntiAimResolver;
import im.leet.base.modules.impl.combat.resolver.BackTrackPosResolver;
import im.leet.base.modules.impl.combat.resolver.Lag2Resolver;
import im.leet.base.modules.impl.combat.resolver.ResolverMode;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.MultiChoice;
import im.leet.utils.client.mixin.ResolvedPositionEntity;
import im.leet.utils.client.targets.TargetsUtility;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Resolver
extends Module {
    public static final Resolver INSTANCE = new Resolver();
    private final MultiChoice<ResolverMode> resolvers = this.multiChoice(new ResolverMode[]{new Lag2Resolver(), new AntiAimResolver(), BackTrackPosResolver.INSTANCE});
    private final CheckBox drawReal = this.checkbox("Show real position", false);
    EventBus<Event> events = event -> {
        this.resolvers.onEvent(event);
        if (event instanceof Event3D) {
            Event3D e = (Event3D)event;
            if (this.drawReal.get() && TargetsUtility.getTarget() != null) {
                e.stack.method_22903();
                Client.RENDERER.toCamera(e.stack);
                LivingEntity target = TargetsUtility.getTarget();
                VertexRendering.method_62295((MatrixStack)e.stack, (VertexConsumer)e.buffer.getBuffer((RenderLayer)RenderLayer.field_21695), (Box)target.method_5829().method_997(((ResolvedPositionEntity)target).hachclientport$getResolvedPos().method_1020(target.method_19538())), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                e.stack.method_22909();
            }
        }
    };

    private Resolver() {
        super("Resolver", Category.COMBAT, "Changes the aim point of Aura, ElytraAura, HugTarget", new Tag[0]);
    }

    public Vec3d resolveAura(Vec3d origin, float cooldown) {
        if (!this.isEnabled()) {
            return origin;
        }
        return this.resolvers.reduce(origin, (resolver, point) -> resolver.resolveAura((Vec3d)point, cooldown));
    }

    public Vec3d resolveElytra(Vec3d origin, float cooldown) {
        if (!this.isEnabled()) {
            return origin;
        }
        return this.resolvers.reduce(origin, (resolver, point) -> resolver.resolveElytra((Vec3d)point, cooldown));
    }

    public boolean isFakeLagging() {
        if (!this.isEnabled()) {
            return false;
        }
        for (ResolverMode resolver : (ResolverMode[])this.resolvers.get()) {
            if (!resolver.isEnabled() || !resolver.isFakeLagging()) continue;
            return true;
        }
        return false;
    }
}

