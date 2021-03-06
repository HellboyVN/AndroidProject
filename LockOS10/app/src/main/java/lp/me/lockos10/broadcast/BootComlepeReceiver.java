package lp.me.lockos10.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import lp.me.lockos10.activity.LockHasPasscode;
import lp.me.lockos10.activity.LockNoPasscode;
import lp.me.lockos10.util.Values;

public class BootComlepeReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!sharedPreferences.getBoolean(Values.ACTIVATE_LOCK, true)) {
            return;
        }
        if (sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, false)) {
            Intent intent1 = new Intent(context, LockHasPasscode.class);
            intent1.setFlags(268435460);
            context.startActivity(intent1);
            return;
        }
        Intent intent1 = new Intent(context, LockNoPasscode.class);
        intent1.setFlags(268435460);
        context.startActivity(intent1);
    }
}
