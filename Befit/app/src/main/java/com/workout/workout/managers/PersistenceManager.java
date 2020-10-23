package com.workout.workout.managers;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.modal.User;
import com.workout.workout.util.AppUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PersistenceManager {
    private static void writeContentToSharedPreferences(String key, Object value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(CachingManager.getAppContext()).edit();
        if (value instanceof String) {
            editor.putString(key, ((String) value).trim());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set) value);
        }
        if (VERSION.SDK_INT >= 9) {
            editor.apply();
        }
    }

    private static Object getContentFromSharedPreferences(String key, Class<?> classType) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CachingManager.getAppContext());
        if (classType.equals(String.class)) {
            return sharedPreferences.getString(key, "");
        }
        if (classType.equals(Boolean.class)) {
            return Boolean.valueOf(sharedPreferences.getBoolean(key, false));
        }
        if (classType.equals(Integer.class)) {
            return Integer.valueOf(sharedPreferences.getInt(key, 0));
        }
        if (classType.equals(Float.class)) {
            return Float.valueOf(sharedPreferences.getFloat(key, 0.0f));
        }
        if (classType.equals(Long.class)) {
            return Long.valueOf(sharedPreferences.getLong(key, 0));
        }
        if (classType.equals(Set.class)) {
            return sharedPreferences.getStringSet(key, null);
        }
        return null;
    }

    public static void addWorkoutId(String workoutId) {
        ArrayList<String> workoutIdList = getLikedWorkoutIdList();
        if (workoutIdList == null) {
            workoutIdList = new ArrayList();
        }
        workoutIdList.add(workoutId);
        Set<String> set = new HashSet();
        set.addAll(workoutIdList);
        writeContentToSharedPreferences(AppConstants.KEY_PREF_WORKOUT_ID_LIST, set);
    }

    public static void removeWorkoutId(String workoutId) {
        ArrayList<String> workoutIdList = getLikedWorkoutIdList();
        if (workoutIdList == null) {
            workoutIdList = new ArrayList();
        } else if (isLikedWorkout(workoutId)) {
            workoutIdList.remove(workoutId);
        }
        Set<String> set = new HashSet();
        set.addAll(workoutIdList);
        writeContentToSharedPreferences(AppConstants.KEY_PREF_WORKOUT_ID_LIST, set);
    }

    public static ArrayList<String> getLikedWorkoutIdList() {
        Set<String> workoutIdList = (Set) getContentFromSharedPreferences(AppConstants.KEY_PREF_WORKOUT_ID_LIST, Set.class);
        if (workoutIdList != null) {
            return new ArrayList(workoutIdList);
        }
        return null;
    }

    public static boolean isLikedWorkout(String workoutId) {
        ArrayList<String> workoutList = getLikedWorkoutIdList();
        if (!AppUtil.isCollectionEmpty(workoutList) && workoutList.contains(workoutId)) {
            return true;
        }
        return false;
    }

    public static String getNewPlanId() {
        int planId = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_NEW_PLAN_ID, Integer.class)).intValue() + 1;
        writeContentToSharedPreferences(AppConstants.KEY_PREF_NEW_PLAN_ID, Integer.valueOf(planId));
        return String.valueOf(planId + 1000);
    }

    public static boolean isPlanLocked(String planID) {
        Set<String> unlockedplan = (Set) getContentFromSharedPreferences(AppConstants.PLAN_IS_LOCKED, Set.class);
        if (unlockedplan == null) {
            return true;
        }
        if (new ArrayList(unlockedplan).contains(planID) || isPremiumVersion()) {
            return false;
        }
        return true;
    }

    public static void unlockThePlan(String sku) {
        ArrayList<String> unlockedPlanList = getUnlockedPlanList();
        if (unlockedPlanList == null) {
            unlockedPlanList = new ArrayList();
        }
        if (!unlockedPlanList.contains(sku)) {
            unlockedPlanList.add(sku);
        }
        Set<String> set = new HashSet();
        set.addAll(unlockedPlanList);
        writeContentToSharedPreferences(AppConstants.PLAN_IS_LOCKED, set);
    }

    public static ArrayList<String> getUnlockedPlanList() {
        Set<String> unlockedplan = (Set) getContentFromSharedPreferences(AppConstants.PLAN_IS_LOCKED, Set.class);
        if (unlockedplan != null) {
            return new ArrayList(unlockedplan);
        }
        return null;
    }

    public static boolean isUserSignedIn() {
        return ((Boolean) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_SIGNED_IN, Boolean.class)).booleanValue();
    }

    public static void setUserSignedIn(boolean result) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_SIGNED_IN, Boolean.valueOf(result));
    }

    public static String getUserUid() {
        return (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_UID, String.class);
    }

    public static void setUserUid(String result) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_UID, result);
    }

    public static void setIdentifier(String identifier) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_IDENTIFIER, identifier);
    }

    public static String getDisplayName() {
        return (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_DISPLAY_NAME, String.class);
    }

    public static void setDisplayName(String result) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_DISPLAY_NAME, result);
    }

    public static String getIdToken() {
        return (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_ID_TOKEN, String.class);
    }

    public static void setIdToken(String result) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_ID_TOKEN, result);
    }

    public static String getEmail() {
        return (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_EMAIL, String.class);
    }

    public static void setEmail(String result) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_EMAIL, result);
    }

    public static String getPhotoUrl() {
        return (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_PHOTO_URL, String.class);
    }

    public static void setPhotoUrl(String result) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_PHOTO_URL, result);
    }

    public static String getProviderId() {
        return (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_PROVIDER_ID, String.class);
    }

    public static void setProviderId(String providerId) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_PROVIDER_ID, providerId);
    }

    public static String getPhoneNumber() {
        return (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_USER_PHONE_NUMBER, String.class);
    }

    public static void setPhoneNumber(String phoneNumber) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_PHONE_NUMBER, phoneNumber);
    }

    public static void resetUserProfile() {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_SIGNED_IN, Boolean.valueOf(false));
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_UID, "");
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_DISPLAY_NAME, "");
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_ID_TOKEN, "");
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_EMAIL, "");
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_PHOTO_URL, "");
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_PROVIDER_ID, "");
        writeContentToSharedPreferences(AppConstants.KEY_PREF_USER_PHONE_NUMBER, "");
    }

    public static void setUserData(User user) {
        String name = user.getDisplay_name();
        String email = user.getEmail();
        String phoneNumber = user.getMobile_number();
        String profilePicUrl = user.getPhoto_url();
        String providerId = user.getProvider_id();
        String token = user.getToken();
        String uid = user.getUser_uid();
        String identifier = user.getIdentifier();
        if (!AppUtil.isEmpty(name)) {
            setDisplayName(name);
        }
        if (!AppUtil.isEmpty(email)) {
            setEmail(email);
        }
        if (!AppUtil.isEmpty(phoneNumber)) {
            setPhoneNumber(phoneNumber);
        }
        if (!AppUtil.isEmpty(profilePicUrl)) {
            setPhotoUrl(profilePicUrl);
        }
        if (!AppUtil.isEmpty(providerId)) {
            setProviderId(providerId);
        }
        if (!AppUtil.isEmpty(token)) {
            setIdToken(token);
        }
        if (!AppUtil.isEmpty(uid)) {
            setUserUid(uid);
        }
        if (!AppUtil.isEmpty(identifier)) {
            setIdentifier(identifier);
        }
    }

    public static int getWorkoutDetailActivityOpenCount() {
        int count = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_WORKOUT_DETAIL_ACTIVITY_OPEN_COUNT, Integer.class)).intValue();
        writeContentToSharedPreferences(AppConstants.KEY_PREF_WORKOUT_DETAIL_ACTIVITY_OPEN_COUNT, Integer.valueOf(count + 1));
        return count + 1;
    }

    public static int getNotificationActivityOpenCount() {
        int count = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_NOTIFICATION_ACTIVITY_OPEN_COUNT, Integer.class)).intValue();
        writeContentToSharedPreferences(AppConstants.KEY_PREF_NOTIFICATION_ACTIVITY_OPEN_COUNT, Integer.valueOf(count + 1));
        return count + 1;
    }

    public static int getBMICalculateCount() {
        int count = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_BMI_CALCULATE_COUNT, Integer.class)).intValue();
        writeContentToSharedPreferences(AppConstants.KEY_PREF_BMI_CALCULATE_COUNT, Integer.valueOf(count + 1));
        return count + 1;
    }

    public static int getProteinCalculateCount() {
        int count = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_PROTEIN_CALCULATE_COUNT, Integer.class)).intValue();
        writeContentToSharedPreferences(AppConstants.KEY_PREF_PROTEIN_CALCULATE_COUNT, Integer.valueOf(count + 1));
        return count + 1;
    }

    public static int getAppOpenCount() {
        int count = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_APP_OPEN_COUNT, Integer.class)).intValue();
        writeContentToSharedPreferences(AppConstants.KEY_PREF_APP_OPEN_COUNT, Integer.valueOf(count + 1));
        return count + 1;
    }

    public static boolean isAdsFreeVersion() {
        return  ((Boolean) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADS_FREE_VERSION, Boolean.class)).booleanValue() || isPremiumVersion();
    }

    public static void setAdsFreeVersion(boolean showAds) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADS_FREE_VERSION, Boolean.valueOf(showAds));
    }

    public static boolean isPremiumVersion() {
        return ((Boolean) getContentFromSharedPreferences(AppConstants.KEY_PREF_PREMIUM_VERSION, Boolean.class)).booleanValue();
    }

    public static void setPremiumVersion(boolean bool) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_PREMIUM_VERSION, Boolean.valueOf(bool));
    }

    public static void deleteSharedPref(String identifier) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(CachingManager.getAppContext()).edit();
        editor.remove(identifier);
        editor.apply();
    }

    public static Integer getDatabaseFileVersion() {
        return Integer.valueOf(((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_DATABASE_FILE_VERSION, Integer.class)).intValue());
    }

    public static void setDatabaseFileVersion(int databaseFileVersion) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_DATABASE_FILE_VERSION, Integer.valueOf(databaseFileVersion));
    }

    public static int getWorkout_detail_interstitial_ads_interval() {
        int workout_detail_interstitial_ads_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_WORKOUT_DETAIL_INTERSTITIAL_AD_INTERVAL, Integer.class)).intValue();
        if (workout_detail_interstitial_ads_interval == 0) {
            return 4;
        }
        return workout_detail_interstitial_ads_interval;
    }

    public static void setWorkout_detail_interstitial_ads_interval(int workout_detail_interstitial_ads_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_WORKOUT_DETAIL_INTERSTITIAL_AD_INTERVAL, Integer.valueOf(workout_detail_interstitial_ads_interval));
    }

    public static int getNotification_interstitial_ads_interval() {
        int notification_interstitial_ads_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_NOTIFICATIO_INTERSTITIAL_AD_INTERVAL, Integer.class)).intValue();
        if (notification_interstitial_ads_interval == 0) {
            return 3;
        }
        return notification_interstitial_ads_interval;
    }

    public static void setNotification_interstitial_ads_interval(int notification_interstitial_ads_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_NOTIFICATIO_INTERSTITIAL_AD_INTERVAL, Integer.valueOf(notification_interstitial_ads_interval));
    }

    public static int getWorkout_list_native_ads_repeat_interval() {
        int workout_list_native_ads_repeat_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL, Integer.class)).intValue();
        if (workout_list_native_ads_repeat_interval == 0) {
            return 4;
        }
        return workout_list_native_ads_repeat_interval;
    }

    public static void setWorkout_list_native_ads_repeat_interval(int workout_list_native_ads_repeat_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL, Integer.valueOf(workout_list_native_ads_repeat_interval));
    }

    public static int getPlan_workout_list_native_ads_repeat_interval() {
        int plan_workout_list_native_ads_repeat_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_PLAN_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL, Integer.class)).intValue();
        if (plan_workout_list_native_ads_repeat_interval == 0) {
            return 4;
        }
        return plan_workout_list_native_ads_repeat_interval;
    }

    public static void setPlan_workout_list_native_ads_repeat_interval(int plan_workout_list_native_ads_repeat_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_PLAN_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL, Integer.valueOf(plan_workout_list_native_ads_repeat_interval));
    }

    public static void setAdmob_utility_native_ad_id(String utility_native_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_UTILITY_NATIVE_AD_ID, utility_native_ad_id);
    }

    public static String getAdmob_utility_native_ad_id() {
        String utility_native_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_UTILITY_NATIVE_AD_ID, String.class);
        if (AppUtil.isEmpty(utility_native_ad_id)) {
            return "ca-app-pub-6998384649053931/8940702905";
        }
        return utility_native_ad_id;
    }

    public static void setAdmob_plan_workout_list_native_ad_id(String plan_workout_list_native_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_NATIVE_AD_ID, plan_workout_list_native_ad_id);
    }

    public static String getAdmob_plan_workout_list_native_ad_id() {
        String plan_workout_list_native_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_NATIVE_AD_ID, String.class);
        if (AppUtil.isEmpty(plan_workout_list_native_ad_id)) {
            return "ca-app-pub-6998384649053931/9833136264";
        }
        return plan_workout_list_native_ad_id;
    }

    public static void setAdmob_workout_list_native_ad_id(String workout_list_native_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_LIST_NATIVE_AD_ID, workout_list_native_ad_id);
    }

    public static String getAdmob_workout_list_native_ad_id() {
        String workout_list_native_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_LIST_NATIVE_AD_ID, String.class);
        if (AppUtil.isEmpty(workout_list_native_ad_id)) {
            return "ca-app-pub-6998384649053931/8473799047";
        }
        return workout_list_native_ad_id;
    }

    public static void setAdmob_body_part_native_ad_id(String body_part_native_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_BODY_PART_LIST_NATIVE_AD_ID, body_part_native_ad_id);
    }

    public static String getAdmob_body_part_native_ad_id() {
        String body_part_native_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_BODY_PART_LIST_NATIVE_AD_ID, String.class);
        if (AppUtil.isEmpty(body_part_native_ad_id)) {
            return "ca-app-pub-6998384649053931/4843344091";
        }
        return body_part_native_ad_id;
    }

    public static String getAdmob_app_id() {
        String admob_app_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_APP_ID, String.class);
        if (AppUtil.isEmpty(admob_app_id)) {
            return "ca-app-pub-6998384649053931~5707777745";
        }
        return admob_app_id;
    }

    public static void setAdmob_app_id(String admob_app_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_APP_ID, admob_app_id);
    }

    public static String getAdmob_body_part_list_banner_ad_id() {
        String body_part_list_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_BODY_PART_LIST_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(body_part_list_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/8214601131";
        }
        return body_part_list_banner_ad_id;
    }

    public static void setAdmob_body_part_list_banner_ad_id(String body_part_list_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_BODY_PART_LIST_BANNER_AD_ID, body_part_list_banner_ad_id);
    }

    public static String getAdmob_favourite_banner_ad_id() {
        String favourite_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_FAVOURITE_ADMOB_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(favourite_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/2777785083";
        }
        return favourite_banner_ad_id;
    }

    public static void setAdmob_favourite_banner_ad_id(String favourite_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_FAVOURITE_ADMOB_BANNER_AD_ID, favourite_banner_ad_id);
    }

    public static String getAdmob_workout_list_banner_ad_id() {
        String workout_list_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_LIST_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(workout_list_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/5211364978";
        }
        return workout_list_banner_ad_id;
    }

    public static void setAdmob_workout_list_banner_ad_id(String workout_list_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_LIST_BANNER_AD_ID, workout_list_banner_ad_id);
    }

    public static String getAdmob_workout_detail_banner_ad_id() {
        String workout_detail_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_DETAIL_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(workout_detail_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/4426742820";
        }
        return workout_detail_banner_ad_id;
    }

    public static void setAdmob_workout_detail_banner_ad_id(String workout_detail_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_DETAIL_BANNER_AD_ID, workout_detail_banner_ad_id);
    }

    public static String getAdmob_bmi_banner_ad_id() {
        String bmi_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_BMI_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(bmi_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/6191126920";
        }
        return bmi_banner_ad_id;
    }

    public static void setAdmob_bmi_banner_ad_id(String bmi_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_BMI_BANNER_AD_ID, bmi_banner_ad_id);
    }

    public static String getAdmob_fat_banner_ad_id() {
        String fat_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_FAT_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(fat_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/5392990504";
        }
        return fat_banner_ad_id;
    }

    public static void setAdmob_fat_banner_ad_id(String fat_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_FAT_BANNER_AD_ID, fat_banner_ad_id);
    }

    public static String getAdmob_protein_banner_ad_id() {
        String protein_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_PROTEIN_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(protein_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/5690711277";
        }
        return protein_banner_ad_id;
    }

    public static void setAdmob_protein_banner_ad_id(String protein_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_PROTEIN_BANNER_AD_ID, protein_banner_ad_id);
    }

    public static String getAdmob_plan_day_list_banner_ad_id() {
        String plan_day_list_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_DAY_LIST_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(plan_day_list_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/1201813682";
        }
        return plan_day_list_banner_ad_id;
    }

    public static void setAdmob_plan_day_list_banner_ad_id(String plan_day_list_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_DAY_LIST_BANNER_AD_ID, plan_day_list_banner_ad_id);
    }

    public static String getAdmob_plan_workout_list_banner_ad_id() {
        String plan_workout_list_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(plan_workout_list_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/8919509847";
        }
        return plan_workout_list_banner_ad_id;
    }

    public static void setAdmob_plan_workout_list_banner_ad_id(String plan_workout_list_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_BANNER_AD_ID, plan_workout_list_banner_ad_id);
    }

    public static String getAdmob_notification_banner_ad_id() {
        String notification_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_NOTIFICATION_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(notification_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/1751466260";
        }
        return notification_banner_ad_id;
    }

    public static void setAdmob_notification_banner_ad_id(String notification_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_NOTIFICATION_BANNER_AD_ID, notification_banner_ad_id);
    }

    public static String getAdmob_app_startup_interstitial_ad_id() {
        String app_startup_interstitial_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_APP_STARTUP_INTERSTITIAL_AD_ID, String.class);
        if (AppUtil.isEmpty(app_startup_interstitial_ad_id)) {
            return "ca-app-pub-6998384649053931/1818914872";
        }
        return app_startup_interstitial_ad_id;
    }

    public static void setAdmob_app_startup_interstitial_ad_id(String app_startup_interstitial_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_APP_STARTUP_INTERSTITIAL_AD_ID, app_startup_interstitial_ad_id);
    }

    public static String getAdmob_protein_interstitial_ad_id() {
        String protein_interstitial_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_PROTEIN_INTERSTITIAL_AD_ID, String.class);
        if (AppUtil.isEmpty(protein_interstitial_ad_id)) {
            return "ca-app-pub-6998384649053931/5867693298";
        }
        return protein_interstitial_ad_id;
    }

    public static void setAdmob_protein_interstitial_ad_id(String protein_interstitial_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_PROTEIN_INTERSTITIAL_AD_ID, protein_interstitial_ad_id);
    }

    public static String getAdmob_bmi_interstitial_ad_id() {
        String bmi_interstitial_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_ID, String.class);
        if (AppUtil.isEmpty(bmi_interstitial_ad_id)) {
            return "ca-app-pub-6998384649053931/2120019978";
        }
        return bmi_interstitial_ad_id;
    }

    public static void setAdmob_bmi_interstitial_ad_id(String bmi_interstitial_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_ID, bmi_interstitial_ad_id);
    }

    public static String getAdmob_fat_interstitial_ad_id() {
        String fat_interstitial_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_FAT_INTERSTITIAL_AD_ID, String.class);
        if (AppUtil.isEmpty(fat_interstitial_ad_id)) {
            return "ca-app-pub-6998384649053931/3719804425";
        }
        return fat_interstitial_ad_id;
    }

    public static void setAdmob_fat_interstitial_ad_id(String bmi_interstitial_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_FAT_INTERSTITIAL_AD_ID, bmi_interstitial_ad_id);
    }

    public static String getAdmob_notification_interstitial_ad_id() {
        String notification_interstitial_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_NOTIFICATION_INTERSTITIAL_AD_ID, String.class);
        if (AppUtil.isEmpty(notification_interstitial_ad_id)) {
            return "ca-app-pub-6998384649053931/5607601162";
        }
        return notification_interstitial_ad_id;
    }

    public static void setAdmob_notification_interstitial_ad_id(String notification_interstitial_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_NOTIFICATION_INTERSTITIAL_AD_ID, notification_interstitial_ad_id);
    }

    public static String getAdmob_workout_detail_interstitial_ad_id() {
        String workout_detail_interstitial_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID, String.class);
        if (AppUtil.isEmpty(workout_detail_interstitial_ad_id)) {
            return "ca-app-pub-6998384649053931/6709947778";
        }
        return workout_detail_interstitial_ad_id;
    }

    public static void setAdmob_workout_detail_interstitial_ad_id(String workout_detail_interstitial_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID, workout_detail_interstitial_ad_id);
    }

    public static int getExercise_list_native_ads_repeat_interval() {
        int exercise_list_native_ads_repeat_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_EXERCISE_LIST_NATIVE_ADS_REPEAT_INTERVAL, Integer.class)).intValue();
        if (exercise_list_native_ads_repeat_interval == 0) {
            return 4;
        }
        return exercise_list_native_ads_repeat_interval;
    }

    public static void setExercise_list_native_ads_repeat_interval(int exercise_list_native_ads_repeat_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_EXERCISE_LIST_NATIVE_ADS_REPEAT_INTERVAL, Integer.valueOf(exercise_list_native_ads_repeat_interval));
    }

    public static int getAdmob_protein_interstitial_ads_interval() {
        int protein_interstitial_ads_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_PROTEIN_INSTERSTITIAL_AD_INTERVAL, Integer.class)).intValue();
        if (protein_interstitial_ads_interval == 0) {
            return 1;
        }
        return protein_interstitial_ads_interval;
    }

    public static void setAdmob_protein_interstitial_ads_interval(int protein_interstitial_ads_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_PROTEIN_INSTERSTITIAL_AD_INTERVAL, Integer.valueOf(protein_interstitial_ads_interval));
    }

    public static int getAdmob_bmi_interstitial_ads_interval() {
        int bmi_interstitial_ads_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_INTERVAL, Integer.class)).intValue();
        if (bmi_interstitial_ads_interval == 0) {
            return 1;
        }
        return bmi_interstitial_ads_interval;
    }

    public static void setAdmob_bmi_interstitial_ads_interval(int bmi_interstitial_ads_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_INTERVAL, Integer.valueOf(bmi_interstitial_ads_interval));
    }

    public static int getAdmob_app_start_interstitial_ads_interval() {
        int app_start_interstitial_ads_interval = ((Integer) getContentFromSharedPreferences(AppConstants.KEY_PREF_APP_START_INTERSTITIAL_ADS_INTERVAL, Integer.class)).intValue();
        if (app_start_interstitial_ads_interval == 0) {
            return 5;
        }
        return app_start_interstitial_ads_interval;
    }

    public static void setAdmob_app_start_interstitial_ads_interval(int app_start_interstitial_ads_interval) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_APP_START_INTERSTITIAL_ADS_INTERVAL, Integer.valueOf(app_start_interstitial_ads_interval));
    }

    public static String getAdmob_utility_banner_ad_id() {
        String utility_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_UTILITY_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(utility_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/2749864922";
        }
        return utility_banner_ad_id;
    }

    public static void setAdmob_utility_banner_ad_id(String utility_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_UTILITY_BANNER_AD_ID, utility_banner_ad_id);
    }

    public static String getAdmob_plan_banner_ad_id() {
        String plan_banner_ad_id = (String) getContentFromSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_BANNER_AD_ID, String.class);
        if (AppUtil.isEmpty(plan_banner_ad_id)) {
            return "ca-app-pub-6998384649053931/4665581828";
        }
        return plan_banner_ad_id;
    }

    public static void setAdmob_plan_banner_ad_id(String plan_banner_ad_id) {
        writeContentToSharedPreferences(AppConstants.KEY_PREF_ADMOB_PLAN_BANNER_AD_ID, plan_banner_ad_id);
    }
}
