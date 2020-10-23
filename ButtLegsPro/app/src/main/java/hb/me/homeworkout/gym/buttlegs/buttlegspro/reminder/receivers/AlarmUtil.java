package hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmUtil {
    static final /* synthetic */ boolean $assertionsDisabled = (!AlarmUtil.class.desiredAssertionStatus());
    private static AlarmUtil _instance;

    private AlarmUtil() {
    }

    public static AlarmUtil getInstance() {
        if (_instance == null) {
            _instance = new AlarmUtil();
        }
        return _instance;
    }

    public void updateAlarm(Context context) {
        Intent startIntent = new Intent(context, NotifReceiver.class);
        startIntent.setFlags(268435456);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, startIntent, 134217728);
        long calendar = getFirstCalendar(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        if (calendar == 0) {
            return;
        }
        if (VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(0, calendar, pendingIntent);
        } else if (VERSION.SDK_INT >= 19) {
            alarmManager.setExact(0, calendar, pendingIntent);
        } else {
            alarmManager.set(0, calendar, pendingIntent);
        }
    }

    private long getFirstCalendar(Context context) {
        List<TimeData> timeDataList = SharedPrefsService.getInstance().getReminderData(context);
        if (timeDataList == null) {
            return 0;
        }
        List<Long> calendarList = new ArrayList();
        int dayOfWeek = Calendar.getInstance().get(7) - 1;
        for (TimeData time : timeDataList) {
            if (time.isActive()) {
                int i = 0;
                while (i < time.getListDay().size()) {
                    if (((Boolean) time.getListDay().get(i)).booleanValue()) {
                        Calendar calenderDay = Calendar.getInstance();
                        int dayOffset = dayOfWeek < i ? i - dayOfWeek : (7 - dayOfWeek) + i;
                        if (dayOfWeek == i) {
                            if ((calenderDay.get(11) * 60) + calenderDay.get(12) >= (time.getTime().getHour() * 60) + time.getTime().getMinute()) {
                                calenderDay.set(5, calenderDay.get(5) + dayOffset);
                            }
                        } else {
                            calenderDay.set(5, calenderDay.get(5) + dayOffset);
                        }
                        calenderDay.set(11, time.getTime().getHour());
                        calenderDay.set(12, time.getTime().getMinute());
                        calenderDay.set(13, 0);
                        calendarList.add(Long.valueOf(calenderDay.getTimeInMillis()));
                    }
                    i++;
                }
            }
        }
        Calendar now = Calendar.getInstance();
        long min = Long.MAX_VALUE;
        if ($assertionsDisabled || calendarList != null) {
            for (Long calendarToMilis : calendarList) {
                long diff = calendarToMilis.longValue() - now.getTimeInMillis();
                if (diff <= min && diff > 0) {
                    min = diff;
                }
            }
            long calendarfirst = 0;
            if (min != Long.MAX_VALUE) {
                calendarfirst = min + now.getTimeInMillis();
            }
            SharedPrefsService.getInstance().setHaveAlarm(context, calendarfirst > 0);
            return calendarfirst;
        }
        throw new AssertionError();
    }
}
