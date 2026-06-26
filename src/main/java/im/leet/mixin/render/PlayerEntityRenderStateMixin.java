/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.PlayerEntityRenderState
 *  net.minecraft.entity.player.PlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package im.leet.mixin.render;

import im.leet.utils.client.mixin.IPlayerEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={PlayerEntityRenderState.class})
public class PlayerEntityRenderStateMixin
implements IPlayerEntityRenderState {
    @Unique
    private PlayerEntity leet$entity;

    @Override
    public PlayerEntity leet$getEntity() {
        return this.leet$entity;
    }

    @Override
    public void leet$setEntity(PlayerEntity entity) {
        this.leet$entity = entity;
    }
}

