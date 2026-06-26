/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.InputUtil$Type
 *  net.minecraft.text.MutableText
 *  net.minecraft.text.Text
 *  org.lwjgl.glfw.GLFW
 */
package im.leet.utils.client;

import im.leet.utils.math.ColorUtility;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class TextUtility {
    static ArrayList<Integer> lettersNormal = new ArrayList();
    static ArrayList<Integer> lettersSmall = new ArrayList();

    public static Text applyGradient(String text, Color first, Color second) {
        MutableText result = Text.method_43473();
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            Color color = ColorUtility.linear(first, second, (float)Math.sin((float)i / 10.0f));
            result.method_10852((Text)Text.method_30163((String)String.valueOf(c)).method_27661().method_54663(color.getRGB()));
        }
        return result;
    }

    public static String keyToString(int key) {
        if (key < 0) {
            return "None";
        }
        String k = GLFW.glfwGetKeyName((int)key, (int)0);
        return key > 7 ? (k == null ? InputUtil.Type.field_1668.method_1447(key).method_27445().getString() : k.toUpperCase()) : InputUtil.Type.field_1672.method_1447(key).method_27445().getString();
    }

    public static String smallToNormal(String smallCaps) {
        StringBuilder builder = new StringBuilder();
        for (char ch : smallCaps.toCharArray()) {
            builder.append(TextUtility.charToNormal(ch));
        }
        return builder.toString().replace('\ua731', 's');
    }

    public static char charToNormal(char ch) {
        if (lettersSmall.isEmpty() || lettersNormal.isEmpty() || !lettersSmall.contains(ch)) {
            return ch;
        }
        return (char)lettersNormal.get(lettersSmall.indexOf(ch)).intValue();
    }

    public static String ticksToTime(int ticks) {
        int s = Math.round((float)ticks * 0.05f);
        if (s > 3600) {
            int h = s / 3600;
            int m = (s %= 3600) / 60;
            return String.format("%02d:%02d:%02d", h, m, s %= 60);
        }
        if (s > 60) {
            int m = s / 60;
            return String.format("%02d:%02d", m, s %= 60);
        }
        return String.format("00:%02d", s);
    }

    private TextUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        for (char ch : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            lettersNormal.add(Integer.valueOf(ch));
        }
        for (char ch : "\u1d00\u0299\u1d04\u1d05\u1d07\u0493\u0262\u029c\u026a\u1d0a\u1d0b\u029f\u1d0d\u0274\u1d0f\u1d18\u1d0f\u0280s\u1d1b\u1d1c\u1d20\u1d21x\u028f\u1d22".toCharArray()) {
            lettersSmall.add(Integer.valueOf(ch));
        }
    }
}

