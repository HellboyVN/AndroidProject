package com.sketches.editor;

public final class BlurFunctionClass extends SataticValueInitilizer {
    public static ByteArrayHelper getByteArrayHelper(float f) {
        int ceilValue = (int) Math.ceil((double) f);
        int i2 = (ceilValue * 2) + 1;
        float[] fArr = new float[i2];
        float f2 = f / 3.0f;
        float f3 = (2.0f * f2) * f2;
        float sqrtValue = (float) Math.sqrt((double) (6.2831855f * f2));
        float f4 = f * f;
        int i3 = 0;
        float f5 = 0.0f;
        for (int i4 = -ceilValue; i4 <= ceilValue; i4++) {
            float f6 = (float) (i4 * i4);
            if (f6 > f4) {
                fArr[i3] = 0.0f;
            } else {
                fArr[i3] = ((float) Math.exp((double) ((-f6) / f3))) / sqrtValue;
            }
            f5 += fArr[i3];
            i3++;
        }
        for (int i = 0; i < i2; i++) {
            fArr[i] = fArr[i] / f5;
        }
        return new ByteArrayHelper(i2, fArr);
    }
}
