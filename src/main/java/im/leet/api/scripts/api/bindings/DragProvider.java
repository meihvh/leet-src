/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.scripts.api.bindings;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.scripts.api.bindings.support.DragScripted;

public class DragProvider {
    public DragScripted create(String name, float baseX, float baseY, float baseWidth, float baseHeight) {
        Drag drag = new Drag(name, () -> true).bound(baseX, baseY, baseWidth, baseHeight);
        if (Client.DRAGS.findDrag(name) == null) {
            Client.DRAGS.addDrag(drag);
        } else {
            drag = Client.DRAGS.findDrag(name);
        }
        return new DragScripted(drag);
    }
}

