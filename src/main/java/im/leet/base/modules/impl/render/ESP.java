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
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 */
package im.leet.base.modules.impl.render;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.Event2D;
import im.leet.api.events.list.EventGameTick;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.modules.impl.player.countermine.SkinEntityUtility;
import im.leet.base.modules.impl.render.esp.ESPRenderer;
import im.leet.base.modules.impl.render.esp.impl.BoxRenderer;
import im.leet.base.modules.impl.render.esp.impl.EffectRenderer;
import im.leet.base.modules.impl.render.esp.impl.NametagRenderer;
import im.leet.base.modules.impl.render.esp.impl.SkeletonRenderer;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ESP
extends Module {
    public static final ESP INSTANCE = new ESP();
    public MultiEnumSetting<ESPEntities> entities = this.add(new MultiEnumSetting<ESPEntities>("Entities", ESPEntities.class));
    public MultiEnumSetting<ESPElements> elements = this.add(new MultiEnumSetting<ESPElements>("Elements", ESPElements.class));
    public CheckBox corners = this.add((CheckBox)new CheckBox("Corners", false).visible(() -> this.elements.get(ESPElements.Box)));
    public SliderSetting cornersLenght = (SliderSetting)this.add(new SliderSetting("Corner length", 0.3f, 0.1f, 0.4f).increment(0.1f)).visible(() -> this.corners.getVisible().get() != false && this.corners.get());
    public SliderSetting thickness = (SliderSetting)this.add(new SliderSetting("Thickness", 1.0f, 0.5f, 2.0f).increment(0.1f)).visible(() -> this.elements.get(ESPElements.Box) || this.elements.get(ESPElements.Skeleton));
    public ColorSetting countermineEnemyColor = this.add((ColorSetting)new ColorSetting("Countermine Enemy Color", new Color(0xF80000, true)).visible(() -> this.entities.get(ESPEntities.Countermine)));
    public ColorSetting countermineTeamColor = this.add((ColorSetting)new ColorSetting("Countermine Team Color", new Color(32768, true)).visible(() -> this.entities.get(ESPEntities.Countermine)));
    private ArrayList<Entity> toRender = new ArrayList();
    EventBus<Event> events = event -> {
        if (event instanceof EventGameTick) {
            this.toRender.clear();
            for (Entity entity : ESP.mc.field_1687.method_18112()) {
                if (entity instanceof ClientPlayerEntity && ESP.mc.field_1690.method_31044().method_31034() || entity.method_31481() || !this.entities.get().stream().anyMatch(it -> it.supplier.shouldRender(entity))) continue;
                this.toRender.add(entity);
            }
        }
        if (event instanceof Event2D) {
            Event2D e = (Event2D)event;
            for (Entity entity : this.toRender) {
                Vec3d interp = entity.method_30950(mc.method_61966().method_60637(true));
                Box box = entity.method_5829().method_997(interp.method_1020(entity.method_19538()));
                if (this.isCountermineEntity(entity)) {
                    double centerX = (box.field_1323 + box.field_1320) / 2.0;
                    double centerZ = (box.field_1321 + box.field_1324) / 2.0;
                    double width = 0.3;
                    double height = 1.8;
                    double yOffset = -1.3;
                    box = new Box(centerX - width, box.field_1322 + yOffset, centerZ - width, centerX + width, box.field_1322 + yOffset + height, centerZ + width);
                }
                Vec3d[] corners = new Vec3d[]{new Vec3d(box.field_1323, box.field_1322, box.field_1321), new Vec3d(box.field_1323, box.field_1322, box.field_1324), new Vec3d(box.field_1320, box.field_1322, box.field_1321), new Vec3d(box.field_1320, box.field_1322, box.field_1324), new Vec3d(box.field_1323, box.field_1325, box.field_1321), new Vec3d(box.field_1323, box.field_1325, box.field_1324), new Vec3d(box.field_1320, box.field_1325, box.field_1321), new Vec3d(box.field_1320, box.field_1325, box.field_1324)};
                float minX = Float.MAX_VALUE;
                float minY = Float.MAX_VALUE;
                float maxX = -3.4028235E38f;
                float maxY = -3.4028235E38f;
                boolean anyVisible = false;
                for (Vec3d corner : corners) {
                    Vec3d projected = MathUtility.worldSpaceToScreenSpace(corner);
                    if (projected.field_1350 <= 0.0 || projected.field_1350 >= 1.0) continue;
                    anyVisible = true;
                    minX = (float)Math.min((double)minX, projected.field_1352);
                    minY = (float)Math.min((double)minY, projected.field_1351);
                    maxX = (float)Math.max((double)maxX, projected.field_1352);
                    maxY = (float)Math.max((double)maxY, projected.field_1351);
                }
                if (!anyVisible || maxX < 0.0f || maxY < 0.0f || minX > (float)mc.method_22683().method_4486() || minY > (float)mc.method_22683().method_4502()) continue;
                for (ESPElements element : this.elements.get()) {
                    element.renderer.render(minX, minY, maxX, maxY, this.thickness.get(), entity);
                }
            }
        }
    };

    private ESP() {
        super("ESP", Category.RENDER, "Draws entities through walls", new Tag[0]);
    }

    public boolean dontRenderNametag(Entity entity) {
        if (!this.elements.get(ESPElements.Nametag)) {
            return false;
        }
        return this.entities.get().stream().anyMatch(it -> it.supplier.shouldRender(entity));
    }

    private boolean isCountermineEntity(Entity entity) {
        String skinModel = SkinEntityUtility.getEntitySkinModel(entity);
        return SkinEntityUtility.COUNTERMINE_SKINS.contains(skinModel);
    }

    public static enum ESPEntities implements EnumChoice
    {
        Players(true, e -> e instanceof PlayerEntity),
        Items(true, e -> e instanceof ItemEntity),
        Projectiles(true, e -> e instanceof ProjectileEntity),
        Countermine(false, e -> {
            String skinModel = SkinEntityUtility.getEntitySkinModel(e);
            return SkinEntityUtility.COUNTERMINE_SKINS.contains(skinModel) && SkinEntityUtility.checkIsAlive(e);
        }),
        Other(false, e -> e instanceof LivingEntity && !Players.getSupplier().shouldRender(e) && !Items.getSupplier().shouldRender(e) && !Projectiles.getSupplier().shouldRender(e));

        final boolean defaultEnabled;
        public final Callback supplier;

        private ESPEntities(boolean defaultEnabled, Callback supplier) {
            this.defaultEnabled = defaultEnabled;
            this.supplier = supplier;
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

    public static enum ESPElements implements EnumChoice
    {
        Box(true, new BoxRenderer(ESPRenderer.Align.TOP)),
        Skeleton(true, new SkeletonRenderer(ESPRenderer.Align.TOP)),
        Nametag(true, new NametagRenderer(ESPRenderer.Align.TOP)),
        Effects(true, new EffectRenderer(ESPRenderer.Align.BOTTOM));

        final boolean defaultEnabled;
        final ESPRenderer renderer;

        private ESPElements(boolean defaultEnabled, ESPRenderer renderer) {
            this.defaultEnabled = defaultEnabled;
            this.renderer = renderer;
        }

        @Override
        public boolean isDefaultEnabled() {
            return this.defaultEnabled;
        }

        public ESPRenderer getRenderer() {
            return this.renderer;
        }
    }
}

