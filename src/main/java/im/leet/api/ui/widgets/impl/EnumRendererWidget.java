/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.base.settings.impl.enumsetting.EnumRenderer;
import im.leet.base.settings.impl.enumsetting.EnumSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientColors;
import im.leet.utils.lang.LangUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class EnumRendererWidget
extends UIWidget {
    EnumRenderer<?> parent;
    float scroll;
    float scrollAnim;

    public EnumRendererWidget(EnumRenderer<?> parent) {
        this.parent = parent;
        this.animation.setDirection(Direction.BACKWARDS);
        this.animation.reset();
    }

    @Override
    public void opened() {
        this.animation.setDirection(Direction.FORWARDS);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f(4.0f), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
        Client.RENDERER.outline(this.x, this.y, this.width, this.height, 0.0f, new Vector4f(4.0f), new Vector2f(1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Client.RENDERER.text(LangUtility.getEnumChoiceName(((EnumSetting)this.parent.getSetting()).get()), this.x + 4.0f, this.y + 3.5f, TextureUse.SFMEDIUM, 7.0f, ClientColors.FORE_COLOR);
        float offY = 15.0f;
        float out = this.animation.getOutput();
        system.push(this.x, this.y + 15.0f, this.width, this.height - 15.0f);
        for (EnumRenderer.EnumObject<?> o : this.parent.getObjects()) {
            o.bound(this.x, this.y + offY + this.scrollAnim * out, this.width, 15.0f).render(mouseX, mouseY);
            o.param(Color.RED, ((EnumSetting)this.parent.getSetting()).is(((Enum)o.value).ordinal()));
            offY += o.getHeight();
        }
        system.pop();
        this.height = Math.min(140.0f, Math.max(15.0f, 15.0f + (offY - 15.0f) * out));
        if (this.animation.getDirection() == Direction.BACKWARDS && (double)out < 0.1) {
            this.shouldRemove = true;
        }
        this.scroll = MathHelper.method_15363((float)this.scroll, (float)(-offY + this.height), (float)0.0f);
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 15.0f);
    }

    public boolean canRender() {
        return this.animation.getDirection() == Direction.FORWARDS && (double)this.animation.getOutput() > 0.1;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.hover((int)mouseX, (int)mouseY)) {
            this.scroll += (float)(verticalAmount * 10.0);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (!this.hover(mouseX, mouseY) && this.animation.getDirection() == Direction.FORWARDS) {
            this.animation.setDirection(Direction.BACKWARDS);
            return true;
        }
        for (EnumRenderer.EnumObject<?> o : this.parent.getObjects()) {
            if (!o.hover(mouseX, mouseY)) continue;
            ((EnumSetting)this.parent.getSetting()).select(((Enum)o.value).ordinal());
            break;
        }
        return true;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        this.animation.setDirection(Direction.BACKWARDS);
    }
}

