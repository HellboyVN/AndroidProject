package com.workout.workout.managers;

import android.app.Application;
import android.content.Context;

public class CachingManager {
    public static void setAppContext(Application context) {
        ApplicationCache.getInstance().setAppContext(context);
    }

    public static Context getAppContext() {
        return ApplicationCache.getInstance().getAppContext();
    }

    public static void removeApplicationCache() {
        ApplicationCache.getInstance().removeApplicationCache();
    }
}
