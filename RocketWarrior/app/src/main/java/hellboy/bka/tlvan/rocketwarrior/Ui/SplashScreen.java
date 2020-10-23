package hellboy.bka.tlvan.rocketwarrior.Ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import hellboy.bka.tlvan.rocketwarrior.R;
import hellboy.bka.tlvan.rocketwarrior.Sound.FlashSoundService;

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        final Intent intent = new Intent(this, FlashSoundService.class);
        startService(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();

                stopService(intent);
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

}
