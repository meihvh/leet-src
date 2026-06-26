/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.impl.util.StringUtil
 */
package im.leet.base.settings;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.TextureUse;
import im.leet.base.settings.Setting;
import im.leet.utils.animations.impl.ElasticAnimation;
import im.leet.utils.client.ClientColors;
import net.fabricmc.loader.impl.util.StringUtil;

public abstract class SettingRenderer<T extends Setting<?>>
extends RendererObject {
    protected T setting;
    public ElasticAnimation visible = new ElasticAnimation(500, 1.0, 8.0f, 4.0f, false);

    public SettingRenderer(T setting) {
        this.setting = setting;
    }

    protected float drawDesc(float yOffset) {
        return this.drawDesc(yOffset, 12.0f, 20);
    }

    protected float drawDesc(float yOffset, float lineY, int chars) {
        if (!((Setting)this.setting).getDesc().isEmpty()) {
            float off = 0.0f;
            for (String line : StringUtil.wrapLines((String)((Setting)this.setting).getDesc(), (int)chars).split("\n")) {
                Client.RENDERER.text(line, this.x + 2.0f, this.y + lineY + off + yOffset, TextureUse.SFMEDIUM, 7.0f, ClientColors.DARK_GRAY_COLOR);
                off += 8.0f;
            }
            return off + yOffset;
        }
        return yOffset;
    }

    public T getSetting() {
        return this.setting;
    }
}

