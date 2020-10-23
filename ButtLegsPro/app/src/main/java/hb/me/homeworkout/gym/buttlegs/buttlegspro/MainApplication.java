package hb.me.homeworkout.gym.buttlegs.buttlegspro;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import com.google.android.gms.ads.MobileAds;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.RealmManager;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.CheckInternetConnection;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.DialogService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.EmailHelper;
import io.realm.Realm;

public class MainApplication extends Application {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, ConsKeys.ADMOB_APP_ID);
        DialogService.initialize(this);
        EmailHelper.initialize(this);
        Realm.init(this);
        RealmManager.getInstance().init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation(1);
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            public void onActivityDestroyed(Activity activity) {
            }
        });
        Bundle params = new Bundle();
        params.putBoolean("in_online", CheckInternetConnection.isOnline(this));
    }
}
