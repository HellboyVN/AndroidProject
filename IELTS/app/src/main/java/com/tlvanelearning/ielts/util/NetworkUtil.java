package com.tlvanelearning.ielts.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtil {
    public static boolean isOnline(Context context) {
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void toastMessage(Context context) {
        Toast.makeText(context, "Please check NETWORK conection !", Toast.LENGTH_LONG).show();
    }

    public static void toastPlay(Context context) {
        Toast.makeText(context, "NETWORK is connected, audio will auto play.", Toast.LENGTH_LONG).show();
    }
}
