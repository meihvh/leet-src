/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.gui.ScreenRect
 *  net.minecraft.client.gui.render.state.SimpleGuiElementRenderState
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.texture.TextureSetup
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix3x2f
 */
package im.leet.api.render.system.sys2d;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import im.leet.api.render.system.ClientPipelines;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

@Environment(value=EnvType.CLIENT)
public class ClientLayer
implements SimpleGuiElementRenderState {
    public void method_70917(VertexConsumer vertices, float depth) {
        vertices.method_70815(this.pose(), (float)this.x0(), (float)this.y0(), depth).method_39415(this.col1());
        vertices.method_70815(this.pose(), (float)this.x0(), (float)this.y1(), depth).method_39415(this.col2());
        vertices.method_70815(this.pose(), (float)this.x1(), (float)this.y1(), depth).method_39415(this.col2());
        vertices.method_70815(this.pose(), (float)this.x1(), (float)this.y0(), depth).method_39415(this.col1());
    }

    @Nullable
    private static ScreenRect createBounds(int x0, int y0, int x1, int y1, Matrix3x2f pose, @Nullable ScreenRect scissorArea) {
        ScreenRect screenRect = new ScreenRect(x0, y0, x1 - x0, y1 - y0).method_71523(pose);
        return scissorArea != null ? scissorArea.method_49701(screenRect) : screenRect;
    }

    public RenderPipeline comp_4055() {
        return ClientPipelines.HUD;
    }

    public TextureSetup comp_4056() {
        return TextureSetup.method_70899();
    }

    public Matrix3x2f pose() {
        return new Matrix3x2f();
    }

    public int x0() {
        return 0;
    }

    public int y0() {
        return 0;
    }

    public int x1() {
        return 0;
    }

    public int y1() {
        return 0;
    }

    public int col1() {
        return -1;
    }

    public int col2() {
        return -1;
    }

    @Nullable
    public ScreenRect comp_4069() {
        return ScreenRect.method_48248();
    }

    @Nullable
    public ScreenRect comp_4274() {
        return new ScreenRect(this.x0(), this.y0(), this.x1(), this.y1());
    }
}

