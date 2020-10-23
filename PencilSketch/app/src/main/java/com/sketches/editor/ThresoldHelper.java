package com.sketches.editor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;

public final class ThresoldHelper extends ImageFilerName {
    private int thresoldValue = 102;

    public final void setThresholdValue(int i) {
        this.thresoldValue = i;
    }

    public final Bitmap getThresholdBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                int i3 = mArray[(i2 * width) + i];
                if (((((i3 & 255) * 77) + ((((16711680 & i3) >> 16) * 28) + (((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i3) >> 8) * 151))) >> 8) > this.thresoldValue) {
                    i3 = 255;
                } else {
                    i3 = 0;
                }
                mArray[(i2 * width) + i] = Color.rgb(i3, i3, i3);
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height);
        System.gc();
        return createBitmap;
    }
}
