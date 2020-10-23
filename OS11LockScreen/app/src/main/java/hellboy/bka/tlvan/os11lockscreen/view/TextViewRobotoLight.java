package hellboy.bka.tlvan.os11lockscreen.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import hellboy.bka.tlvan.os11lockscreen.util.Utils;

public class TextViewRobotoLight extends TextView {
    public TextViewRobotoLight(Context context) {
        super(context);
        setTypeface(Utils.getTypefaceRobotoLight(context));
    }

    public TextViewRobotoLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Utils.getTypefaceRobotoLight(context));
    }

    public TextViewRobotoLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(Utils.getTypefaceRobotoLight(context));
    }

    @TargetApi(21)
    public TextViewRobotoLight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(Utils.getTypefaceRobotoLight(context));
    }
}
