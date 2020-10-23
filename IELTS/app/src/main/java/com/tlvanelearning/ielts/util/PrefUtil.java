package com.tlvanelearning.ielts.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefUtil {
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_OPENGAME = "PREFS_OPENGAME";
    public static final String PREFS_REPEAT = "REPEATE";
    public static final String PREFS_SHUFFLE = "PREFS_SHUFFLE";

    public static void saveString(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences("AOP_PREFS", 0).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences("AOP_PREFS", 0).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveInt(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences("AOP_PREFS", 0).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveLong(Context context, String key, long value) {
        Editor editor = context.getSharedPreferences("AOP_PREFS", 0).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static String getStringValue(Context context, String key) {
        return context.getSharedPreferences("AOP_PREFS", 0).getString(key, null);
    }

    public static Long getLongValue(Context context, String key) {
        return Long.valueOf(context.getSharedPreferences("AOP_PREFS", 0).getLong(key, 0));
    }

    public static int getIntValue(Context context, String key) {
        return context.getSharedPreferences("AOP_PREFS", 0).getInt(key, 0);
    }

    public static boolean getBooleanValue(Context context, String key) {
        return context.getSharedPreferences("AOP_PREFS", 0).getBoolean(key, false);
    }

    public static boolean getRepeatValue(Context context, String key) {
        return context.getSharedPreferences("AOP_PREFS", 0).getBoolean(key, true);
    }

    public static boolean checkContains(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }
}
