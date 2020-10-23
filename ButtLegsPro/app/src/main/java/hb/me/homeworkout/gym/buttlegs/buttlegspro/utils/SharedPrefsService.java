package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;

public class SharedPrefsService {
    private static SharedPrefsService INSTANCE = null;

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

    public boolean getRatedStatus(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_IS_RATED, false);
    }

    public void setRatedStatus(Context context, boolean rated) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_IS_RATED, rated).apply();
    }

    public boolean getSoundStatus(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_SETTING_SOUND, true);
    }

    public void setSoundStatus(Context context, boolean isOn) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_SETTING_SOUND, isOn).apply();
    }

    public int getChallengeCurrentDay(Context context, int type) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt("hb.me.homeworkout.gym.buttlegs.buttlegspro_CHALLENGE_CURRENT_DAY_" + type, 0);
    }

    public void setChallengeCurrentDay(Context context, int day, int type) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt("hb.me.homeworkout.gym.buttlegs.buttlegspro_CHALLENGE_CURRENT_DAY_" + type, day).apply();
    }

    public void resetChallenge(Context context, int type) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().remove("hb.me.homeworkout.gym.buttlegs.buttlegspro_CHALLENGE_CURRENT_DAY_" + type).apply();
    }

    public void setLevelPassed(Context context, int level) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean("hb.me.homeworkout.gym.buttlegs.buttlegspro_LEVEL_LOCK_" + level, true).apply();
    }


    public void setFirstTime(Context context, boolean isFirstTime) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_IS_FIRST_TIME, isFirstTime).apply();
    }

    public boolean getFirstTime(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_IS_FIRST_TIME, true);
    }

    public int getWeekStart(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_WEEK_START, 1);
    }

    public void setWeekStart(Context context, int weekStart) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_WEEK_START, weekStart).apply();
    }

    public void setPurchasedRemoveAds(Context context, boolean isPurchased) {
        context.getSharedPreferences(ConsKeys.PREFS_PURCHASED, 0).edit().putBoolean(ConsKeys.PREFS_REMOVE_ADS, isPurchased).apply();
    }

    public boolean getPurchasedRemoveAds(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_PURCHASED, 0).getBoolean(ConsKeys.PREFS_REMOVE_ADS, false);
    }

    public int getRestTime(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_SETTING_REST_TIME, 15);
    }

    public void setRestTime(Context context, int weekStart) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_SETTING_REST_TIME, weekStart).apply();
    }

    public int getReadyTime(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_READY_TIME, 4);
    }

    public void setReadyTime(Context context, int readytime) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_READY_TIME, readytime).apply();
    }

    public boolean isRecommendCardShowed(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_RECOMMENDED_HINT, false);
    }

    public void setRecommendCardShow(Context context, boolean isShowed) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_RECOMMENDED_HINT, isShowed).apply();
    }

    public boolean isHaveAlarm(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.PREFS_HAS_ALARM, false);
    }

    public void setHaveAlarm(Context context, boolean isShowed) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.PREFS_HAS_ALARM, isShowed).apply();
    }

    public void seveReminderData(Context context, List<TimeData> data) {
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

    public int getNotifiTexId(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.NOTIFY_KEY, 0);
    }

    public int getLastTab(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_LAST_TAB, 20);
    }

    public void setLastTab(Context context, int id) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_LAST_TAB, id).apply();
    }

    public void addTimeData(TimeData timeData, Context context) {
        List<TimeData> timedata;
        if (getReminderData(context) != null) {
            timedata = getReminderData(context);
        } else {
            timedata = new ArrayList();
        }
        timedata.add(timeData);
        seveReminderData(context, timedata);
    }

    public int getCDVoiceStart(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.PREFS_CD_VOICE_START, 10);
    }

    public void setCDVoiceStart(Context context, int voiceStart) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.PREFS_CD_VOICE_START, voiceStart).apply();
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

    public int getWeightMetric(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.WEIGHT_METRICS, 0);
    }

    public void setWeightMetric(Context context, int metric) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.WEIGHT_METRICS, metric).apply();
    }

    public List<Exercise> getCustomWorkoutData(Context context, ExerciseType type) {
        String customWorkoutJson = context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getString("hb.me.homeworkout.gym.buttlegs.buttlegspro_CUSTOM_WORKOUT_DATA_" + type.getType(), null);
        List<Exercise> exercises = new ArrayList();
        if (customWorkoutJson != null) {
            return (List) new Gson().fromJson(customWorkoutJson, new TypeToken<List<Exercise>>() {
            }.getType());
        }
        return exercises;
    }

    public void setCustomWorkoutData(Context context, List<Exercise> exercises, ExerciseType type) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putString("hb.me.homeworkout.gym.buttlegs.buttlegspro_CUSTOM_WORKOUT_DATA_" + type.getType(), new Gson().toJson((Object) exercises)).apply();
    }

    public void setLevelPreviewHintShowed(Context context, boolean showed) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putBoolean(ConsKeys.LEVEL_PREVIEW_HINT, showed).apply();
    }

    public boolean isLevelPreviewHintShowed(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getBoolean(ConsKeys.LEVEL_PREVIEW_HINT, false);
    }

    public int getChallengeType(Context context) {
        return context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).getInt(ConsKeys.CHALLENGE_TYPE_KEY, 0);
    }

    public void setChallengeType(Context context, int type) {
        context.getSharedPreferences(ConsKeys.PREFS_NAME, 0).edit().putInt(ConsKeys.CHALLENGE_TYPE_KEY, type).apply();
    }
}
