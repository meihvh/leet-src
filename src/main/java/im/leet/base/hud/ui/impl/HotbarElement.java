/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.hud.bar.Bar
 *  net.minecraft.client.gui.hud.bar.ExperienceBar
 *  net.minecraft.client.gui.hud.bar.JumpBar
 *  net.minecraft.client.gui.hud.bar.LocatorBar
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.render.RenderTickCounter
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 *  net.minecraft.util.Arm
 *  net.minecraft.util.Formatting
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.api.render.ClientRenderer;
import im.leet.api.render.system.TextureUse;
import im.leet.base.hud.ui.HudElement;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.lang.runtime.SwitchBootstraps;
import java.util.Objects;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.gui.hud.bar.JumpBar;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class HotbarElement
extends HudElement {
    public static final HotbarElement INSTANCE = new HotbarElement();
    static float slotChangeAnim = 0.0f;
    static final float round = 13.0f;
    static final float roundSmall = 5.0f;
    static final float smooth = 1.0f;
    static final float slotSize = 24.0f;
    static final float slotOffset = 2.0f;
    static final float hotbarWidth = 198.0f;
    static final float hotbarHeight = 22.0f;
    static final float hotbarOffset = 28.0f;
    static final float itemOffset = -4.0f;
    static final float statusHeight = 8.0f;
    static final float statusOffset = 2.0f;
    static final float statusCenterOffset = 30.0f;
    static final float statusWidth = 84.0f;
    static final float statusTextSize = 7.0f;
    static final float expOffset = 12.0f;
    static final float expHeight = 8.0f;
    static final float expTextSize = 7.0f;
    static float expAnim = 0.0f;
    static float[] statusAnims = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    static final float tooltipOffset = 6.0f;
    static final float tooltipTextSize = 8.0f;
    static final float tooltipPadding = 2.0f;

    private HotbarElement() {
        super("Hotbar", null);
    }

    @Override
    public void render(int mouseX, int mouseY) {
    }

    public static void renderHotbar(DrawContext context, RenderTickCounter tickCounter) {
        int slot = HotbarElement.mc.field_1724.method_31548().method_67532();
        slotChangeAnim = MathUtility.linear(slotChangeAnim, slot, 0.41f);
        float halfWidth = (float)mc.method_22683().method_4486() / 2.0f;
        float x = halfWidth - 99.0f;
        float y = (float)mc.method_22683().method_4502() - 28.0f;
        ClientRenderer r = Client.RENDERER;
        ClientPlayerEntity player = HotbarElement.mc.field_1724;
        Color backgroundColor = ColorUtility.injectAlpha(ClientColors.BACK_COLOR, 128.0f);
        Color c1 = ColorUtility.transfusionEffect(15, 0, ClientColors.FORE_COLOR, ClientColors.SECONDARY_FORE_COLOR);
        r.rect(x, y, 198.0f, 22.0f, new Vector4f(13.0f), 1.0f, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
        r.outline(halfWidth - 99.0f + 22.0f * slotChangeAnim - 1.0f, y - 1.0f, 24.0f, 24.0f, 2.0f, new Vector4f(13.0f), new Vector2f(1.0f), c1, c1, c1, c1);
        ItemStack offhandStack = player.method_6079();
        Arm offhand = player.method_6068().method_5928();
        if (!offhandStack.method_7960()) {
            r.rect(offhand == Arm.field_6182 ? halfWidth - 99.0f - 28.0f : halfWidth + 99.0f + 4.0f, y, 22.0f, 22.0f, new Vector4f(13.0f), 1.0f, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
        }
        int l = 1;
        for (int m = 0; m < 9; ++m) {
            float n = halfWidth - 99.0f + (float)m * 22.0f + 4.0f;
            float o = (float)mc.method_22683().method_4502() - 28.0f - -4.0f;
            HotbarElement.renderHotbarItem(context, n, o, tickCounter, player.method_31548().method_5438(m), l++);
        }
        if (!offhandStack.method_7960()) {
            float m = (float)mc.method_22683().method_4502() - 28.0f - -4.0f;
            if (offhand == Arm.field_6182) {
                HotbarElement.renderHotbarItem(context, halfWidth - 99.0f - 24.0f - 2.0f, m, tickCounter, offhandStack, l++);
            } else {
                HotbarElement.renderHotbarItem(context, halfWidth + 99.0f + 2.0f + 4.0f, m, tickCounter, offhandStack, l++);
            }
        }
    }

    private static void renderHotbarItem(DrawContext context, float x, float y, RenderTickCounter tickCounter, ItemStack stack, int seed) {
        if (!stack.method_7960()) {
            float f = (float)stack.method_7965() - tickCounter.method_60637(false);
            if (f > 0.0f) {
                float g = 1.0f + f / 5.0f;
                context.method_51448().pushMatrix();
                context.method_51448().translate(x + 8.0f, y + 12.0f);
                context.method_51448().scale(1.0f / g, (g + 1.0f) / 2.0f);
                context.method_51448().translate(-(x + 8.0f), -(y + 12.0f));
            }
            context.method_51423((LivingEntity)HotbarElement.mc.field_1724, stack, (int)x, (int)y, seed);
            if (f > 0.0f) {
                context.method_51448().popMatrix();
            }
            context.method_51431(HotbarElement.mc.field_1772, stack, (int)x, (int)y);
        }
    }

    public static void renderBar(Bar bar, DrawContext context, RenderTickCounter tickCounter) {
        Bar bar2 = bar;
        Objects.requireNonNull(bar2);
        Bar bar3 = bar2;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{ExperienceBar.class, LocatorBar.class, JumpBar.class}, (Object)bar3, n)) {
            case 0: {
                ExperienceBar ignored = (ExperienceBar)bar3;
                float x = ((float)mc.method_22683().method_4486() - 198.0f) / 2.0f;
                float y = (float)mc.method_22683().method_4502() - 28.0f - 12.0f;
                Color backgroundColor = ColorUtility.injectAlpha(ClientColors.BACK_COLOR, 128.0f);
                Client.RENDERER.rect(x, y, 198.0f, 8.0f, new Vector4f(5.0f), 1.0f, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
                float nextLevelExperience = HotbarElement.mc.field_1724.method_7349();
                expAnim = MathUtility.linear(expAnim, HotbarElement.mc.field_1724.field_7510, 0.5f);
                if (nextLevelExperience > 0.0f) {
                    float l = expAnim * 198.0f;
                    Color enabledColor = ClientColors.ENABLED;
                    if (l > 0.0f) {
                        Client.RENDERER.rect(x, y, l, 8.0f, new Vector4f(5.0f), 1.0f, enabledColor, enabledColor, enabledColor, enabledColor);
                    }
                }
                String text = String.valueOf(HotbarElement.mc.field_1724.field_7520);
                float textWidth = Client.RENDERER.textWidth(text, TextureUse.SFMEDIUM, 7.0f);
                Client.RENDERER.text(text, x + Math.max(1.0f, 198.0f * expAnim - textWidth - 2.0f), y - 4.0f + 4.0f, TextureUse.SFMEDIUM, 7.0f, expAnim > 0.01f ? ClientColors.BACK_COLOR : ClientColors.FORE_COLOR);
                break;
            }
            case 1: {
                LocatorBar locator = (LocatorBar)bar3;
                locator.method_70865(context, tickCounter);
                break;
            }
            case 2: {
                JumpBar jump = (JumpBar)bar3;
                jump.method_70865(context, tickCounter);
                break;
            }
            default: {
                bar.method_70865(context, tickCounter);
            }
        }
    }

    public static float renderStatusBars(DrawContext context) {
        float y;
        float halfWidth = (float)mc.method_22683().method_4486() / 2.0f;
        float x = halfWidth - 99.0f;
        float yl = y = (float)mc.method_22683().method_4502() - 28.0f - 22.0f - 2.0f;
        float yr = y;
        HotbarElement.renderStatusBar(x, yl, ClientColors.HEALTH_COLOR, HotbarElement.mc.field_1724.method_6032(), HotbarElement.mc.field_1724.method_6063(), 0, false);
        if (HotbarElement.mc.field_1724.method_6067() > 0.0f) {
            HotbarElement.renderStatusBar(x, yl -= 10.0f, ClientColors.GOLDEN_HP, HotbarElement.mc.field_1724.method_6067(), Math.max(HotbarElement.mc.field_1724.method_6067(), HotbarElement.mc.field_1724.method_52541()), 1, false);
        }
        if (HotbarElement.mc.field_1724.method_6096() > 0) {
            HotbarElement.renderStatusBar(x, yl -= 10.0f, ClientColors.ARMOR_COLOR, HotbarElement.mc.field_1724.method_6096(), 20.0f, 3, false);
        }
        HotbarElement.renderStatusBar(x, yr, ClientColors.HUNGER_COLOR, HotbarElement.mc.field_1724.method_7344().method_7586(), 20.0f, 2, true);
        if (HotbarElement.mc.field_1724.method_5669() < HotbarElement.mc.field_1724.method_5748()) {
            HotbarElement.renderStatusBar(x, yr -= 10.0f, ClientColors.AIR_COLOR, HotbarElement.mc.field_1724.method_5669(), HotbarElement.mc.field_1724.method_5748(), 4, true);
        }
        return Math.min(yl, yr) - 8.0f;
    }

    private static void renderStatusBar(float x, float y, Color color, float value, float max, int i, boolean reverse) {
        if (reverse) {
            x += 114.0f;
        }
        float progress = HotbarElement.statusAnims[i] = MathUtility.linear(statusAnims[i], MathHelper.method_15363((float)value, (float)0.0f, (float)max) / max, 0.5f);
        String text = value % 1.0f == 0.0f ? String.valueOf(Math.round(value)) : String.format("%.1f", Float.valueOf(value));
        float textWidth = Client.RENDERER.textWidth(text, TextureUse.SFMEDIUM, 7.0f);
        Color backgroundColor = ColorUtility.injectAlpha(ClientColors.BACK_COLOR, 128.0f);
        Client.RENDERER.rect(x, y, 84.0f, 8.0f, new Vector4f(5.0f), 1.0f, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
        Client.RENDERER.rect(reverse ? x + 84.0f * (1.0f - progress) : x, y, 84.0f * progress, 8.0f, new Vector4f(5.0f), 1.0f, color, color, color, color);
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        Color textColor = (double)hsb[2] > 0.8 ? ClientColors.BACK_COLOR : ClientColors.FORE_COLOR;
        Client.RENDERER.text(text, x + (reverse ? 84.0f * Math.min(0.88f, 1.0f - progress) : Math.max(1.0f, 84.0f * progress - textWidth - 2.0f)), y, TextureUse.SFMEDIUM, 7.0f, textColor);
    }

    public static void renderTooltipAndOverlay(float statusY, int ticks, Text overlayMessage, int overlayRemaining, RenderTickCounter tickCounter) {
        ItemStack currentStack = HotbarElement.mc.field_1724.method_6047();
        float y = statusY;
        Color backgroundColor = ColorUtility.injectAlpha(ClientColors.BACK_COLOR, 128.0f);
        if (ticks > 0 && !currentStack.method_7960()) {
            y -= 6.0f;
            MutableText text = Text.method_43473().method_10852(currentStack.method_7964()).method_27692(currentStack.method_7932().method_58413());
            if (currentStack.method_57826(DataComponentTypes.field_49631)) {
                text.method_27692(Formatting.field_1056);
            }
            float textWidth = Client.RENDERER.textWidth(Formatting.method_539((String)text.getString()), TextureUse.SFMEDIUM, 8.0f);
            float anim = (float)ticks / 10.0f;
            float prevAlpha = Client.RENDERER.getCrenderSystem().alpha();
            Client.RENDERER.getCrenderSystem().alpha(anim * prevAlpha);
            Client.RENDERER.rect((float)mc.method_22683().method_4486() / 2.0f - textWidth / 2.0f - 2.0f, y - 2.0f, textWidth + 2.0f + 4.0f, 13.0f, new Vector4f(5.0f), 1.0f, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
            Client.RENDERER.text((Text)text, (float)mc.method_22683().method_4486() / 2.0f - textWidth / 2.0f, y, TextureUse.SFMEDIUM, 8.0f);
            Client.RENDERER.getCrenderSystem().alpha(prevAlpha);
        }
        if (overlayMessage != null && overlayRemaining > 0) {
            y -= 16.0f;
            float textWidth = Client.RENDERER.textWidth(Formatting.method_539((String)overlayMessage.getString()), TextureUse.SFMEDIUM, 8.0f);
            float anim = ((float)overlayRemaining - tickCounter.method_60637(true)) / 20.0f;
            float prevAlpha = Client.RENDERER.getCrenderSystem().alpha();
            Client.RENDERER.getCrenderSystem().alpha(anim * prevAlpha);
            Client.RENDERER.rect((float)mc.method_22683().method_4486() / 2.0f - textWidth / 2.0f - 2.0f, y - 2.0f, textWidth + 2.0f + 4.0f, 13.0f, new Vector4f(5.0f), 1.0f, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
            Client.RENDERER.text(overlayMessage, (float)mc.method_22683().method_4486() / 2.0f - textWidth / 2.0f, y, TextureUse.SFMEDIUM, 8.0f);
            Client.RENDERER.getCrenderSystem().alpha(prevAlpha);
        }
    }
}

