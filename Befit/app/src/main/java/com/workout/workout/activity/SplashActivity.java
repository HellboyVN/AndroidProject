package com.workout.workout.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.Purchase.PurchasesResult;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.database.DatabaseManager;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.util.AppUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class SplashActivity extends BaseActivity implements PurchasesUpdatedListener {
    private static final String TAG = SplashActivity.class.getName();
    private Handler handler;
    private BillingClient mBillingClient;
    public FirebaseRemoteConfig mRemoteConfig;
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
        //initializeMobileAds();
        //initializeRemoteConfig();
//        if (!isMyServiceRunning(FirebaseDBUpdateService.class)) {
//            startService(new Intent(this, FirebaseDBUpdateService.class));
//        }
        prepareDB();
        prepareNotificationsTable();
        initializeView();
//        prepareBillingClient();
//        UserManager.syncUserProfile();
//        fcmTopicSubscriptionForAll();
//        fcmMessageSubscriptionForDeveloper();
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

    private void prepareBillingClient() {
        this.mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(int billingResponseCode) {
                if (billingResponseCode == 0) {
                    SplashActivity.this.checkForPurchasedPlansFromGooglePlayServices();
                }
            }

            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void checkForPurchasedPlansFromGooglePlayServices() {
        int i;
        PersistenceManager.deleteSharedPref(AppConstants.PLAN_IS_LOCKED);
        PersistenceManager.deleteSharedPref(AppConstants.KEY_PREF_ADS_FREE_VERSION);
        PersistenceManager.deleteSharedPref(AppConstants.KEY_PREF_PREMIUM_VERSION);
        PurchasesResult inAppPurchasesResult = this.mBillingClient.queryPurchases("inapp");
        PurchasesResult subscriptionPurchasesResult = this.mBillingClient.queryPurchases("subs");
        if (inAppPurchasesResult != null) {
            List<Purchase> inAppPurchasesList = inAppPurchasesResult.getPurchasesList();
            if (inAppPurchasesList != null) {
                i = 0;
                while (i < inAppPurchasesList.size()) {
                    if (!(inAppPurchasesList.get(i) == null || AppUtil.isEmpty(((Purchase) inAppPurchasesList.get(i)).getSku()))) {
                        if (((Purchase) inAppPurchasesList.get(i)).getSku().equalsIgnoreCase(AppConstants.SKU_AD_FREE)) {
                            PersistenceManager.setAdsFreeVersion(true);
                        }
                        if (((Purchase) inAppPurchasesList.get(i)).getSku().equalsIgnoreCase(AppConstants.SKU_PREMIUM_VERSION)) {
                            PersistenceManager.setPremiumVersion(true);
                        }
                        PersistenceManager.unlockThePlan(((Purchase) inAppPurchasesList.get(i)).getSku());
                    }
                    i++;
                }
            }
        }
        if (subscriptionPurchasesResult != null) {
            List<Purchase> subscriptionPurchasesList = subscriptionPurchasesResult.getPurchasesList();
            if (subscriptionPurchasesList != null) {
                i = 0;
                while (i < subscriptionPurchasesList.size()) {
                    if (!(subscriptionPurchasesList.get(i) == null || AppUtil.isEmpty(((Purchase) subscriptionPurchasesList.get(i)).getSku()))) {
                        PersistenceManager.unlockThePlan(((Purchase) subscriptionPurchasesList.get(i)).getSku());
                        if (((Purchase) subscriptionPurchasesList.get(i)).getSku().equalsIgnoreCase(AppConstants.PREMIUM_VERSION_3_MONTH) || ((Purchase) subscriptionPurchasesList.get(i)).getSku().equalsIgnoreCase(AppConstants.PREMIUM_VERSION_6_MONTH)) {
                            PersistenceManager.setPremiumVersion(true);
                        }
                    }
                    i++;
                }
            }
        }
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
                File currentDB = new File(data, "/data/com.workout.workout/databases/WorkoutDB.db");
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

    public void onPurchasesUpdated(int responseCode, List<Purchase> list) {
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

    private void initializeRemoteConfig() {
        this.mRemoteConfig = FirebaseRemoteConfig.getInstance();
        this.mRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build());
        this.mRemoteConfig.setDefaults((int) R.xml.remote_config_default_values);
        long cacheExpiration = 3600;
        if (this.mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        this.mRemoteConfig.fetch(cacheExpiration).addOnCompleteListener((Activity) this, new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    SplashActivity.this.mRemoteConfig.activateFetched();
                }
            }
        });
    }

    private void initializeMobileAds() {
//        MobileAds.initialize(this, AppConstants.ADMOB_APP_ID);
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
