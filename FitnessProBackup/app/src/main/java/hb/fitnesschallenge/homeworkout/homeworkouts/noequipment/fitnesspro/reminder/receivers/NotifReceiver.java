package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.receivers;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;

public class NotifReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (isAppIsInBackground(context.getApplicationContext())) {
            ReminderNotification.notify(context);
        }
        AlarmUtil.getInstance().updateAlarm(context.getApplicationContext());
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        if (VERSION.SDK_INT > 21) {
            for (RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
                if (processInfo.importance == 100) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        }
        return isInBackground;
    }
}
