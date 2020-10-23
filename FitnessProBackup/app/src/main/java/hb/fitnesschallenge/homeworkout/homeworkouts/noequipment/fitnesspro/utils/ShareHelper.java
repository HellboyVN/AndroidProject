package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareHelper {
    public static void shareApp(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction("android.intent.action.SEND");
        sendIntent.putExtra("android.intent.extra.TEXT", "http://play.google.com/store/apps/details?id=" + context.getPackageName());
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent, " ");
        chooser.setFlags(268435456);
        context.startActivity(chooser);
    }

    public static void shareWithInstagram(Context context) {
        Intent likeIng = new Intent("android.intent.action.VIEW", Uri.parse(ConsKeys.INSTA_URL));
        likeIng.setPackage("com.instagram.android");
        try {
            context.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(ConsKeys.INSTA_URL)));
        }
    }
}
