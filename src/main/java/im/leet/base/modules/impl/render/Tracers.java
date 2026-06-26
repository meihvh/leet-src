/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.projectile.ProjectileEntity
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.render;

import im.leet.Client;
import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event2D;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;

public class Tracers
extends Module {
    public static final Tracers INSTANCE = new Tracers();
    public MultiEnumSetting<TracerEntities> entities = this.add(new MultiEnumSetting<TracerEntities>("Entities", TracerEntities.class));
    public ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 200)));
    public SliderSetting thickness = this.add(new SliderSetting("Thickness", 1.0f, 0.5f, 3.0f).increment(0.1f));
    public SliderSetting distance = this.add(new SliderSetting("Distance", 100.0f, 10.0f, 200.0f).increment(5.0f));
    private final ArrayList<Entity> toRender = new ArrayList();
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            this.toRender.clear();
            if (Tracers.mc.field_1687 == null || Tracers.mc.field_1724 == null) {
                return;
            }
            for (Entity entity : Tracers.mc.field_1687.method_18112()) {
                if (entity instanceof ClientPlayerEntity || entity.method_31481() || Tracers.mc.field_1724.method_5858(entity) > (double)(this.distance.get() * this.distance.get())) continue;
                boolean shouldRender = false;
                for (TracerEntities ent : this.entities.get()) {
                    shouldRender |= ent.getSupplier().shouldRender(entity);
                }
                if (!shouldRender) continue;
                this.toRender.add(entity);
            }
        }
        if (event instanceof Event2D) {
            if (Tracers.mc.field_1724 == null) {
                return;
            }
            float centerX = (float)mc.method_22683().method_4486() / 2.0f;
            float centerY = (float)mc.method_22683().method_4502() / 2.0f;
            for (Entity entity : this.toRender) {
                Color color;
                Vec3d entityPos = entity.method_30950(mc.method_61966().method_60637(true));
                Vec3d projected = MathUtility.worldSpaceToScreenSpace(new Vec3d(entityPos.field_1352, entityPos.field_1351 + (double)entity.method_17682() / 2.0, entityPos.field_1350));
                if (projected.field_1350 < 0.0 || projected.field_1350 >= 1.0 || !Double.isFinite(projected.field_1352) || !Double.isFinite(projected.field_1351)) continue;
                if (entity instanceof LivingEntity) {
                    LivingEntity l = (LivingEntity)entity;
                    v0 = ClientSettings.INSTANCE.targetValidator.getColor(l);
                } else {
                    v0 = color = null;
                }
                if (color == null) {
                    color = this.color.get();
                }
                Client.RENDERER.line(centerX, centerY, (float)projected.field_1352, (float)projected.field_1351, this.thickness.get(), 1.0f, 0.0f, color, color);
            }
        }
    };

    private Tracers() {
        super("Tracers", Category.RENDER, "Draws lines from the crosshair to entities", new Tag[0]);
    }

    public static enum TracerEntities implements EnumChoice
    {
        Players("Players", true, e -> e instanceof PlayerEntity),
        Items("Items", false, e -> e instanceof ItemEntity),
        Projectiles("Projectiles", false, e -> e instanceof ProjectileEntity),
        Other("Other", false, e -> e instanceof LivingEntity);

        final String renderName;
        final boolean defaultEnabled;
        public final Callback supplier;

        private TracerEntities(String renderName, boolean defaultEnabled, Callback supplier) {
            this.renderName = renderName;
            this.defaultEnabled = defaultEnabled;
            this.supplier = supplier;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }

        @Override
        public boolean isDefaultEnabled() {
            return this.defaultEnabled;
        }

        public Callback getSupplier() {
            return this.supplier;
        }

        public static interface Callback {
            public boolean shouldRender(Entity var1);
        }
    }
}

