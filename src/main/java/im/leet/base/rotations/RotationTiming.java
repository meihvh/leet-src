/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.rotations;

import im.leet.base.settings.EnumChoice;

public enum RotationTiming implements EnumChoice
{
    Normal,
    Snap,
    Tick;


    @Override
    public String getLangClassName() {
        return "RotationTiming";
    }
}

