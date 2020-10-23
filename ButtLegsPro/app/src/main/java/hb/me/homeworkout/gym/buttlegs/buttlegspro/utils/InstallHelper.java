package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class InstallHelper {
    public static void openPlayStore(Context context, String packageName) {
        try {
            Intent goToMarket = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName));
            goToMarket.setFlags(268435456);
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
            intent.setFlags(268435456);
            context.startActivity(intent);
        }
    }

    public static boolean appInstalledOrNot(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
