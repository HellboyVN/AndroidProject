package com.example.sev_user.captainrocket.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.sev_user.captainrocket.R;
import com.example.sev_user.captainrocket.Ui.EndBroadcaseReceiver;
import com.example.sev_user.captainrocket.Ui.MainActivity;


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

        int backgroundType = (int) (Math.random() * 3 + 1);
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
