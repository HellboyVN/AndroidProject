package lp.me.lockos10.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import lp.me.lockos10.util.Utils;

public class TextViewRobotoThin extends TextView {
    public TextViewRobotoThin(Context context) {
        super(context);
        setTypeface(Utils.getTypefaceRobotoThin(context));
        setTextColor(-1);
    }

    public TextViewRobotoThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Utils.getTypefaceRobotoThin(context));
        setTextColor(-1);
    }

    public TextViewRobotoThin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(Utils.getTypefaceRobotoThin(context));
        setTextColor(-1);
    }

    @TargetApi(21)
    public TextViewRobotoThin(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(Utils.getTypefaceRobotoThin(context));
        setTextColor(-1);
    }
}
