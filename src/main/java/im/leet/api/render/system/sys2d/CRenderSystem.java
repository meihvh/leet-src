/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.opengl.GlStateManager
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.Window
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.MathHelper
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 *  org.joml.Vector4f
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL15C
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.system.MemoryUtil
 */
package im.leet.api.render.system.sys2d;

import com.mojang.blaze3d.opengl.GlStateManager;
import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.render.system.ShaderUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.AttributeHelper;
import im.leet.api.render.system.sys2d.CFramebuf;
import im.leet.api.render.system.sys2d.Shader;
import im.leet.api.render.system.sys2d.TextureAtlas;
import java.awt.Color;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import sun.misc.Unsafe;

public class CRenderSystem {
    private static final CRenderSystem INSTANCE = new CRenderSystem();
    private final Stack<ScissorState> scissorStack = new Stack();
    private int vao;
    private int vbo;
    private int ebo;
    private TextureAtlas atlas;
    public CFramebuf cFramebuf;
    private final Map<RenderLayer, LayerData> layers = new HashMap<RenderLayer, LayerData>();
    private RenderLayer currentLayer = RenderLayer.OVERLAY;
    int FBO = 0;
    int FBO_TEX = 0;
    int FBO_WIDTH = 0;
    int FBO_HEIGHT = 0;
    int FBO_RBO = 0;
    private float globalAlpha = 1.0f;
    float x;
    float y;
    float z;
    float blurRadius;
    float width;
    float height;
    float texU;
    float texV;
    float texW = 1.0f;
    float texH = 1.0f;
    float thickness;
    float msdfRange;
    float scissorX;
    float scissorY;
    float scissorWidth;
    float scissorHeight;
    Color color1 = Color.WHITE;
    Color color2 = Color.WHITE;
    Color color3 = Color.WHITE;
    Color color4 = Color.WHITE;
    ShaderUse using = ShaderUse.RECTANGLE;
    Vector4f round = new Vector4f();
    Vector2f smoothness = new Vector2f();
    float texId;
    float hatch;
    float useCircle;
    boolean extraColor;
    private static final int MAX_QUADS = 1000;
    int[] textures = new int[16];
    private Shader shader;
    int sisike = 0;
    private static final Unsafe UNSAFE;
    private ArrayList<Identifier> prepareTasks = new ArrayList();

    public void putTex(TextureUse textureUse, int glId) {
        this.textures[textureUse.id] = glId;
    }

    public CRenderSystem() {
        this.atlas = new TextureAtlas(5000, 5000);
        try {
            this.atlas.add(TextureUse.SFMEDIUM, Identifier.method_60655((String)"leet", (String)"fonts/sfmedium.png"));
            this.atlas.add(TextureUse.ICONS, Identifier.method_60655((String)"leet", (String)"fonts/icons.png"));
            this.atlas.add(TextureUse.EMOJIS, Identifier.method_60655((String)"leet", (String)"fonts/emojis.png"));
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.vao = GL30.glGenVertexArrays();
        this.vbo = GL15.glGenBuffers();
        this.ebo = GL15.glGenBuffers();
        GL30.glBindVertexArray((int)this.vao);
        GL15.glBindBuffer((int)34962, (int)this.vbo);
        GL15.glBufferData((int)34962, (long)(4000L * (long)AttributeHelper.SIZE * 4L), (int)35040);
        GL15.glBindBuffer((int)34963, (int)this.ebo);
        GL15.glBufferData((int)34963, (long)24000L, (int)35040);
        AttributeHelper.addAttribute(3, 5126, false);
        AttributeHelper.addAttribute(2, 5126, true);
        AttributeHelper.addAttribute(4, 5126, false);
        AttributeHelper.addAttribute(2, 5126, false);
        AttributeHelper.addAttribute(4, 5126, false);
        AttributeHelper.addAttribute(1, 5126, false);
        AttributeHelper.addAttribute(2, 5126, false);
        AttributeHelper.addAttribute(1, 5126, false);
        AttributeHelper.addAttribute(1, 5126, false);
        AttributeHelper.addAttribute(1, 5126, false);
        AttributeHelper.addAttribute(4, 5126, false);
        AttributeHelper.addAttribute(2, 5126, false);
        AttributeHelper.addAttribute(2, 5126, true);
        AttributeHelper.addAttribute(1, 5126, false);
        AttributeHelper.addAttribute(1, 5126, false);
        AttributeHelper.createAttributes();
        GL30.glBindVertexArray((int)0);
        GL15.glBindBuffer((int)34962, (int)0);
        GL15.glBindBuffer((int)34963, (int)0);
        for (RenderLayer layer : RenderLayer.values()) {
            this.layers.put(layer, new LayerData(1000));
        }
        this.shader = new Shader();
        this.FBO = GL30.glGenFramebuffers();
        this.FBO_TEX = GL11.glGenTextures();
        this.FBO_RBO = GL30.glGenRenderbuffers();
        this.cFramebuf = new CFramebuf(MinecraftHolder.mc.method_1522().field_1482, MinecraftHolder.mc.method_1522().field_1481);
    }

    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (width == this.FBO_WIDTH && height == this.FBO_HEIGHT) {
            return;
        }
        this.FBO_WIDTH = width;
        this.FBO_HEIGHT = height;
        GL30.glBindFramebuffer((int)36160, (int)this.FBO);
        GL11.glBindTexture((int)3553, (int)this.FBO_TEX);
        GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)this.FBO_WIDTH, (int)this.FBO_HEIGHT, (int)0, (int)6408, (int)5121, (ByteBuffer)null);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)this.FBO_TEX, (int)0);
        GL30.glBindRenderbuffer((int)36161, (int)this.FBO_RBO);
        GL30.glRenderbufferStorage((int)36161, (int)35056, (int)this.FBO_WIDTH, (int)this.FBO_HEIGHT);
        GL30.glFramebufferRenderbuffer((int)36160, (int)33306, (int)36161, (int)this.FBO_RBO);
        int status = GL30.glCheckFramebufferStatus((int)36160);
        if (status != 36053) {
            GL30.glBindFramebuffer((int)36160, (int)0);
            throw new RuntimeException("FBO incomplete on resize, status: 0x" + Integer.toHexString(status));
        }
        GL30.glBindFramebuffer((int)36160, (int)0);
    }

    public CRenderSystem layer(RenderLayer layer) {
        this.currentLayer = layer;
        return this;
    }

    public RenderLayer layer() {
        return this.currentLayer;
    }

    public CRenderSystem layerTex(int texId) {
        this.layers.get((Object)((Object)this.currentLayer)).texId = texId;
        return this;
    }

    public CRenderSystem alpha(float alpha) {
        this.globalAlpha = Math.min(alpha, 1.0f);
        return this;
    }

    public float alpha() {
        return this.globalAlpha;
    }

    public CRenderSystem hatch(float hatch) {
        this.hatch = hatch;
        return this;
    }

    public CRenderSystem scissor(float x, float y, float width, float height) {
        int h = MinecraftClient.method_1551().method_22683().method_4502();
        int f = MinecraftClient.method_1551().method_22683().method_4495();
        this.scissorX = x * (float)f;
        this.scissorY = ((float)h - y - height) * (float)f;
        this.scissorWidth = width * (float)f;
        this.scissorHeight = height * (float)f;
        return this;
    }

    public CRenderSystem disableScissor() {
        int w = MinecraftClient.method_1551().method_22683().method_4489();
        int h = MinecraftClient.method_1551().method_22683().method_4506();
        this.scissorX = 0.0f;
        this.scissorY = 0.0f;
        this.scissorWidth = w;
        this.scissorHeight = h;
        return this;
    }

    public CRenderSystem useCircle(float value) {
        this.useCircle = value;
        return this;
    }

    public float useCircle() {
        return this.useCircle;
    }

    public void extraColor(boolean value) {
        this.extraColor = value;
    }

    public boolean extraColor() {
        return this.extraColor;
    }

    public void push(float x, float y, float width, float height) {
        if (!this.scissorStack.isEmpty()) {
            this.push2(x, y, width, height);
            return;
        }
        int f = MinecraftClient.method_1551().method_22683().method_4495();
        this.scissorStack.push(new ScissorState((int)x, (int)y, (int)width, (int)height));
        this.scissor(x, y, width, height);
    }

    public void push2(float x, float y, float width, float height) {
        if (this.scissorStack.isEmpty()) {
            throw new IllegalStateException("push2 called without a corresponding push call");
        }
        int f = MinecraftClient.method_1551().method_22683().method_4495();
        ScissorState currentState = this.scissorStack.peek();
        int newX = (int)MathHelper.method_15363((float)x, (float)currentState.x, (float)(currentState.x + currentState.width));
        int newY = (int)MathHelper.method_15363((float)y, (float)currentState.y, (float)(currentState.y + currentState.height));
        int newWidth = (int)(MathHelper.method_15363((float)(x + width), (float)newX, (float)(currentState.x + currentState.width)) - (float)newX);
        int newHeight = (int)(MathHelper.method_15363((float)(y + height), (float)newY, (float)(currentState.y + currentState.height)) - (float)newY);
        this.scissorStack.push(new ScissorState(newX, newY, newWidth, newHeight));
        this.scissor(newX, newY, newWidth, newHeight);
    }

    public void pop() {
        if (this.scissorStack.isEmpty()) {
            throw new IllegalStateException("pop called without a corresponding push call");
        }
        this.scissorStack.pop();
        this.disableScissor();
        if (!this.scissorStack.isEmpty()) {
            ScissorState currentState = this.scissorStack.peek();
            this.scissor(currentState.x, currentState.y, currentState.width, currentState.height);
        }
    }

    public CRenderSystem line(float x1, float y1, float x2, float y2) {
        this.x = x1;
        this.y = y1;
        this.width = x2 - x1;
        this.height = y2 - y1;
        return this;
    }

    public CRenderSystem rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public CRenderSystem z(float z) {
        this.z = z;
        return this;
    }

    public CRenderSystem msdfRange(float range) {
        this.msdfRange = range;
        return this;
    }

    public CRenderSystem blur(float blurRadius) {
        this.blurRadius = blurRadius;
        return this;
    }

    public CRenderSystem color(Color color1, Color color2, Color color3, Color color4) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        return this;
    }

    public CRenderSystem uv(float u, float v, float width, float height) {
        this.texU = u;
        this.texV = v;
        this.texW = width;
        this.texH = height;
        return this;
    }

    public CRenderSystem round(float r1, float r2, float r3, float r4) {
        this.round = new Vector4f(r1, r2, r3, r4);
        return this;
    }

    public CRenderSystem shader(ShaderUse shaderUse) {
        this.using = shaderUse;
        return this;
    }

    public CRenderSystem smoothness(float s1, float s2) {
        this.smoothness.x = s1;
        this.smoothness.y = s2;
        return this;
    }

    public CRenderSystem thickness(float thickness) {
        this.thickness = thickness;
        return this;
    }

    public CRenderSystem texture(int texId) {
        this.texId = texId;
        return this;
    }

    private void ensureCapacityForLayer(LayerData layer, int additionalFloats, int additionalIndices) {
        Buffer newBuf;
        int newCap;
        int needed;
        if (layer.vertices.remaining() < additionalFloats) {
            needed = layer.vertices.position() + additionalFloats;
            newCap = Math.max(layer.vertices.capacity() * 2, needed);
            newBuf = BufferUtils.createFloatBuffer((int)newCap);
            layer.vertices.flip();
            ((FloatBuffer)newBuf).put(layer.vertices);
            layer.vertices = newBuf;
        }
        if (layer.indexes.remaining() < additionalIndices) {
            needed = layer.indexes.position() + additionalIndices;
            newCap = Math.max(layer.indexes.capacity() * 2, needed);
            newBuf = BufferUtils.createIntBuffer((int)newCap);
            layer.indexes.flip();
            ((IntBuffer)newBuf).put(layer.indexes);
            layer.indexes = newBuf;
        }
    }

    public void build() {
        LayerData layer = this.layers.get((Object)this.currentLayer);
        this.ensureCapacityForLayer(layer, 4 * AttributeHelper.SIZE, 6);
        int base = layer.quadCount * 4;
        int u_shader = this.using.ordinal();
        Matrix4f matrix = Client.RENDERER.getStack().method_23760().method_23761();
        this.x /= (float)MinecraftHolder.window.method_4486() / (float)MinecraftHolder.window.method_4480();
        this.y /= (float)MinecraftHolder.window.method_4502() / (float)MinecraftHolder.window.method_4507();
        this.width /= (float)MinecraftHolder.window.method_4486() / (float)MinecraftHolder.window.method_4480();
        this.height /= (float)MinecraftHolder.window.method_4502() / (float)MinecraftHolder.window.method_4507();
        Vector3f a0 = new Vector3f(this.x, this.y, this.z);
        Vector3f a1 = new Vector3f(this.x, this.y + this.height, this.z);
        Vector3f a2 = new Vector3f(this.x + this.width, this.y + this.height, this.z);
        Vector3f a3 = new Vector3f(this.x + this.width, this.y, this.z);
        matrix.transformPosition(a0);
        matrix.transformPosition(a1);
        matrix.transformPosition(a2);
        matrix.transformPosition(a3);
        float sc = (float)MinecraftHolder.window.method_4486() / (float)MinecraftHolder.window.method_4480() + (float)MinecraftHolder.window.method_4502() / (float)MinecraftHolder.window.method_4507();
        this.round.mul(1.0f / sc);
        Vector4f b0 = new Vector4f(this.scissorX, this.scissorY, this.z, 0.0f).mul((Matrix4fc)matrix);
        Vector4f b3 = new Vector4f(this.scissorWidth, this.scissorHeight, this.z, 0.0f).mul((Matrix4fc)matrix);
        float colorFactor = this.extraColor ? 100.0f : 255.0f;
        layer.vertices.put(new float[]{a0.x, a0.y, a0.z, this.texU, this.texV, (float)this.color1.getRed() / colorFactor, (float)this.color1.getGreen() / colorFactor, (float)this.color1.getBlue() / colorFactor, (float)this.color1.getAlpha() / 255.0f * this.globalAlpha, this.width, this.height, this.round.x, this.round.y, this.round.z, this.round.w, u_shader, this.smoothness.x, this.smoothness.y, this.thickness, this.msdfRange, this.blurRadius, this.scissorX, this.scissorY, this.scissorWidth, this.scissorHeight, this.x, this.y, 0.0f, 0.0f, this.hatch, this.useCircle, a1.x, a1.y, a1.z, this.texU, this.texV + this.texH, (float)this.color2.getRed() / colorFactor, (float)this.color2.getGreen() / colorFactor, (float)this.color2.getBlue() / colorFactor, (float)this.color2.getAlpha() / 255.0f * this.globalAlpha, this.width, this.height, this.round.x, this.round.y, this.round.z, this.round.w, u_shader, this.smoothness.x, this.smoothness.y, this.thickness, this.msdfRange, this.blurRadius, this.scissorX, this.scissorY, this.scissorWidth, this.scissorHeight, this.x, this.y, 0.0f, 1.0f, this.hatch, this.useCircle, a2.x, a2.y, a2.z, this.texU + this.texW, this.texV + this.texH, (float)this.color3.getRed() / colorFactor, (float)this.color3.getGreen() / colorFactor, (float)this.color3.getBlue() / colorFactor, (float)this.color3.getAlpha() / 255.0f * this.globalAlpha, this.width, this.height, this.round.x, this.round.y, this.round.z, this.round.w, u_shader, this.smoothness.x, this.smoothness.y, this.thickness, this.msdfRange, this.blurRadius, this.scissorX, this.scissorY, this.scissorWidth, this.scissorHeight, this.x, this.y, 1.0f, 1.0f, this.hatch, this.useCircle, a3.x, a3.y, a3.z, this.texU + this.texW, this.texV, (float)this.color4.getRed() / colorFactor, (float)this.color4.getGreen() / colorFactor, (float)this.color4.getBlue() / colorFactor, (float)this.color4.getAlpha() / 255.0f * this.globalAlpha, this.width, this.height, this.round.x, this.round.y, this.round.z, this.round.w, u_shader, this.smoothness.x, this.smoothness.y, this.thickness, this.msdfRange, this.blurRadius, this.scissorX, this.scissorY, this.scissorWidth, this.scissorHeight, this.x, this.y, 1.0f, 0.0f, this.hatch, this.useCircle});
        layer.indexes.put(new int[]{base, base + 1, base + 3, base + 1, base + 2, base + 3});
        ++layer.quadCount;
        this.texU = 0.0f;
        this.texV = 0.0f;
        this.texW = 1.0f;
        this.texH = 1.0f;
    }

    public CRenderSystem addPrepare(Identifier identifier) {
        this.prepareTasks.add(identifier);
        return this;
    }

    public void prepare() {
        this.textures[0] = this.atlas.getGlId();
        for (Identifier prepared : this.prepareTasks) {
            if (this.getAtlas().has(prepared.toString())) continue;
            this.getAtlas().addTextureFromImage(prepared.toString(), prepared);
        }
        this.prepareTasks.clear();
    }

    public void render(RenderLayer renderLayer) {
        Window window = MinecraftClient.method_1551().method_22683();
        MinecraftClient mc = MinecraftClient.method_1551();
        LayerData layer = this.layers.get((Object)renderLayer);
        int currentVao = GL30.glGetInteger((int)34229);
        float f = mc.method_61966().method_60637(true);
        Matrix4f projFov = this.createProjectionMatrix(mc.field_1773.method_3196(mc.field_1773.method_19418(), f, false));
        Matrix4f projFb = this.createProjectionMatrix(mc.method_1522().field_1482, mc.method_1522().field_1481);
        projFov.translate(0.0f, 0.0f, -0.7146f);
        int[] samplers = new int[this.textures.length];
        GlStateManager.glActiveTexture((int)33984);
        GlStateManager._bindTexture((int)layer.texId);
        GlStateManager._texParameter((int)3553, (int)10241, (int)9729);
        GlStateManager._texParameter((int)3553, (int)10240, (int)9728);
        GlStateManager._glBindVertexArray((int)this.vao);
        GlStateManager._glBindBuffer((int)34962, (int)this.vbo);
        GlStateManager._glBindBuffer((int)34963, (int)this.ebo);
        GlStateManager._glBufferData((int)34962, (long)((long)layer.vertices.capacity() * 4L), (int)35048);
        GlStateManager._glBufferData((int)34963, (long)((long)layer.indexes.capacity() * 4L), (int)35048);
        layer.vertices.flip();
        layer.indexes.flip();
        long vAddr = MemoryUtil.memAddress((FloatBuffer)layer.vertices);
        long vSizeBytes = (long)layer.vertices.limit() * 4L;
        GL15C.nglBufferSubData((int)34962, (long)0L, (long)vSizeBytes, (long)vAddr);
        long iAddr = MemoryUtil.memAddress((IntBuffer)layer.indexes);
        long iSizeBytes = (long)layer.indexes.limit() * 4L;
        GL15C.nglBufferSubData((int)34963, (long)0L, (long)iSizeBytes, (long)iAddr);
        this.shader.bind();
        Matrix4f projOrtho = this.createProjectionMatrix(window.method_4489(), window.method_4506());
        this.shader.uploadMatrix(projOrtho, new Matrix4f().identity());
        int locTextures = GlStateManager._glGetUniformLocation((int)this.shader.getId(), (CharSequence)"tex");
        GlStateManager._glUniform1i((int)locTextures, (int)0);
        int locRes = GlStateManager._glGetUniformLocation((int)this.shader.getId(), (CharSequence)"iResolution");
        if (locRes >= 0) {
            GlStateManager._glUniform2((int)locRes, (FloatBuffer)FloatBuffer.wrap(new float[]{window.method_4486(), window.method_4502()}));
        }
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3089);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDrawElements((int)4, (int)layer.indexes.limit(), (int)5125, (long)0L);
        layer.clear();
        GL11.glEnable((int)2929);
        GlStateManager._glBindVertexArray((int)0);
        GlStateManager._glBindBuffer((int)34962, (int)0);
        GlStateManager._glBindBuffer((int)34963, (int)0);
        if (currentVao != 0) {
            GlStateManager._glBindVertexArray((int)currentVao);
        }
        Client.RENDERER.getCrenderSystem().disableScissor();
    }

    public void postRender() {
        this.layers.values().forEach(LayerData::clearTimeout);
    }

    private Matrix4f createProjectionMatrix(float f, float g) {
        return new Matrix4f().setOrtho(0.0f, f, g, 0.0f, 1000.0f, -11000.0f);
    }

    private Matrix4f createProjectionMatrix(float fovDeg) {
        return new Matrix4f().perspective(fovDeg * ((float)Math.PI / 180), 1.0f, 0.05f, 1000.0f);
    }

    public static CRenderSystem getInstance() {
        return INSTANCE;
    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe)theUnsafe.get(null);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to get Unsafe instance", e);
        }
    }

    public static enum RenderLayer {
        POST,
        BLUR,
        HUD,
        ESP,
        POST_SCREEN,
        OVERLAY;

    }

    private static class LayerData {
        FloatBuffer vertices;
        IntBuffer indexes;
        int quadCount;
        int texId;
        long lastReset = System.currentTimeMillis();
        long vertexAddress;

        LayerData(int maxQuads) {
            this.vertices = BufferUtils.createFloatBuffer((int)(maxQuads * 4 * AttributeHelper.SIZE));
            this.indexes = BufferUtils.createIntBuffer((int)(maxQuads * 6));
            this.quadCount = 0;
            this.vertexAddress = MemoryUtil.memAddress((FloatBuffer)this.vertices);
        }

        void clear() {
            this.vertices.clear();
            this.indexes.clear();
            this.quadCount = 0;
            this.lastReset = System.currentTimeMillis();
        }

        void clearTimeout() {
            if (System.currentTimeMillis() - this.lastReset > 5000L) {
                this.clear();
            }
        }

        int vertexFloatCount() {
            return this.vertices.position();
        }

        int indexCount() {
            return this.indexes.position();
        }
    }

    private static class ScissorState {
        int x;
        int y;
        int width;
        int height;

        ScissorState(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
        }
    }
}

