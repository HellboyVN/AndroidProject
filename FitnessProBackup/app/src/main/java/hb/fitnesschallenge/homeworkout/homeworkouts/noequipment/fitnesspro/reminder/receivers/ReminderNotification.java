package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.receivers;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.MainActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import java.util.Arrays;
import java.util.List;

public class ReminderNotification {
    private static final String NOTIFICATION_TAG = "NewMessage";

    public static void notify(Context context) {
        String title = context.getResources().getString(R.string.not_title);
        String text = getNextString(context);
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.setFlags(603979776);
        notify(context, new Builder(context).setDefaults(-1).setSmallIcon(getNotificationIcon()).setContentTitle(title).setContentText(text).setPriority(0).setContentIntent(PendingIntent.getActivity(context, 0, intent, 0)).setStyle(new BigTextStyle().bigText(text).setBigContentTitle(title)).setAutoCancel(true).build());
    }

    @TargetApi(5)
    private static void notify(Context context, Notification notification) {
        ((NotificationManager) context.getSystemService("notification")).notify(NOTIFICATION_TAG.hashCode(), notification);
    }

    @TargetApi(5)
    public static void cancel(Context context) {
        ((NotificationManager) context.getSystemService("notification")).cancel(NOTIFICATION_TAG.hashCode());
    }

    public static String getNextString(Context context) {
        List<String> texts = Arrays.asList(context.getResources().getStringArray(R.array.notify_texts));
        int notifyID = SharedPrefsService.getInstance().getNotifiTextId(context);
        String text = "";
        if (notifyID == texts.size()) {
            notifyID = 0;
        }
        text = (String) texts.get(notifyID);
        SharedPrefsService.getInstance().saveNotifiTexID(context, notifyID + 1);
        return text;
    }

    private static int getNotificationIcon() {
        return VERSION.SDK_INT >= 21 ? R.drawable.ic_watch_later_black_24dp : R.mipmap.ic_launcher;
    }
}
