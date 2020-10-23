package com.sketches.editor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public final class SecondSketchFilter extends ImageFilerName {
    int value = 2;

    public final Bitmap getSimpleSketch(Bitmap bitmap) {
        int i;
        int j;
        int red;
        System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                int k = (i * width) + j;
                red = (((Color.red(mArray[k]) * 28) + (Color.green(mArray[k]) * 151)) + (Color.blue(mArray[k]) * 77)) >> 8;
                mArray[k] = Color.rgb(255 - red, 255 - red, 255 - red);
            }
        }
        int[] mArray2 = new int[(width * height)];
        MinBlurValue minBlurValue = new MinBlurValue();
        minBlurValue.minBlurVal = this.value;
        i = 0;
        System.currentTimeMillis();
        int i4 = 0;
        while (i4 < height) {
            red = 0;
            int i5 = i;
            while (red < width) {
                i = -1;
                int k ;
                for (k = -1; k <= 1; k++) {
                    j = i4 + k;
                    if (j >= 0 && j < height) {
                        int i6 = j * width;
                        j = -minBlurValue.minBlurVal;
                        while (true) {
                            int i7 = minBlurValue.minBlurVal;
                            if (j > 0) {
                                break;
                            }
                            i7 = red + j;
                            if (i7 >= 0 && i7 < width) {
                                i = RandomColorBalance.getActionColor(i, mArray[i7 + i6]);
                            }
                            j++;
                        }
                    }
                }
                k = i5 + 1;
                mArray2[i5] = i;
                red++;
                i5 = k;
            }
            i4++;
            i = i5;
        }
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        simpleRGB(mArray, width, height);
        RandomColorBalance.getColorRGB(mArray, mArray2, width, height);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        createBitmap.setPixels(mArray2, 0, width, 0, 0, width, height);
        System.gc();
        return createBitmap;
    }

    public static void simpleRGB(int[] mArray, int i, int j) {
        for (int k = 0; k < j; k++) {
            for (int i4 = 0; i4 < i; i4++) {
                int i5 = (k * i) + i4;
                int red = (((Color.red(mArray[i5]) * 28) + (Color.green(mArray[i5]) * 151)) + (Color.blue(mArray[i5]) * 77)) >> 8;
                mArray[i5] = Color.rgb(red, red, red);
            }
        }
    }

    public final void getSimpleSketchValue(int i) {
        this.value = i;
    }
}
