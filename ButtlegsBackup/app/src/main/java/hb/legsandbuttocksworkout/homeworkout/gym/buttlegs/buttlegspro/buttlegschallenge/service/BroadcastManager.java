package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.service;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.exersices.ExerciseActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.NotificationCompat.Builder;

public class BroadcastManager extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Notification(context, "Paused", intent.getLongExtra("current_time", 0));
    }

    public void Notification(Context context, String message, long currentTime) {
        String strtitle = context.getString(R.string.app_name);
        Intent intent = new Intent(context, ExerciseActivity.class);
        intent.putExtra("current_time", currentTime);
        ((NotificationManager) context.getSystemService("notification")).notify(0, ((Builder) new Builder(context).setSmallIcon(R.mipmap.ic_launcher).setTicker(message).setContentTitle(context.getString(R.string.app_name)).setContentText(message).setContentIntent(PendingIntent.getActivity(context, 0, intent, 134217728)).setAutoCancel(true)).build());
    }

    private boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }
}
