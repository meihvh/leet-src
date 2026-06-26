/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package im.leet.utils.math;

import im.leet.utils.math.MathUtility;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.math.MathHelper;

public final class ColorUtility {
    private static final Map<Integer, Color> COLOR_CACHE = new HashMap<Integer, Color>();

    public static Color injectAlpha(Color color, float alpha) {
        int rgba = color.getRGB() & 0xFFFFFF | Math.clamp((long)((int)alpha), 0, 255) << 24;
        return COLOR_CACHE.computeIfAbsent(rgba, k -> new Color(rgba, true));
    }

    public static Color linear(Color src, Color dest, float amount) {
        amount = MathHelper.method_15363((float)amount, (float)0.0f, (float)1.0f);
        int red = (int)MathUtility.linear(src.getRed(), dest.getRed(), amount);
        int green = (int)MathUtility.linear(src.getGreen(), dest.getGreen(), amount);
        int blue = (int)MathUtility.linear(src.getBlue(), dest.getBlue(), amount);
        int alpha = (int)MathUtility.linear(src.getAlpha(), dest.getAlpha(), amount);
        return new Color(MathHelper.method_15340((int)red, (int)0, (int)255), MathHelper.method_15340((int)green, (int)0, (int)255), MathHelper.method_15340((int)blue, (int)0, (int)255), MathHelper.method_15340((int)alpha, (int)0, (int)255));
    }

    public static Color contrast(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    public static Color brightness(Color color, int amount) {
        return new Color(Math.clamp((long)(color.getRed() - amount), 0, 255), Math.clamp((long)(color.getGreen() - amount), 0, 255), Math.clamp((long)(color.getBlue() - amount), 0, 255), color.getAlpha());
    }

    public static Color transfusionEffect(int speed, int index, Color ... colors) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        int colorIndex = (int)((float)angle / 360.0f * (float)colors.length);
        if (colorIndex == colors.length) {
            --colorIndex;
        }
        Color color1 = colors[colorIndex];
        Color color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1];
        return ColorUtility.linear(color1, color2, (float)angle / 360.0f * (float)colors.length - (float)colorIndex);
    }

    public static Color rainbowEffect(int speed, int index) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        return Color.getHSBColor((float)angle / 360.0f, 0.8f, 0.8f);
    }

    public static Color rainbowEffectBright(int speed, int index) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        return Color.getHSBColor((float)angle / 360.0f, 0.8f, 1.0f);
    }

    public static double distance(int color1, int color2) {
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 & 0xFF;
        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        double rmean = (double)(r1 + r2) / 2.0;
        double r = r1 - r2;
        double g = g1 - g2;
        double b = b1 - b2;
        double weightR = 2.0 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2.0 + (255.0 - rmean) / 256.0;
        return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b);
    }

    public static Color max(Color a, Color b) {
        if (a.getAlpha() > b.getAlpha() && a.getRed() > b.getRed() && a.getGreen() > b.getGreen() && a.getBlue() > b.getBlue()) {
            return b;
        }
        return a;
    }

    private ColorUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

