package hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
    public static boolean isNetworkAvailable(Context appContext) {
        if (appContext == null) {
            return false;
        }
        NetworkInfo networkInfo = ((ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }
}
