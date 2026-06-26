/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package im.leet.base.hud.ui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventKey;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.modules.impl.render.Interface;
import java.util.ArrayList;
import java.util.Arrays;

public class Hud
implements MinecraftHolder {
    private final ArrayList<HudElement> elements = new ArrayList();
    ArrayList<UIWidget> overlays = new ArrayList();
    EventBus<EventKey> keyEvent = key -> {
        if (key.action == 1) {
            // empty if block
        }
    };

    public void register(HudElement ... elements) {
        this.elements.addAll(Arrays.asList(elements));
    }

    public void registerOverlay(UIWidget r) {
        if (!this.overlays.contains(r)) {
            this.overlays.add(r);
        }
    }

    public void unregisterOverlay(UIWidget w) {
        this.overlays.remove(w);
    }

    public void render(int mouseX, int mouseY) {
        Client.RENDERER.getCrenderSystem().layerTex(Client.RENDERER.getCrenderSystem().getAtlas().getGlId()).layer(CRenderSystem.RenderLayer.HUD);
        if (!Interface.INSTANCE.isEnabled()) {
            return;
        }
        for (HudElement element : this.elements) {
            if (!element.isEnabled()) continue;
            element.render(mouseX, mouseY);
            element._render(mouseX, mouseY);
        }
        for (UIWidget w : this.overlays) {
            w.render(mouseX, mouseY);
        }
        Client.DRAGS.update(mouseX, mouseY);
    }

    public void click(int mouseX, int mouseY, int button) {
        if (!Interface.INSTANCE.isEnabled()) {
            return;
        }
        for (UIWidget w : this.overlays.reversed()) {
            if (!w.click(mouseX, mouseY, button)) continue;
            return;
        }
        for (HudElement element : this.elements) {
            if (!element.click(mouseX, mouseY, button)) continue;
            return;
        }
        Client.DRAGS.click(mouseX, mouseY, button);
    }

    public void release(int button) {
        if (!Interface.INSTANCE.isEnabled()) {
            return;
        }
        for (UIWidget w : this.overlays.reversed()) {
            w.release(button);
        }
        for (HudElement element : this.elements) {
            element.release(button);
        }
        Client.DRAGS.release(0.0, 0.0);
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!Interface.INSTANCE.isEnabled()) {
            return;
        }
        for (UIWidget w : this.overlays.reversed()) {
            w.keyPressed(keyCode, scanCode, modifiers);
        }
        for (HudElement element : this.elements) {
            element.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void charTyped(char ch, int modifiers) {
        if (!Interface.INSTANCE.isEnabled()) {
            return;
        }
        for (UIWidget w : this.overlays.reversed()) {
            w.chartyped(ch, modifiers);
        }
        for (HudElement element : this.elements) {
            element.chartyped(ch, modifiers);
        }
    }

    public void save(JsonObject gson) {
        JsonObject json = new JsonObject();
        gson.add("HudSettings", (JsonElement)json);
        for (HudElement element : this.elements) {
            JsonObject j = new JsonObject();
            json.add(element.getName(), (JsonElement)j);
            element.save(j);
        }
    }

    public void load(JsonObject gson) {
        if (gson.has("HudSettings")) {
            JsonObject json = gson.getAsJsonObject("HudSettings");
            for (HudElement element : this.elements) {
                if (!json.has(element.getName())) continue;
                JsonObject j = json.getAsJsonObject(element.getName());
                element.load(j);
            }
        }
    }

    public ArrayList<UIWidget> getOverlays() {
        return this.overlays;
    }

    public EventBus<EventKey> getKeyEvent() {
        return this.keyEvent;
    }

    public ArrayList<HudElement> getElements() {
        return this.elements;
    }
}

