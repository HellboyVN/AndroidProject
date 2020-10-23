package hellboy.bka.tlvan.rocketwarrior.Components;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import hellboy.bka.tlvan.rocketwarrior.R;


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
                mediaDead.stop();
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
        mediaDead.release();
        super.onDestroy();
    }
}
