package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.database;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.database.model.Workout;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatManager {
    private static final long DAY_TO_MILLIS = 86400000;

    public static List<Workout> getWeeklyData(long currentTime) {
        List<Workout> list = new ArrayList();
        long weekStart = getWeekStartTime(getDayStart(currentTime));
        list.addAll(Realm.getDefaultInstance().where(Workout.class).greaterThan("time", weekStart).lessThan("time", getWeekEndTime(getDayEnd(currentTime))).findAll());
        return list;
    }

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
