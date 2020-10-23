package lp.me.lockos10.gcm;

/**
 * Created by tlvan on 10/5/2017.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import lp.me.lockos10.R;
import lp.me.lockos10.activity.LockHasPasscode;


public class GcmMessageHandler  extends IntentService {
    String tag = "levan";
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if(!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("GCM", "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("GCM", "Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle extras) {
        try {
            String message = extras.getString("message");
            if(message != null) {
                Log.e(tag, "Notification received " + message);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle("TITLE");
                mBuilder.setContentText(message);
                mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, LockHasPasscode.class),Intent.FLAG_ACTIVITY_NEW_TASK );
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setDefaults(Notification.DEFAULT_ALL); // this line sets the default vibration and sound for notification
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
            } else {
                Log.d(tag, "Message is empty.");
            }
        } catch (Exception ex) {
            Log.e(tag, Log.getStackTraceString(ex));
        }
    }

}