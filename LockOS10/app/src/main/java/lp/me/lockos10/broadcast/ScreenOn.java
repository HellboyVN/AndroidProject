package lp.me.lockos10.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import lp.me.lockos10.activity.LockHasPasscode;
import lp.me.lockos10.activity.LockNoPasscode;
import lp.me.lockos10.util.Values;

public class ScreenOn extends BroadcastReceiver {
    public static ScreenOn screenOn;

    public static ScreenOn newInstance() {
        if (screenOn == null) {
            screenOn = new ScreenOn();
        }
        return screenOn;
    }

    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        if (intent.getAction().equalsIgnoreCase("android.intent.action.SCREEN_OFF")) {
            editor.putLong(Values.TIME_LAST_OFF, System.currentTimeMillis());
            editor.commit();
        } else if (System.currentTimeMillis() - sharedPreferences.getLong(Values.TIME_LAST_OFF, -1) > sharedPreferences.getLong(Values.TIME_OUT, -1) && sharedPreferences.getBoolean(Values.ACTIVATE_LOCK, true)) {
            Intent intent1;
            if (sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, false)) {
                if (!LockHasPasscode.booleanisCall) {
                    intent1 = new Intent(context, LockHasPasscode.class);
                    intent1.setFlags(268435460);
                    context.startActivity(intent1);
                }
            } else if (!LockNoPasscode.booleanisCall) {
                intent1 = new Intent(context, LockNoPasscode.class);
                intent1.setFlags(268435460);
                context.startActivity(intent1);
            }
        }
    }
}
