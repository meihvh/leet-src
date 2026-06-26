/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.Items
 *  net.minecraft.registry.Registries
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.api.ui.parts.impl.UITextField;
import im.leet.base.settings.impl.binder.BinderRenderer;
import im.leet.base.settings.impl.binder.BinderSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.TextUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class BinderWidget
extends UIWidget {
    private final BinderRenderer renderer;
    public boolean expanded = false;
    private final Drag drag = new Drag("Binder", () -> true);
    private final Map<Item, Rectangle> hitboxes = new HashMap<Item, Rectangle>();
    private ArrayList<ItemRenderer> renderers = new ArrayList();
    private Rectangle addButton = new Rectangle();
    private Rectangle cancelButton = new Rectangle();
    private UITextField search = new UITextField();
    private String searchText = "";
    private float scroll;
    private float scrollAnim;
    private SmoothStepAnimation swapAnim = new SmoothStepAnimation(300, 1.0);
    private boolean isAdding = false;

    public BinderWidget(BinderRenderer renderer) {
        this.renderer = renderer;
        this.search.setDesc("Search item...");
        this.search.setCallback(str -> {
            this.searchText = str;
        });
        for (Map.Entry<Identifier, Integer> entry : ((BinderSetting)this.renderer.getSetting()).getEntry()) {
            this.renderers.add(new ItemRenderer((Item)Registries.field_41178.method_63535(entry.getKey()), this.renderer));
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.drag.dragging) {
            this.x = (float)mouseX - this.drag.dX;
            this.y = (float)mouseY - this.drag.dY;
        }
        float pExpandAnim = 1.0f - this.animation.getOutput();
        float pSwapAnim = 1.0f - this.swapAnim.getOutput();
        Client.RENDERER.getStack().method_22903();
        MathUtility.scale(Client.RENDERER.getStack(), this.x + this.width / 2.0f, this.y + this.height / 2.0f, pExpandAnim * 0.5f + 0.5f);
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        float prevAlpha = system.alpha();
        system.alpha(pExpandAnim * prevAlpha);
        Client.RENDERER.rect(this.x, this.y, this.width, this.height, new Vector4f(6.0f), 1.0f, ClientColors.GUI_BACKGROUND, ClientColors.GUI_BACKGROUND, ClientColors.GUI_BACKGROUND, ClientColors.GUI_BACKGROUND);
        Client.RENDERER.outline(this.x, this.y, this.width, this.height, 1.0f, new Vector4f(6.0f), new Vector2f(1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Client.RENDERER.text(((BinderSetting)this.renderer.getSetting()).getName(), this.x + 6.0f, this.y + 6.0f, TextureUse.SFMEDIUM, 9.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.text(IconUse.CROSS, this.x + this.width - 16.0f, this.y + 6.0f, TextureUse.ICONS, 9.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.rect(this.x + 5.0f, this.y + 23.0f, this.width - 10.0f, 1.0f, new Vector4f(0.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
        system.push(this.x, this.y, this.width, this.height);
        Client.RENDERER.getStack().method_22903();
        Client.RENDERER.getStack().method_46416(-pSwapAnim * this.width * 2.0f, 0.0f, 0.0f);
        float scrollHeight = 0.0f;
        float off = 0.0f;
        system.push(this.x, this.y + 30.0f, this.width, this.height - 30.0f);
        for (ItemRenderer itemRenderer : this.renderers) {
            itemRenderer.bound(this.x, this.y + 30.0f + off - this.scrollAnim, this.width, 20.0f).render(mouseX, mouseY);
            off += itemRenderer.getHeight() + 4.0f;
        }
        scrollHeight = off;
        this.addButton.bound(this.x + 4.0f, this.y + 30.0f + off - this.scrollAnim, this.width - 8.0f, 20.0f);
        system.hatch(0.16f);
        Client.RENDERER.outline(this.addButton.getX(), this.addButton.getY(), this.addButton.getWidth(), this.addButton.getHeight(), 1.0f, new Vector4f(6.0f), new Vector2f(1.0f, 1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        system.hatch(0.0f);
        Client.RENDERER.textCentered("+", this.x + this.width / 2.0f - 2.0f, this.y + 27.0f + off - this.scrollAnim, TextureUse.SFMEDIUM, 20.0f, ClientColors.DARK_GRAY_COLOR);
        system.pop();
        Client.RENDERER.getStack().method_46416(pSwapAnim * this.width * 2.0f, 0.0f, 0.0f);
        Client.RENDERER.getStack().method_46416((1.0f - pSwapAnim) * this.width * 2.0f, 0.0f, 0.0f);
        if ((double)pSwapAnim > 0.1) {
            this.cancelButton.bound(this.x + 4.0f, this.y + this.height - 24.0f, this.width - 8.0f, 20.0f);
            Client.RENDERER.rect(this.cancelButton, new Vector4f(6.0f), 1.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
            Client.RENDERER.textCentered("Cancel", this.cancelButton.getX() + this.cancelButton.getWidth() / 2.0f - 1.0f, this.cancelButton.getY() + this.cancelButton.getHeight() / 2.0f - 4.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
            this.search.bound(this.x + 4.0f, this.y + 30.0f, this.width - 8.0f, 20.0f).render(mouseX, mouseY);
            Iterator iterator = Registries.field_41178.iterator();
            float off2 = 0.0f;
            Vector4f b = new Vector4f(0.0f);
            Vector4f c = new Vector4f(6.0f);
            system.push(this.x, this.y + 52.0f, this.width, this.height - 52.0f - 24.0f);
            float originAlpha = system.alpha();
            while (iterator.hasNext()) {
                Item item = (Item)iterator.next();
                if (item == Items.field_8162 || item instanceof BlockItem) continue;
                Rectangle rect = this.hitboxes.computeIfAbsent(item, k -> new Rectangle());
                if (!this.searchText.isEmpty() && !item.method_63680().getString().toLowerCase().replace(" ", "").contains(this.searchText.toLowerCase().replace(" ", ""))) {
                    rect.bound(-999.0f, -999.0f, 0.0f, 0.0f);
                    continue;
                }
                if (55.0f + off2 - this.scrollAnim < this.height && off2 - this.scrollAnim + 20.0f > 0.0f) {
                    rect.bound(this.x + 4.0f, this.y + 55.0f + off2 - this.scrollAnim, this.width - 8.0f, 20.0f);
                    system.alpha(originAlpha * 0.3f);
                    Client.RENDERER.rect(rect, c, 1.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
                    system.alpha(originAlpha);
                    Client.RENDERER.texture(item, rect.getX() + 4.0f, rect.getY() + 2.0f, 16.0f, 16.0f, 1.0f, b, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
                    Client.RENDERER.text(item.method_63680(), rect.getX() + 24.0f, rect.getY() + 5.0f, TextureUse.SFMEDIUM, 8.0f);
                }
                off2 += 24.0f;
            }
            if (this.isAdding) {
                scrollHeight = off2 - 20.0f;
            }
            system.pop();
        }
        Client.RENDERER.getStack().method_22909();
        system.pop();
        system.alpha(prevAlpha);
        Client.RENDERER.getStack().method_22909();
        this.swapAnim.setDirection(this.isAdding ? Direction.BACKWARDS : Direction.FORWARDS);
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 10.0f);
        this.scroll = MathHelper.method_15363((float)this.scroll, (float)0.0f, (float)scrollHeight);
        for (ItemRenderer renderer1 : this.renderers) {
            if (!renderer1.shouldRemove) continue;
            ((BinderSetting)this.renderer.getSetting()).bind(renderer1.item, -1);
        }
        this.renderers.removeIf(ItemRenderer::removed);
        if ((double)pExpandAnim < 0.1 && this.animation.getDirection() == Direction.FORWARDS) {
            this.shouldRemove = true;
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.shouldRemove) {
            return true;
        }
        boolean anyBinding = false;
        if (MathUtility.mouseIn(this.x, this.y, this.width, 20.0f, mouseX, mouseY)) {
            this.drag.dragging = true;
            this.drag.dX = (float)mouseX - this.x;
            this.drag.dY = (float)mouseY - this.y;
        }
        if (!this.isAdding) {
            if (this.addButton.hovered(mouseX, mouseY)) {
                this.isAdding = true;
                this.scroll = 0.0f;
                return true;
            }
            for (ItemRenderer itemRenderer : this.renderers) {
                anyBinding |= itemRenderer.isBinding();
                if (!itemRenderer.click(mouseX, mouseY, button)) continue;
                return true;
            }
        } else {
            if (this.cancelButton.hovered(mouseX, mouseY)) {
                this.isAdding = false;
                this.scroll = 0.0f;
                this.search.setText("");
                this.searchText = "";
                return true;
            }
            this.search.click(mouseX, mouseY, button);
            if (MathUtility.mouseIn(this.x, this.y + 52.0f, this.width, this.height - 62.0f, mouseX, mouseY)) {
                for (Map.Entry<Item, Rectangle> entry : this.hitboxes.entrySet()) {
                    if (!entry.getValue().hovered(mouseX, mouseY)) continue;
                    ((BinderSetting)this.renderer.getSetting()).bind(entry.getKey(), -1);
                    this.renderers.add(new ItemRenderer(entry.getKey(), this.renderer));
                    this.isAdding = false;
                    this.scroll = 0.0f;
                    this.search.setText("");
                    this.searchText = "";
                    return true;
                }
            }
        }
        if (!(this.hover(mouseX, mouseY) && !MathUtility.mouseIn(this.x + this.width - 16.0f, this.y + 6.0f, 9.0f, 9.0f, mouseX, mouseY) || anyBinding)) {
            this.animation.setDirection(Direction.FORWARDS);
            return true;
        }
        return true;
    }

    @Override
    public void release(int button) {
        super.release(button);
        this.search.release(button);
        this.drag.dragging = false;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        for (ItemRenderer itemRenderer : this.renderers) {
            itemRenderer.keyPressed(keyCode, scanCode, modifiers);
        }
        this.search.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        super.chartyped(ch, keyCode);
        this.search.chartyped(ch, keyCode);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.hover((int)mouseX, (int)mouseY)) {
            this.scroll -= (float)(verticalAmount * 10.0);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private static class ItemRenderer
    extends RendererObject {
        private Item item;
        private boolean binding = false;
        private BinderRenderer renderer;
        private boolean shouldRemove = false;
        private Rectangle bindbox = new Rectangle();

        public boolean removed() {
            return this.shouldRemove || ((BinderSetting)this.renderer.getSetting()).get(this.item) == -2;
        }

        public ItemRenderer(Item item, BinderRenderer renderer) {
            this.item = item;
            this.renderer = renderer;
        }

        @Override
        public void render(int mouseX, int mouseY) {
            CRenderSystem system = Client.RENDERER.getCrenderSystem();
            String textBind = this.binding ? "..." : TextUtility.keyToString(((BinderSetting)this.renderer.getSetting()).get(this.item));
            float textWidth = Math.max(20.0f, Client.RENDERER.textWidth(textBind, TextureUse.SFMEDIUM, 8.0f));
            float originAlpha = system.alpha();
            this.bindbox.bound(this.x + 4.0f, this.y, this.width - 8.0f, 20.0f);
            system.alpha(originAlpha * 0.3f);
            Client.RENDERER.rect(this.bindbox, new Vector4f(6.0f), 1.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
            system.alpha(originAlpha);
            Client.RENDERER.texture(this.item, this.x + 6.0f, this.y + 2.0f, 16.0f, 16.0f, 1.0f, new Vector4f(0.0f), Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
            Client.RENDERER.text(this.item.method_63680(), this.x + 26.0f, this.y + 5.0f, TextureUse.SFMEDIUM, 8.0f);
            Client.RENDERER.drawModuleRect(this.x + this.width - textWidth - 8.0f, this.y + 4.0f, textWidth, 12.0f);
            Client.RENDERER.textCentered(textBind, this.x + this.width - textWidth / 2.0f - 9.0f, this.y + 6.0f, TextureUse.SFMEDIUM, 6.0f, ClientColors.DARK_GRAY_COLOR);
        }

        @Override
        public boolean click(int mouseX, int mouseY, int button) {
            if (this.binding && button > 1) {
                ((BinderSetting)this.renderer.getSetting()).bind(this.item, button);
                this.binding = false;
                return true;
            }
            if (this.hover(mouseX, mouseY)) {
                if (button == 1) {
                    this.shouldRemove = true;
                    return true;
                }
                if (this.bindbox.hovered(mouseX, mouseY)) {
                    this.binding = true;
                }
            }
            return super.click(mouseX, mouseY, button);
        }

        @Override
        public void keyPressed(int keyCode, int scanCode, int modifiers) {
            if (this.binding) {
                if (keyCode == 259 || keyCode == 261 || keyCode == 256) {
                    keyCode = -1;
                }
                ((BinderSetting)this.renderer.getSetting()).bind(this.item, keyCode);
                this.binding = false;
            }
            super.keyPressed(keyCode, scanCode, modifiers);
        }

        public boolean isBinding() {
            return this.binding;
        }
    }
}

