package hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers;

import android.app.Application;

public class ApplicationCache {
    private static ApplicationCache applicationCache = null;
    private Application appContext;

    private ApplicationCache() {
    }

    public static synchronized ApplicationCache getInstance() {
       // ApplicationCache applicationCache = null;
        synchronized (ApplicationCache.class) {
            if (applicationCache == null) {
                applicationCache = new ApplicationCache();
            }
            applicationCache = applicationCache;
        }
        return applicationCache;
    }

    public static ApplicationCache getApplicationCache() {
        return applicationCache;
    }

    public static void setApplicationCache(ApplicationCache applicationCache) {
        applicationCache = applicationCache;
    }

    public Application getAppContext() {
        return this.appContext;
    }

    public void setAppContext(Application appContext) {
        this.appContext = appContext;
    }

    public void removeApplicationCache() {
        applicationCache = null;
    }
}
