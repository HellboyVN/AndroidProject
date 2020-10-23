package com.workout.workout.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.Thread.UncaughtExceptionHandler;

/* compiled from: AppRate */
class ExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultExceptionHandler;
    SharedPreferences preferences;

    public ExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler, Context context) {
        this.preferences = context.getSharedPreferences(PrefsContract.SHARED_PREFS_NAME, 0);
        this.defaultExceptionHandler = uncaughtExceptionHandler;
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        this.preferences.edit().putBoolean(PrefsContract.PREF_APP_HAS_CRASHED, true).commit();
        this.defaultExceptionHandler.uncaughtException(thread, throwable);
    }
}
