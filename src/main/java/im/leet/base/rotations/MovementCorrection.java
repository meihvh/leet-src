/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.rotations;

import im.leet.base.settings.EnumChoice;

public enum MovementCorrection implements EnumChoice
{
    NONE("None"),
    STRICT("Strict"),
    SILENT("Silent"),
    LOOK("Client look");

    final String renderName;

    private MovementCorrection(String renderName) {
        this.renderName = renderName;
    }

    @Override
    public String getRenderName() {
        return this.renderName;
    }
}

