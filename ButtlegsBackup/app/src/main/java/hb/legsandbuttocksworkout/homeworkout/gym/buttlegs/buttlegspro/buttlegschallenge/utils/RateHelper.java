package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.Toast;

public class RateHelper {
    public static void openPlayStore(Context context, String packageName, String message) {
        Toast.makeText(context, message, 0).show();
        Intent goToMarket = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName));
        goToMarket.setFlags(268435456);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
            intent.setFlags(268435456);
            context.startActivity(intent);
        }
    }

    public static void shareApp(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction("android.intent.action.SEND");
        sendIntent.putExtra("android.intent.extra.TEXT", "http://play.google.com/store/apps/details?id=" + context.getPackageName());
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent, " ");
        chooser.setFlags(268435456);
        context.startActivity(chooser);
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
