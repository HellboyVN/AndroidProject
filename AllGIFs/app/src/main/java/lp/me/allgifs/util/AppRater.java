package lp.me.allgifs.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;

import lp.me.allgifs.R;

public class AppRater {
    private static final int DAYS_UNTIL_PROMPT = 0;
    private static final int LAUNCH_UNTIL_PROMPT = 3;

    public static void app_launched(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("rate_app", 0);
            if (!prefs.getBoolean("dontshowagain", false)) {
                Editor editor = prefs.edit();
                long launch_count = prefs.getLong("launch_count", 0) + 1;
                editor.putLong("launch_count", launch_count);
                Long date_firstLaunch = Long.valueOf(prefs.getLong("date_first_launch", 0));
                if (date_firstLaunch.longValue() == 0) {
                    date_firstLaunch = Long.valueOf(System.currentTimeMillis());
                    editor.putLong("date_first_launch", date_firstLaunch.longValue());
                }
                if (launch_count >= LAUNCH_UNTIL_PROMPT && System.currentTimeMillis() >= date_firstLaunch.longValue() + DAYS_UNTIL_PROMPT) {
                    showRateDialog(context, editor);
                }
                editor.apply();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
        }
    }

    public static void showRateDialog(final Context context, final Editor editor) {
        try {
            new Builder(context).title(R.string.rate_dialog_title).content(R.string.rate_dialog_msg).positiveText(R.string.rate_now).negativeText(R.string.rate_never).neutralText(R.string.rate_later).cancelable(true).contentColor(ViewCompat.MEASURED_STATE_MASK).positiveColorRes(R.color.primary).negativeColorRes(R.color.primary).neutralColorRes(R.color.primary).callback(new ButtonCallback() {
                public void onPositive(MaterialDialog dialog) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                    try {
                        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=lp.me.allgifs")));
                    } catch (Exception e) {
                        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=lp.me.allgifs")));
                    }
                    dialog.dismiss();
                }

                public void onNegative(MaterialDialog dialog) {
                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();
                    }
                    dialog.dismiss();
                }

                public void onNeutral(MaterialDialog dialog) {
                    dialog.dismiss();
                }
            }).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
        }
    }
}
