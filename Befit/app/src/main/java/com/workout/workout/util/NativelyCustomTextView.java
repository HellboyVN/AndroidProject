package com.workout.workout.util;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class NativelyCustomTextView extends AppCompatTextView {
    public NativelyCustomTextView(Context context) {
        super(context);
        setTypeface(RobotoBlack.getInstance(context).getTypeFace());
    }

    public NativelyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(RobotoBlack.getInstance(context).getTypeFace());
    }

    public NativelyCustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(RobotoBlack.getInstance(context).getTypeFace());
    }
}
