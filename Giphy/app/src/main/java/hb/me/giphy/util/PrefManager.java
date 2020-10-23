package hb.me.giphy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nxtruong on 7/12/2017.
 */

public class PrefManager {
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    public PrefManager(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPref.edit();
    }

    private static final String IS_CHARGING = "IsCharging";

    public void enableCharging(boolean enable) {
        mEditor.putBoolean(IS_CHARGING, enable);
        mEditor.commit();
    }

    public boolean isEnableCharging() {
        return mPref.getBoolean(IS_CHARGING, true);
    }
}
