/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package im.leet.api.drags;

import com.google.gson.JsonObject;
import im.leet.utils.math.Rectangle;
import java.util.function.Supplier;

public class Drag {
    public float x;
    public float y;
    public float width;
    public float height;
    public float dX;
    public float dY;
    public final String name;
    public final Supplier<Boolean> canDrag;
    public boolean dragging = false;

    public Drag(String name, Supplier<Boolean> canDrag) {
        this.name = name;
        this.canDrag = canDrag;
    }

    public Drag() {
        this.name = "";
        this.canDrag = () -> true;
    }

    public Drag bound(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public Drag bound(Rectangle rectangle) {
        this.x = rectangle.getX();
        this.y = rectangle.getY();
        this.width = rectangle.getWidth();
        this.height = rectangle.getHeight();
        return this;
    }

    public void save(JsonObject json) {
        json.addProperty("x", (Number)Float.valueOf(this.x));
        json.addProperty("y", (Number)Float.valueOf(this.y));
    }

    public void load(JsonObject json) {
        if (json.has("x")) {
            this.x = json.get("x").getAsFloat();
        }
        if (json.has("y")) {
            this.y = json.get("y").getAsFloat();
        }
    }
}

