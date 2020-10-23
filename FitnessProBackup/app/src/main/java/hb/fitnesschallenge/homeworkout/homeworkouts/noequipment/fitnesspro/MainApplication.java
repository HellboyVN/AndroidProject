package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.ads.MobileAds;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.database.RealmManager;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.AnimationService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ConsKeys;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.EmailHelper;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SoundHelper;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.views.DialogService;

public class MainApplication extends Application {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void onCreate() {
        super.onCreate();
        DialogService.initialize(this);
        SoundHelper.initialize(this);
        AnimationService.initialize(this);
        EmailHelper.initialize(this);
        MobileAds.initialize(this, ConsKeys.ADMOB_APP_ID);
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
    }
}
