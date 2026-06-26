# Native wrappers

No Java `native` keyword declarations were recovered from the decompiled project.

What exists instead:
- `src/main/java/im/leet/utils/jni/DwmApi.java`
  Windows Desktop Window Manager wrapper via JNA (`Native.load("dwmapi", ...)`)
- `src/main/java/im/leet/utils/jni/NtDll.java`
  Windows `ntdll` wrapper via JNA (`Native.load("ntdll", ...)`)

Notes:
- These are Java-to-native bindings, not JNI methods declared with the `native` keyword.
- They rely on JNA and only make sense on Windows.
- `DwmApi` exposes `DwmSetWindowAttribute` and `DwmExtendFrameIntoClientArea`.
- `NtDll` exposes `RtlGetNtVersionNumbers`.
