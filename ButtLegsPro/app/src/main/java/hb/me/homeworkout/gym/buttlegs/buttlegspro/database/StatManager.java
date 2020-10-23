package hb.me.homeworkout.gym.buttlegs.buttlegspro.database;

import java.util.Calendar;
import java.util.Date;

public class StatManager {
    private static final long DAY_TO_MILLIS = 86400000;

    public static long getWeekStartTime(long currentDayStart) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDayStart);
        return currentDayStart - (((long) ((calendar.get(7) - 1) - 1)) * DAY_TO_MILLIS);
    }

    public static long getWeekEndTime(long currentDayEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDayEnd);
        return (((long) (7 - (calendar.get(7) - 1))) * DAY_TO_MILLIS) + currentDayEnd;
    }

    public static int getDayInWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int dayOfWeek = calendar.get(7) - 1;
        if (dayOfWeek == 0 || dayOfWeek == 6) {
            return 6 - dayOfWeek;
        }
        return dayOfWeek;
    }

    public static long getDayStart(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTimeInMillis();
    }

    public static long getDayEnd(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTimeInMillis();
    }
}
