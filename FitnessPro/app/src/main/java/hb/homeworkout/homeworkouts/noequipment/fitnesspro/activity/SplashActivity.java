package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseHelper;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseManager;

public class SplashActivity extends BaseActivity  {
    private static final String TAG = SplashActivity.class.getName();
    private Handler handler;
//    public FirebaseRemoteConfig mRemoteConfig;
    private SplashRunnable splashRunnable;

    private class SplashRunnable implements Runnable {
        private SplashRunnable() {
        }

        public void run() {
            SplashActivity.this.handleNavigation();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_splash);
        initializeMobileAds();
        MultiDex.install(this);
        prepareDB();
        prepareNotificationsTable();
        initializeView();

    }

    private void fcmMessageSubscriptionForDeveloper() {
    }

    private void prepareNotificationsTable() {
        try {
            DatabaseManager.getInstance(this).createNotificationsTableIfNotExists();
            if (DatabaseManager.getInstance(this).newsTableSize() == 0) {
                DatabaseManager.getInstance(this).addFirstFewNotifications();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fcmTopicSubscriptionForAll() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic("all2");
    }



    private void copyDBFileToExternalStorage() {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Workout Training");
        if (direct.exists() || direct.mkdir()) {
            exportDB();
        } else {
            exportDB();
        }
    }

    private void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                File currentDB = new File(data, "/data/hb.homeworkout.homeworkouts.noequipment.fitnesspro/databases/WorkoutDB.db");
                File backupDB = new File(sd, "/Workout Training/WorkoutDB.db");
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
        }
    }

    private void prepareDB() {
        DatabaseHelper myDbHelper = new DatabaseHelper(this);
        try {
            myDbHelper.createSystemDataBaseFromDBFile();
            try {
                myDbHelper.openDataBase();
                myDbHelper.close();
            } catch (SQLException sqle) {
                throw sqle;
            } catch (Throwable th) {
                myDbHelper.close();
            }
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
    }

    private void initializeView() {
        this.handler = new Handler();
        this.splashRunnable = new SplashRunnable();
    }


    private void handleNavigation() {
        startActivity(new Intent().setClass(this, HomeActivity.class).setData(getIntent().getData()));
        finish();
    }

    public void onResume() {
        super.onResume();
        this.handler.postDelayed(this.splashRunnable, 3000);
    }

    public void onPause() {
        super.onPause();
        this.handler.removeCallbacks(this.splashRunnable);
    }



    private void initializeMobileAds() {
        MobileAds.initialize(this,"ca-app-pub-8468661407843417~5376290279");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        for (RunningServiceInfo service : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
