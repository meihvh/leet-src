/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.client.mixin;

import im.leet.base.modules.impl.combat.resolver.BackTrackPosResolver;

public interface IOtherClientPlayerEntity {
    public void resolve(BackTrackPosResolver.Resolve var1);

    public void releaseResolver();
}

