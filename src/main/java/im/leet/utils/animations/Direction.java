/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.animations;

public enum Direction {
    FORWARDS,
    BACKWARDS;


    public Direction opposite() {
        if (this == FORWARDS) {
            return BACKWARDS;
        }
        return FORWARDS;
    }
}

