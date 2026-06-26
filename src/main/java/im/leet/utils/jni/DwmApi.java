/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Library
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 *  com.sun.jna.PointerType
 *  com.sun.jna.Structure
 *  com.sun.jna.platform.win32.WinDef$HWND
 *  com.sun.jna.ptr.IntByReference
 *  org.lwjgl.glfw.GLFWNativeWin32
 *  org.lwjgl.system.NativeType
 */
package im.leet.utils.jni;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import java.util.List;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.system.NativeType;
import ru.aloweeed.perfect.Ignored;

@Ignored
public interface DwmApi
extends Library {
    public static final DwmApi INSTANCE = DwmApi.isWindows() ? (DwmApi)Native.load((String)"dwmapi", DwmApi.class) : null;
    public static final int INT_SIZE = 4;
    public static final int BOOL_FALSE = 0;
    public static final int BOOL_TRUE = 1;
    public static final int DWMWA_USE_IMMERSIVE_DARK_MODE = 20;
    public static final int DWMWA_WINDOW_CORNER_PREFERENCE = 33;
    public static final int DWMWA_BORDER_COLOR = 34;
    public static final int DWMWA_CAPTION_COLOR = 35;
    public static final int DWMWA_TEXT_COLOR = 36;
    public static final int DWMWA_SYSTEMBACKDROP_TYPE = 38;
    public static final int DWMWA_COLOR_NONE = -2;
    public static final int DWMWA_COLOR_DEFAULT = 0;

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    @NativeType(value="HRESULT")
    public int DwmSetWindowAttribute(WinDef.HWND var1, int var2, PointerType var3, int var4);

    public static void updateDwm(boolean fullscreen, long window) {
        if (INSTANCE == null) {
            return;
        }
        WinDef.HWND hwnd = new WinDef.HWND(Pointer.createConstant((long)GLFWNativeWin32.glfwGetWin32Window((long)window)));
        if (fullscreen) {
            DwmApi.disableWindowEffect(hwnd);
            return;
        }
        INSTANCE.DwmSetWindowAttribute(hwnd, 34, (PointerType)new IntByReference(0x353535), 4);
        INSTANCE.DwmSetWindowAttribute(hwnd, 35, (PointerType)new IntByReference(0x202020), 4);
        INSTANCE.DwmSetWindowAttribute(hwnd, 36, (PointerType)new IntByReference(0xFFFFFF), 4);
    }

    public static void disableWindowEffect(WinDef.HWND hwnd) {
        if (INSTANCE == null) {
            return;
        }
        INSTANCE.DwmSetWindowAttribute(hwnd, 38, (PointerType)new IntByReference(DWM_SYSTEMBACKDROP_TYPE.DWMSBT_AUTO.ordinal()), 4);
        INSTANCE.DwmSetWindowAttribute(hwnd, 33, (PointerType)new IntByReference(DWM_WINDOW_CORNER_PREFERENCE.DWMWCP_DEFAULT.ordinal()), 4);
        INSTANCE.DwmSetWindowAttribute(hwnd, 34, (PointerType)new IntByReference(0), 4);
        INSTANCE.DwmSetWindowAttribute(hwnd, 35, (PointerType)new IntByReference(0), 4);
        INSTANCE.DwmSetWindowAttribute(hwnd, 36, (PointerType)new IntByReference(0), 4);
    }

    private static int convert(int color) {
        int b = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int r = color & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    @NativeType(value="HRESULT")
    public int DwmExtendFrameIntoClientArea(WinDef.HWND var1, MARGINS var2);

    public static enum DWM_SYSTEMBACKDROP_TYPE {
        DWMSBT_AUTO("auto"),
        DWMSBT_NONE("none"),
        DWMSBT_MAINWINDOW("mica"),
        DWMSBT_TRANSIENTWINDOW("acrylic"),
        DWMSBT_TABBEDWINDOW("tabbed");

        public final String translate;

        private DWM_SYSTEMBACKDROP_TYPE(String translate) {
            this.translate = translate;
        }
    }

    public static enum DWM_WINDOW_CORNER_PREFERENCE {
        DWMWCP_DEFAULT("default"),
        DWMWCP_DONOTROUND("do_not_round"),
        DWMWCP_ROUND("round"),
        DWMWCP_ROUNDSMALL("round_small");

        public final String translate;

        private DWM_WINDOW_CORNER_PREFERENCE(String translate) {
            this.translate = translate;
        }
    }

    public static class MARGINS
    extends Structure {
        public int cxLeftWidth;
        public int cxRightWidth;
        public int cyTopHeight;
        public int cyBottomHeight;

        public MARGINS(int cxLeftWidth, int cxRightWidth, int cyTopHeight, int cyBottomHeight) {
            this.cxLeftWidth = cxLeftWidth;
            this.cxRightWidth = cxRightWidth;
            this.cyTopHeight = cyTopHeight;
            this.cyBottomHeight = cyBottomHeight;
        }

        protected List<String> getFieldOrder() {
            return List.of("cxLeftWidth", "cxRightWidth", "cyTopHeight", "cyBottomHeight");
        }
    }
}

