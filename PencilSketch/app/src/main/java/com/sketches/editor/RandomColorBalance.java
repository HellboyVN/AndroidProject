package com.sketches.editor;

import android.graphics.Color;
import java.util.Random;

public final class RandomColorBalance {
    static {
        Random random = new Random();
    }

    public static int getActionMask(int i) {
        if (i < 0) {
            return 0;
        }
        if (i > 255) {
            return 255;
        }
        return i;
    }

    public static int getActionColor(int i, int i2) {
        int i3 = (i >> 24) & 255;
        int i4 = (i >> 8) & 255;
        int i5 = i & 255;
        int i6 = (i2 >> 24) & 255;
        int i7 = (i2 >> 16) & 255;
        int i8 = (i2 >> 8) & 255;
        int i9 = i2 & 255;
        int min = Math.min((i >> 16) & 255, i7);
        int min2 = Math.min(i4, i8);
        i4 = Math.min(i5, i9);
        if (i3 != 255) {
            i3 = (i3 * 255) / 255;
            i5 = ((255 - i3) * i6) / 255;
            min = getActionMask(((min * i3) + (i7 * i5)) / 255);
            min2 = getActionMask(((min2 * i3) + (i8 * i5)) / 255);
            i4 = getActionMask(((i4 * i3) + (i9 * i5)) / 255);
            i3 = getActionMask(i3 + i5);
        }
        return ((min2 << 8) | ((min << 16) | (i3 << 24))) | i4;
    }

    public static void getColorRGB(int[] mArray, int[] mArray2, int i, int i2) {
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = mArray[(i4 * i) + i3];
                int i6 = mArray2[(i4 * i) + i3];
                int red = Color.red(i5);
                int green = Color.green(i5);
                i5 = Color.blue(i5);
                mArray2[(i4 * i) + i3] = Color.argb(255, getRandColorInt(red, Color.red(i6)), getRandColorInt(green, Color.green(i6)), getRandColorInt(i5, Color.blue(i6)));
            }
        }
    }

    private static int getRandColorInt(int i, int i2) {
        int i3 = ((i * i2) / (256 - i2)) + i;
        if (i3 > 255) {
            return 255;
        }
        return i3;
    }
}
