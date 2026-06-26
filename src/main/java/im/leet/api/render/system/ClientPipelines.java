/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.BlendFunction
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.vertex.VertexFormat$DrawMode
 *  net.minecraft.client.gl.RenderPipelines
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.RenderLayer$MultiPhase
 *  net.minecraft.client.render.RenderLayer$MultiPhaseParameters
 *  net.minecraft.client.render.RenderPhase
 *  net.minecraft.client.render.RenderPhase$LineWidth
 *  net.minecraft.client.render.RenderPhase$Texture
 *  net.minecraft.client.render.RenderPhase$TextureBase
 *  net.minecraft.client.render.VertexFormats
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.Util
 */
package im.leet.api.render.system;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import im.leet.api.render.system.CustomVertexConsumerProvider;
import java.util.OptionalDouble;
import java.util.function.Function;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class ClientPipelines {
    public static final RenderPipeline HUD = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_56863}).withLocation("pipeline/gui").build());
    public static final RenderPipeline WORLD = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_56860}).withLocation("pipeline/wclient").build());
    public static final RenderPipeline TARGET_ESP_PIPELINE = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_60125}).withLocation("pipeline/wtex").withVertexShader("core/position_tex_color").withFragmentShader("core/position_tex_color").withSampler("Sampler0").withBlend(BlendFunction.LIGHTNING).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withVertexFormat(VertexFormats.field_1575, VertexFormat.DrawMode.field_27382).build());
    public static final RenderPipeline PARTICLE_PIPELINE = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_60125}).withLocation("pipeline/wtex").withVertexShader("core/position_tex_color").withFragmentShader("core/position_tex_color").withSampler("Sampler0").withBlend(BlendFunction.LIGHTNING).withCull(false).withVertexFormat(VertexFormats.field_1575, VertexFormat.DrawMode.field_27382).build());
    public static final Function<Identifier, RenderLayer> TARGET_ESP = Util.method_34866(texture -> RenderLayer.method_24049((String)"wtex", (int)1536, (boolean)true, (boolean)false, (RenderPipeline)TARGET_ESP_PIPELINE, (RenderLayer.MultiPhaseParameters)RenderLayer.MultiPhaseParameters.method_23598().method_34577((RenderPhase.TextureBase)new RenderPhase.Texture(texture, false)).method_23617(false)));
    public static final Function<Identifier, RenderLayer> PARTICLES = Util.method_34866(texture -> RenderLayer.method_24049((String)"wparticles", (int)1536, (boolean)true, (boolean)false, (RenderPipeline)PARTICLE_PIPELINE, (RenderLayer.MultiPhaseParameters)RenderLayer.MultiPhaseParameters.method_23598().method_34577((RenderPhase.TextureBase)new RenderPhase.Texture(texture, false)).method_23617(false)));
    public static final RenderPipeline.Snippet RENDER_TYPE_QUADS = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_60127, RenderPipelines.field_60126}).withVertexShader("core/position_color").withFragmentShader("core/position_color").withBlend(BlendFunction.TRANSLUCENT).withCull(false).withVertexFormat(VertexFormats.field_1576, VertexFormat.DrawMode.field_27382).buildSnippet();
    public static final RenderPipeline QUADS = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RENDER_TYPE_QUADS}).withLocation("pipeline/quads").build());
    public static final RenderLayer.MultiPhase QUAD = RenderLayer.method_24048((String)"lines", (int)1536, (RenderPipeline)QUADS, (RenderLayer.MultiPhaseParameters)RenderLayer.MultiPhaseParameters.method_23598().method_23609(new RenderPhase.LineWidth(OptionalDouble.empty())).method_23607(RenderPhase.field_22241).method_23610(RenderPhase.field_25643).method_23617(false));
    public static final RenderPipeline.Snippet RENDER_TYPE_TRIANGLE_FAN = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_60127, RenderPipelines.field_60126}).withVertexShader("core/position_color").withFragmentShader("core/position_color").withBlend(BlendFunction.TRANSLUCENT).withCull(true).withVertexFormat(VertexFormats.field_1576, VertexFormat.DrawMode.field_27381).buildSnippet();
    public static final RenderPipeline TRIANGLE_FAN = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RENDER_TYPE_TRIANGLE_FAN}).withLocation("pipeline/triangle_fan").build());
    public static final RenderLayer.MultiPhase WORLD_TRIANGLE_FAN = RenderLayer.method_24048((String)"wtrianglefan", (int)1536, (RenderPipeline)TRIANGLE_FAN, (RenderLayer.MultiPhaseParameters)RenderLayer.MultiPhaseParameters.method_23598().method_23617(false));
    public static final RenderPipeline.Snippet RENDER_TYPE_TRIANGLE_STRIP = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_60127, RenderPipelines.field_60126}).withVertexShader("core/position_color").withFragmentShader("core/position_color").withBlend(BlendFunction.TRANSLUCENT).withCull(true).withVertexFormat(VertexFormats.field_1576, VertexFormat.DrawMode.field_27380).buildSnippet();
    public static final RenderPipeline TRIANGLE_STRIP = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RENDER_TYPE_TRIANGLE_STRIP}).withLocation("pipeline/triangle_fan").build());
    public static final RenderLayer.MultiPhase WORLD_TRIANGLE_STRIP = RenderLayer.method_24048((String)"wtrianglestrip", (int)1536, (RenderPipeline)TRIANGLE_STRIP, (RenderLayer.MultiPhaseParameters)RenderLayer.MultiPhaseParameters.method_23598().method_23617(false));
    public static final RenderPipeline.Snippet RENDER_TYPE_POINTS = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.field_60127, RenderPipelines.field_60126}).withVertexShader("core/position_color").withFragmentShader("core/position_color").withBlend(BlendFunction.TRANSLUCENT).withCull(false).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withVertexFormat(VertexFormats.field_1576, VertexFormat.DrawMode.field_27382).buildSnippet();
    public static final RenderPipeline POINTS_PIPELINE = RenderPipelines.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RENDER_TYPE_POINTS}).withLocation("pipeline/points").build());
    public static final RenderLayer.MultiPhase POINTS = RenderLayer.method_24048((String)"points", (int)1536, (RenderPipeline)POINTS_PIPELINE, (RenderLayer.MultiPhaseParameters)RenderLayer.MultiPhaseParameters.method_23598().method_23607(RenderPhase.field_22241).method_23610(RenderPhase.field_25643).method_23617(false));
    public static CustomVertexConsumerProvider customVertexConsumerProvider;
}

