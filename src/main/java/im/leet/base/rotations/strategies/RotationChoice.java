/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.rotations.strategies;

import im.leet.base.rotations.strategies.Rotation;
import im.leet.base.settings.impl.choice.Choice;

public abstract class RotationChoice
extends Choice
implements Rotation {
    protected RotationChoice(String name) {
        super(name);
    }
}

