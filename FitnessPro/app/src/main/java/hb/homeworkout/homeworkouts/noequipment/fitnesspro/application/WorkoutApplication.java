package hb.homeworkout.homeworkouts.noequipment.fitnesspro.application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.CachingManager;

public class WorkoutApplication extends Application {
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        CachingManager.setAppContext((Application) getApplicationContext());
//        Stetho.initializeWithDefaults(this);
    }

}
