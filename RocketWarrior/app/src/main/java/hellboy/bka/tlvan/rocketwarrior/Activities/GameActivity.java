package hellboy.bka.tlvan.rocketwarrior.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import hellboy.bka.tlvan.rocketwarrior.R;


public class GameActivity extends Activity {
    private GameView gameView;

    public MediaPlayer backgroundSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int captainType = intent.getIntExtra("cap", 1);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int backgroundType = (int) (Math.random() * 4 + 1);
        gameView = new GameView(this, size.x, size.y, captainType, backgroundType);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gameView);

        backgroundSound = MediaPlayer.create(getBaseContext(), R.raw.background);
        backgroundSound.setLooping(true);
        backgroundSound.setVolume(100, 100);
        backgroundSound.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        backgroundSound.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        backgroundSound.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundSound.release();
    }

    @Override
    protected void onStop() {
        backgroundSound.stop();
        super.onStop();
        gameView.destroy();
    }

    @Override
    public void onBackPressed() {
        //    super.onBackPressed();
        //  finish();
        // return resume game
    }
}
