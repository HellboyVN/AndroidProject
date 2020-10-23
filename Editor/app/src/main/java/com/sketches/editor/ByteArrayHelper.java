package com.sketches.editor;

public final class ByteArrayHelper {
    private float[] byteArray;
    private int byteInt;

    public ByteArrayHelper(int i, float[] fArr) {
        this.byteInt = i;
        int i2 = i * 1;
        if (fArr.length < i2) {
            throw new IllegalArgumentException("small data (is " + fArr.length + " requird is " + i2);
        }
        this.byteArray = new float[i2];
        System.arraycopy(fArr, 0, this.byteArray, 0, i2);
    }

    public final int setByteInt() {
        return this.byteInt;
    }

    public final float[] setByteArray() {
        float[] obj = new float[this.byteArray.length];
        System.arraycopy(this.byteArray, 0, obj, 0, this.byteArray.length);
        return obj;
    }
}
