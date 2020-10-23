package com.sketches.editor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import java.lang.reflect.Array;

public final class FilterForStackBlur extends ImageFilerName {
    int blurValue = 3;

    public final Bitmap getBluredBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        makeItBlur(mArray, width, height);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public final void makeItBlur(int[] mArray, int i, int i2) {
        System.currentTimeMillis();
        int i3 = this.blurValue;
        System.currentTimeMillis();
        if (i3 > 0) {
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12;
            int i13;
            int i14;
            int i15 = i - 1;
            int i16 = i2 - 1;
            int i17 = i * i2;
            int i18 = (i3 + i3) + 1;
            short[] sArr = new short[i17];
            short[] sArr2 = new short[i17];
            short[] sArr3 = new short[i17];
            int[] mArray2 = new int[Math.max(i, i2)];
            i17 = (i18 + 1) >> 1;
            int i19 = i17 * i17;
            short[] sArr4 = new short[(i19 * 256)];
            for (i17 = 0; i17 < i19 * 256; i17++) {
                sArr4[i17] = (short) (i17 / i19);
            }
            int[][] mArray3 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{i18, 3});
            int i20 = i3 + 1;
            int i21 = 0;
            int i22 = 0;
            for (i4 = 0; i4 < i2; i4++) {
                int[] mArray4;
                i19 = 0;
                i5 = 0;
                i6 = 0;
                i7 = 0;
                i8 = 0;
                i9 = 0;
                i10 = 0;
                i11 = 0;
                i12 = 0;
                for (i13 = -i3; i13 <= i3; i13++) {
                    i14 = mArray[Math.min(i15, Math.max(i13, 0)) + i22];
                    mArray4 = mArray3[i13 + i3];
                    mArray4[0] = (16711680 & i14) >> 16;
                    mArray4[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & i14) >> 8;
                    mArray4[2] = i14 & 255;
                    i14 = i20 - Math.abs(i13);
                    i11 += mArray4[0] * i14;
                    i10 += mArray4[1] * i14;
                    i9 += mArray4[2] * i14;
                    if (i13 > 0) {
                        i5 += mArray4[0];
                        i12 += mArray4[1];
                        i19 += mArray4[2];
                    } else {
                        i8 += mArray4[0];
                        i7 += mArray4[1];
                        i6 += mArray4[2];
                    }
                }
                i14 = i11;
                i11 = i10;
                i10 = i9;
                i13 = i3;
                for (i9 = 0; i9 < i; i9++) {
                    sArr[i22] = sArr4[i14];
                    sArr2[i22] = sArr4[i11];
                    sArr3[i22] = sArr4[i10];
                    i14 -= i8;
                    i11 -= i7;
                    i10 -= i6;
                    mArray4 = mArray3[((i13 - i3) + i18) % i18];
                    i8 -= mArray4[0];
                    i7 -= mArray4[1];
                    i6 -= mArray4[2];
                    if (i4 == 0) {
                        mArray2[i9] = Math.min((i9 + i3) + 1, i15);
                    }
                    int i23 = mArray[mArray2[i9] + i21];
                    mArray4[0] = (16711680 & i23) >> 16;
                    mArray4[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & i23) >> 8;
                    mArray4[2] = i23 & 255;
                    i5 += mArray4[0];
                    i12 += mArray4[1];
                    i19 += mArray4[2];
                    i14 += i5;
                    i11 += i12;
                    i10 += i19;
                    i13 = (i13 + 1) % i18;
                    mArray4 = mArray3[i13 % i18];
                    i8 += mArray4[0];
                    i7 += mArray4[1];
                    i6 += mArray4[2];
                    i5 -= mArray4[0];
                    i12 -= mArray4[1];
                    i19 -= mArray4[2];
                    i22++;
                }
                i21 += i;
            }
            for (i13 = 0; i13 < i; i13++) {
                i12 = 0;
                i6 = 0;
                i7 = 0;
                i8 = 0;
                i22 = 0;
                i14 = -i3;
                i9 = 0;
                i10 = 0;
                i11 = 0;
                i19 = (-i3) * i;
                i5 = 0;
                while (i14 <= i3) {
                    i4 = Math.max(0, i19) + i13;
                    int[] sArr5 = mArray3[i14 + i3];
                    sArr5[0] = sArr[i4];
                    sArr5[1] = sArr2[i4];
                    sArr5[2] = sArr3[i4];
                    int abs = i20 - Math.abs(i14);
                    i21 = (sArr[i4] * abs) + i11;
                    i11 = (sArr2[i4] * abs) + i10;
                    i10 = (sArr3[i4] * abs) + i9;
                    if (i14 > 0) {
                        i6 += sArr5[0];
                        i5 += sArr5[1];
                        i12 += sArr5[2];
                    } else {
                        i22 += sArr5[0];
                        i8 += sArr5[1];
                        i7 += sArr5[2];
                    }
                    if (i14 < i16) {
                        i19 += i;
                    }
                    i14++;
                    i9 = i10;
                    i10 = i11;
                    i11 = i21;
                }
                i14 = i10;
                i21 = i11;
                i11 = i9;
                i9 = i3;
                i19 = i12;
                i12 = i5;
                i5 = i6;
                i6 = i7;
                i7 = i8;
                i8 = i22;
                i22 = i13;
                for (i10 = 0; i10 < i2; i10++) {
                    mArray[i22] = ((ViewCompat.MEASURED_STATE_MASK | (sArr4[i21] << 16)) | (sArr4[i14] << 8)) | sArr4[i11];
                    i21 -= i8;
                    i14 -= i7;
                    i11 -= i6;
                    int[] mArray5 = mArray3[((i9 - i3) + i18) % i18];
                    i8 -= mArray5[0];
                    i7 -= mArray5[1];
                    i6 -= mArray5[2];
                    if (i13 == 0) {
                        mArray2[i10] = Math.min(i10 + i20, i16) * i;
                    }
                    i15 = mArray2[i10] + i13;
                    mArray5[0] = sArr[i15];
                    mArray5[1] = sArr2[i15];
                    mArray5[2] = sArr3[i15];
                    i5 += mArray5[0];
                    i12 += mArray5[1];
                    i19 += mArray5[2];
                    i21 += i5;
                    i14 += i12;
                    i11 += i19;
                    i9 = (i9 + 1) % i18;
                    mArray5 = mArray3[i9];
                    i8 += mArray5[0];
                    i7 += mArray5[1];
                    i6 += mArray5[2];
                    i5 -= mArray5[0];
                    i12 -= mArray5[1];
                    i19 -= mArray5[2];
                    i22 += i;
                }
            }
            System.gc();
        }
    }
}
