package com.workout.workout.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.Html;

import com.android.billingclient.api.BillingClient;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.workout.workout.R;
import com.workout.workout.activity.NotificationActivity;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseManager;

import java.util.Date;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String body;
    String title;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            Answers.getInstance().logShare((ShareEvent) new ShareEvent().putMethod("Notification received").putCustomAttribute("Title", this.title));
        } catch (Exception e) {

            e.printStackTrace();
        }
        Intent destinationActivity = new Intent(this, NotificationActivity.class);
        destinationActivity.setAction(Long.toString(System.currentTimeMillis()));
        destinationActivity.addFlags(67108864);
        Map<String, String> m = remoteMessage.getData();
        String imageUrl = (String) m.get(AppConstants.NOTIFICATION_IMAGE);
        String linkUrl = (String) m.get("link");
        this.title = (String) m.get("title");
        this.body = (String) m.get(AppConstants.NOTIFICATION_BODY);
        String date = String.valueOf(System.currentTimeMillis());
        Bundle b = new Bundle();
        b.putString("title", this.title);
        b.putString(AppConstants.NOTIFICATION_BODY, this.body);
        b.putString(AppConstants.NOTIFICATION_IMAGE, imageUrl);
        b.putString("link", linkUrl);
        b.putString(AppConstants.NOTIFICATION_DATE, date);
        destinationActivity.putExtras(b);
        destinationActivity.setFlags(268435456);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, destinationActivity, 1073741824);
        Builder builder = new Builder(this);
        builder.setContentTitle(this.title);
        this.body = this.body.replace("\n", "<br>");
        builder.setContentText(Html.fromHtml(this.body));
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        if (imageUrl != null) {
            try {
                builder.setStyle(new BigPictureStyle().bigPicture((Bitmap) Glide.with((Context) this).load(imageUrl).asBitmap().into(400, 400).get()).setBigContentTitle(this.title).setSummaryText(this.body));
            } catch (Exception e3) {

                try {
                    e3.printStackTrace();
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(getRandomNumberForID(), builder.build());
                DatabaseManager.getInstance(this).addNotificationNews(date, this.title, this.body, imageUrl, false, false);
                DatabaseManager.getInstance(this).addSourceToNotification(date, linkUrl, "");
                ShortcutBadger.applyCount(this, DatabaseManager.getInstance(this).numberOfUnreadReadNews());
            }
        }
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(getRandomNumberForID(), builder.build());
        DatabaseManager.getInstance(this).addNotificationNews(date, this.title, this.body, imageUrl, false, false);
        try {
            DatabaseManager.getInstance(this).addSourceToNotification(date, linkUrl, "");
        } catch (Exception e222) {
            e222.printStackTrace();
        }
        ShortcutBadger.applyCount(this, DatabaseManager.getInstance(this).numberOfUnreadReadNews());
    }

    private int getRandomNumberForID() {
        try {
            return (int) ((new Date().getTime() / 1000) % 2147483647L);
        } catch (Exception e) {
            return 0;
        }
    }
}
