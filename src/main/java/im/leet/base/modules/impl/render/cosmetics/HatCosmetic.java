/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.feature.FeatureRenderer
 *  net.minecraft.client.render.entity.feature.FeatureRendererContext
 *  net.minecraft.client.render.entity.model.ModelWithHead
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.util.math.MatrixStack$Entry
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package im.leet.base.modules.impl.render.cosmetics;

import im.leet.Client;
import im.leet.MinecraftHolder;
import im.leet.api.render.system.ClientPipelines;
import im.leet.base.modules.impl.render.Cosmetics;
import im.leet.base.settings.impl.checkbox.CheckBox;
import im.leet.base.settings.impl.choice.Choice;
import im.leet.base.settings.impl.colorsetting.ColorSetting;
import im.leet.base.settings.impl.group.Group;
import im.leet.base.settings.impl.multienum.MultiEnumSetting;
import im.leet.utils.client.ClientSettings;
import im.leet.utils.client.mixin.IPlayerEntityRenderState;
import im.leet.utils.math.ColorUtility;
import im.leet.utils.math.MathShortcuts;
import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class HatCosmetic
extends Choice {
    public static final HatCosmetic INSTANCE = new HatCosmetic();
    final CheckBox line = this.checkbox("Line", true);
    final MultiEnumSetting<Target> target = this.multiEnumSetting("Target", new Target[]{Target.Self, Target.Friends});
    final CheckBox useCustomColor = this.checkbox("Use custom colors", false);
    final Group selfColors;
    final ColorSetting selfColor1;
    final ColorSetting selfColor2;
    final Group friendColors;
    final ColorSetting friendColor1;
    final ColorSetting friendColor2;
    final Group otherColors;
    final ColorSetting otherColor1;
    final ColorSetting otherColor2;

    private HatCosmetic() {
        super("Hat");
        Supplier[] supplierArray = new Supplier[2];
        supplierArray[0] = this.useCustomColor::get;
        supplierArray[1] = () -> this.target.get(Target.Self);
        this.selfColors = (Group)this.group("Self colors").visible(supplierArray);
        this.selfColor1 = this.selfColors.colorSetting("Color 1", new Color(0));
        this.selfColor2 = this.selfColors.colorSetting("Color 2", new Color(0));
        Supplier[] supplierArray2 = new Supplier[2];
        supplierArray2[0] = this.useCustomColor::get;
        supplierArray2[1] = () -> this.target.get(Target.Friends);
        this.friendColors = (Group)this.group("Friend colors").visible(supplierArray2);
        this.friendColor1 = this.friendColors.colorSetting("Color 1", new Color(0));
        this.friendColor2 = this.friendColors.colorSetting("Color 2", new Color(0));
        Supplier[] supplierArray3 = new Supplier[2];
        supplierArray3[0] = this.useCustomColor::get;
        supplierArray3[1] = () -> this.target.get(Target.Others);
        this.otherColors = (Group)this.group("Others colors").visible(supplierArray3);
        this.otherColor1 = this.otherColors.colorSetting("Color 1", new Color(0));
        this.otherColor2 = this.otherColors.colorSetting("Color 2", new Color(0));
    }

    Color getColor(int d, Color color1, Color color2) {
        if (this.useCustomColor.get()) {
            return ColorUtility.transfusionEffect(ClientSettings.INSTANCE.colorSpeed.getInt(), d, color1, color2);
        }
        return ClientSettings.INSTANCE.getColor(d);
    }

    void draw(MatrixStack matrices, VertexConsumerProvider provider, Color color1, Color color2, boolean hasHelmet) {
        Color color;
        float z;
        float x;
        int d;
        float radius = 0.57f;
        float y = hasHelmet ? 0.49f : 0.42f;
        MatrixStack.Entry matrix = matrices.method_23760();
        VertexConsumer filler = provider.getBuffer((RenderLayer)ClientPipelines.WORLD_TRIANGLE_FAN);
        filler.method_56824(matrix, 0.0f, y + 0.4f, 0.0f).method_39415(color1.getRGB());
        for (d = 0; d <= 360; ++d) {
            x = MathShortcuts.sin((float)d * ((float)Math.PI / 180)) * radius;
            z = -MathShortcuts.cos((float)d * ((float)Math.PI / 180)) * radius;
            color = ColorUtility.injectAlpha(this.getColor(d, color1, color2), 100.0f);
            filler.method_56824(matrix, x, y, z).method_39415(color.getRGB());
        }
        filler.method_56824(matrix, 0.0f, y + 0.4f, 0.0f).method_39415(color1.getRGB());
        for (d = 360; d >= 0; --d) {
            x = MathShortcuts.sin((float)d * ((float)Math.PI / 180)) * radius;
            z = -MathShortcuts.cos((float)d * ((float)Math.PI / 180)) * radius;
            color = ColorUtility.injectAlpha(this.getColor(d, color1, color2), 100.0f);
            filler.method_56824(matrix, x, y, z).method_39415(color.getRGB());
        }
        if (this.line.get()) {
            VertexConsumer liner = provider.getBuffer((RenderLayer)RenderLayer.field_29456);
            Vector3f last = new Vector3f();
            for (int d2 = 0; d2 <= 360; ++d2) {
                float x2 = MathShortcuts.sin((float)d2 * ((float)Math.PI / 180)) * radius;
                float z2 = -MathShortcuts.cos((float)d2 * ((float)Math.PI / 180)) * radius;
                Color color3 = ColorUtility.injectAlpha(this.getColor(d2, color1, color2), 255.0f);
                Vector3f normal = MathUtility.getNormal(last.x, last.y, last.z, x2, y, z2);
                liner.method_56824(matrix, x2, y, z2).method_39415(color3.getRGB()).method_60831(matrix, normal.x, normal.y, normal.z);
                last = new Vector3f(x2, y, z2);
            }
        }
    }

    public FeatureRenderer featureRenderer(FeatureRendererContext ctx) {
        return new FeatureRenderer(ctx);
    }

    static enum Target {
        Self,
        Friends,
        Others;

    }

    class FeatureRenderer
    extends net.minecraft.client.render.entity.feature.FeatureRenderer {
        public FeatureRenderer(FeatureRendererContext context) {
            super(context);
        }

        public void method_4199(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EntityRenderState state, float limbAngle, float limbDistance) {
            Color color2;
            Color color1;
            if (!(HatCosmetic.this.isEnabled() && Cosmetics.INSTANCE.isEnabled() && state instanceof IPlayerEntityRenderState)) {
                return;
            }
            IPlayerEntityRenderState rs = (IPlayerEntityRenderState)state;
            PlayerEntity player = rs.leet$getEntity();
            if (player == MinecraftHolder.mc.field_1724) {
                if (!HatCosmetic.this.target.get(Target.Self) || MinecraftHolder.mc.field_1690.method_31044().method_31034()) {
                    return;
                }
            } else {
                if (!HatCosmetic.this.target.get(Target.Others) && !Client.FRIENDS.isFriend(player)) {
                    return;
                }
                if (!HatCosmetic.this.target.get(Target.Friends) && Client.FRIENDS.isFriend(player)) {
                    return;
                }
            }
            matrices.method_22903();
            ((ModelWithHead)this.method_17165()).method_2838().method_22703(matrices);
            matrices.method_22907((Quaternionfc)RotationAxis.field_40717.rotationDegrees(180.0f));
            matrices.method_22907((Quaternionfc)RotationAxis.field_40715.rotationDegrees(90.0f));
            if (HatCosmetic.this.useCustomColor.get()) {
                if (player == MinecraftHolder.mc.field_1724) {
                    color1 = HatCosmetic.this.selfColor1.get();
                    color2 = HatCosmetic.this.selfColor2.get();
                } else if (Client.FRIENDS.isFriend(player)) {
                    color1 = HatCosmetic.this.friendColor1.get();
                    color2 = HatCosmetic.this.friendColor2.get();
                } else {
                    color1 = HatCosmetic.this.otherColor1.get();
                    color2 = HatCosmetic.this.otherColor2.get();
                }
            } else {
                color1 = ClientSettings.INSTANCE.getColor(0);
                color2 = null;
            }
            HatCosmetic.this.draw(matrices, vertexConsumers, color1, color2, !player.method_6118(EquipmentSlot.field_6169).method_7960());
            matrices.method_22909();
        }
    }
}

