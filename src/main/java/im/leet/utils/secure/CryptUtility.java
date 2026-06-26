/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.secure;

public class CryptUtility {
    static final byte[] MAGIC = new byte[]{-68, -52, -51, -35};

    public static byte[] proccessXOR(byte[] input) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = (byte)(input[i] ^ 1337 * i);
        }
        return output;
    }
}

