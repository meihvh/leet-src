/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Library
 *  com.sun.jna.Native
 *  com.sun.jna.ptr.IntByReference
 */
package im.leet.utils.jni;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import ru.aloweeed.perfect.Ignored;

@Ignored
public interface NtDll
extends Library {
    public static final NtDll INSTANCE = (NtDll)Native.load((String)"ntdll", NtDll.class);

    public void RtlGetNtVersionNumbers(IntByReference var1, IntByReference var2, IntByReference var3);

    public static void getBuildNumber() {
        IntByReference majorVersion = new IntByReference();
        IntByReference buildNumber = new IntByReference();
        INSTANCE.RtlGetNtVersionNumbers(majorVersion, new IntByReference(), buildNumber);
    }
}

