package com.tlvanelearning.ielts.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MUtil {
    public static void println(String message) {
        Log.d("DEBUG", message);
    }

    public static void alert(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || "google_sdk".equals(Build.PRODUCT));
    }

    public static String changeFormatDate(String oldDateString) {
        try {
            oldDateString = new SimpleDateFormat("MMM dd, yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(oldDateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return oldDateString;
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog createDialog(Context context, String title, String message) {
        return new Builder(context).setMessage(Html.fromHtml(message)).setPositiveButton(title, new OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();
            }
        }).create();
    }

    public static boolean isStoragePermissionGranted(Activity activity) {
        if (VERSION.SDK_INT < 23 || activity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        return false;
    }
}
