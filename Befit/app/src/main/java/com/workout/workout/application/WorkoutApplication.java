package com.workout.workout.application;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.firebase.database.FirebaseDatabase;
import com.workout.workout.managers.CachingManager;

public class WorkoutApplication extends Application {
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        CachingManager.setAppContext((Application) getApplicationContext());
        Stetho.initializeWithDefaults(this);
    }

}
