package com.sketches.editor;

final class IntColorSetting {
    boolean colorSetBoolean;
    int colorsettingValue1;
    int colorsettingValue2;
    int colorsettingValue3;
    int colorsettingValue4;
    int colorsettingValue5;
    int colorsettingValue6;
    int colorsettingValue7;
    IntColorSetting intColorSetting;
    IntColorSetting[] intColorSettingArray = new IntColorSetting[8];
    final IntColorValueSetting intColorValueSetting;

    IntColorSetting(IntColorValueSetting intColorValueSetting) {
        this.intColorValueSetting = intColorValueSetting;
    }
}
