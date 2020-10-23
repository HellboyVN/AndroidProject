package hellboy.bka.tlvan.rocketwarrior.Sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import hellboy.bka.tlvan.rocketwarrior.R;


public class FlashSoundService extends Service {
    MediaPlayer plashSound;
    public FlashSoundService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        plashSound = MediaPlayer.create(getApplicationContext(), R.raw.flash_sound);
        plashSound.setLooping(true);
        plashSound.setVolume(100, 100);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        plashSound.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        plashSound.stop();
        plashSound.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
