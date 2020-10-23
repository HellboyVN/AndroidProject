package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import android.app.Activity;
import android.content.Intent;

public class ThemeUtils {
    public static void changeToTheme(Activity activity, int theme) {
        SharedPrefsService.getInstance().setGender(activity, theme);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (SharedPrefsService.getInstance().getGender(activity)) {
            case 1:
                activity.setTheme(R.style.OverlayPrimaryColorMale);
                return;
            case 2:
                activity.setTheme(R.style.OverlayPrimaryColorFemale);
                return;
            default:
                activity.setTheme(R.style.OverlayPrimaryColorDefault);
                return;
        }
    }
}
