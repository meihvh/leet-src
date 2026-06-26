/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.decoration.ArmorStandEntity
 *  net.minecraft.entity.decoration.DisplayEntity$ItemDisplayEntity
 *  net.minecraft.entity.decoration.DisplayEntity$ItemDisplayEntity$Data
 *  net.minecraft.util.Identifier
 */
package im.leet.base.modules.impl.render;

import im.leet.api.events.Event;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGetFov;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.slider.SliderSetting;
import im.leet.mixin.accessor.IItemDisplayEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.Identifier;

public class Removals
extends Module {
    public static final Removals INSTANCE = new Removals();
    public MultiEnumSetting<Removal> removals = this.multiEnumSetting("Elements", Removal.class);
    public MultiEnumSetting<EntityKind> entities = this.multiEnumSetting("Entites", EntityKind.class);
    public MultiEnumSetting<Sound> soundMultiEnumSetting = this.multiEnumSetting("Sound", Sound.class);
    public SliderSetting fov = (SliderSetting)this.sliderSetting("FOV", 90.0f, 0.0f, 180.0f).visible(() -> this.removals.get(Removal.FOV));
    EventBus<Event> events = event -> {
        if (event instanceof EventGetFov) {
            EventGetFov e = (EventGetFov)event;
            if (this.removals.get(Removal.FOV) && e.fov == (float)((Integer)Removals.mc.field_1690.method_41808().method_41753()).intValue()) {
                e.fov = this.fov.get();
            }
        }
    };

    private Removals() {
        super("Removals", Category.RENDER, "Removes unnecessary game elements", new Tag[0]);
    }

    public boolean doesNotRenderEntity(Entity entity) {
        Identifier model;
        DisplayEntity.ItemDisplayEntity d;
        DisplayEntity.ItemDisplayEntity.Data data;
        if (entity instanceof ArmorStandEntity && this.entities.get(EntityKind.ArmorStand)) {
            return true;
        }
        return entity instanceof DisplayEntity.ItemDisplayEntity && (data = ((IItemDisplayEntity)(d = (DisplayEntity.ItemDisplayEntity)entity)).client$data()) != null && (model = (Identifier)data.comp_1322().method_58694(DataComponentTypes.field_54199)) != null && model.method_12832().equals("fa38604e66601514") && this.entities.get(EntityKind.CounterMineBlood);
    }

    public static enum Removal implements EnumChoice
    {
        HurtView("Hurt view", true),
        FireOverlay("Fire overlay", true),
        BlockOverlay("Block overlay", true),
        Darkness("Darkness", true),
        Blindness("Blindness", true),
        SignText("Sign text", false),
        Armor("Armor", false),
        TotemPop("Totem pop", true),
        FOV("FOV limit", false),
        Nametags("Nametags", false),
        Invisibility("Invisibility", true),
        Vegetation("Vegetation", true),
        Scoreboard("Scoreboard", false),
        Nausea("Nausea", true);

        final String renderName;
        final boolean defaultEnabled;

        private Removal(String renderName, boolean defaultEnabled) {
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

    public static enum EntityKind {
        ArmorStand,
        CounterMineBlood;

    }

    public static enum Sound implements EnumChoice
    {
        SoundWithering("Withering sound", true),
        SoundTotem("Totem sound", true),
        SoundExperience("Experience sound", true),
        SoundTRIDENT("Trident sound", true);

        final String renderName;
        private final boolean defaultEnabled;

        private Sound(String renderName, boolean defaultEnabled) {
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
}

