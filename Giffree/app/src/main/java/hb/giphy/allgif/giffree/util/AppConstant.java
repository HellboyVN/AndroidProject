package hb.giphy.allgif.giffree.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nxtruong on 7/5/2017.
 */

public class AppConstant {

    public static final String BUNDLE_CATEGORY_ID = "BUNDLE_CATEGORY_ID";
    public static final String BUNDLE_CATEGORY_NAME = "BUNDLE_CATEGORY_NAME";
    public static final String ggpublishkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArDVRWLPyNBarKS9vTijO+8kzgLB6p5BkAYTQHztgGFxncg4uDEvHTNIm6yfyuk5gVq1Rel7/SOpqVLUp/nzIhBUlSiikPtckmoRWRf31C0EghZBndN+CGETxUlwpJt16WDDH5b4S3wTOyMQ437Anf82oUelkkwUrm7ftrts+fFtktDMfoOAliG7Pt9oD6xr9bFaV5BRQ9gx265a/cWoPPOTBpgdCuIqPysMl3cn0bYI3nYyBsSltAiDGFcAo3Kz/KcZLodIDV73TjFyv+Ke1NS801W+uaOC9AoXfkqf/BRzk6jEaVxNc4kAW6guoJhEGanWBXxzurFhGpwqYG6juDQIDAQAB";

    public static final int ID_CHRISTMAS_ITEM = 1;
    public static final int ID_NEWYEAR_ITEM = 2;
    public static final int ID_TRENDING_ITEM = 3;
    public static final int ID_EXPLORE_ITEM = 4;
    public static final int ID_VARIETY_ITEM = 5;
    public static final int ID_FAVORITE_ITEM = 6;
    public static final int ID_SETTING_ITEM = 7;
    public static final int ID_ABOUT_ITEM = 8;
    public static final int ID_APP_IELTS = 9;
    public static final int ID_APP_FITNESS = 10;

    public static final int ID_ACTION_CATEGORY = 1;
    public static final int ID_ANIMAL_CATEGORY = 2;
    public static final int ID_ART_CATEGORY = 3;
    public static final int ID_CARTOON_CATEGORY = 4;
    public static final int ID_FUNNY_CATEGORY = 5;
    public static final int ID_FILM_CATEGORY = 6;
    public static final int ID_GAME_CATEGORY = 7;
    public static final int ID_ISLAMIC_CATEGORY = 8;
    public static final int ID_NATURE_CATEGORY = 9;
    public static final int ID_NEWS_CATEGORY = 10;
    public static final int ID_SCIENCE_CATEGORY = 11;
    public static final int ID_SPORT_CATEGORY = 12;

    public static final String REMOVEADS = "REMOVEADS";
    public static boolean isRemoveAds(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Boolean.valueOf(sharedPreferences.getBoolean(REMOVEADS, false));
    }
    public static void setAdsFreeVersion(Context context, boolean value1){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(REMOVEADS, value1);
        editor.apply();
    }

}
