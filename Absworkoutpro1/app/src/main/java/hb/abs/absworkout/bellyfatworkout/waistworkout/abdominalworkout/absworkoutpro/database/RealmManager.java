package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.database;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.database.model.ExerciseRealm;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.database.model.Workout;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.ConsKeys;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.SharedPrefsService;
import android.content.Context;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmConfiguration.Builder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RealmManager {
    private static RealmManager _instance;
    private final long DAY_TO_MILLIS = 86400000;

    private RealmManager() {
    }

    public static RealmManager getInstance() {
        if (_instance == null) {
            _instance = new RealmManager();
        }
        return _instance;
    }

    public void init(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(new Builder().name(ConsKeys.REALM_NAME).schemaVersion(0).build());
    }

    public Realm getDefaultInstance(Context context) {
        try {
            Realm.getDefaultInstance();
        } catch (IllegalStateException e) {
            init(context);
        }
        return Realm.getDefaultInstance();
    }

    public void addWorkout(Context context, final int level, final int duration) {
        Realm realm = getDefaultInstance(context);
        final int gender = SharedPrefsService.getInstance().getGender(context);
        realm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                Workout workout = (Workout) realm.createObject(Workout.class);
                workout.setId(RealmManager.this.getNextWorkoutPK());
                workout.setLevel(level);
                workout.setGender(gender);
                workout.setDuration(duration);
                workout.setTime(new Date().getTime());
            }
        });
    }

    public void addExercise(Context context, final List<ExerciseRealm> exList) {
        final long time = new Date().getTime();
        getDefaultInstance(context).executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                for (ExerciseRealm ex : exList) {
                    ExerciseRealm exRealm = (ExerciseRealm) realm.createObject(ExerciseRealm.class);
                    exRealm.setId(RealmManager.this.getNextExercisePK());
                    exRealm.setLevel(ex.getLevel());
                    exRealm.setDuration(ex.getDuration());
                    exRealm.setGender(ex.getGender());
                    exRealm.setName(ex.getName());
                    exRealm.setTime(time);
                }
            }
        });
    }

    public void addWorkoutTest(Context context, int level, int duration) {
        Realm realm = getDefaultInstance(context);
        for (int i = -2; i < 7; i++) {
            final long time = new Date().getTime() + ((long) (86400000 * i));
            final int i2 = level;
            final int i3 = duration;
            realm.executeTransaction(new Transaction() {
                public void execute(Realm realm) {
                    Workout workout = (Workout) realm.createObject(Workout.class);
                    workout.setId(RealmManager.this.getNextWorkoutPK());
                    workout.setLevel(i2);
                    workout.setDuration(i3);
                    workout.setTime(time);
                }
            });
        }
    }

    public long getNextWorkoutPK() {
        try {
            return (long) (Realm.getDefaultInstance().where(Workout.class).max("id").intValue() + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public long getNextExercisePK() {
        try {
            return (long) (Realm.getDefaultInstance().where(ExerciseRealm.class).max("id").intValue() + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public List<ExerciseRealm> getExerciseData(Context context) {
        int gender = SharedPrefsService.getInstance().getGender(context);
        List<ExerciseRealm> list = new ArrayList();
        list.addAll(getDefaultInstance(context).where(ExerciseRealm.class).equalTo("gender", Integer.valueOf(gender)).findAll());
        return list;
    }

    public boolean hasExerciseData(Context context) {
        return ((ExerciseRealm) getDefaultInstance(context).where(ExerciseRealm.class).equalTo("gender", Integer.valueOf(SharedPrefsService.getInstance().getGender(context))).findFirst()) != null;
    }

    public List<Workout> getWeeklyData(Context context, long currentTime, int weekStartPosition) {
        int gender = SharedPrefsService.getInstance().getGender(context);
        List<Workout> list = new ArrayList();
        long weekStart = getWeekStartTime(getDayStart(currentTime), weekStartPosition);
        list.addAll(getDefaultInstance(context).where(Workout.class).equalTo("gender", Integer.valueOf(gender)).greaterThan("time", weekStart).lessThan("time", getWeekEndTime(getDayEnd(currentTime), weekStartPosition)).findAll());
        return list;
    }

    public long getWeekStartTime(long currentDayStart, int weekStartPosition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDayStart);
        int dayOfTheWeek = calendar.get(7);
        if (weekStartPosition == 0) {
            dayOfTheWeek--;
        } else if (dayOfTheWeek == 1) {
            dayOfTheWeek = 6;
        } else {
            dayOfTheWeek -= 2;
        }
        return currentDayStart - (((long) dayOfTheWeek) * 86400000);
    }

    public long getWeekEndTime(long currentDayEnd, int weekStartPosition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDayEnd);
        int dayOfTheWeek = calendar.get(7);
        if (weekStartPosition == 0) {
            dayOfTheWeek--;
        } else if (dayOfTheWeek == 1) {
            dayOfTheWeek = 6;
        } else {
            dayOfTheWeek -= 2;
        }
        return (((long) (6 - dayOfTheWeek)) * 86400000) + currentDayEnd;
    }

    public int getDayInWeek(long time, int weekStartPosition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int dayOfWeek = calendar.get(7);
        if (weekStartPosition == 0) {
            return dayOfWeek - 1;
        }
        if (dayOfWeek == 1) {
            return 6;
        }
        return dayOfWeek - 2;
    }

    public long getDayStart(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return calendar.getTimeInMillis();
    }

    public long getDayEnd(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTimeInMillis();
    }

    public String getWeekStartFormatted(long time, int weekStartPosition) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        long weekStart = getInstance().getWeekStartTime(getInstance().getDayStart(calendar.getTimeInMillis()), weekStartPosition);
        DateFormat format = new SimpleDateFormat("yyyy MMMM dd");
        calendar.setTimeInMillis(weekStart);
        return format.format(calendar.getTime());
    }

    public String getWeekStartFormatted(int weekStartPosition, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, offset * 7);
        long weekStart = getInstance().getWeekStartTime(getInstance().getDayStart(calendar.getTimeInMillis()), weekStartPosition);
        DateFormat format = new SimpleDateFormat("MMM dd");
        calendar.setTimeInMillis(weekStart);
        return format.format(calendar.getTime());
    }

    public String getWeekEndFormatted(int weekStartPosition, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, offset * 7);
        long weekEnd = getInstance().getWeekEndTime(getInstance().getDayEnd(calendar.getTimeInMillis()), weekStartPosition);
        DateFormat format = new SimpleDateFormat("MMM dd");
        calendar.setTimeInMillis(weekEnd);
        return format.format(calendar.getTime());
    }
}
