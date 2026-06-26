/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.sound.Channel$SourceManager
 *  net.minecraft.client.sound.SoundInstance
 *  net.minecraft.client.sound.SoundSystem
 *  net.minecraft.client.sound.SoundSystem$PlayResult
 *  net.minecraft.util.Identifier
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package im.leet.mixin.client;

import im.leet.Client;
import im.leet.base.modules.impl.render.Removals;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.mixin.accessor.SourceAccessor;
import java.util.Map;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SoundSystem.class})
public abstract class SoundSystemMixin {
    @Shadow
    private Map<SoundInstance, Channel.SourceManager> field_18950;

    @Inject(method={"play"}, at={@At(value="RETURN")})
    private void onSoundPlay(SoundInstance sound, CallbackInfoReturnable<SoundSystem.PlayResult> cir) {
        if (!Client.INITIALIZED || Client.RTX_ENGINE == null) {
            return;
        }
        try {
            Channel.SourceManager sourceManager = this.field_18950.get(sound);
            if (sourceManager != null) {
                sourceManager.method_19735(source -> {
                    try {
                        int sourceId = ((SourceAccessor)source).rtx$getSourceId();
                        if (sourceId > 0) {
                            Client.RTX_ENGINE.getMixer().injectFiltersToChannel(sound, sourceId);
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                });
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Inject(method={"play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPlaySound(SoundInstance sound, CallbackInfoReturnable<SoundSystem.PlayResult> cir) {
        Identifier id = sound.method_4775();
        if (!Removals.INSTANCE.isEnabled()) {
            return;
        }
        MultiEnumSetting<Removals.Sound> soundSetting = Removals.INSTANCE.soundMultiEnumSetting;
        if (soundSetting.get(Removals.Sound.SoundWithering) && this.isWitherSound(id)) {
            cir.cancel();
        }
        if (soundSetting.get(Removals.Sound.SoundTRIDENT) && this.isTridentSound(id)) {
            cir.cancel();
        }
        if (soundSetting.get(Removals.Sound.SoundTotem) && this.isTotemSound(id)) {
            cir.cancel();
        }
        if (soundSetting.get(Removals.Sound.SoundExperience) && this.isExperienceOrbSound(id)) {
            cir.cancel();
        }
    }

    @Unique
    private boolean isWitherSound(Identifier id) {
        String path = id.method_12832();
        return path.contains("wither") || id.equals((Object)Identifier.method_60656((String)"entity.wither.ambient")) || id.equals((Object)Identifier.method_60656((String)"entity.wither.shoot")) || id.equals((Object)Identifier.method_60656((String)"entity.wither.death")) || id.equals((Object)Identifier.method_60656((String)"entity.wither.hurt"));
    }

    @Unique
    private boolean isTotemSound(Identifier id) {
        return id.equals((Object)Identifier.method_60656((String)"item.totem.use"));
    }

    @Unique
    private boolean isTridentSound(Identifier id) {
        String path = id.method_12832();
        return path.contains("trident") || id.equals((Object)Identifier.method_60656((String)"item.trident.hit")) || id.equals((Object)Identifier.method_60656((String)"item.trident.hit_ground")) || id.equals((Object)Identifier.method_60656((String)"item.trident.throw")) || id.equals((Object)Identifier.method_60656((String)"item.trident.return"));
    }

    @Unique
    private boolean isExperienceOrbSound(Identifier id) {
        String path = id.method_12832();
        return path.contains("experience") || id.equals((Object)Identifier.method_60656((String)"entity.experience_orb.pickup")) || id.equals((Object)Identifier.method_60656((String)"entity.experience_bottle.throw"));
    }
}

