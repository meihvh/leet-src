/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.PlayerListEntry
 *  net.minecraft.util.Nullables
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.drags.Drag;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.settings.EnumChoice;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.base.settings.impl.text.TextSetting;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.secure.UserProfile;
import java.util.function.Supplier;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Nullables;

public class WatermarkElement
extends HudElement {
    final MultiEnumSetting<Element> elements;
    final CheckBox useCustomName;
    final TextSetting customName;
    final CheckBox animate;
    float namewidth;
    int a1;
    String[] a2;
    TimeUtility time;

    public WatermarkElement(Drag drag) {
        super("Watermark", drag);
        this.elements = this.settings.multiEnumSetting("\u042d\u043b\u0435\u043c\u0435\u043d\u0442\u044b", new Element[]{Element.USERNAME, Element.FPS});
        this.useCustomName = this.settings.checkbox("Use custom name", false);
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = this.useCustomName::get;
        this.customName = (TextSetting)((TextSetting)this.settings.text("Custom name", "LEET").visible(supplierArray)).onChanged(it -> {
            try {
                this.namewidth = Client.RENDERER.textWidth((String)it, TextureUse.SFMEDIUM, 8.0f);
            }
            catch (Exception exception) {
                // empty catch block
            }
        });
        this.animate = (CheckBox)this.settings.checkbox("Animate", true).visible(() -> !this.useCustomName.get());
        this.namewidth = -1.0f;
        this.a2 = new String[]{"LEET", "1EET", "13ET", "133T", "1337", "L337", "LE37", "LEE7"};
        this.time = new TimeUtility();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        Client.RENDERER.drawHudRect(this.x, this.y, this.width, this.height);
        if (this.animate.get() && this.time.reached(500L, true)) {
            this.a1 = (this.a1 + 1) % this.a2.length;
        }
        Client.RENDERER.backIcon(IconUse.LOGO, this.x, this.y);
        String name = this.useCustomName.get() ? this.customName.getText() : (this.animate.get() ? this.a2[this.a1] : "LEET");
        Client.RENDERER.text(name, this.x + 20.0f, this.y + 5.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        if (this.useCustomName.get() && this.namewidth == -1.0f) {
            this.namewidth = Client.RENDERER.textWidth(this.customName.getText(), TextureUse.SFMEDIUM, 8.0f);
        }
        float mainoffs = this.useCustomName.get() ? this.namewidth + 25.0f : 50.0f;
        float offset = 0.0f;
        for (Element element : this.elements.get()) {
            String key = element.key;
            String value = element.getValue.get();
            if (value == null) continue;
            float tWidth = Client.RENDERER.textWidth(key, TextureUse.SFMEDIUM, 8.0f);
            float sWidth = Client.RENDERER.textWidth(value, TextureUse.SFMEDIUM, 8.0f);
            Client.RENDERER.text(key, this.x + mainoffs + offset, this.y + 5.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
            Client.RENDERER.text(value, this.x + tWidth + mainoffs + 5.0f + offset, this.y + 5.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
            offset += tWidth + sWidth + 15.0f;
        }
        this.drag.width = mainoffs + offset;
    }

    static enum Element implements EnumChoice
    {
        USERNAME("Username", "user", () -> UserProfile.USERNAME),
        UID("UID", "uid", () -> String.valueOf(UserProfile.UID)),
        FPS("FPS", "fps", () -> String.valueOf(MinecraftHolder.mc.method_47599())),
        SERVER_IP("IP \u0441\u0435\u0440\u0432\u0435\u0440\u0430", "ip", () -> (String)Nullables.method_49077((Object)MinecraftHolder.mc.method_1558(), info -> info.field_3761)),
        PING("\u041f\u0438\u043d\u0433", "ping", () -> {
            Integer latency = (Integer)Nullables.method_49077((Object)MinecraftHolder.mc.method_1562(), networkHandler -> (Integer)Nullables.method_49077((Object)networkHandler.method_2871(MinecraftHolder.mc.field_1724.method_5667()), PlayerListEntry::method_2959));
            return latency == null ? "-" : latency.toString();
        });

        final String renderName;
        final String key;
        final Supplier<String> getValue;

        private Element(String renderName, String key, Supplier<String> getValue) {
            this.renderName = renderName;
            this.key = key;
            this.getValue = getValue;
        }

        @Override
        public String getRenderName() {
            return this.renderName;
        }
    }
}

