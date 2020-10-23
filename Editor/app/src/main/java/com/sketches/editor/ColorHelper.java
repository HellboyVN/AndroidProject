package com.sketches.editor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public final class ColorHelper extends ImageFilerName {
    protected static final int[] ColorHelperIntArray;
    private boolean ColorHelperBoleanValue;
    private boolean ColorHelperBoleanValue2 = true;
    private int ColorHelperIntValue = 16;
    private int ColorHelperIntValue2 = 256;

    static {
        int[] mArray = new int[9];
        mArray[5] = 7;
        mArray[6] = 3;
        mArray[7] = 5;
        mArray[8] = 1;
        ColorHelperIntArray = mArray;
    }

    public final Bitmap getColorQuantizeFilter(Bitmap bitmap) {
        System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        int[] mArray2 = new int[(width * height)];
        bitmap.getPixels(mArray2, 0, width, 0, 0, width, height);
        System.currentTimeMillis();
        int i = this.ColorHelperIntValue2;
        boolean z = this.ColorHelperBoleanValue;
        boolean z2 = this.ColorHelperBoleanValue2;
        int i2 = width * height;
        IntValueInterface intValueInter = new IntColorValueSetting();
        intValueInter.intValueFunction1(i);
        intValueInter.intValueFunction2(mArray2, i2);
        int[] a = intValueInter.intArrayValueFunction();
        if (z) {
            for (int i3 = 0; i3 < height; i3++) {
                int i4;
                Integer obj = (z2 && (i3 & 1) == 1) ? Integer.valueOf(1) : null;
                if (obj != null) {
                    i4 = ((i3 * width) + width) - 1;
                    i = -1;
                } else {
                    i4 = i3 * width;
                    i = 1;
                }
                int i5 = 0;
                int i6 = i4;
                while (i5 < width) {
                    i4 = mArray2[i6];
                    i2 = a[intValueInter.intValueFunction3(i4)];
                    mArray[i6] = i2;
                    int i7 = ((i4 >> 16) & 255) - ((i2 >> 16) & 255);
                    int i8 = ((i4 >> 8) & 255) - ((i2 >> 8) & 255);
                    int i9 = (i4 & 255) - (i2 & 255);
                    for (int i10 = -1; i10 <= 1; i10++) {
                        i4 = i10 + i3;
                        if (i4 >= 0 && i4 < height) {
                            int i11 = -1;
                            while (i11 <= 1) {
                                i4 = i11 + i5;
                                if (i4 >= 0 && i4 < width) {
                                    i2 = obj != null ? ColorHelperIntArray[(((i10 + 1) * 3) - i11) + 1] : ColorHelperIntArray[(((i10 + 1) * 3) + i11) + 1];
                                    if (i2 != 0) {
                                        i4 = obj != null ? i6 - i11 : i6 + i11;
                                        int i12 = mArray2[i4];
                                        mArray2[i4] = RandomColorBalance.getActionMask(((i2 * i9) / this.ColorHelperIntValue) + (i12 & 255)) | (RandomColorBalance.getActionMask((((i12 >> 16) & 255) + ((i7 * i2) / this.ColorHelperIntValue)) << 16) | (RandomColorBalance.getActionMask(((i12 >> 8) & 255) + ((i8 * i2) / this.ColorHelperIntValue)) << 8));
                                    }
                                }
                                i11++;
                            }
                        }
                    }
                    i5++;
                    i6 += i;
                }
            }
        } else {
            for (i = 0; i < i2; i++) {
                mArray[i] = a[intValueInter.intValueFunction3(mArray2[i])];
            }
        }
        System.gc();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height);
        System.gc();
        return createBitmap;
    }
}
