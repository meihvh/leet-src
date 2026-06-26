/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL30
 */
package im.leet.api.render.system.sys2d;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL30;

public class AttributeHelper {
    private static int totalSize = 0;
    private static List<Attribute> attributeOffsets = new ArrayList<Attribute>();
    public static int SIZE;

    public static int getTotalSize() {
        return totalSize;
    }

    public static void addAttribute(int size, int type, boolean normalized) {
        int typeSize = AttributeHelper.getTypeSize(type);
        int attributeSize = size * typeSize;
        int offset = totalSize;
        attributeOffsets.add(new Attribute(offset, type, size, normalized));
        totalSize += attributeSize;
        SIZE += size;
    }

    public static void createAttributes() {
        int index = 0;
        for (Attribute attribute : attributeOffsets) {
            GL30.glVertexAttribPointer((int)index, (int)attribute.getSize(), (int)attribute.getType(), (boolean)attribute.isNormalized(), (int)totalSize, (long)attribute.getOffset());
            GL30.glEnableVertexAttribArray((int)index);
            ++index;
        }
        attributeOffsets.clear();
        totalSize = 0;
    }

    private static int getTypeSize(int type) {
        switch (type) {
            case 5126: {
                return 4;
            }
            case 5124: 
            case 5125: {
                return 4;
            }
            case 5121: {
                return 1;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }

    public static class Attribute {
        private final int offset;
        private final int type;
        private final int size;
        private final boolean normalized;

        public boolean isNormalized() {
            return this.normalized;
        }

        public Attribute(int offset, int type, int size, boolean normalized) {
            this.offset = offset;
            this.type = type;
            this.size = size;
            this.normalized = normalized;
        }

        public int getOffset() {
            return this.offset;
        }

        public int getType() {
            return this.type;
        }

        public int getSize() {
            return this.size;
        }
    }
}

