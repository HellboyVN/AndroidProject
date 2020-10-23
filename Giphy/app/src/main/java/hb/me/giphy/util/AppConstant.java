package hb.me.giphy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nxtruong on 7/5/2017.
 */

public class AppConstant {

    public static final String BUNDLE_CATEGORY_ID = "BUNDLE_CATEGORY_ID";
    public static final String BUNDLE_CATEGORY_NAME = "BUNDLE_CATEGORY_NAME";
    public static final String ggpublishkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjfthb6+liJSX3KFSyQ5nglD6mY+a65AFSi+KYzTJH7WB6XKodbWZIjeJSlubmyen1Dn9KMQHdxB4YwM39nnZuL36W6PRY7O+zI+476P0eFsjYDYWJBZ92GnaaYN3zOhM6ygJob0tlKN05rbAk2qxX0yAonDj+F68Lt97Y6pnFsfW3nsOL9WUx6NAzXkIUqzexHTPPE+2J/B4gu59tfST52TxHsh/dthy7Nfc+ieqZAwf7kodLBKbwBkKjQ/8DE2R4JyoYoQwLhqZT4/xdatuf0mHcWeuye9OyGjUzYBM9KZaR2i08cSSGso3Q95YwoMJQqwTs7mkmKev1OUZKWAYnQIDAQAB";

    public static final int ID_TRENDING_ITEM = 1;
    public static final int ID_EXPLORE_ITEM = 2;
    public static final int ID_VARIETY_ITEM = 3;
    public static final int ID_FAVORITE_ITEM = 4;
    public static final int ID_SETTING_ITEM = 5;
    public static final int ID_ABOUT_ITEM = 6;
    public static final int ID_APP_IELTS = 7;
    public static final int ID_APP_ROCKET = 8;
    public static final int ID_REMOVEADS = 9;

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
