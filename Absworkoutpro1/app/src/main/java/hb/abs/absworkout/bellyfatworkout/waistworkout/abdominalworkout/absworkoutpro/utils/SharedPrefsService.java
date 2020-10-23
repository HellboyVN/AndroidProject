package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.Exercise;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.data.TimeData;

public class SharedPrefsService {
    public static SharedPrefsService INSTANCE = null;

    public static SharedPrefsService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedPrefsService();
        }
        return INSTANCE;
    }

    private SharedPrefsService() {
    }

    public int getGender(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_GENDER, 0);
    }

    public void setGender(Context context, int gender) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_GENDER, gender).apply();
    }

    public int getWeightMetric(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.WEIGHT_METRICS, 0);
    }

    public void setWeightMetric(Context context, int metric) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.WEIGHT_METRICS, metric).apply();
    }

    public int getWeightCount(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.WEIGHT_COUNT, 0);
    }

    public void setWeightCount(Context context, int metric) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.WEIGHT_COUNT, metric).apply();
    }

    public boolean isFirstWeight(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.FIRST_METRICS, true);
    }

    public void setFirstWeight(Context context, boolean first) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.FIRST_METRICS, first).apply();
    }

    public long getWeightLastCalendar(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getLong(ConsKeys.WEIGT_LAST_CALENDAR, Long.MIN_VALUE);
    }

    public void setWeightLastCalendar(Context context, long calendar) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putLong(ConsKeys.WEIGT_LAST_CALENDAR, calendar).apply();
    }

    public boolean getRatedStatus(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_IS_RATED, false);
    }

    public void setRatedStatus(Context context, boolean rated) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_IS_RATED, rated).apply();
    }

    public boolean getRecommendedStatus(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_IS_RECEMMENDED_CLICKED, false);
    }

    public void setRecommendedStatus(Context context, boolean recommended) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_IS_RECEMMENDED_CLICKED, recommended).apply();
    }

    public boolean getSoundStatus(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_SETTING_SOUND, true);
    }

    public void setSoundStatus(Context context, boolean isOn) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_SETTING_SOUND, isOn).apply();
    }

    public boolean isRecommendCardShowed(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_REMINDER_HINT, false);
    }

    public void setRecommendCardShow(Context context, boolean isShowed) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_REMINDER_HINT, isShowed).apply();
    }

    public boolean isHaveAlarm(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_HAS_ALARM, false);
    }

    public void setHaveAlarm(Context context, boolean isShowed) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_HAS_ALARM, isShowed).apply();
    }

    public void setLevelPassed(Context context, int level) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_LEVEL_LOCK + "_" + level, true).apply();
    }

    public boolean isLevelOpened(Context context, int level) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_LEVEL_LOCK + "_" + (level - 1), false);
    }

    public void setFirstTime(Context context, boolean isFirstTime) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_IS_FIRST_TIME, isFirstTime).apply();
    }

    public boolean getFirstTime(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_IS_FIRST_TIME, true);
    }

    public int getWeekStart(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, Context.MODE_PRIVATE).getInt(ConsKeys.PREFS_WEEK_START, 1);
    }

    public void setWeekStart(Context context, int weekStart) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_WEEK_START, weekStart).apply();
    }

    public List<Exercise> getCustomWorkoutData(Context context) {
        String customWorkoutJson = context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getString(ConsKeys.PREFS_CUSTOM_WORKOUT, null);
        List<Exercise> exercises = new ArrayList();
        if (customWorkoutJson != null) {
            return (List) new Gson().fromJson(customWorkoutJson, new TypeToken<List<Exercise>>() {
            }.getType());
        }
        return exercises;
    }

    public void setCustomWorkoutData(Context context, List<Exercise> exercises) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putString(ConsKeys.PREFS_CUSTOM_WORKOUT, new Gson().toJson((Object) exercises)).apply();
    }

    public int getRestTime(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_REST_TIME, 20);
    }

    public void setRestTime(Context context, int weekStart) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_REST_TIME, weekStart).apply();
    }

    public int getReadyTime(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_READY_TIME, 3);
    }

    public void setReadyTime(Context context, int readytime) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_READY_TIME, readytime).apply();
    }

    public void setPurchasedProPackage(Context context, boolean isPurchased) {
        context.getSharedPreferences(ConsKeys.PREFS_IAP_PRO_PACKAGE, 0).edit().putBoolean(ConsKeys.PREFS_IAP_PRO_PACKAGE, isPurchased).apply();
    }

    public boolean getPurchasedProPackage(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_IAP_PRO_PACKAGE, 0).getBoolean(ConsKeys.PREFS_IAP_PRO_PACKAGE, false);
    }

    public void setPurchaseAvailable(Context context, boolean isAvailable) {
        context.getSharedPreferences(ConsKeys.PREFS_IAP_PURCHASE_AVAILABLE, 0).edit().putBoolean(ConsKeys.PREFS_IAP_PURCHASE_AVAILABLE, isAvailable).apply();
    }

    public boolean getPurchaseAvailable(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_IAP_PURCHASE_AVAILABLE, 0).getBoolean(ConsKeys.PREFS_IAP_PURCHASE_AVAILABLE, false);
    }

    public void saveReminderData(Context context, List<TimeData> data) {
        Collections.sort(data);
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putString(ConsKeys.LIST_KEY, new Gson().toJson((Object) data)).apply();
    }

    public List<TimeData> getReminderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ConsKeys.PREFS_NAME, 0);
        return (List) new Gson().fromJson(preferences.getString(ConsKeys.LIST_KEY, null), new TypeToken<List<TimeData>>() {
        }.getType());
    }

    public void saveNotifiTexID(Context context, int notifyID) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.NOTIFY_KEY, notifyID).apply();
    }

    public int getNotifiTextId(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.NOTIFY_KEY, 0);
    }

    public void addTimeData(TimeData time, Context context) {
        List<TimeData> timedata;
        if (getReminderData(context) != null) {
            timedata = getReminderData(context);
        } else {
            timedata = new ArrayList();
        }
        timedata.add(time);
        saveReminderData(context, timedata);
    }

    public void setLevelPreviewHintShowed(Context context, boolean showed) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.LEVEL_PREVIEW_HINT, showed).apply();
    }

    public boolean isLevelPreviewHintShowed(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.LEVEL_PREVIEW_HINT, false);
    }

    public int getChallengeType(Context context, int gender) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.CHALLENGE_TYPE_KEY + "_" + gender, 0);
    }

    public void setChallengeType(Context context, int type, int gender) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.CHALLENGE_TYPE_KEY + "_" + gender, type).apply();
    }

    public int getChallengeCurrentDay(Context context, int type, int gender) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_CHALLENGE_CURRENT_DAY + "_" + type + "_" + gender, 0);
    }

    public void setChallengeCurrentDay(Context context, int day, int type, int gender) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_CHALLENGE_CURRENT_DAY + "_" + type + "_" + gender, day).apply();
    }

    public void resetChallenge(Context context, int type, int gender) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().remove(ConsKeys.PREFS_CHALLENGE_CURRENT_DAY + "_" + type + "_" + gender).apply();
    }
}
