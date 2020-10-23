package com.photo.sketch.editor;

import android.graphics.Bitmap;
import java.util.HashMap;

public final class FilterSizeHelper {
    public static final HashMap<Integer, String> hashmap1FSH = new LineHelper();
    public static final HashMap<Integer, String> hashmap2FSH = new LineHelper_2();
    public static int[] intArrayFSH = new int[]{640, 480, 320};
    private static int intFSH = 0;

    public static int getFSHbitmap(Bitmap bitmap, int i, int i2) {
        return Math.min(bitmap.getWidth(), bitmap.getHeight()) > 480 ? i2 : i;
    }

    public static void setIntFSH(int i) {
        intFSH = i;
    }

    public static int getIntFSH() {
        return intFSH;
    }
}
