package hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;

public class AppConstants {
    public static com.facebook.ads.InterstitialAd interstitialAd;
    public static int  checkcount = 1;
    public static void showFAdOnStart(Context context) {
        if (!PersistenceManager.isAdsFreeVersion()) {
            interstitialAd = new com.facebook.ads.InterstitialAd(context, "333762600459223_333764300459053");
            interstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial displayed callback
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    Log.e("---HellBoy---", "Error: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Show the ad when it's done loading.
                    interstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }
            });

            interstitialAd.loadAd();
        }
    }
    public static void showFAd(Context context) {
        if (!PersistenceManager.isAdsFreeVersion()) {
            interstitialAd = new com.facebook.ads.InterstitialAd(context, "333762600459223_333764967125653");
            interstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial displayed callback
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    Log.e("---HellBoy---", "Error: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Show the ad when it's done loading.
                    interstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }
            });

            interstitialAd.loadAd();
        }
    }
    public static final String BODY_PART = "BODY_PART";
    public static final String CLOSE_BRACKET = " ) ";
    public static final String COLUMN_BODY_PART_NAME = "BODY_PART_NAME";
    public static final String COLUMN_WORKOUT_DESCRIPTION = "WORKOUT_DESCRIPTION";
    public static final String COLUMN_WORKOUT_ID = "WORKOUT_ID";
    public static final String COLUMN_WORKOUT_IMAGE_NAME = "WORKOUT_IMAGE_NAME";
    public static final String COLUMN_WORKOUT_IMAGE_URL = "WORKOUT_IMAGE_URL";
    public static final String COLUMN_WORKOUT_NAME = "WORKOUT_NAME";
    public static final String COLUMN_WORKOUT_VIDEO_NAME = "WORKOUT_VIDEO_NAME";
    public static final String COLUMN_WORKOUT_VIDEO_URL = "WORKOUT_VIDEO_URL";
    public static final String COMMA = " , ";
    public static final String CREATE_TABLE = "CREATE TABLE";
    public static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS";
    public static final int DATABASE_FILE_VERSION = 1;
    public static final String DELETE_FROM = "DELETE FROM ";
    public static final String EQUALS = " = ";
    public static final String FROM = "FROM ";
    public static final String INTEGER = "INTEGER";
    public static final String KEY_PREF_ADMOB_APP_ID = "KEY_PREF_ADMOB_APP_ID";
    public static final String KEY_PREF_ADMOB_APP_STARTUP_INTERSTITIAL_AD_ID = "KEY_PREF_ADMOB_APP_STARTUP_INTERSTITIAL_AD_ID";
    public static final String KEY_PREF_ADMOB_BANNER_AD_ID = "KEY_PREF_ADMOB_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_BMI_BANNER_AD_ID = "KEY_PREF_ADMOB_BMI_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_ID = "KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_ID";
    public static final String KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_INTERVAL = "KEY_PREF_ADMOB_BMI_INTERSTITIAL_AD_INTERVAL";
    public static final String KEY_PREF_ADMOB_BODY_PART_LIST_BANNER_AD_ID = "KEY_PREF_ADMOB_BODY_PART_LIST_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_BODY_PART_LIST_NATIVE_AD_ID = "KEY_PREF_ADMOB_BODY_PART_LIST_NATIVE_AD_ID";
    public static final String KEY_PREF_ADMOB_FAT_BANNER_AD_ID = "KEY_PREF_ADMOB_FAT_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_FAT_INTERSTITIAL_AD_ID = "KEY_PREF_ADMOB_FAT_INTERSTITIAL_AD_ID";
    public static final String KEY_PREF_ADMOB_INTERSTITIAL_AD_ID = "KEY_PREF_ADMOB_INTERSTITIAL_AD_ID";
    public static final String KEY_PREF_ADMOB_NATIVE_AD_ID = "KEY_PREF_ADMOB_NATIVE_AD_ID";
    public static final String KEY_PREF_ADMOB_NOTIFICATION_BANNER_AD_ID = "KEY_PREF_ADMOB_NOTIFICATION_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_NOTIFICATION_INTERSTITIAL_AD_ID = "KEY_PREF_ADMOB_NOTIFICATION_INTERSTITIAL_AD_ID";
    public static final String KEY_PREF_ADMOB_PLAN_BANNER_AD_ID = "KEY_PREF_ADMOB_PLAN_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_PLAN_DAY_LIST_BANNER_AD_ID = "KEY_PREF_ADMOB_PLAN_DAY_LIST_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_BANNER_AD_ID = "KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_NATIVE_AD_ID = "KEY_PREF_ADMOB_PLAN_WORKOUT_LIST_NATIVE_AD_ID";
    public static final String KEY_PREF_ADMOB_PROTEIN_BANNER_AD_ID = "KEY_PREF_ADMOB_PROTEIN_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_PROTEIN_INSTERSTITIAL_AD_INTERVAL = "KEY_PREF_ADMOB_PROTEIN_INSTERSTITIAL_AD_INTERVAL";
    public static final String KEY_PREF_ADMOB_PROTEIN_INTERSTITIAL_AD_ID = "KEY_PREF_ADMOB_PROTEIN_INTERSTITIAL_AD_ID";
    public static final String KEY_PREF_ADMOB_UTILITY_BANNER_AD_ID = "KEY_PREF_ADMOB_UTILITY_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_UTILITY_NATIVE_AD_ID = "KEY_PREF_ADMOB_UTILITY_NATIVE_AD_ID";
    public static final String KEY_PREF_ADMOB_WORKOUT_DETAIL_BANNER_AD_ID = "KEY_PREF_ADMOB_WORKOUT_DETAIL_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID = "KEY_PREF_ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID";
    public static final String KEY_PREF_ADMOB_WORKOUT_LIST_BANNER_AD_ID = "KEY_PREF_ADMOB_WORKOUT_LIST_BANNER_AD_ID";
    public static final String KEY_PREF_ADMOB_WORKOUT_LIST_NATIVE_AD_ID = "KEY_PREF_ADMOB_WORKOUT_LIST_NATIVE_AD_ID";
    public static final String KEY_PREF_ADS_FREE_REWARD_VIDEO_TIME = "KEY_PREF_ADS_FREE_REWARD_VIDEO_TIME";
    public static final String KEY_PREF_ADS_FREE_VERSION = "KEY_PREF_ADS_FREE_VERSION";
    public static final String KEY_PREF_APP_OPEN_COUNT = "KEY_PREF_APP_OPEN_COUNT";
    public static final String KEY_PREF_APP_START_INTERSTITIAL_ADS_INTERVAL = "KEY_PREF_APP_START_INTERSTITIAL_ADS_INTERVAL";
    public static final String KEY_PREF_BMI_CALCULATE_COUNT = "KEY_PREF_BMI_CALCULATE_COUNT";
    public static final String KEY_PREF_BMI_CALCULATOR_BANNER_ADS_ENABLE = "KEY_PREF_BMI_CALCULATOR_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_DATABASE_FILE_VERSION = "KEY_PREF_DATABASE_FILE_VERSION";
    public static final String KEY_PREF_EXERCISE_BANNER_ADS_ENABLE = "KEY_PREF_EXERCISE_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_EXERCISE_LIST_NATIVE_ADS_REPEAT_INTERVAL = "KEY_PREF_EXERCISE_LIST_NATIVE_ADS_REPEAT_INTERVAL";
    public static final String KEY_PREF_FAVOURITE_ADMOB_BANNER_AD_ID = "KEY_PREF_FAVOURITE_ADMOB_BANNER_AD_ID";
    public static final String KEY_PREF_FAVOURITE_BANNER_ADS_ENABLE = "KEY_PREF_FAVOURITE_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_MORE_BANNER_ADS_ENABLE = "KEY_PREF_MORE_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_NEW_PLAN_ID = "KEY_PREF_NEW_PLAN_ID";
    public static final String KEY_PREF_NOTIFICATION_ACTIVITY_BANNER_ADS_ENABLE = "KEY_PREF_NOTIFICATION_ACTIVITY_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_NOTIFICATION_ACTIVITY_OPEN_COUNT = "KEY_PREF_NOTIFICATION_ACTIVITY_OPEN_COUNT";
    public static final String KEY_PREF_NOTIFICATIO_INTERSTITIAL_AD_INTERVAL = "KEY_PREF_NOTIFICATIO_INTERSTITIAL_AD_INTERVAL";
    public static final String KEY_PREF_PLAN_BANNER_ADS_ENABLE = "KEY_PREF_PLAN_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_PLAN_DAY_LIST_BANNER_ADS_ENABLE = "KEY_PREF_PLAN_DAY_LIST_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_PLAN_WORKOUT_LIST_BANNER_ADS_ENABLE = "KEY_PREF_PLAN_WORKOUT_LIST_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_PLAN_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL = "KEY_PREF_PLAN_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL";
    public static final String KEY_PREF_PREMIUM_VERSION = "KEY_PREF_PREMIUM_VERSION";
    public static final String KEY_PREF_PROTEIN_CALCULATE_COUNT = "KEY_PREF_PROTEIN_CALCULATE_COUNT";
    public static final String KEY_PREF_PROTEIN_CALCULATOR_BANNER_ADS_ENABLE = "KEY_PREF_PROTEIN_CALCULATOR_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_USER_DISPLAY_NAME = "KEY_PREF_USER_DISPLAY_NAME";
    public static final String KEY_PREF_USER_EMAIL = "KEY_PREF_USER_EMAIL";
    public static final String KEY_PREF_USER_IDENTIFIER = "KEY_PREF_USER_IDENTIFIER";
    public static final String KEY_PREF_USER_ID_TOKEN = "KEY_PREF_USER_ID_TOKEN";
    public static final String KEY_PREF_USER_PHONE_NUMBER = "KEY_PREF_USER_PHONE_NUMBER";
    public static final String KEY_PREF_USER_PHOTO_URL = "KEY_PREF_USER_PHOTO_URL";
    public static final String KEY_PREF_USER_PROVIDER_ID = "KEY_PREF_USER_PROVIDER_ID";
    public static final String KEY_PREF_USER_SIGNED_IN = "KEY_PREF_USER_SIGNED_IN";
    public static final String KEY_PREF_USER_UID = "KEY_PREF_USER_UID";
    public static final String KEY_PREF_UTILITY_BANNER_ADS_ENABLE = "KEY_PREF_UTILITY_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_WORKOUT_DETAIL_ACTIVITY_OPEN_COUNT = "KEY_PREF_WORKOUT_DETAIL_ACTIVITY_OPEN_COUNT";
    public static final String KEY_PREF_WORKOUT_DETAIL_BANNER_ADS_ENABLE = "KEY_PREF_WORKOUT_DETAIL_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_WORKOUT_DETAIL_INTERSTITIAL_AD_INTERVAL = "KEY_PREF_WORKOUT_DETAIL_INTERSTITIAL_AD_INTERVAL";
    public static final String KEY_PREF_WORKOUT_ID_LIST = "KEY_PREF_WORKOUT_ID_LIST";
    public static final String KEY_PREF_WORKOUT_LIST_BANNER_ADS_ENABLE = "KEY_PREF_WORKOUT_LIST_BANNER_ADS_ENABLE";
    public static final String KEY_PREF_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL = "KEY_PREF_WORKOUT_LIST_NATIVE_ADS_REPEAT_INTERVAL";
    public static final String MAX = "MAX ";
    public static final String NEWS_OBJECT = "PLAN_OBJECT";
    public static final String NOTIFICATION_BODY = "body";
    public static final String NOTIFICATION_DATE = "date";
    public static final String NOTIFICATION_EXTRA = "extra";
    public static final String NOTIFICATION_IMAGE = "image";
    public static final String NOTIFICATION_READ = "read";
    public static final String NOTIFICATION_SEEN = "seen";
    public static final String NOTIFICATION_SOURCE = "source";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String OPEN_BRACKET = " ( ";
    public static final String PLAN_IS_LOCKED = "plan_is_locked";
    public static final String PLAN_NAME = "plan_name";
    public static final String PLAN_OBJECT = "PLAN_OBJECT";
    public static final String PREMIUM_VERSION_3_MONTH = "premiumversion3months";
    public static final String PREMIUM_VERSION_6_MONTH = "premiumversion6months";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String PURCHASE_TYPE_INAPP = "inapp";
    public static final String PURCHASE_TYPE_SUBSCRIPTION = "subs";
    public static final String QUOTES = "\"";
    public static final String SELECT = "SELECT ";
    public static final String SELECT_DISTINCT = "SELECT DISTINCT";
    public static final String SELECT_STAR_FROM = "SELECT * FROM";
    public static final String SKU_AD_FREE = "adsfreepurchase";
    public static final String SKU_PREMIUM_VERSION = "premiumversion";
    public static final int SPLASH_TIME = 3000;
    public static final String TABLE_NAME_BODYPART = "Bodyparts";
    public static String TABLE_NAME_FAVOURITE_WORKOUT = "FAVOURITE_WORKOUT";
    public static final String TABLE_NAME_MY_WORKOUT_PLANS = "MyWorkoutPlans";
    public static String TABLE_NAME_NOTIFICATION_NEWS = "MyNotifications";
    public static String TABLE_NAME_NOTIFICATION_SOURCE = "MyNotificationSource";
    public static final String TABLE_NAME_PLAN_CATEGORY = "plan_category";
    public static final String TABLE_NAME_PLAN_DETAIL = "plan_detail";
    public static final String TABLE_NAME_WORKOUT = "Workout";
    public static final String TABLE_NAME_WORKOUT_PLAN = "workout_plan";
    public static final String TEXT = "TEXT";
    public static final String UNIQUE = "UNIQUE";
    public static final String VALID_AGE_REGEX = "\"^[0-9]{1,3}$\"";
    public static final String VALID_EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String VALID_PASSWORD_REGX = "[^A-Za-z0-9]";
    public static final String WHERE = " WHERE ";
    public static final String WORKOUT_LIST = "WORKOUT_LIST";
    public static final String WORKOUT_OBJECT = "WORKOUT_OBJECT";
}
