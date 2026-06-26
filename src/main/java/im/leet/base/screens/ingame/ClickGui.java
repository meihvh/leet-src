/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.base.screens.ingame;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.drags.Drag;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventKey;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIStyle;
import im.leet.api.ui.UIWidget;
import im.leet.api.ui.particles.Particles2DEngine;
import im.leet.api.ui.parts.impl.UITextField;
import im.leet.api.ui.widgets.UIModuleWidget;
import im.leet.api.ui.widgets.impl.PanicWidget;
import im.leet.api.ui.widgets.impl.TextFieldWidget;
import im.leet.api.ui.widgets.other.OtherCategoryWidget;
import im.leet.base.modules.Category;
import im.leet.base.modules.Module;
import im.leet.base.screens.ingame.Widgets;
import im.leet.base.screens.ingame.objects.ModuleRenderer;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.secure.UserProfile;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ClickGui
extends RendererObject
implements MinecraftHolder {
    private boolean isOpened = false;
    public final Widgets WIDGETS = new Widgets();
    public boolean WRITING = false;
    public boolean BINDING = false;
    final Map<Category, ArrayList<ModuleRenderer>> renderers = new HashMap<Category, ArrayList<ModuleRenderer>>();
    Rectangle rect;
    Rectangle unhook;
    SmoothStepAnimation animation;
    public Drag drag;
    float anim1 = 0.0f;
    float anim2 = 0.0f;
    float dmx = 0.0f;
    float dmxl = 0.0f;
    Category current;
    float scroll;
    float scrollAnim;
    private ModuleRenderer hoveredModule = null;
    private final TimeUtility descScrollDelay = new TimeUtility();
    private final SmoothStepAnimation descScrollAnim = new SmoothStepAnimation(3500, 1.0);
    private String lastDescKey = "";
    String search = "";
    TimeUtility searchUpdate = new TimeUtility();
    UITextField textField = new UITextField();
    TextFieldWidget textWidget = new TextFieldWidget(this.textField);
    OtherCategoryWidget otherCategoryWidget = new OtherCategoryWidget();
    SmoothStepAnimation settingAnim = new SmoothStepAnimation(300, 1.0);
    ModuleRenderer currentSetting = null;
    PanicWidget panicWidget = new PanicWidget();
    private final ArrayList<ModuleRenderer> lastRendered = new ArrayList();
    private Particles2DEngine particles = new Particles2DEngine();
    EventBus<EventKey> eventKey = event -> {
        int key = event.getKey();
        if (key == ClientSettings.INSTANCE.clickguiBind.getBind()) {
            if (event.action == 0) {
                if (Client.IS_PANIC) {
                    return;
                }
                this.isOpened = true;
                ClickGui.mc.field_1729.method_1610();
            }
            event.cancel();
        } else if (key == 256 && this.isOpened()) {
            event.cancel();
            if ((double)(1.0f - this.settingAnim.getOutput()) > 0.2) {
                this.settingAnim.setDirection(Direction.FORWARDS);
                return;
            }
            if (event.action == 0) {
                this.isOpened = false;
                ClickGui.mc.field_1729.method_1612();
                this.settingAnim.setDirection(Direction.FORWARDS);
            }
        }
    };

    public ClickGui() {
        Client.EVENTS.register(this);
        this.rect = new Rectangle(0.0f, 0.0f, 200.0f, 200.0f).center((float)window.method_4486() / 2.0f, (float)window.method_4502() / 2.0f);
        this.unhook = new Rectangle();
        this.animation = new SmoothStepAnimation(200, 1.0);
        this.drag = new Drag("ClickGui", () -> this.isOpened);
        this.drag.bound(this.rect);
        this.current = Category.values()[0];
        this.textField.setDesc("\u041f\u043e\u0438\u0441\u043a");
        this.textField.style = UIStyle.OUTLINED;
        this.textField.setIcon(IconUse.SEARCH);
        this.WIDGETS.register(this.textWidget);
        this.WIDGETS.register(this.otherCategoryWidget);
    }

    public void panicStarted() {
        this.panicWidget.timer = -1;
        this.panicWidget.open = false;
    }

    public void RELOAD() {
        this.renderers.clear();
        for (Category category : Category.values()) {
            ArrayList<ModuleRenderer> rendererList = this.renderers.get((Object)category);
            if (rendererList == null) {
                rendererList = new ArrayList();
            }
            for (Module module : Client.MODULES.get(category)) {
                rendererList.add(new ModuleRenderer(module));
            }
            this.renderers.put(category, rendererList);
        }
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        for (UIWidget UIWidget2 : this.WIDGETS.get().reversed()) {
            if (!UIWidget2.click(mouseX, mouseY, button)) continue;
            return true;
        }
        if (this.unhook.hovered(mouseX, mouseY) && button == 0) {
            this.panicWidget.open = true;
            this.panicWidget.timer = 5;
            this.WIDGETS.register(this.panicWidget);
            return true;
        }
        for (Category category : Category.values()) {
            if (!MathUtility.mouseIn(category.rect.getX(), category.rect.getY(), category.rect.getWidth(), category.rect.getHeight(), mouseX, mouseY)) continue;
            this.current = category;
            this.settingAnim.setDirection(Direction.FORWARDS);
            this.scroll = 0.0f;
            this.textField.setText("");
            return true;
        }
        if (MathUtility.mouseIn(this.drag.x + 90.0f, this.drag.y + 42.0f, this.drag.width - 90.0f, this.drag.height - 42.0f, mouseX, mouseY)) {
            for (ModuleRenderer renderer : this.getModules()) {
                if (!renderer.click(mouseX, mouseY, button)) continue;
                return true;
            }
        }
        if (MathUtility.mouseIn(this.drag.x, this.drag.y, this.drag.width, this.drag.height, mouseX, mouseY)) {
            this.drag.dX = (float)mouseX - this.drag.x;
            this.drag.dY = (float)mouseY - this.drag.y;
            this.drag.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public void release(int button) {
        this.WIDGETS.release(button);
        for (ModuleRenderer renderer : this.getModules()) {
            renderer.release(button);
        }
        if (this.drag.dragging) {
            this.drag.dragging = false;
            return;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (UIWidget UIWidget2 : this.WIDGETS.get().reversed()) {
            if (!UIWidget2.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) continue;
            return true;
        }
        this.scroll += (float)verticalAmount * 10.0f;
        return true;
    }

    @Override
    public void mouseDragged(int mouseX, int mouseY) {
        if (this.drag.dragging) {
            return;
        }
        for (UIWidget UIWidget2 : this.WIDGETS.get().reversed()) {
            UIWidget2.mouseDragged(mouseX, mouseY);
        }
        for (ModuleRenderer renderer : this.getModules()) {
            renderer.mouseDragged(mouseX, mouseY);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (UIWidget UIWidget2 : this.WIDGETS.get().reversed()) {
            UIWidget2.keyPressed(keyCode, scanCode, modifiers);
        }
        for (ModuleRenderer renderer : this.getModules()) {
            renderer.keyPressed(keyCode, scanCode, modifiers);
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void chartyped(char ch, int keyCode) {
        for (UIWidget UIWidget2 : this.WIDGETS.get().reversed()) {
            UIWidget2.chartyped(ch, keyCode);
        }
        for (ModuleRenderer renderer : this.getModules()) {
            renderer.chartyped(ch, keyCode);
        }
        super.chartyped(ch, keyCode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void render(int mouseX, int mouseY) {
        Object r;
        Client.RENDERER.getCrenderSystem().layer(CRenderSystem.RenderLayer.OVERLAY);
        this.drag.width = 480.0f;
        this.drag.height = ClientSettings.INSTANCE.clickguiHeight.get();
        if (this.drag.dragging) {
            this.drag.x = MathUtility.linear(this.drag.x, (float)mouseX - this.drag.dX, 0.5f);
            this.drag.y = MathUtility.linear(this.drag.y, (float)mouseY - this.drag.dY, 0.5f);
        }
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 10.0f);
        this.animation.setDirection(this.isOpened ? Direction.BACKWARDS : Direction.FORWARDS);
        float originAnim = this.animation.getOutput();
        float anim = 1.0f - originAnim;
        if ((double)anim < 0.1) {
            return;
        }
        float pAnim = 1.0f - this.settingAnim.getOutput();
        if (this.currentSetting != null) {
            boolean settingsClosed;
            boolean bl = settingsClosed = this.settingAnim.isDone() && this.settingAnim.getDirection() == Direction.FORWARDS;
            if (settingsClosed) {
                this.WIDGETS.unregister(this.currentSetting.settingWidget);
                this.currentSetting = null;
            }
        }
        MatrixStack stack = Client.RENDERER.getStack();
        Client.RENDERER.getCrenderSystem().alpha(anim);
        stack.method_22903();
        MathUtility.scale(stack, this.drag.x + this.drag.width / 2.0f, this.drag.y + this.drag.height / 2.0f, anim * 0.1f + 0.9f);
        Client.RENDERER.rect(this.drag.x, this.drag.y, this.drag.width, this.drag.height, new Vector4f(6.0f), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
        Color logoColor = ClientSettings.INSTANCE.getColorBright(0);
        Color logoColor2 = ClientSettings.INSTANCE.getColorBright(90);
        Client.RENDERER.text(IconUse.LOGO, this.drag.x + 33.0f, this.drag.y + 8.0f, TextureUse.ICONS, 17.0f, logoColor2, logoColor, logoColor, logoColor2);
        Client.RENDERER.text(IconUse.PLAYER, this.drag.x + 90.0f, this.drag.y + 14.0f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.text(UserProfile.USERNAME, this.drag.x + 100.0f, this.drag.y + 13.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        Client.RENDERER.rect(this.drag.x + 15.0f, this.drag.y + 32.0f, this.drag.width - 30.0f, 0.5f, new Vector4f(0.0f), 0.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Client.RENDERER.getCrenderSystem().alpha(anim);
        if (this.current != Category.OTHER) {
            boolean searching;
            float offsetX = 0.0f;
            float offsetY = 0.0f;
            float offsetY2 = 0.0f;
            float defaultWidth = 135.0f;
            Client.RENDERER.getCrenderSystem().alpha(this.current.animation * anim);
            Client.RENDERER.getCrenderSystem().push(this.drag.x + 90.0f, this.drag.y + 42.0f, this.drag.width - 90.0f - 90.0f * this.animation.getOutput(), this.drag.height - 43.0f);
            r = this.getModules();
            boolean bl = searching = !this.textField.getText().isEmpty();
            if (!((ArrayList)r).isEmpty()) {
                Category lastCategory = null;
                Iterator<ModuleRenderer> iterator = ((ArrayList)r).iterator();
                while (iterator.hasNext()) {
                    ModuleRenderer renderer = iterator.next();
                    if (searching && (lastCategory == null || lastCategory != renderer.module.getCategory())) {
                        float mx;
                        lastCategory = renderer.module.getCategory();
                        float wd = Client.RENDERER.textWidth(lastCategory.getName(), TextureUse.SFMEDIUM, 8.0f);
                        float yt = this.drag.y + 44.0f + this.scrollAnim + Math.max(offsetY2, offsetY);
                        float wt = this.drag.width - 100.0f;
                        Client.RENDERER.rect(this.drag.x + 100.0f - pAnim * this.drag.width, yt + 5.0f, (wt - 10.0f) / 2.0f - wd / 2.0f - 10.0f, 0.8f, new Vector4f(0.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
                        Client.RENDERER.textCentered(lastCategory.getName(), this.drag.x + 90.0f + wt / 2.0f - pAnim * this.drag.width, yt, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
                        Client.RENDERER.rect(this.drag.x + 100.0f + (wt - 10.0f) / 2.0f + wd / 2.0f + 4.0f - pAnim * this.drag.width, yt + 5.0f, (wt - 10.0f) / 2.0f - wd / 2.0f - 10.0f, 0.8f, new Vector4f(0.0f), 1.0f, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR, ClientColors.DARK_GRAY_COLOR);
                        offsetY = mx = Math.max(offsetY, offsetY2) + 20.0f;
                        offsetY2 = mx;
                        offsetX = 0.0f;
                    }
                    boolean rightSide = offsetX > 0.0f;
                    float offset = 80.0f * originAnim * (float)(rightSide ? 1 : -1);
                    renderer.bound(this.drag.x + 90.0f + offsetX - pAnim * this.drag.width, this.drag.y + 42.0f + this.scrollAnim + (rightSide ? offsetY2 : offsetY), (this.drag.width - 100.0f) / 2.0f, 20.0f);
                    stack.method_22903();
                    stack.method_46416(renderer.getX() + renderer.getWidth() / 2.0f, renderer.getY(), 0.0f);
                    stack.method_22905(1.0f, this.current.animation, 1.0f);
                    stack.method_46416(-(renderer.getX() + renderer.getWidth() / 2.0f), -renderer.getY(), 0.0f);
                    renderer.render(mouseX, mouseY);
                    stack.method_22909();
                    offsetX += renderer.getWidth() + 5.0f;
                    if (rightSide) {
                        offsetY2 += renderer.getHeight() + 5.0f;
                    } else {
                        offsetY += renderer.getHeight() + 5.0f;
                    }
                    if (!(offsetX > this.drag.width - 100.0f)) continue;
                    offsetX = 0.0f;
                }
                float of = Math.max(offsetY, offsetY2);
                this.scroll = MathHelper.method_15363((float)this.scroll, (float)(-of + Math.min(of, this.drag.height - 70.0f)), (float)0.0f);
            } else {
                Client.RENDERER.textCentered(this.textField.getText().isEmpty() ? "\u041f\u043e\u043a\u0430 \u0447\u0442\u043e \u0442\u0443\u0442 \u043d\u0438\u0447\u0435\u0433\u043e \u043d\u0435\u0442 :(" : "\u041f\u043e\u0445\u043e\u0436\u0435, \u043d\u0438\u0447\u0435\u0433\u043e \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e...", this.drag.x + 65.0f + (this.drag.width - 65.0f) / 2.0f, this.drag.y + this.drag.height / 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
            }
            Client.RENDERER.getCrenderSystem().pop();
            Client.RENDERER.getCrenderSystem().alpha(anim);
        }
        if (this.currentSetting != null) {
            this.currentSetting.settingWidget.bound(this.drag.x + 90.0f, this.drag.y + 35.0f, this.drag.width - 90.0f, this.drag.height - 35.0f);
            if ((double)pAnim > 0.1) {
                this.WIDGETS.register(this.currentSetting.settingWidget);
            } else {
                this.WIDGETS.unregister(this.currentSetting.settingWidget);
                for (UIWidget widget : this.WIDGETS.get()) {
                    if (!(widget instanceof UIModuleWidget)) continue;
                    this.WIDGETS.unregister(widget);
                }
            }
        }
        this.textWidget.bound(this.drag.x + 6.0f, this.drag.y + 42.0f, 80.0f, 18.0f);
        this.unhook.bound(this.drag.x + 6.0f, this.drag.y + this.drag.height - 24.0f, 70.0f, 18.0f);
        Client.RENDERER.drawModuleRect(this.unhook);
        Client.RENDERER.outline(this.unhook.getX(), this.unhook.getY(), this.unhook.getWidth(), this.unhook.getHeight(), 0.0f, new Vector4f(7.0f), new Vector2f(1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Client.RENDERER.text("Panic mode", this.unhook.getX() + 16.0f, this.unhook.getY() + this.unhook.getHeight() / 2.0f - 5.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.DARK_GRAY_COLOR);
        Client.RENDERER.text(IconUse.EXIT, this.unhook.getX() + 4.0f, this.unhook.getY() + this.unhook.getHeight() / 2.0f - 5.0f, TextureUse.ICONS, 8.0f, ClientColors.DARK_GRAY_COLOR);
        this.panicWidget.bound(this.drag.x, this.drag.y, this.drag.width, this.drag.height);
        this.otherCategoryWidget.bound(this.drag.x + 90.0f, this.drag.y + 35.0f, this.drag.width - 90.0f, this.drag.height - 35.0f);
        this.particles.render();
        for (Category category : Category.values()) {
            r = category.rect;
            float yoffset = 22.0f;
            ((Rectangle)r).bound(this.drag.x + 8.0f, this.drag.y + 76.0f + (float)category.ordinal() * yoffset, 80.0f, 18.0f);
        }
        Color color = ClientSettings.INSTANCE.getColor(0);
        Color color2 = ClientSettings.INSTANCE.getColor(90);
        this.anim1 = MathUtility.linearFps(this.anim1, this.current.rect.getY() - this.drag.y, 5.0f);
        this.anim2 = MathUtility.linearFps(this.anim2, Client.RENDERER.textWidth(this.current.getName(), TextureUse.SFMEDIUM, 8.0f), 5.0f);
        float p = Client.RENDERER.getCrenderSystem().alpha();
        int i = 0;
        while ((float)i < this.anim2 + 12.0f) {
            this.particles.addParticle(new Vector2f(this.current.rect.getX() + (float)i + 8.0f, this.current.rect.getY() + this.current.rect.getHeight() / 2.0f - 1.0f), new Vector2f(MathUtility.deltaTime() * 60.0f), MathUtility.random(0, 360), (float)Math.sin((double)System.currentTimeMillis() / 1000.0), 150L);
            i += 4;
        }
        float sum = 0.0f;
        for (Category category : Category.values()) {
            if (category == this.current) continue;
            sum += category.animation;
        }
        for (Category category : Category.values()) {
            Rectangle r2 = category.rect;
            float off = category == Category.PLAYER ? 1.5f : 0.0f;
            category.animation = MathUtility.linearFps(category.animation, this.current == category && (double)sum < 0.2 ? 1.0f : 0.0f, 10.0f);
            Client.RENDERER.text(category.getIcon(), r2.getX() + off + 2.0f, r2.getY() + 2.0f, TextureUse.ICONS, 10.0f, ClientColors.ICON_FOREGROUND_COLOR);
            Client.RENDERER.text(category.getName(), r2.getX() + 16.0f, r2.getY() + 3.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.ICON_FOREGROUND_COLOR);
        }
        Widgets widgets = this.WIDGETS;
        synchronized (widgets) {
            this.WIDGETS.render(mouseX, mouseY);
        }
        Client.RENDERER.getCrenderSystem().alpha(1.0f);
        stack.method_22909();
        if (this.drag.dragging) {
            this.dmx = mouseX;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ArrayList<ModuleRenderer> getModules() {
        ArrayList<ModuleRenderer> base = new ArrayList<ModuleRenderer>((Collection)this.renderers.get((Object)this.current));
        if (!this.textField.getText().isEmpty()) {
            if (this.searchUpdate.reached(50L, true)) {
                ArrayList<ModuleRenderer> arrayList = this.lastRendered;
                synchronized (arrayList) {
                    this.lastRendered.clear();
                    base.clear();
                    for (Category category : this.renderers.keySet()) {
                        base.addAll((Collection<ModuleRenderer>)this.renderers.get((Object)category));
                    }
                    for (ModuleRenderer renderer : base) {
                        String name = renderer.module.getName();
                        if (!this.shouldPromptSearch(name)) continue;
                        this.lastRendered.add(renderer);
                    }
                    this.lastRendered.sort(Comparator.comparingInt(d -> d.module.getCategory().ordinal()));
                }
            }
            return this.lastRendered;
        }
        return base;
    }

    private boolean shouldPromptSearch(String name) {
        return name.toLowerCase().replace(" ", "").contains(this.textField.getText().toLowerCase().replace(" ", ""));
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    public void setOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    public Category getCurrent() {
        return this.current;
    }

    public TimeUtility getSearchUpdate() {
        return this.searchUpdate;
    }

    public UITextField getTextField() {
        return this.textField;
    }

    public TextFieldWidget getTextWidget() {
        return this.textWidget;
    }

    public SmoothStepAnimation getSettingAnim() {
        return this.settingAnim;
    }

    public void setCurrentSetting(ModuleRenderer currentSetting) {
        this.currentSetting = currentSetting;
    }

    public ModuleRenderer getCurrentSetting() {
        return this.currentSetting;
    }
}

