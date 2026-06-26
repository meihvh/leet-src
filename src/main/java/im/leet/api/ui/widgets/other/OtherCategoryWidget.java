/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.other;

import im.leet.Client;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIPage;
import im.leet.api.ui.UIPageHandler;
import im.leet.api.ui.UIWidget;
import im.leet.api.ui.widgets.other.pages.ClientSettingsPage;
import im.leet.api.ui.widgets.other.pages.FriendPage;
import im.leet.api.ui.widgets.other.pages.MacrosPage;
import im.leet.base.modules.Category;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import java.awt.Color;
import org.joml.Vector4f;

public class OtherCategoryWidget
extends UIWidget {
    private Page currentPage = Page.values()[0];
    private float xAnim = 0.0f;
    private final UIPageHandler pageHandler = new UIPageHandler().register(new FriendPage(), new MacrosPage(), new ClientSettingsPage());

    @Override
    public void render(int mouseX, int mouseY) {
        if (!this.handling()) {
            return;
        }
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float prevAlpha = system.alpha();
        system.alpha(Category.OTHER.animation * prevAlpha);
        float widthPerPage = this.width / (float)Page.values().length - 1.0f;
        Client.RENDERER.rect(this.x, this.y, this.width - 2.0f, 15.0f, new Vector4f(6.0f, 6.0f, 6.0f, 6.0f), 1.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        this.xAnim = MathUtility.linearFps(this.xAnim, this.currentPage.ordinal(), (float)(Math.pow(this.xAnim, 2.0) + Math.pow(this.xAnim, 3.0)) / 1000.0f + 5.0f);
        float left = this.currentPage.ordinal() == 0 ? 6.0f : 0.0f;
        float right = this.currentPage.ordinal() == Page.values().length - 1 ? 6.0f : 0.0f;
        Client.RENDERER.rect(this.x - 0.5f + this.xAnim * widthPerPage, this.y - 0.5f, widthPerPage + 2.0f, 16.0f, new Vector4f(left, left, right, right), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
        for (Page page : Page.values()) {
            int ord = page.ordinal();
            page.animation = MathUtility.linearFps(page.animation, this.currentPage == page ? 1.0f : 0.0f, 10.0f);
            page.hitbox.bound(this.x + (float)ord * widthPerPage, this.y, widthPerPage + 0.5f, 15.0f);
            Color tColor = ColorUtility.linear(ClientColors.DARK_GRAY_COLOR, ClientColors.FORE_COLOR, page.animation);
            Client.RENDERER.textCentered(page.title, page.hitbox.getX() + widthPerPage / 2.0f, page.hitbox.getY() + 2.0f, TextureUse.SFMEDIUM, 8.0f, tColor);
        }
        Client.RENDERER.rect(this.x + 12.0f, this.y + 25.0f, this.width - 24.0f, 1.0f, new Vector4f(0.0f), 1.5f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        system.push(this.x, this.y, this.width, this.height);
        Client.RENDERER.getStack().method_22903();
        for (int i = 0; i < this.pageHandler.getPages().size(); ++i) {
            UIPage page = this.pageHandler.getPages().get(i);
            Page page1 = Page.values()[i];
            system.alpha(prevAlpha * page1.animation * Category.OTHER.animation);
            page.bound(this.x, this.y, this.width, this.height).render(mouseX, mouseY);
        }
        Client.RENDERER.getStack().method_22909();
        system.pop();
        system.alpha(prevAlpha);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (!this.handling()) {
            return false;
        }
        for (Page page : Page.values()) {
            if (!page.hitbox.hovered(mouseX, mouseY) || button != 0) continue;
            this.currentPage = page;
            return true;
        }
        this.pageHandler.getPages().get(this.currentPage.ordinal()).click(mouseX, mouseY, button);
        return this.hover(mouseX, mouseY);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.handling()) {
            return;
        }
        this.pageHandler.keyPressed(keyCode, scanCode, modifiers);
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void release(int button) {
        if (!this.handling()) {
            return;
        }
        this.pageHandler.release(button);
        super.release(button);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        if (!this.handling()) {
            return;
        }
        this.pageHandler.chartyped(ch, keyCode);
        super.chartyped(ch, keyCode);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!this.handling()) {
            return false;
        }
        return this.pageHandler.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private boolean handling() {
        return Client.CLICKGUI.getCurrent() == Category.OTHER;
    }

    static enum Page {
        FRIENDS("\u0414\u0440\u0443\u0437\u044c\u044f"),
        MACROS("\u041c\u0430\u043a\u0440\u043e\u0441\u044b"),
        SETTINGS("\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438");

        final String title;
        float animation = 0.0f;
        Rectangle hitbox = new Rectangle();

        private Page(String title) {
            this.title = title;
        }
    }
}

