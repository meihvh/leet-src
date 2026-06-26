/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.api.ui.widgets.impl;

import im.leet.Client;
import im.leet.api.drags.Drag;
import im.leet.api.macros.Macro;
import im.leet.api.macros.constructor.MacroBlock;
import im.leet.api.render.RendererObject;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.ui.UIWidget;
import im.leet.utils.LogUtility;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.Rectangle;
import java.util.ArrayList;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MacrosWidget
extends UIWidget {
    Drag drag = new Drag();
    ArrayList<BlockSample> blocks = new ArrayList();
    ArrayList<BlockSample> samples = new ArrayList();
    public BlockSample currentSample = null;
    int insertingAt = -1;
    float scroll;
    float scrollAnim;
    Rectangle submit = new Rectangle();
    public SmoothStepAnimation animation = new SmoothStepAnimation(300, 1.0);

    public MacrosWidget() {
        for (MacroBlock macroBlock : Client.MACROS.getBlocks().getHandled()) {
            this.samples.add(new BlockSample(macroBlock, this, false));
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.drag.dragging) {
            this.x = (float)mouseX - this.drag.dX;
            this.y = (float)mouseY - this.drag.dY;
        }
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        MatrixStack stack = Client.RENDERER.getStack();
        float alpha = system.alpha();
        float anim = 1.0f - this.animation.getOutput();
        system.alpha(anim * alpha);
        stack.method_22903();
        MathUtility.scale(stack, this.x + this.width / 2.0f, this.y + this.height / 2.0f, 0.8f + anim * 0.2f);
        Client.RENDERER.outlined(this.x, this.y, this.width, this.height);
        Client.RENDERER.text("Macros constructor", this.x + 8.0f, this.y + 8.0f, TextureUse.SFMEDIUM, 9.0f, ClientColors.FORE_COLOR);
        system.push(this.x, this.y + 30.0f, this.width, this.height - 60.0f);
        float offy = 30.0f;
        for (BlockSample object : this.samples) {
            object.bound(this.x + this.width / 2.0f + 4.0f, this.y + offy, this.width / 2.0f - 8.0f, 15.0f).render(mouseX, mouseY);
            offy += object.getHeight() + 2.0f;
        }
        int i = 0;
        float offy2 = 30.0f;
        float lastY = -1.0f;
        for (BlockSample object : this.blocks) {
            if (object == this.currentSample) continue;
            object.bound(this.x + 4.0f, this.y + offy2 + this.scroll, this.width / 2.0f - 8.0f, 15.0f);
            if (this.currentSample != null && !this.drag.dragging && MathUtility.delta(mouseY, object.getY() + 16.0f) < 10.0f) {
                offy2 += 27.0f;
                this.insertingAt = i;
            }
            object.render(mouseX, mouseY);
            offy2 += object.getHeight() + 12.0f;
            if (object != this.blocks.getLast()) {
                Client.RENDERER.rect(this.x + this.width / 4.0f - 0.5f, this.y + offy2 - 10.0f + this.scrollAnim, 1.0f, 8.0f, new Vector4f(0.0f), 0.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
            }
            lastY = object.lerpedY;
            ++i;
        }
        if (this.currentSample != null && (float)mouseX < this.x + this.width / 2.0f) {
            Client.RENDERER.outline(this.x + 4.0f, this.y + 30.0f + (float)(27 * (this.insertingAt + 1)) + this.scrollAnim, this.width / 2.0f - 8.0f, 15.0f, 1.0f, new Vector4f(4.0f), new Vector2f(1.0f), ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        }
        system.pop();
        this.submit.bound(this.x + 6.0f, this.y + this.height - 26.0f, 100.0f, 22.0f);
        Client.RENDERER.rect(this.submit, new Vector4f(4.0f), 1.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Client.RENDERER.textCentered("Submit", this.submit.getX() + this.submit.getWidth() / 2.0f - 1.0f, this.submit.getY() + this.submit.getHeight() / 2.0f - Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f) / 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        if (this.currentSample != null) {
            this.currentSample.bound((float)mouseX - this.currentSample.dX, (float)mouseY - this.currentSample.dY, this.currentSample.getWidth(), this.currentSample.getHeight()).render(mouseX, mouseY);
        } else {
            this.insertingAt = -1;
        }
        stack.method_22909();
        system.alpha(alpha);
        this.scrollAnim = MathUtility.linearFps(this.scrollAnim, this.scroll, 10.0f);
        if ((double)anim < 0.1 && this.animation.getDirection() == Direction.FORWARDS) {
            this.shouldRemove = true;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.hover((int)mouseX, (int)mouseY)) {
            this.scroll += (float)(verticalAmount * 15.0);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean click(int mouseX, int mouseY, int button) {
        if (this.submit.hovered(mouseX, mouseY)) {
            Macro macro = Client.MACROS.addFromBuilder(this.blocks);
            macro.setKey(85);
            LogUtility.debug("created macros from " + this.blocks.size() + " insn");
            this.blocks.clear();
        }
        if (MathUtility.mouseIn(this.x, this.y + 30.0f, this.width, this.height - 60.0f, mouseX, mouseY)) {
            for (BlockSample object : this.samples) {
                if (!object.click(mouseX, mouseY, button)) continue;
                return true;
            }
            for (BlockSample object : this.blocks) {
                if (!object.click(mouseX, mouseY, button)) continue;
                return true;
            }
        }
        if (this.hover(mouseX, mouseY)) {
            this.drag.dragging = true;
            this.drag.dX = (float)mouseX - this.x;
            this.drag.dY = (float)mouseY - this.y;
            return true;
        }
        this.animation.setDirection(Direction.FORWARDS);
        return true;
    }

    @Override
    public void release(int button) {
        if (this.currentSample != null) {
            if (!this.currentSample.isAdded && this.currentSample.getX() < this.x + this.width / 2.0f) {
                BlockSample sample = new BlockSample(this.currentSample.original, this, true);
                if (this.insertingAt != -1) {
                    this.blocks.add(this.insertingAt + 1, sample);
                } else {
                    this.blocks.add(sample);
                }
            } else if (this.currentSample.isAdded && this.currentSample.getX() > this.x + this.width / 2.0f) {
                this.blocks.remove(this.currentSample);
            }
            if (this.currentSample.isAdded && this.currentSample.getX() < this.x + this.width / 2.0f && this.insertingAt != -1) {
                this.blocks.remove(this.currentSample);
                this.blocks.add(this.insertingAt + 1, this.currentSample);
            }
        }
        this.currentSample = null;
        for (BlockSample object : this.samples) {
            object.release(button);
        }
        for (BlockSample object : this.blocks) {
            object.release(button);
        }
        this.drag.dragging = false;
        super.release(button);
    }

    public static class BlockSample
    extends RendererObject {
        private MacroBlock original;
        private MacrosWidget parent;
        public float dX;
        public float dY;
        public float lerpedX = -999.0f;
        public float lerpedY = -999.0f;
        public boolean isAdded = false;

        public BlockSample(MacroBlock original, MacrosWidget parent, boolean isAdded) {
            this.original = original;
            this.parent = parent;
            this.isAdded = isAdded;
        }

        @Override
        public void render(int mouseX, int mouseY) {
            float dx = this.x - this.parent.x;
            float dy = this.y - this.parent.y;
            if (this.isAdded && this.lerpedX != -999.0f && this.lerpedY != -999.0f) {
                this.lerpedX = MathUtility.linearFps(this.lerpedX, dx, 10.0f);
                this.lerpedY = MathUtility.linearFps(this.lerpedY, dy, 10.0f);
            } else {
                this.lerpedX = this.x - this.parent.x;
                this.lerpedY = this.y - this.parent.y;
            }
            Client.RENDERER.rect(this.parent.x + this.lerpedX, this.parent.y + this.lerpedY, this.width, this.height, new Vector4f(0.0f), 0.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
            Client.RENDERER.text(this.original.renderName(), this.parent.x + this.lerpedX + 4.0f, this.parent.y + this.lerpedY + this.height / 2.0f - Client.RENDERER.textHeight(TextureUse.SFMEDIUM, 8.0f) / 2.0f, TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        }

        @Override
        public boolean click(int mouseX, int mouseY, int button) {
            if (!this.hover(mouseX, mouseY)) {
                return false;
            }
            this.dX = (float)mouseX - this.x;
            this.dY = (float)mouseY - this.y;
            this.parent.currentSample = this;
            return true;
        }

        @Override
        public void release(int button) {
            super.release(button);
        }

        public MacroBlock getOriginal() {
            return this.original;
        }
    }
}

