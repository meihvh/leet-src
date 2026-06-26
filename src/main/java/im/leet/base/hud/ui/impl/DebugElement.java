/*
 * Decompiled with CFR 0.152.
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.TextureUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.DebugUtility;
import java.util.Map;

public class DebugElement
extends HudElement {
    public DebugElement(Drag drag) {
        super("Debug", drag);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float oy = 0.0f;
        String[] trim = new String[DebugUtility.traces.size()];
        int a = 0;
        long now = System.currentTimeMillis();
        for (Map.Entry<String, DebugUtility.Trace> entry : DebugUtility.traces.entrySet()) {
            oy += this.draw(entry.getKey(), entry.getValue(), oy);
            if (now - entry.getValue().timestamp() <= 5000L) continue;
            trim[a++] = entry.getKey();
        }
        for (String s : trim) {
            if (s == null) break;
            DebugUtility.traces.remove(s);
        }
    }

    float draw(String name, DebugUtility.Trace trace, float oy) {
        float xo = 0.0f;
        Client.RENDERER.text(Long.toString(trace.timestamp()), this.x, this.y + oy + 2.0f, TextureUse.SFMEDIUM, 7.0f, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.text(name, this.x + (xo += Client.RENDERER.textWidth(Long.toString(trace.timestamp()), TextureUse.SFMEDIUM, 7.0f) + 2.0f), this.y + oy, TextureUse.SFMEDIUM, 9.0f, ClientColors.GREEN);
        Client.RENDERER.text(trace.content(), this.x + (xo += Client.RENDERER.textWidth(name, TextureUse.SFMEDIUM, 9.0f) + 2.0f), this.y + oy + 1.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        return Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 9.0f) + 1.0f;
    }
}

