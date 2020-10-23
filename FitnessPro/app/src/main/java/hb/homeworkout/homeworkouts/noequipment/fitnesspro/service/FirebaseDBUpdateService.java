package hb.homeworkout.homeworkouts.noequipment.fitnesspro.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.AppSettings;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.BodyPart;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.PlanCategory;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;

public class FirebaseDBUpdateService extends Service {
    private static final String TAG = FirebaseDBUpdateService.class.getName();
    private Context appContext;
    private List<BodyPart> bodyPartList = new ArrayList();
    private List<PlanCategory> planCategoryList = new ArrayList();
    private List<Training> trainingList = new ArrayList();

    void showToast(final String str) {
        if (this.appContext != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Toast.makeText(FirebaseDBUpdateService.this.appContext, str, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.appContext = getBaseContext();
        startListeningForAppSettings();
        return 1;
    }

    private void startListeningForAppSettings() {
//        FirebaseDatabase.getInstance().getReference("app_settings").addValueEventListener(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot != null && dataSnapshot.exists()) {
//                    Log.e("Settings List Count ", "" + dataSnapshot.getChildrenCount());
//                    AppSettings appSettings = (AppSettings) dataSnapshot.getValue(AppSettings.class);
//                    if (appSettings != null) {
//                        Log.w(FirebaseDBUpdateService.TAG, appSettings.toString());
//                        FirebaseDBUpdateService.this.saveAppSettings(appSettings);
//                    }
//                }
//            }
//
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
    }

    private void saveAppSettings(AppSettings appSettings) {
        int workoutListNativeAdInterval;
        int workoutDetailInterstitialAdInterval;
        int notificationInterstitialAdInterval;
        int exerciseListNativeAdInterval;
        int planWorkoutListNativeAdInterval;
        int app_start_interstitial_ads_interval;
        int bmi_interstitial_ads_interval;
        int protein_interstitial_ads_interval;
        try {
            workoutListNativeAdInterval = Integer.parseInt(appSettings.getWorkout_list_native_ads_repeat_interval_new());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            workoutListNativeAdInterval = 4;
        }
        String admobAppId = appSettings.getAdmob_app_id();
        try {
            workoutDetailInterstitialAdInterval = Integer.parseInt(appSettings.getWorkout_detail_interstitial_ads_interval());
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
            workoutDetailInterstitialAdInterval = 3;
        }
        try {
            notificationInterstitialAdInterval = Integer.parseInt(appSettings.getNotification_interstitial_ads_repeat_interval());
        } catch (NumberFormatException e22) {
            e22.printStackTrace();
            notificationInterstitialAdInterval = 3;
        }
        try {
            exerciseListNativeAdInterval = Integer.parseInt(appSettings.getExercise_list_native_ads_repeat_interval());
        } catch (NumberFormatException e222) {
            e222.printStackTrace();
            exerciseListNativeAdInterval = 3;
        }
        try {
            planWorkoutListNativeAdInterval = Integer.parseInt(String.valueOf(0));
        } catch (NumberFormatException e2222) {
            e2222.printStackTrace();
            planWorkoutListNativeAdInterval = 4;
        }
        String body_part_native_ad_id = appSettings.getBody_part_native_ad_id();
        String workout_list_native_ad_id = appSettings.getWorkout_list_native_ad_id();
        String plan_workout_list_native_ad_id = appSettings.getPlan_workout_list_native_ad_id();
        String utility_native_ad_id = appSettings.getUtility_native_ad_id();
        String workout_detail_interstitial_ad_id = appSettings.getWorkout_detail_interstitial_ad_id();
        String bmi_interstitial_ad_id = appSettings.getBmi_interstitial_ad_id();
        String protein_interstitial_ad_id = appSettings.getProtein_interstitial_ad_id();
        String app_startup_interstitial_ad_id = appSettings.getApp_startup_interstitial_ad_id();
        String body_part_list_banner_ad_id = appSettings.getBody_part_list_banner_ad_id();
        String favourite_banner_ad_id = appSettings.getFavourite_banner_ad_id();
        String workout_list_banner_ad_id = appSettings.getWorkout_list_banner_ad_id();
        String workout_detail_banner_ad_id = appSettings.getWorkout_detail_banner_ad_id();
        String bmi_banner_ad_id = appSettings.getBmi_banner_ad_id();
        String protein_banner_ad_id = appSettings.getProtein_banner_ad_id();
        String plan_day_list_banner_ad_id = appSettings.getPlan_day_list_banner_ad_id();
        String plan_workout_list_banner_ad_id = appSettings.getPlan_workout_list_banner_ad_id();
        String notification_banner_ad_id = appSettings.getNotification_banner_ad_id();
        String utility_banner_ad_id = appSettings.getUtility_banner_ad_id();
        String plan_banner_ad_id = appSettings.getPlan_banner_ad_id();
        String fat_banner_ad_id = appSettings.getFat_banner_ad_id();
        String fat_interstitial_ad_id = appSettings.getFat_interstitial_ad_id();
        String notification_interstitial_ad_id = appSettings.getNotification_interstitial_ad_id();
        try {
            app_start_interstitial_ads_interval = Integer.parseInt(appSettings.getApp_start_interstitial_ads_interval());
        } catch (NumberFormatException e22222) {
            e22222.printStackTrace();
            app_start_interstitial_ads_interval = 5;
        }
        try {
            bmi_interstitial_ads_interval = Integer.parseInt(appSettings.getBmi_interstitial_ads_interval());
        } catch (NumberFormatException e222222) {
            e222222.printStackTrace();
            bmi_interstitial_ads_interval = 1;
        }
        try {
            protein_interstitial_ads_interval = Integer.parseInt(appSettings.getProtein_interstitial_ads_interval());
        } catch (NumberFormatException e2222222) {
            e2222222.printStackTrace();
            protein_interstitial_ads_interval = 1;
        }
        PersistenceManager.setWorkout_list_native_ads_repeat_interval(workoutListNativeAdInterval);
        PersistenceManager.setAdmob_app_id(admobAppId);
        PersistenceManager.setWorkout_detail_interstitial_ads_interval(workoutDetailInterstitialAdInterval);
        PersistenceManager.setExercise_list_native_ads_repeat_interval(exerciseListNativeAdInterval);
        PersistenceManager.setPlan_workout_list_native_ads_repeat_interval(planWorkoutListNativeAdInterval);
        PersistenceManager.setNotification_interstitial_ads_interval(notificationInterstitialAdInterval);
        PersistenceManager.setAdmob_app_startup_interstitial_ad_id(app_startup_interstitial_ad_id);
        PersistenceManager.setAdmob_bmi_interstitial_ad_id(bmi_interstitial_ad_id);
        PersistenceManager.setAdmob_workout_detail_interstitial_ad_id(workout_detail_interstitial_ad_id);
        PersistenceManager.setAdmob_protein_interstitial_ad_id(protein_interstitial_ad_id);
        PersistenceManager.setAdmob_body_part_native_ad_id(body_part_native_ad_id);
        PersistenceManager.setAdmob_utility_native_ad_id(utility_native_ad_id);
        PersistenceManager.setAdmob_plan_workout_list_native_ad_id(plan_workout_list_native_ad_id);
        PersistenceManager.setAdmob_workout_list_native_ad_id(workout_list_native_ad_id);
        PersistenceManager.setAdmob_body_part_list_banner_ad_id(body_part_list_banner_ad_id);
        PersistenceManager.setAdmob_workout_detail_banner_ad_id(workout_detail_banner_ad_id);
        PersistenceManager.setAdmob_workout_list_banner_ad_id(workout_list_banner_ad_id);
        PersistenceManager.setAdmob_favourite_banner_ad_id(favourite_banner_ad_id);
        PersistenceManager.setAdmob_bmi_banner_ad_id(bmi_banner_ad_id);
        PersistenceManager.setAdmob_protein_banner_ad_id(protein_banner_ad_id);
        PersistenceManager.setAdmob_notification_banner_ad_id(notification_banner_ad_id);
        PersistenceManager.setAdmob_plan_day_list_banner_ad_id(plan_day_list_banner_ad_id);
        PersistenceManager.setAdmob_plan_workout_list_banner_ad_id(plan_workout_list_banner_ad_id);
        PersistenceManager.setAdmob_plan_banner_ad_id(plan_banner_ad_id);
        PersistenceManager.setAdmob_utility_banner_ad_id(utility_banner_ad_id);
        PersistenceManager.setAdmob_app_start_interstitial_ads_interval(app_start_interstitial_ads_interval);
        PersistenceManager.setAdmob_bmi_interstitial_ads_interval(bmi_interstitial_ads_interval);
        PersistenceManager.setAdmob_protein_interstitial_ads_interval(protein_interstitial_ads_interval);
        PersistenceManager.setAdmob_fat_banner_ad_id(fat_banner_ad_id);
        PersistenceManager.setAdmob_fat_interstitial_ad_id(fat_interstitial_ad_id);
        PersistenceManager.setAdmob_notification_interstitial_ad_id(notification_interstitial_ad_id);
    }
}
