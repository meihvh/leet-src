/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.api.ui.widgets.other.pages;

import im.leet.Client;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIPage;
import im.leet.base.settings.Setting;
import im.leet.base.settings.SettingRenderer;
import im.leet.utils.animations.Direction;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.MathUtility;
import java.util.List;
import net.minecraft.util.math.MathHelper;

public class ClientSettingsPage
extends UIPage {
    private final List<SettingRenderer<?>> settingRenderers = ClientSettings.INSTANCE.getSettingRenderers();
    private float scroll = 0.0f;
    private float scrollAnim;

    @Override
    public void render(int mouseX, int mouseY) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        system.push(this.x, this.y + 27.0f, this.width, this.height - 27.0f);
        float offsetY1 = 0.0f;
        float offsetY2 = 0.0f;
        float offsetX = 0.0f;
        for (SettingRenderer<?> settingRenderer : this.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            boolean isRight = offsetX > 0.0f;
            settingRenderer.bound(this.x + 8.0f + offsetX, this.y + 28.0f + (isRight ? offsetY2 : offsetY1) + this.scrollAnim, this.width / 2.0f - 16.0f, settingRenderer.getHeight()).render(mouseX, mouseY);
            offsetX += settingRenderer.getWidth() + 12.0f;
            if (!isRight) {
                offsetY1 += settingRenderer.getHeight();
            } else {
                offsetY2 += settingRenderer.getHeight();
            }
            if (!(offsetX > this.width / 2.0f)) continue;
            offsetX = 0.0f;
        }
        system.pop();
        this.scroll = MathHelper.method_15363((float)this.scroll, (float)(-(offsetY1 + offsetY2) + 18.0f), (float)0.0f);
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 10.0f);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (MathUtility.mouseIn(this.x + 6.0f, this.y + 6.0f, 9.0f, 9.0f, mouseX, mouseY)) {
            Client.CLICKGUI.getSettingAnim().setDirection(Direction.FORWARDS);
            return true;
        }
        for (SettingRenderer<?> settingRenderer : this.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue() || !settingRenderer.click(mouseX, mouseY, button)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (SettingRenderer<?> settingRenderer : this.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (SettingRenderer<?> settingRenderer : this.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void release(int button) {
        for (SettingRenderer<?> settingRenderer : this.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.release(button);
        }
        super.release(button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scroll += (float)verticalAmount * 10.0f;
        return true;
    }

    @Override
    public void mouseDragged(int mouseX, int mouseY) {
        for (SettingRenderer<?> settingRenderer : this.settingRenderers) {
            if (!((Setting)settingRenderer.getSetting()).getVisible().get().booleanValue()) continue;
            settingRenderer.mouseDragged(mouseX, mouseY);
        }
        super.mouseDragged(mouseX, mouseY);
    }
}

