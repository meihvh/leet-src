/*
 * Decompiled with CFR 0.152.
 */
package im.leet.api.drags;

import im.leet.api.drags.Drag;
import im.leet.utils.math.MathUtility;
import java.util.ArrayList;

public class Drags {
    final ArrayList<Drag> drags = new ArrayList();

    public void addDrag(Drag drag) {
        this.drags.add(drag);
    }

    public Drag findDrag(String name) {
        return this.drags.stream().filter(drag -> drag.name.equals(name)).findFirst().orElse(null);
    }

    public void click(double mouseX, double mouseY, int button) {
        for (Drag drag : this.drags) {
            if (!drag.canDrag.get().booleanValue() || !MathUtility.mouseIn(drag.x, drag.y, drag.width, drag.height, mouseX, mouseY)) continue;
            drag.dX = (float)(mouseX - (double)drag.x);
            drag.dY = (float)(mouseY - (double)drag.y);
            drag.dragging = true;
        }
    }

    public void release(double mouseX, double mouseY) {
        for (Drag drag : this.drags) {
            drag.dragging = false;
        }
    }

    public void update(double mouseX, double mouseY) {
        for (Drag drag : this.drags) {
            if (!drag.dragging) continue;
            drag.x = (float)(mouseX - (double)drag.dX);
            drag.y = (float)(mouseY - (double)drag.dY);
        }
    }
}

