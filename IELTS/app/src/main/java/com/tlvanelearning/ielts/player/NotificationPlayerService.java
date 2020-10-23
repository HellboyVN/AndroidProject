package com.tlvanelearning.ielts.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;

import com.tlvanelearning.ielts.DetailActivity;
import com.tlvanelearning.ielts.R;
import com.tlvanelearning.ielts.player.PlayerListener.NotificationServiceListener;

class NotificationPlayerService implements NotificationServiceListener {
    static final String ACTION = "ACTION";
    static final String KILL = "KILL";
    private static final int KILL_ID = 4;
    static final String NEXT = "NEXT";
    private static final int NEXT_ID = 0;
    private static final int NOTIFICATION_ID = 100;
    static final String PAUSE = "PAUSE";
    private static final int PAUSE_ID = 3;
    static final String PLAY = "PLAY";
    private static final int PLAY_ID = 2;
    static final String PREVIOUS = "PREVIOUS";
    private static final int PREVIOUS_ID = 1;
    private Context context;
    private Notification notification;
    private Builder notificationCompat;
    private NotificationManager notificationManager;
    private Options options = new Options();
    private PlayerService playerService;
    private String title;

    public NotificationPlayerService(Context context) {
        this.context = context;
        this.options.inScaled = true;
    }

    public void createNotificationPlayer(String title) {
        this.title = title;
        Intent openUi = new Intent(this.context, DetailActivity.class);
        openUi.setFlags(536870912);
        this.playerService = PlayerService.getInstance();
        this.playerService.registerNotificationListener(this);
        if (this.notificationManager == null) {
            this.notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (VERSION.SDK_INT >= 21) {
            this.notification = new Notification.Builder(this.context).setVisibility(1).setSmallIcon(R.drawable.ic_notification).setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_notification, this.options)).setContent(createNotificationPlayerView()).setContentIntent(PendingIntent.getActivity(this.context, 100, openUi, 268435456)).setCategory("social").build();
            this.notificationManager.notify(100, this.notification);
            return;
        }
        this.notificationCompat = new Builder(this.context).setVisibility(1).setSmallIcon(R.drawable.ic_notification).setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_notification, this.options)).setContent(createNotificationPlayerView()).setContentIntent(PendingIntent.getActivity(this.context, 100, openUi, 268435456)).setCategory("social");
        this.notificationManager.notify(100, this.notificationCompat.build());
    }

    public void updateNotification() {
        createNotificationPlayer(this.title);
    }

    private RemoteViews createNotificationPlayerView() {
        RemoteViews remoteView;
        if (this.playerService.isPlaying()) {
            remoteView = new RemoteViews(this.context.getPackageName(), R.layout.notification_play);
            remoteView.setOnClickPendingIntent(R.id.btn_pause_notification, buildPendingIntent("PAUSE", 3));
        } else {
            remoteView = new RemoteViews(this.context.getPackageName(), R.layout.notification_pause);
            remoteView.setOnClickPendingIntent(R.id.btn_play_notification, buildPendingIntent("PLAY", 2));
        }
        remoteView.setTextViewText(R.id.txt_current_notification, this.title);
        remoteView.setImageViewResource(R.id.icon_player, R.mipmap.ic_launcher);
        remoteView.setOnClickPendingIntent(R.id.btn_next_notification, buildPendingIntent("NEXT", 0));
        remoteView.setOnClickPendingIntent(R.id.btn_prev_notification, buildPendingIntent("PREVIOUS", 1));
        remoteView.setOnClickPendingIntent(R.id.btn_kill_notification, buildPendingIntent("KILL", 4));
        return remoteView;
    }

    private PendingIntent buildPendingIntent(String action, int id) {
        Intent playIntent = new Intent(this.context.getApplicationContext(), PlayerNotificationReceiver.class);
        playIntent.putExtra("ACTION", action);
        return PendingIntent.getBroadcast(this.context.getApplicationContext(), id, playIntent, 134217728);
    }

    public void onCompletedAudio() {
        createNotificationPlayer(this.title);
    }

    public void onPaused() {
        createNotificationPlayer(this.title);
    }

    public void onPlaying() {
        createNotificationPlayer(this.title);
    }

    public void updateTitle(String title) {
        this.title = title;
        createNotificationPlayer(title);
    }

    public void onTimeChanged(long currentTime) {
    }

    public void destroyNotificationIfExists() {
        if (this.notificationManager != null) {
            try {
                this.notificationManager.cancel(100);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
