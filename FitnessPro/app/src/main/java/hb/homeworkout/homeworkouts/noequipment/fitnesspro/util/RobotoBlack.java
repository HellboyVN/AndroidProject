package hb.homeworkout.homeworkouts.noequipment.fitnesspro.util;

import android.content.Context;
import android.graphics.Typeface;

/* compiled from: NativelyCustomTextView */
class RobotoBlack {
    private static RobotoBlack instance;
    private static Typeface typeface;

    RobotoBlack() {
    }

    public static RobotoBlack getInstance(Context context) {
        RobotoBlack robotoBlack;
        synchronized (RobotoBlack.class) {
            if (instance == null) {
                instance = new RobotoBlack();
                typeface = Typeface.createFromAsset(context.getResources().getAssets(), "RobotoBlack.ttf");
            }
            robotoBlack = instance;
        }
        return robotoBlack;
    }

    public Typeface getTypeFace() {
        return typeface;
    }
}
