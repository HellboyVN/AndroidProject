package com.proframeapps.videoframeplayer.util;

import java.nio.ByteBuffer;

public class NativeHelper {
    public static int SETTING_AA_FILTER_LENGTH = 1;
    public static int SETTING_OVERLAP_MS = 5;
    public static int SETTING_SEEKWINDOW_MS = 4;
    public static int SETTING_SEQUENCE_MS = 3;
    public static int SETTING_USE_AA_FILTER = 0;
    public static int SETTING_USE_QUICKSEEK = 2;
    long mNativeContext;
    public NativeHelper() {
    }
    static {
        System.loadLibrary("soundtouch");
        System.loadLibrary("native-helper");
        native_init();
    }
    public static final native String getVersionString();

    private static final native void native_init();

    private final native void native_release();

    private final native void native_setup(int i, int i2);

    public native void flush();

    public native int getSetting(int i);

    public native void putSamples(ByteBuffer byteBuffer, int i);

    public native int receiveSamples(ByteBuffer byteBuffer, int i);

    public final native void setRate(float f);

    public native void setSetting(int i, int i2);


    public void initialize(int sampleCount, int channels) {
        native_setup(sampleCount, channels);
    }

    public void release() {
        native_release();
    }
}
