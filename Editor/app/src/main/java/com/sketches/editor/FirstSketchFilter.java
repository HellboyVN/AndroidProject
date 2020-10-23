package com.sketches.editor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public final class FirstSketchFilter extends ImageFilerName {
    int sketchValue = 6;

    public final void setSketchValue(int i) {
        this.sketchValue = i;
    }

    public final Bitmap getFirstSketch(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        int[] mArray2 = new int[(width * height)];
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int i3 = (i * width) + i2;
                int red = (((Color.red(mArray[i3]) * 28) + (Color.green(mArray[i3]) * 151)) + (Color.blue(mArray[i3]) * 77)) >> 8;
                mArray[i3] = Color.rgb(red, red, red);
                mArray2[i3] = Color.rgb(255 - red, 255 - red, 255 - red);
            }
        }
        FilterForStackBlur filterForStackBlur = new FilterForStackBlur();
        filterForStackBlur.blurValue = this.sketchValue;
        filterForStackBlur.makeItBlur(mArray2, width, height);
        RandomColorBalance.getColorRGB(mArray, mArray2, width, height);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        createBitmap.setPixels(mArray2, 0, width, 0, 0, width, height);
        System.gc();
        return createBitmap;
    }
}
