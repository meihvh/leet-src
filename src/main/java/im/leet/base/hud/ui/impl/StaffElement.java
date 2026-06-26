/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.network.PlayerListEntry
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.StringHelper
 *  net.minecraft.world.GameMode
 *  org.joml.Vector4f
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventGameTick;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringHelper;
import net.minecraft.world.GameMode;
import org.joml.Vector4f;

public class StaffElement
extends HudElement {
    Map<PlayerListEntry, StaffType> staff = new HashMap<PlayerListEntry, StaffType>();
    Pattern pattern = Pattern.compile("^(\u0430\u0434\u043c\u0438\u043d|yt|\u0432\u043b\u0430\u0434\u0435\u043b|\u043e\u0432\u043d\u0435\u0440|\u0441\u043e\u0437\u0434|\u0440\u0430\u0437\u0440\u0430\u0431|dev|mod|admin|help|supp|staff|intern|\u0441\u0442\u0430\u0436\u0435\u0440|\u044e\u0442|\u043c\u0435\u0434\u0438\u0430|media|\ua560|\ua503|\ua507\ua511|\ua515|\ua519|\ua523|\ua527|\ua531|\ua535|\ua539|\ua543|\ua547|\ua551|\ua555)");
    EventBus<EventGameTick> gametick = event -> {
        if (StaffElement.mc.field_1724.field_6012 % 20 == 0) {
            this.staff.clear();
            if (mc.method_1562() == null) {
                return;
            }
            for (PlayerListEntry entry : mc.method_1562().method_2880()) {
                String team = (entry.method_2955() != null ? StringHelper.method_15440((String)entry.method_2955().method_1144().getString()).toLowerCase() : "").replace("\u25cf", "").trim();
                if ((entry.method_2955() == null || !this.pattern.matcher(team).find()) && entry.method_2958() != GameMode.field_9219) continue;
                this.staff.put(entry, entry.method_2958() == GameMode.field_9219 ? StaffType.SPECTATE : StaffType.ACTIVE);
            }
        }
    };

    public StaffElement(Drag drag) {
        super("Staff", drag);
        Client.EVENTS.register(this);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        boolean shown = !this.staff.isEmpty() || StaffElement.mc.field_1755 instanceof ChatScreen;
        this.animation.setDirection(shown ? Direction.BACKWARDS : Direction.FORWARDS);
        float anim = 1.0f - this.animation.getOutput();
        if ((double)anim < 0.1) {
            return;
        }
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        system.push(this.x, this.y, this.width, this.height);
        float a = system.alpha();
        system.alpha(anim * a);
        this.drawBase(IconUse.PLAYER, "Staff", 0.6f, 0.0f);
        float off = 20.0f;
        float maxwidth = 100.0f;
        for (Map.Entry<PlayerListEntry, StaffType> entry : this.staff.entrySet()) {
            PlayerListEntry player = entry.getKey();
            StaffType type = entry.getValue();
            float ofx = 0.0f;
            if (player.method_2955() != null) {
                Client.RENDERER.text(player.method_2955().method_1144(), this.x + 22.0f, this.y + off + 2.0f, TextureUse.SFMEDIUM, 8.0f);
                ofx += Client.RENDERER.textWidth(StringHelper.method_15440((String)player.method_2955().method_1144().getString()), TextureUse.SFMEDIUM, 8.0f);
            }
            float tw = Client.RENDERER.textWidth(player.method_2966().getName(), TextureUse.SFMEDIUM, 8.0f);
            Client.RENDERER.text(player.method_2966().getName(), this.x + 26.0f + ofx, this.y + off + 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
            Identifier skin = player.method_52810().comp_1626();
            Client.RENDERER.texture(skin, this.x + 4.0f, this.y + off + 1.0f, 12.0f, 12.0f, 1.5f, new Vector4f(0.04f, 0.038f, 0.345f, 0.5f), new Vector4f(10.0f), Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
            Client.RENDERER.textCenteredStrict(IconUse.RENDER.glyph, this.x + 36.0f + ofx + tw, this.y + off + 6.5f, TextureUse.ICONS, 8.0f, type == StaffType.SPECTATE ? ClientColors.UI_RED : ClientColors.DARK_GRAY_COLOR);
            maxwidth = Math.max(maxwidth, 46.0f + ofx + tw);
            off += 16.0f;
        }
        this.drag.height = MathUtility.linearFps(this.drag.height, off, 15.0f);
        this.drag.width = MathUtility.linearFps(this.drag.width, maxwidth, 15.0f);
        system.alpha(a);
        system.pop();
    }

    static enum StaffType {
        ACTIVE,
        SPECTATE;

    }
}

