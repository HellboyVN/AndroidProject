package com.sketches.editor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public final class FilterForSmartBlur extends ImageFilerName {
    private FilterApplyInterface filterApplyInterface;
    private int smartBlurValue1 = 50;
    private int smartBlurValue2 = 10;
    private int smartValue3 = 0;

    public FilterForSmartBlur(FilterApplyInterface filterApplyInterface) {
        this.filterApplyInterface = filterApplyInterface;
    }

    public final Bitmap getSmartBluredBitmap(Bitmap bitmap) {
        System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        int[] mArray2 = new int[(width * height)];
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        ByteArrayHelper a = BlurFunctionClass.getByteArrayHelper((float) this.smartBlurValue1);
        if (this.filterApplyInterface != null) {
            this.filterApplyInterface.filterApplyInterfaceFunction(0);
        }
        this.smartValue3 = 1;
        makeItSmartBlur(a, mArray, mArray2, width, height);
        if (this.filterApplyInterface != null) {
            this.filterApplyInterface.filterApplyInterfaceFunction(50);
        }
        this.smartValue3 = 2;
        makeItSmartBlur(a, mArray2, mArray, height, width);
        if (this.filterApplyInterface != null) {
            this.filterApplyInterface.filterApplyInterfaceFunction(100);
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height);
        System.gc();
        return createBitmap;
    }

    private void makeItSmartBlur(ByteArrayHelper byteArrayHelper, int[] mArray, int[] mArray2, int i, int i2) {
        float[] b = byteArrayHelper.setByteArray();
        int a = byteArrayHelper.setByteInt() / 2;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i3 * i;
            int i5 = 0;
            int i6 = i3;
            while (i5 < i) {
                float f = 0.0f;
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                int i7 = mArray[i4 + i5];
                int i8 = (i7 >> 24) & 255;
                int i9 = (i7 >> 16) & 255;
                int i10 = (i7 >> 8) & 255;
                int i11 = i7 & 255;
                float f5 = 0.0f;
                float f6 = 0.0f;
                float f7 = 0.0f;
                float f8 = 0.0f;
                int i12 = -a;
                while (i12 <= a) {
                    float f9;
                    float f10;
                    float f11 = b[a + i12];
                    if (f11 != 0.0f) {
                        int i13;
                        int i14;
                        int i15;
                        i7 = i5 + i12;
                        if (i7 < 0 || i7 >= i) {
                            i7 = i5;
                        }
                        i7 = mArray[i7 + i4];
                        int i16 = (i7 >> 24) & 255;
                        int i17 = (i7 >> 16) & 255;
                        int i18 = (i7 >> 8) & 255;
                        int i19 = i7 & 255;
                        i7 = i8 - i16;
                        if (i7 >= 0 && i7 <= 0) {
                            f9 = f5 + f11;
                            f5 = f4 + (((float) i16) * f11);
                            i13 = i9 - i17;
                            i16 = -this.smartBlurValue2;
                            if (i13 >= 0) {
                                i16 = this.smartBlurValue2;
                                if (i13 <= 0) {
                                    f4 = f6 + f11;
                                    f6 = f + (((float) i17) * f11);
                                    i14 = i10 - i18;
                                    i16 = -this.smartBlurValue2;
                                    if (i14 >= 0) {
                                        i16 = this.smartBlurValue2;
                                        if (i14 <= 0) {
                                            f = f7 + f11;
                                            f7 = f2 + (((float) i18) * f11);
                                            i15 = i11 - i19;
                                            i16 = -this.smartBlurValue2;
                                            if (i15 >= 0) {
                                                i16 = this.smartBlurValue2;
                                                if (i15 <= 0) {
                                                    f2 = f8 + f11;
                                                    f8 = f7;
                                                    f7 = f5;
                                                    f5 = f9;
                                                    f9 = f2;
                                                    f2 = f3 + (((float) i19) * f11);
                                                }
                                            }
                                            f2 = f3;
                                            f10 = f9;
                                            f9 = f8;
                                            f8 = f7;
                                            f7 = f5;
                                            f5 = f10;
                                        }
                                    }
                                    f = f7;
                                    f7 = f2;
                                    i15 = i11 - i19;
                                    i16 = -this.smartBlurValue2;
                                    if (i15 >= 0) {
                                        i16 = this.smartBlurValue2;
                                        if (i15 <= 0) {
                                            f2 = f8 + f11;
                                            f8 = f7;
                                            f7 = f5;
                                            f5 = f9;
                                            f9 = f2;
                                            f2 = f3 + (((float) i19) * f11);
                                        }
                                    }
                                    f2 = f3;
                                    f10 = f9;
                                    f9 = f8;
                                    f8 = f7;
                                    f7 = f5;
                                    f5 = f10;
                                }
                            }
                            f4 = f6;
                            f6 = f;
                            i14 = i10 - i18;
                            i16 = -this.smartBlurValue2;
                            if (i14 >= 0) {
                                i16 = this.smartBlurValue2;
                                if (i14 <= 0) {
                                    f = f7 + f11;
                                    f7 = f2 + (((float) i18) * f11);
                                    i15 = i11 - i19;
                                    i16 = -this.smartBlurValue2;
                                    if (i15 >= 0) {
                                        i16 = this.smartBlurValue2;
                                        if (i15 <= 0) {
                                            f2 = f8 + f11;
                                            f8 = f7;
                                            f7 = f5;
                                            f5 = f9;
                                            f9 = f2;
                                            f2 = f3 + (((float) i19) * f11);
                                        }
                                    }
                                    f2 = f3;
                                    f10 = f9;
                                    f9 = f8;
                                    f8 = f7;
                                    f7 = f5;
                                    f5 = f10;
                                }
                            }
                            f = f7;
                            f7 = f2;
                            i15 = i11 - i19;
                            i16 = -this.smartBlurValue2;
                            if (i15 >= 0) {
                                i16 = this.smartBlurValue2;
                                if (i15 <= 0) {
                                    f2 = f8 + f11;
                                    f8 = f7;
                                    f7 = f5;
                                    f5 = f9;
                                    f9 = f2;
                                    f2 = f3 + (((float) i19) * f11);
                                }
                            }
                            f2 = f3;
                            f10 = f9;
                            f9 = f8;
                            f8 = f7;
                            f7 = f5;
                            f5 = f10;
                        }
                        f9 = f5;
                        f5 = f4;
                        i13 = i9 - i17;
                        i16 = -this.smartBlurValue2;
                        if (i13 >= 0) {
                            i16 = this.smartBlurValue2;
                            if (i13 <= 0) {
                                f4 = f6 + f11;
                                f6 = f + (((float) i17) * f11);
                                i14 = i10 - i18;
                                i16 = -this.smartBlurValue2;
                                if (i14 >= 0) {
                                    i16 = this.smartBlurValue2;
                                    if (i14 <= 0) {
                                        f = f7 + f11;
                                        f7 = f2 + (((float) i18) * f11);
                                        i15 = i11 - i19;
                                        i16 = -this.smartBlurValue2;
                                        if (i15 >= 0) {
                                            i16 = this.smartBlurValue2;
                                            if (i15 <= 0) {
                                                f2 = f8 + f11;
                                                f8 = f7;
                                                f7 = f5;
                                                f5 = f9;
                                                f9 = f2;
                                                f2 = f3 + (((float) i19) * f11);
                                            }
                                        }
                                        f2 = f3;
                                        f10 = f9;
                                        f9 = f8;
                                        f8 = f7;
                                        f7 = f5;
                                        f5 = f10;
                                    }
                                }
                                f = f7;
                                f7 = f2;
                                i15 = i11 - i19;
                                i16 = -this.smartBlurValue2;
                                if (i15 >= 0) {
                                    i16 = this.smartBlurValue2;
                                    if (i15 <= 0) {
                                        f2 = f8 + f11;
                                        f8 = f7;
                                        f7 = f5;
                                        f5 = f9;
                                        f9 = f2;
                                        f2 = f3 + (((float) i19) * f11);
                                    }
                                }
                                f2 = f3;
                                f10 = f9;
                                f9 = f8;
                                f8 = f7;
                                f7 = f5;
                                f5 = f10;
                            }
                        }
                        f4 = f6;
                        f6 = f;
                        i14 = i10 - i18;
                        i16 = -this.smartBlurValue2;
                        if (i14 >= 0) {
                            i16 = this.smartBlurValue2;
                            if (i14 <= 0) {
                                f = f7 + f11;
                                f7 = f2 + (((float) i18) * f11);
                                i15 = i11 - i19;
                                i16 = -this.smartBlurValue2;
                                if (i15 >= 0) {
                                    i16 = this.smartBlurValue2;
                                    if (i15 <= 0) {
                                        f2 = f8 + f11;
                                        f8 = f7;
                                        f7 = f5;
                                        f5 = f9;
                                        f9 = f2;
                                        f2 = f3 + (((float) i19) * f11);
                                    }
                                }
                                f2 = f3;
                                f10 = f9;
                                f9 = f8;
                                f8 = f7;
                                f7 = f5;
                                f5 = f10;
                            }
                        }
                        f = f7;
                        f7 = f2;
                        i15 = i11 - i19;
                        i16 = -this.smartBlurValue2;
                        if (i15 >= 0) {
                            i16 = this.smartBlurValue2;
                            if (i15 <= 0) {
                                f2 = f8 + f11;
                                f8 = f7;
                                f7 = f5;
                                f5 = f9;
                                f9 = f2;
                                f2 = f3 + (((float) i19) * f11);
                            }
                        }
                        f2 = f3;
                        f10 = f9;
                        f9 = f8;
                        f8 = f7;
                        f7 = f5;
                        f5 = f10;
                    } else {
                        f9 = f8;
                        f8 = f2;
                        f2 = f3;
                        f10 = f;
                        f = f7;
                        f7 = f4;
                        f4 = f6;
                        f6 = f10;
                    }
                    i12++;
                    f3 = f2;
                    f2 = f8;
                    f8 = f9;
                    f10 = f6;
                    f6 = f4;
                    f4 = f7;
                    f7 = f;
                    f = f10;
                }
                float f12 = f5 == 0.0f ? (float) i8 : f4 / f5;
                mArray2[i6] = ((RandomColorBalance.getActionMask((int) (((double) (f7 == 0.0f ? (float) i10 : f2 / f7)) + 0.5d)) << 8) | ((RandomColorBalance.getActionMask((int) (((double) (f6 == 0.0f ? (float) i9 : f / f6)) + 0.5d)) << 16) | (RandomColorBalance.getActionMask((int) (((double) f12) + 0.5d)) << 24))) | RandomColorBalance.getActionMask((int) (((double) (f8 == 0.0f ? (float) i11 : f3 / f8)) + 0.5d));
                i5++;
                i6 += i2;
            }
            if (i3 % 100 == 0 && this.filterApplyInterface != null) {
                double d = ((((double) i3) * 1.0d) / ((double) i2)) * 100.0d;
                if (this.smartValue3 == 1) {
                    d /= 2.0d;
                } else if (this.smartValue3 == 2) {
                    d = (d / 2.0d) + 50.0d;
                }
                this.filterApplyInterface.filterApplyInterfaceFunction((int) d);
            }
        }
    }

    public final void setSmartBlurValue1() {
        this.smartBlurValue1 = 40;
    }

    public final void setSmartBlurValue2() {
    }

    public final void setSmartBlurValue3() {
        this.smartBlurValue2 = 30;
    }
}
