package com.sketches.editor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.view.ViewCompat;

public final class ColorLevels extends ImageFilerName {
    private int[][] colorLevelArray;

    public final Bitmap getColorLevelBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        System.currentTimeMillis();
        int i2 = 0;
        int i3 = 0;
        while (i3 < height) {
            int i = i2;
            for (i2 = 0; i2 < width; i2++) {
                int i4 = mArray[i];
                if (this.colorLevelArray != null) {
                    i4 = this.colorLevelArray[2][i4 & 255] | (((ViewCompat.MEASURED_STATE_MASK & i4) | (this.colorLevelArray[0][(i4 >> 16) & 255] << 16)) | (this.colorLevelArray[1][(i4 >> 8) & 255] << 8));
                }
                mArray[i] = i4;
                i++;
            }
            i3++;
            i2 = i;
        }
        this.colorLevelArray = null;
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        if (createBitmap == null) {
            return bitmap;
        }
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height);
        bitmap.recycle();
        System.gc();
        return createBitmap;
    }
}
