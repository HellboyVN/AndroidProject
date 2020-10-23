package lp.me.lockos10.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import lp.me.lockos10.util.Utils;

public class TextViewRobotoMedium extends TextView {
    public TextViewRobotoMedium(Context context) {
        super(context);
        setTypeface(Utils.getTypefaceRobotoMedium(context));
    }

    public TextViewRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Utils.getTypefaceRobotoMedium(context));
    }

    public TextViewRobotoMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(Utils.getTypefaceRobotoMedium(context));
    }

    @TargetApi(21)
    public TextViewRobotoMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(Utils.getTypefaceRobotoMedium(context));
    }
}
