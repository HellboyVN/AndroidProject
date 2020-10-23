package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.model;

import android.content.Context;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.RealmManager;
import io.realm.RealmObject;
import io.realm.internal.RealmObjectProxy;

public class Workout extends RealmObject   {
    private int duration;
    private int gender;
    private long id;
    private int level;
    private long time;

    public static class WeeklyWorkoutForChart {
        int currentDay;
        int maxValue;
        float[] result;

        public float[] getResult() {
            return this.result;
        }

        public void setResult(float[] result) {
            this.result = result;
        }

        public int getCurrentDay() {
            return this.currentDay;
        }

        public void setCurrentDay(int currentDay) {
            this.currentDay = currentDay;
        }

        public int getMaxValue() {
            return this.maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }
    }

    public int realmGet$duration() {
        return this.duration;
    }

    public int realmGet$gender() {
        return this.gender;
    }

    public long realmGet$id() {
        return this.id;
    }

    public int realmGet$level() {
        return this.level;
    }

    public long realmGet$time() {
        return this.time;
    }

    public void realmSet$duration(int i) {
        this.duration = i;
    }

    public void realmSet$gender(int i) {
        this.gender = i;
    }

    public void realmSet$id(long j) {
        this.id = j;
    }

    public void realmSet$level(int i) {
        this.level = i;
    }

    public void realmSet$time(long j) {
        this.time = j;
    }

    public Workout() {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public long getId() {
        return realmGet$id();
    }

    public void setId(long id) {
        realmSet$id(id);
    }

    public long getTime() {
        return realmGet$time();
    }

    public void setTime(long time) {
        realmSet$time(time);
    }

    public int getLevel() {
        return realmGet$level();
    }

    public void setLevel(int level) {
        realmSet$level(level);
    }

    public int getDuration() {
        return realmGet$duration();
    }

    public void setDuration(int duration) {
        realmSet$duration(duration);
    }

    public int getGender() {
        return realmGet$gender();
    }

    public void setGender(int gender) {
        realmSet$gender(gender);
    }

    public static WeeklyWorkoutForChart getWeekData(Context context, long time, int weekStartPosition) {
        float[] result = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        int maxDuration = 0;
        for (Workout workout : RealmManager.getInstance().getWeeklyData(context, time, weekStartPosition)) {
            int index = RealmManager.getInstance().getDayInWeek(workout.getTime(), weekStartPosition);
            result[index] = result[index] + (((float) workout.getDuration()) / 60000.0f);
            if (result[index] > ((float) maxDuration)) {
                maxDuration = (int) result[index];
            }
            result[index] = roundOneDecimal(result[index]);
        }
        WeeklyWorkoutForChart weeklyWorkoutForChart = new WeeklyWorkoutForChart();
        weeklyWorkoutForChart.setResult(result);
        weeklyWorkoutForChart.setMaxValue(maxDuration);
        weeklyWorkoutForChart.setCurrentDay(RealmManager.getInstance().getDayInWeek(time, weekStartPosition));
        return weeklyWorkoutForChart;
    }

    private static float roundOneDecimal(float d) {
        return ((float) ((int) (d * 10.0f))) / 10.0f;
    }
}
