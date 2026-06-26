/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.impl.util.StringUtil
 *  org.joml.Vector4f
 */
package im.leet.base.screens.ingame.objects;

import im.leet.Client;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.ui.widgets.impl.ModuleBindWidget;
import im.leet.api.ui.widgets.impl.ModuleSettingWidget;
import im.leet.base.modules.Module;
import im.leet.base.modules.Tag;
import im.leet.base.settings.SettingRenderer;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.EaseInOutQuad;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.Rectangle;
import im.leet.utils.math.TimeUtility;
import java.awt.Color;
import java.util.List;
import net.fabricmc.loader.impl.util.StringUtil;
import org.joml.Vector4f;

public class ModuleRenderer
extends RendererObject {
    public final Module module;
    public boolean expanded = false;
    public boolean binding = false;
    public boolean bindingScrollingDirection;
    public final List<SettingRenderer<?>> settingRenderers;
    public final EaseInOutQuad expandAnim = new EaseInOutQuad(300, 1.0);
    public final EaseInOutQuad enableAnim = new EaseInOutQuad(300, 1.0);
    public final SmoothStepAnimation bindingScrolling = new SmoothStepAnimation(1000, 1.0);
    public final SmoothStepAnimation nameScrolling = new SmoothStepAnimation(1000, 1.0);
    private TimeUtility nameScroll = new TimeUtility();
    public final ModuleSettingWidget settingWidget;
    Rectangle bindButton = new Rectangle();
    ModuleBindWidget bindWidget;

    public ModuleRenderer(Module module) {
        this.module = module;
        this.settingRenderers = module.getSettingRenderers();
        this.settingWidget = new ModuleSettingWidget(this);
        this.bindWidget = new ModuleBindWidget(this);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        String[] descWrapped = StringUtil.wrapLines((String)this.module.getDesc(), (int)25).split("\n");
        this.height = 20 + (this.module.getDesc().isEmpty() ? 0 : descWrapped.length * 10);
        float pEnableAnim = (float)(this.enableAnim.getEndPoint() - (double)this.enableAnim.getOutput());
        Color contrast = Color.WHITE;
        Color thumbColor = ColorUtility.linear(ClientColors.DARK_GRAY_COLOR, ClientColors.GREEN, pEnableAnim);
        Color thumbBackColor = ClientColors.GUI_STROKE;
        Client.RENDERER.outlined(this.x, this.y, this.width, this.height);
        Client.RENDERER.rect(this.x + this.width - 25.0f, this.y + this.height / 2.0f - 5.5f, 20.0f, 11.0f, new Vector4f(9.0f), 1.0f, thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
        Client.RENDERER.rect(this.x + this.width - 23.0f + 8.0f * pEnableAnim, this.y + this.height / 2.0f - 4.0f, 8.0f, 8.0f, new Vector4f(6.0f), 2.0f, thumbColor, thumbColor, thumbColor, thumbColor);
        String bind = this.binding ? "..." : (this.module.getKey() == -1 ? "None" : this.module.getKeyText());
        float tWidth = Client.RENDERER.textWidth(bind, TextureUse.SFMEDIUM, 6.0f) + 4.0f;
        boolean shouldScroll = tWidth > 25.0f;
        float offset = tWidth - 25.0f;
        this.bindButton.bound(this.x + this.width - 40.0f, this.y + this.height / 2.0f - 6.0f, 12.0f, 12.0f);
        Client.RENDERER.drawModuleRect(this.bindButton);
        Client.RENDERER.textCenteredStrict(IconUse.LINK.glyph, this.bindButton.getX() + this.bindButton.getWidth() / 2.0f - 0.25f, this.bindButton.getY() + this.bindButton.getHeight() / 2.0f - 0.25f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
        if (!this.settingRenderers.isEmpty()) {
            Client.RENDERER.drawModuleRect(this.x + this.width - 56.0f, this.y + this.height / 2.0f - 6.0f, 12.0f, 12.0f);
            Client.RENDERER.textCenteredStrict(IconUse.GEAR.glyph, this.x + this.width - 50.0f, this.y + this.height / 2.0f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
        }
        float cursorX = this.x + 8.0f;
        float nameY = this.y + 5.5f;
        float nameWidth = Client.RENDERER.textWidth(this.module.getName(), TextureUse.SFMEDIUM, 8.0f);
        Client.RENDERER.text(this.module.getName(), cursorX, nameY, TextureUse.SFMEDIUM, 8.0f, contrast);
        cursorX += nameWidth + 6.0f;
        if (!this.module.getTags().isEmpty()) {
            for (Tag tag : this.module.getTags()) {
                String tagText = tag.getName();
                float textWidth = Client.RENDERER.textWidth(tagText, TextureUse.SFMEDIUM, 6.0f);
                float tagWidth = textWidth + 6.0f;
                float tagHeight = 9.0f;
                float tagX = cursorX;
                float tagY = nameY + 0.5f;
                Client.RENDERER.rect(tagX - 3.0f, tagY, tagWidth, tagHeight, new Vector4f(4.5f), 2.0f, tag.getColor(), tag.getColor(), tag.getColor(), tag.getColor());
                Client.RENDERER.text(tagText, tagX - 1.0f, tagY + tagHeight / 2.0f - Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 6.0f) / 2.0f, TextureUse.SFMEDIUM, 6.0f, Color.WHITE);
                cursorX += tagWidth + 2.5f;
            }
        }
        for (int i = 0; i < descWrapped.length; ++i) {
            Client.RENDERER.text(descWrapped[i], this.x + 8.0f, this.y + 16.0f + (float)(i * 10), TextureUse.SFMEDIUM, 7.0f, ClientColors.DARK_GRAY_COLOR);
        }
        this.expandAnim.setDirection(this.expanded ? Direction.BACKWARDS : Direction.FORWARDS);
        this.expandAnim.setEndPoint(1.0);
        this.enableAnim.setDirection(this.module.isEnabled() ? Direction.BACKWARDS : Direction.FORWARDS);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        boolean hoverBind = this.bindButton.hovered(mouseX, mouseY);
        if (hoverBind && button == 1) {
            this.module.setKey(-1);
            return true;
        }
        if ((hoverBind || this.hover(mouseX, mouseY) && button == 2) && !Client.CLICKGUI.BINDING) {
            Client.CLICKGUI.WIDGETS.register(this.bindWidget);
            return true;
        }
        if (this.hover(mouseX, mouseY)) {
            if (button == 0) {
                this.module.setEnabled(!this.module.isEnabled());
            } else if (button == 1 && !this.module.getSettings().isEmpty()) {
                Client.CLICKGUI.setCurrentSetting(this);
                Client.CLICKGUI.getSettingAnim().setDirection(Direction.BACKWARDS);
            }
            return true;
        }
        return false;
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        super.chartyped(ch, keyCode);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.binding) {
            if (keyCode == 259 || keyCode == 256 || keyCode == 261) {
                keyCode = -1;
            }
            this.module.setKey(keyCode);
            this.binding = false;
            Client.CLICKGUI.BINDING = false;
        }
    }

    public boolean isExpanded() {
        return this.expanded;
    }
}

