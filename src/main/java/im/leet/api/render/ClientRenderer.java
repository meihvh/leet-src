/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.impl.util.StringUtil
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.texture.GlTextureView
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.Item
 *  net.minecraft.registry.Registries
 *  net.minecraft.text.Text
 *  net.minecraft.util.Formatting
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.RotationAxis
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix4f
 *  org.joml.Quaternionfc
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.render;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.render.msdf.MsdfFont;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.ShaderUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.render.system.sys2d.TextureAtlas;
import im.leet.base.modules.impl.render.Interface;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.GlTextureView;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ClientRenderer {
    final MatrixStack stack = new MatrixStack();
    ArrayList<Runnable> postTasks = new ArrayList();
    CRenderSystem crenderSystem = new CRenderSystem();
    @Nullable
    DrawContext drawContext;

    public void queueTask(Runnable task) {
        this.postTasks.add(task);
    }

    public void runTasks() {
        Iterator<Runnable> iterator = this.postTasks.iterator();
        while (iterator.hasNext()) {
            Runnable item = iterator.next();
            item.run();
            iterator.remove();
        }
    }

    public void render() {
        this.crenderSystem.layerTex(this.crenderSystem.getAtlas().getGlId()).render(CRenderSystem.RenderLayer.OVERLAY);
    }

    public void prepare() {
        this.crenderSystem.prepare();
    }

    public void rect(float x, float y, float width, float height, Vector4f round, float smooth, Color topLeft, Color bottomLeft, Color bottomRight, Color topRight) {
        this.crenderSystem.shader(ShaderUse.RECTANGLE).rect(x, y, width, height).round(round.x, round.y, round.z, round.w).smoothness(smooth, smooth).color(topLeft, bottomLeft, bottomRight, topRight).build();
    }

    public void rect(Rectangle r, Vector4f round, float smooth, Color color1, Color color2, Color color3, Color color4) {
        this.crenderSystem.shader(ShaderUse.RECTANGLE).rect(r.getX(), r.getY(), r.getWidth(), r.getHeight()).round(round.x, round.y, round.z, round.w).smoothness(smooth, smooth).color(color1, color2, color3, color4).build();
    }

    public void glow(float x, float y, float width, float height, Vector4f round, float smooth, Color color1, Color color2, Color color3, Color color4) {
        this.crenderSystem.shader(ShaderUse.GLOW).rect(x, y, width, height).round(round.x, round.y, round.z, round.w).smoothness(smooth, smooth).color(color1, color2, color3, color4).build();
    }

    public void outline(float x, float y, float width, float height, float thickness, Vector4f round, Vector2f smooth, Color color1, Color color2, Color color3, Color color4) {
        this.crenderSystem.shader(ShaderUse.OUTLINE).rect(x, y, width, height).round(round.x, round.y, round.z, round.w).smoothness(smooth.x, smooth.y).thickness(thickness).color(color1, color2, color3, color4).build();
    }

    public void line(float x1, float y1, float x2, float y2, float width, float smooth, float round, Color color1, Color color2) {
        float d = x2 - x1;
        float e = y2 - y1;
        double dist = Math.sqrt(d * d + e * e);
        float angle = (float)((double)((float)Math.atan2(e, d)) * 57.29577951308232);
        this.getStack().method_22903();
        this.getStack().method_46416(MathUtility.scaledX(x1), MathUtility.scaledY(y1), 0.0f);
        this.getStack().method_22907((Quaternionfc)RotationAxis.field_40718.rotationDegrees(angle));
        this.rect(-width / 2.0f, -width / 2.0f, (float)dist, width, new Vector4f(round), smooth, color1, color1, color2, color2);
        this.getStack().method_22909();
    }

    public void blur(float x, float y, float width, float height, Vector4f round, float blurRadius, float smooth) {
        this.blur(x, y, width, height, round, blurRadius, smooth, CRenderSystem.RenderLayer.BLUR);
    }

    public void blur(float x, float y, float width, float height, Vector4f round, float blurRadius, float smooth, CRenderSystem.RenderLayer layer) {
        CRenderSystem.RenderLayer prev = this.crenderSystem.layer();
        this.crenderSystem.layer(layer);
        this.crenderSystem.layerTex(((GlTextureView)MinecraftHolder.mc.method_1522().method_71639()).method_71638().method_68427());
        this.crenderSystem.shader(ShaderUse.BLUR).uv(x / (float)MinecraftHolder.window.method_4486(), 1.0f - y / (float)MinecraftHolder.window.method_4502(), width / (float)MinecraftHolder.window.method_4486(), -(height / (float)MinecraftHolder.window.method_4502())).rect(x, y, width, height).color(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE).round(round.x, round.y, round.z, round.w).blur(blurRadius).smoothness(smooth, smooth).build();
        this.crenderSystem.layer(prev);
    }

    public void text(String text, float x, float y, TextureUse font, float size, Color color) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        msdfFont.applyGlyphs(new Matrix4f(), text, size, 0.0f, 0.3f, x, y + msdfFont.getMetrics().baselineHeight() * size, 0.0f, color, color, color, color, font);
    }

    public void text(String text, float x, float y, TextureUse font, float size, Color color1, Color color2, Color color3, Color color4) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        msdfFont.applyGlyphs(new Matrix4f(), text, size, 0.0f, 0.3f, x, y + msdfFont.getMetrics().baselineHeight() * size, 0.0f, color1, color2, color3, color4, font);
    }

    public void text(Text origin, float x, float y, TextureUse font, float size) {
        float off = 0.0f;
        for (Text text : origin.method_36136(origin.method_10866())) {
            off += this._text(text, x + off, y, font, size);
        }
    }

    private float _text(Text origin, float x, float y, TextureUse font, float size) {
        List texts = origin.method_36136(origin.method_10866());
        if (texts.size() <= 1) {
            Color c = origin.method_10866().method_10973() == null ? ClientColors.FORE_COLOR : new Color(origin.method_10866().method_10973().method_27716());
            this.text(Formatting.method_539((String)origin.getString()), x, y, font, size, c);
            return this.textWidth(Formatting.method_539((String)origin.getString()), font, size);
        }
        float offset = 0.0f;
        for (Text text : texts) {
            offset += this._text(text, x, y, font, size);
        }
        return offset;
    }

    public void text(IconUse icon, float x, float y, TextureUse font, float size, Color color) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        msdfFont.applyGlyphs(new Matrix4f(), icon.glyph, size, 0.0f, 0.3f, x, y + msdfFont.getMetrics().baselineHeight() * size, 0.0f, color, color, color, color, font);
    }

    public void text(IconUse icon, float x, float y, TextureUse font, float size, Color color1, Color color2, Color color3, Color color4) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        msdfFont.applyGlyphs(new Matrix4f(), icon.glyph, size, 0.0f, 0.3f, x, y + msdfFont.getMetrics().baselineHeight() * size, 0.0f, color1, color2, color3, color4, font);
    }

    public void text(IconUse icon, float x, float y, TextureUse font, float size, float smooth, Color color) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        msdfFont.applyGlyphs(new Matrix4f(), icon.glyph, size, 0.0f, 0.3f, x, y + msdfFont.getMetrics().baselineHeight() * size, 0.0f, color, font, smooth);
    }

    public void textCentered(String text, float x, float y, TextureUse font, float size, Color color) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        if (msdfFont == null) {
            return;
        }
        float textWidth = msdfFont.getWidth(text, size);
        this.text(text, x - textWidth / 2.0f, y, font, size, color);
    }

    public void textCenteredStrict(String text, float x, float y, TextureUse font, float size, Color color) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        if (msdfFont == null) {
            return;
        }
        float textWidth = msdfFont.getWidth(text, size);
        this.text(text, x - textWidth / 2.0f - 1.0f, y - this.textHeight(font, 8.0f) / 2.0f, font, size, color);
    }

    public float textWidth(String text, TextureUse font, float size) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        if (msdfFont == null) {
            return 0.0f;
        }
        return msdfFont.getWidth(text, size);
    }

    public float wrappedTextWidth(String text, TextureUse font, float size, int wrap) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        if (msdfFont == null) {
            return 0.0f;
        }
        float width = 0.0f;
        for (String line : StringUtil.wrapLines((String)text, (int)wrap).split("\n")) {
            width = Math.max(width, msdfFont.getWidth(line, size));
        }
        return width;
    }

    public float textHeight(TextureUse font, float size) {
        MsdfFont msdfFont = Client.FONTS.get(font);
        if (msdfFont == null) {
            return 0.0f;
        }
        return msdfFont.getHeight(size);
    }

    public void texture(Identifier id, float x, float y, float width, float height, float smooth, Vector4f round, Color color1, Color color2, Color color3, Color color4) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        TextureAtlas.UV uv = system.getAtlas().getOrCreate(id.toString(), id);
        if (uv == null) {
            return;
        }
        float a = 0.001f;
        system.shader(ShaderUse.TEXTURE).smoothness(smooth, smooth).uv(uv.u0 + a, uv.v0 + a, uv.u1 - a, uv.v1 - a).color(color1, color2, color3, color4).rect(x, y, width, height).build();
    }

    public void texture(Identifier id, float x, float y, float width, float height, float smooth, Vector4f uv2, Vector4f round, Color color1, Color color2, Color color3, Color color4) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        TextureAtlas.UV uv = system.getAtlas().getOrCreate(id.toString(), id);
        if (uv == null) {
            return;
        }
        float a = 0.001f;
        float ux = uv.u0 + a;
        float uy = uv.v0 + a;
        float uw = uv.u1 - a;
        float uh = uv.v1 - a;
        system.shader(ShaderUse.TEXTURE).smoothness(smooth, smooth).uv(ux + uv2.x / uv.width, uy + uv2.y / uv.height, uw - (1.0f - uv2.z) / uv.width, uh - (1.0f - uv2.z) / uv.height).round(round.x, round.y, round.z, round.w).color(color1, color2, color3, color4).rect(x, y, width, height).build();
    }

    public void textureRaw(Identifier id, float x, float y, float width, float height, float smooth, Vector4f round, Color color1, Color color2, Color color3, Color color4) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        TextureAtlas.UV uv = system.getAtlas().getOrCreate(id.toString(), id);
        if (uv == null) {
            return;
        }
        system.shader(ShaderUse.TEXTURE).smoothness(smooth, smooth).uv(uv.u0, uv.v0, uv.u1, uv.v1).round(round.x, round.y, round.z, round.w).color(color1, color2, color3, color4).rect(x, y, width, height).build();
    }

    public void textureRaw(Identifier id, float x, float y, float width, float height, float smooth, Vector4f round, Vector4f tex, Color color1, Color color2, Color color3, Color color4) {
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        TextureAtlas.UV uv = system.getAtlas().getOrCreate(id.toString(), id);
        if (uv == null) {
            return;
        }
        system.shader(ShaderUse.TEXTURE).smoothness(smooth, smooth).uv(uv.u0 + tex.x, uv.v0 + tex.y, tex.z, tex.w).round(round.x, round.y, round.z, round.w).color(color1, color2, color3, color4).rect(x, y, width, height).build();
    }

    public void texture(Item item, float x, float y, float width, float height, float smooth, Vector4f round, Color color1, Color color2, Color color3, Color color4) {
        this.textureRaw(item instanceof BlockItem ? Registries.field_41178.method_10221((Object)item).method_45138("textures/block/").method_48331(".png") : Registries.field_41178.method_10221((Object)item).method_45138("textures/item/").method_48331(".png"), x, y, width, height, smooth, round, color1, color2, color3, color4);
    }

    public void drawModuleRect(float x, float y, float width, float height) {
        Color thumbBackColor = ClientColors.GUI_STROKE;
        Client.RENDERER.rect(x, y, width, height, new Vector4f(7.0f), 1.0f, thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
    }

    public void drawModuleRect(Rectangle rect) {
        this.drawModuleRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void drawHudRect(float x, float y, float width, float height) {
        if (Interface.INSTANCE.blur.get()) {
            Client.RENDERER.blur(x, y, width, height, new Vector4f(10.0f), 20.0f, 1.0f);
        }
        Color c = ColorUtility.injectAlpha(ClientColors.BACK_COLOR, 160.0f);
        Client.RENDERER.rect(x, y, width, height, new Vector4f(10.0f), 1.0f, c, c, c, c);
    }

    public void outlined(float x, float y, float width, float height) {
        Color bgColor = ClientColors.GUI_BACKGROUND;
        Color thumbBackColor = ClientColors.GUI_STROKE;
        Client.RENDERER.rect(x, y, width, height, new Vector4f(8.0f), 1.0f, bgColor, bgColor, bgColor, bgColor);
        Client.RENDERER.outline(x, y, width, height, 0.0f, new Vector4f(8.0f), new Vector2f(1.0f), thumbBackColor, thumbBackColor, thumbBackColor, thumbBackColor);
    }

    public void shader(Rectangle r, Vector4f round, float smooth, ShaderUse shader) {
        this.crenderSystem.shader(shader).rect(r.getX(), r.getY(), r.getWidth(), r.getHeight()).round(round.x, round.y, round.z, round.w).smoothness(smooth, smooth).build();
    }

    public void shader(Rectangle r, Vector4f round, float smooth, ShaderUse shader, Color a) {
        this.crenderSystem.shader(shader).rect(r.getX(), r.getY(), r.getWidth(), r.getHeight()).round(round.x, round.y, round.z, round.w).color(a, a, a, a).smoothness(smooth, smooth).build();
    }

    public void backIcon(IconUse icon, float x, float y, float rectSize, float iconXOff, float iconYOff, float iconSize, int color1, int color2) {
        Color themeColor = ClientSettings.INSTANCE.getColor(color1);
        Color secondaryColor = ClientSettings.INSTANCE.getColor(color2);
        Client.RENDERER.rect(x + 4.0f, y + 4.0f, rectSize, rectSize, new Vector4f(4.0f), 1.0f, secondaryColor, themeColor, themeColor, secondaryColor);
        Client.RENDERER.text(icon, x + iconXOff, y + iconYOff, TextureUse.ICONS, iconSize, ClientColors.ICON_FOREGROUND_COLOR);
    }

    public void backIcon(IconUse icon, float x, float y) {
        this.backIcon(icon, x, y, 12.0f, 4.2f, 5.5f, 8.0f, 0, 90);
    }

    public float textLined(String text, int limit, float x, float y, TextureUse tex, float size, Color color) {
        String[] l = StringUtil.wrapLines((String)text, (int)limit).split("\n");
        if (l.length <= 1) {
            this.text(text, x, y, tex, size, color);
            return 0.0f;
        }
        float off = 0.0f;
        for (String line : l) {
            this.text(line, x, y + off, tex, size, color);
            off += size;
        }
        return off - size;
    }

    public void toCamera(MatrixStack matrixStack) {
        Vec3d cam = MinecraftHolder.mc.field_1773.method_19418().method_19326();
        matrixStack.method_22904(-cam.field_1352, -cam.field_1351, -cam.field_1350);
    }

    public void toWorld(MatrixStack matrixStack) {
        Vec3d cam = MinecraftHolder.mc.field_1773.method_19418().method_19326();
        matrixStack.method_22904(cam.field_1352, cam.field_1351, cam.field_1350);
    }

    public MatrixStack getStack() {
        return this.stack;
    }

    public ArrayList<Runnable> getPostTasks() {
        return this.postTasks;
    }

    public CRenderSystem getCrenderSystem() {
        return this.crenderSystem;
    }

    @Nullable
    public DrawContext getDrawContext() {
        return this.drawContext;
    }

    public void setDrawContext(@Nullable DrawContext drawContext) {
        this.drawContext = drawContext;
    }
}

