/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 *  org.joml.Vector2f
 *  org.joml.Vector4f
 */
package im.leet.base.hud.ui.impl;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.drags.Drag;
import im.leet.api.events.EventBus;
import im.leet.api.events.list.EventAttack;
import im.leet.api.render.system.IconUse;
import im.leet.api.render.system.TextureUse;
import im.leet.api.render.system.sys2d.CRenderSystem;
import im.leet.api.render.system.sys2d.TextureAtlas;
import im.leet.api.ui.particles.Particles2DEngine;
import im.leet.base.hud.ui.HudElement;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.text.TextSetting;
import im.leet.utils.animations.Direction;
import im.leet.utils.animations.impl.SmoothStepAnimation;
import im.leet.utils.client.ClientColors;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.client.HealthUtility;
import im.leet.utils.client.TextAnimation;
import im.leet.utils.client.targets.TargetsUtility;
import im.leet.utils.math.MathUtility;
import im.leet.utils.math.TimeUtility;
import im.leet.utils.network.NetworkUtility;
import java.awt.Color;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TargetHudElement
extends HudElement
implements MinecraftHolder {
    CheckBox killeffect;
    TextSetting killtext;
    CheckBox particles;
    EventBus<EventAttack> onAttack;
    float healthAnim;
    float absorptionAnim;
    float health_factor;
    float killanimation;
    TimeUtility killTime;
    boolean killAnim;
    LivingEntity prevTarget;
    TimeUtility scrollDelay;
    SmoothStepAnimation scrollAnimation;
    TextAnimation textAnimation;
    Particles2DEngine engine;

    public TargetHudElement(Drag drag) {
        super("TargetHud", drag);
        this.killeffect = this.settings.checkbox("Kill effect", false);
        this.killtext = this.settings.text("Kill text", "\u0421\u041e\u0421\u0410\u041b");
        this.particles = this.settings.checkbox("Particles", false);
        this.onAttack = event -> {
            if (TargetsUtility.getTarget() != null && event.target == TargetsUtility.getTarget()) {
                this.health_factor = MathUtility.random(0.0f, 20.0f);
            }
        };
        this.healthAnim = 0.0f;
        this.absorptionAnim = 0.0f;
        this.health_factor = 0.0f;
        this.killanimation = 0.0f;
        this.killTime = new TimeUtility();
        this.killAnim = false;
        this.prevTarget = null;
        this.scrollDelay = new TimeUtility();
        this.scrollAnimation = new SmoothStepAnimation(1000, 1.0);
        this.textAnimation = new TextAnimation().texts("\u0421\u041e\u0421\u0410\u041b").delay(8).interval(5);
        this.engine = new Particles2DEngine();
        Client.EVENTS.register(this);
        this.animation.setDuration(600);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float tWidth;
        this.textAnimation.texts(this.killtext.getText());
        boolean isInChat = TargetHudElement.mc.field_1755 instanceof ChatScreen;
        boolean shown = isInChat || TargetsUtility.getTarget() != null || (double)this.killanimation > 0.1;
        TextureAtlas atlas = Client.RENDERER.getCrenderSystem().getAtlas();
        if (TargetsUtility.getTarget() != null) {
            this.prevTarget = TargetsUtility.getTarget();
        } else if (isInChat || this.prevTarget == null) {
            this.prevTarget = TargetHudElement.mc.field_1724;
        }
        boolean isFuntime = NetworkUtility.is("funtime");
        boolean healthHidden = isFuntime && this.prevTarget.method_5767();
        CRenderSystem system = Client.RENDERER.getCrenderSystem();
        this.animation.setDirection(shown ? Direction.BACKWARDS : Direction.FORWARDS);
        this.healthAnim = Math.max(1.0f, MathUtility.linearFps(this.healthAnim, healthHidden ? this.health_factor : HealthUtility.get(this.prevTarget), 10.0f));
        this.absorptionAnim = MathUtility.linearFps(this.absorptionAnim, this.prevTarget.method_6067(), 10.0f);
        float anim = 1.0f - this.animation.getOutput();
        if (anim < 0.05f) {
            return;
        }
        float prevAlpha = Client.RENDERER.getCrenderSystem().alpha();
        system.useCircle(1.0f - anim);
        Client.RENDERER.drawHudRect(this.x, this.y, this.width, this.height);
        system.alpha(anim);
        system.push(this.x, this.y, this.width, this.height);
        Client.RENDERER.text(IconUse.PLAYER, this.x + 8.0f - 20.0f * this.animation.getOutput() - 40.0f * this.killanimation, this.y + 6.0f, TextureUse.ICONS, 14.0f, ClientColors.FORE_COLOR);
        if (this.scrollAnimation.isDone() && this.scrollDelay.reached(1000L)) {
            this.scrollDelay.reset();
            this.scrollAnimation.changeDirection();
        }
        boolean isOversize = (tWidth = Client.RENDERER.textWidth(this.prevTarget.method_5477().getString(), TextureUse.SFMEDIUM, 8.0f)) > 55.0f;
        float tScroll = this.scrollAnimation.getOutput();
        float delta = tWidth - 55.0f;
        float offset = delta * tScroll;
        Client.RENDERER.text(this.prevTarget.method_5477().getString(), this.x + 28.0f - (isOversize ? offset : 0.0f), this.y + 5.0f - 20.0f * this.animation.getOutput(), TextureUse.SFMEDIUM, 8.0f, ClientColors.FORE_COLOR);
        float healthFactor = Math.min(1.0f, this.healthAnim / this.prevTarget.method_6063());
        float absorptionFactor = this.absorptionAnim / 20.0f;
        MutableText abstractionText = this.prevTarget.method_6067() > 0.0f && !isFuntime ? Text.method_30163((String)String.format(" + %.1f", Float.valueOf(this.absorptionAnim))).method_27661().method_54663(ClientColors.GOLDEN_HP.getRGB()) : Text.method_43473();
        Text finalHealthText = Text.method_30163((String)String.format("Health: %s", healthHidden ? "?" : Float.valueOf((float)Math.round(this.healthAnim * 10.0f) / 10.0f)));
        MutableText combinedText = finalHealthText.method_27661().method_10852((Text)abstractionText);
        Client.RENDERER.text((Text)combinedText, this.x + 28.0f, this.y + 16.0f - 20.0f * this.animation.getOutput(), TextureUse.SFMEDIUM, 6.0f);
        float barX = this.x + 6.0f;
        float barY = this.y + 28.0f + 20.0f * this.animation.getOutput();
        float barWidth = this.width - 12.0f;
        float barHeight = 4.0f;
        Client.RENDERER.rect(barX, barY, barWidth, barHeight, new Vector4f(3.0f), 1.0f, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE, ClientColors.GUI_STROKE);
        Color color1 = ClientSettings.INSTANCE.getColor(0);
        Color color2 = ClientSettings.INSTANCE.getColor(90);
        Client.RENDERER.rect(barX, barY, barWidth * healthFactor, barHeight, new Vector4f(3.0f), 1.0f, color1, color1, color2, color2);
        if (this.prevTarget.method_6067() > 0.0f && !NetworkUtility.is("funtime")) {
            Client.RENDERER.rect(barX, barY, barWidth * absorptionFactor, barHeight, new Vector4f(3.0f), 1.0f, ClientColors.GOLDEN_HP, ClientColors.GOLDEN_HP, ClientColors.GOLDEN_HP, ClientColors.GOLDEN_HP);
        }
        if (this.prevTarget.field_6235 > 0) {
            this.engine.addParticle(new Vector2f(barX + barWidth * healthFactor - 5.0f, barY + barHeight / 2.0f), new Vector2f(MathUtility.random(0.4f, 0.7f)), 90 + MathUtility.random(-10, 10), 0.0f, 600L);
        }
        system.alpha(prevAlpha * this.killanimation);
        float pWidth = Client.RENDERER.textWidth(this.textAnimation.current(), TextureUse.SFMEDIUM, 10.0f) + 4.0f;
        Client.RENDERER.rect(this.x + this.width / 2.0f - pWidth / 2.0f - 1.0f, this.y + this.height / 2.0f - 5.0f, pWidth, 11.0f, new Vector4f(0.0f), 1.0f, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR, ClientColors.BACK_COLOR);
        Client.RENDERER.text(this.textAnimation.get(), this.x + this.width / 2.0f - pWidth / 2.0f - 1.0f, this.y + this.height / 2.0f - 5.0f, TextureUse.SFMEDIUM, 10.0f, Color.RED);
        system.alpha(prevAlpha);
        system.pop();
        this.engine.render();
        system.alpha(prevAlpha);
        if (!(TargetsUtility.getLastTarget() == null || this.prevTarget instanceof ClientPlayerEntity || !this.killeffect.get() || TargetsUtility.getLastTarget() != this.prevTarget || this.prevTarget.field_6213 <= 0 && this.prevTarget.method_5805() || this.killAnim || this.killTime.reached(900L))) {
            this.killAnim = true;
            this.killTime.reset();
            this.textAnimation.reset();
        }
        if (this.killTime.reached(1000L) && this.textAnimation.done()) {
            this.killAnim = false;
        }
        if (!this.textAnimation.done()) {
            this.killTime.reset();
        }
        if (TargetsUtility.getTarget() != null && TargetsUtility.getTarget().field_6235 > 0) {
            this.killTime.reset();
        }
        this.textAnimation.delay(4);
        system.useCircle(0.0f);
        this.killanimation = MathUtility.linearFps(this.killanimation, this.killAnim && this.killeffect.get() ? 1.0f : 0.0f, 5.0f);
    }
}

