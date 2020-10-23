package com.example.sev_user.captainrocket.Components;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.sev_user.captainrocket.R;

/**
 * Created by sev_user on 8/11/2016.
 */
public class SoundGame extends Service {

    private int idSound;
    private MediaPlayer mediaJump, mediaDead;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        idSound = intent.getIntExtra("idSound", 0);

        switch (idSound) {
            case 1:
                // jump
                mediaJump.start();
                mediaJump.setVolume(100, 100);
                break;
            case 2:
                // dead
                mediaDead.start();
                mediaDead.setVolume(100, 100);
                break;
            default:

                mediaJump.stop();

        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // Nhac jump
        mediaJump = MediaPlayer.create(getApplicationContext(), R.raw.jump);
        mediaDead = MediaPlayer.create(getApplicationContext(), R.raw.dead);
    }

    @Override
    public void onDestroy() {
        mediaJump.release();
        super.onDestroy();
    }
}
