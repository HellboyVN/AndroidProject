package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefreshReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        AlarmUtil.getInstance().updateAlarm(context.getApplicationContext());
    }
}
