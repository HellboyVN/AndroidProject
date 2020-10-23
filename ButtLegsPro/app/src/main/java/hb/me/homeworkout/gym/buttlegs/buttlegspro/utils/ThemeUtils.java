package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.app.Activity;
import android.content.Intent;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;

public class ThemeUtils {
    public static void changeToTheme(Activity activity, int theme) {
        SharedPrefsService.getInstance().setGender(activity, theme);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        int theme = SharedPrefsService.getInstance().getGender(activity);
        activity.setTheme(R.style.OverlayPrimaryColorDefault);
    }
}
