package com.sketches.editor;

import android.support.v4.view.ViewCompat;
import java.util.Vector;

public final class IntColorValueSetting implements IntValueInterface {
    private int intColor1;
    private int intColor2;
    private int intColor3 = 0;
    private IntColorSetting intColorSetting;
    private Vector[] vectorArray;

    public IntColorValueSetting() {
        setIntColorValue(256);
        this.vectorArray = new Vector[6];
        for (int i = 0; i < 6; i++) {
            this.vectorArray[i] = new Vector();
        }
        this.intColorSetting = new IntColorSetting(this);
    }

    public final void setIntColorValue(int i) {
        this.intColor2 = i;
        this.intColor1 = Math.max(512, i * 2);
    }

    private void setIntColorMethod(int i) {
        for (int i2 = 4; i2 >= 0; i2--) {
            Vector<?> vector = this.vectorArray[i2];
            if (vector != null && vector.size() > 0) {
                for (int i3 = 0; i3 < vector.size(); i3++) {
                    IntColorSetting intColorSetting = (IntColorSetting) vector.elementAt(i3);
                    if (intColorSetting.colorsettingValue1 > 0) {
                        for (int i4 = 0; i4 < 8; i4++) {
                            IntColorSetting intColorset = intColorSetting.intColorSettingArray[i4];
                            if (intColorset != null) {
                                boolean z = intColorset.colorSetBoolean;
                                intColorSetting.colorsettingValue3 += intColorset.colorsettingValue3;
                                intColorSetting.colorsettingValue4 += intColorset.colorsettingValue4;
                                intColorSetting.colorsettingValue5 += intColorset.colorsettingValue5;
                                intColorSetting.colorsettingValue6 += intColorset.colorsettingValue6;
                                intColorSetting.intColorSettingArray[i4] = null;
                                intColorSetting.colorsettingValue1--;
                                this.intColor3--;
                                this.vectorArray[i2 + 1].removeElement(intColorset);
                            }
                        }
                        intColorSetting.colorSetBoolean = true;
                        this.intColor3++;
                        if (this.intColor3 <= i) {
                            return;
                        }
                    }
                }
                continue;
            }
        }
    }

    private int setIntColorMethod2(IntColorSetting intColorSetting, int[] mArray, int i) {
        int i2 = 0;
        if (this.intColor3 > this.intColor2) {
            setIntColorMethod(this.intColor2);
        }
        if (intColorSetting.colorSetBoolean) {
            i2 = intColorSetting.colorsettingValue3;
            mArray[i] = (intColorSetting.colorsettingValue6 / i2) | ((ViewCompat.MEASURED_STATE_MASK | ((intColorSetting.colorsettingValue4 / i2) << 16)) | ((intColorSetting.colorsettingValue5 / i2) << 8));
            i2 = i + 1;
            intColorSetting.colorsettingValue7 = i;
            return i2;
        }
        i2 = i;
        for (int i3 = 0; i3 < 8; i3++) {
            if (intColorSetting.intColorSettingArray[i3] != null) {
                intColorSetting.colorsettingValue7 = i2;
                i2 = setIntColorMethod2(intColorSetting.intColorSettingArray[i3], mArray, i2);
            }
        }
        return i2;
    }

    public void intValueFunction1(int i) {
        for (int i2 = 4; i2 >= 0; i2--) {
            Vector<?> vector = this.vectorArray[i2];
            if (vector != null && vector.size() > 0) {
                for (int i3 = 0; i3 < vector.size(); i3++) {
                    IntColorSetting intColorSetting = (IntColorSetting) vector.elementAt(i3);
                    if (intColorSetting.colorsettingValue1 > 0) {
                        for (int i4 = 0; i4 < 8; i4++) {
                            IntColorSetting intColorset = intColorSetting.intColorSettingArray[i4];
                            if (intColorset != null) {
                                boolean z = intColorset.colorSetBoolean;
                                intColorSetting.colorsettingValue3 += intColorset.colorsettingValue3;
                                intColorSetting.colorsettingValue4 += intColorset.colorsettingValue4;
                                intColorSetting.colorsettingValue5 += intColorset.colorsettingValue5;
                                intColorSetting.colorsettingValue6 += intColorset.colorsettingValue6;
                                intColorSetting.intColorSettingArray[i4] = null;
                                intColorSetting.colorsettingValue1--;
                                this.intColor3--;
                                this.vectorArray[i2 + 1].removeElement(intColorset);
                            }
                        }
                        intColorSetting.colorSetBoolean = true;
                        this.intColor3++;
                        if (this.intColor3 <= i) {
                            return;
                        }
                    }
                }
                continue;
            }
        }
    }

    public void intValueFunction2(int[] mArray, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = mArray[i2 + 0];
            int i4 = (i3 >> 16) & 255;
            int i5 = (i3 >> 8) & 255;
            int i6 = i3 & 255;
            int i7 = 0;
            IntColorSetting intColorSetting = this.intColorSetting;
            while (i7 <= 5) {
                IntColorSetting intColorset;
                int i8 = 128 >> i7;
                i3 = (i4 & i8) != 0 ? 4 : 0;
                if ((i5 & i8) != 0) {
                    i3 += 2;
                }
                if ((i8 & i6) != 0) {
                    i3++;
                }
                IntColorSetting colorSet2 = intColorSetting.intColorSettingArray[i3];
                if (colorSet2 == null) {
                    intColorSetting.colorsettingValue1++;
                    colorSet2 = new IntColorSetting(this);
                    colorSet2.intColorSetting = intColorSetting;
                    intColorSetting.intColorSettingArray[i3] = colorSet2;
                    intColorSetting.colorSetBoolean = false;
                    this.vectorArray[i7].addElement(colorSet2);
                    if (i7 == 5) {
                        colorSet2.colorSetBoolean = true;
                        colorSet2.colorsettingValue3 = 1;
                        colorSet2.colorsettingValue4 = i4;
                        colorSet2.colorsettingValue5 = i5;
                        colorSet2.colorsettingValue6 = i6;
                        colorSet2.colorsettingValue2 = i7;
                        this.intColor3++;
                        break;
                    }
                    intColorset = colorSet2;
                } else if (colorSet2.colorSetBoolean) {
                    colorSet2.colorsettingValue3++;
                    colorSet2.colorsettingValue4 += i4;
                    colorSet2.colorsettingValue5 += i5;
                    colorSet2.colorsettingValue6 += i6;
                    break;
                } else {
                    intColorset = colorSet2;
                }
                i7++;
                intColorSetting = intColorset;
            }
            if (this.intColor3 > this.intColor1) {
                setIntColorMethod(this.intColor1);
            }
        }
    }

    public int[] intArrayValueFunction() {
        int[] mArray = new int[this.intColor3];
        setIntColorMethod2(this.intColorSetting, mArray, 0);
        return mArray;
    }

    public int intValueFunction3(int i) {
        int i2 = (i >> 16) & 255;
        int i3 = (i >> 8) & 255;
        int i4 = i & 255;
        int i5 = 0;
        IntColorSetting intColorSetting = this.intColorSetting;
        while (i5 <= 5) {
            int i6;
            int i7 = 128 >> i5;
            if ((i2 & i7) != 0) {
                i6 = 4;
            } else {
                i6 = 0;
            }
            if ((i3 & i7) != 0) {
                i6 += 2;
            }
            if ((i7 & i4) != 0) {
                i6++;
            }
            IntColorSetting intColorset = intColorSetting.intColorSettingArray[i6];
            if (intColorset == null) {
                return intColorSetting.colorsettingValue7;
            }
            if (intColorset.colorSetBoolean) {
                return intColorset.colorsettingValue7;
            }
            i5++;
            intColorSetting = intColorset;
        }
        return 0;
    }
}
