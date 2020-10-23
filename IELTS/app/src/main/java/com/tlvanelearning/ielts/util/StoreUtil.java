package com.tlvanelearning.ielts.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class StoreUtil {
    public static void openWordGame(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.arm.edu.wordsearch")));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.arm.edu.wordsearch")));
        }
    }

    public static void rateApp(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void moreApp(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://dev?id=8417530190358697558")));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/dev?id=8417530190358697558")));
        }
    }

    public static void sendEmail(Context context) {
        Log.i("Send email", "");
        String[] TO = new String[]{"levan.kstn.k57@gmail.com"};
        String[] CC = new String[]{""};
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra("android.intent.extra.EMAIL", TO);
        emailIntent.putExtra("android.intent.extra.CC", CC);
        emailIntent.putExtra("android.intent.extra.SUBJECT", "BBC Feedback");
        emailIntent.putExtra("android.intent.extra.TEXT", "Your message goes here:");
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
