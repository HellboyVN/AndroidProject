package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
@SuppressWarnings("ResourceType")
public class SnackBarService {
    public static void showQuickSnackBar(Activity activity, String message) {
        Snackbar.make(activity.getWindow().getDecorView().findViewById(16908290), (CharSequence) message, -1).show();
    }

    public static void showSnackBar(Activity activity, String message) {
        Snackbar.make(activity.getWindow().getDecorView().findViewById(16908290), (CharSequence) message, 0).show();
    }

    public static void showSnackBar(Activity activity, String message, int duration) {
        Snackbar.make(activity.getWindow().getDecorView().findViewById(16908290), (CharSequence) message, duration).show();
    }

    public static void showColoredSnackBar(Activity activity, String message, int bgColor, int textColor, Typeface tp) {
        Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView().findViewById(16908290), (CharSequence) message, 0);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(bgColor);
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextColor(textColor);
        if (tp != null) {
            tv.setTypeface(tp);
        }
        snackbar.show();
    }
}
