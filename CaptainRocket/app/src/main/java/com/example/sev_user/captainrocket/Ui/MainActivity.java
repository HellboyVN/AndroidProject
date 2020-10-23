package com.example.sev_user.captainrocket.Ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sev_user.captainrocket.Activities.GameActivity;
import com.example.sev_user.captainrocket.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private EndBroadcaseReceiver endBroadcaseReceiver;
    public MediaPlayer backgroundSound;

    private ImageView imgCap1;
    private ImageView imgCap2;
    private ImageView imgCap3;
    private ImageView imgCap4;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("HighScore", MODE_PRIVATE);
        endBroadcaseReceiver = new EndBroadcaseReceiver();


        init();

        backgroundSound = MediaPlayer.create(getBaseContext(), R.raw.choose_music);
        backgroundSound.setLooping(true);
        backgroundSound.setVolume(100, 100);
        backgroundSound.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCap1:
                Intent intentCap1 = new Intent(MainActivity.this, GameActivity.class);
                intentCap1.putExtra("cap", 1);

                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putInt("avatar", 1);
                editor1.commit();

                startActivity(intentCap1);
                finish();
                break;

            case R.id.imgCap2:
                Intent intentCap2 = new Intent(MainActivity.this, GameActivity.class);
                intentCap2.putExtra("cap", 2);
                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                editor2.putInt("avatar", 2);
                editor2.commit();
                startActivity(intentCap2);
                break;

            case R.id.imgCap3:
                Intent intentCap3 = new Intent(MainActivity.this, GameActivity.class);
                intentCap3.putExtra("cap", 3);
                SharedPreferences.Editor editor3 = sharedPreferences.edit();
                editor3.putInt("avatar", 3);
                editor3.commit();
                startActivity(intentCap3);
                break;

            case R.id.imgCap4:
                Intent intentCap4 = new Intent(MainActivity.this, GameActivity.class);
                intentCap4.putExtra("cap", 4);
                SharedPreferences.Editor editor4 = sharedPreferences.edit();
                editor4.putInt("avatar", 4);
                editor4.commit();
                startActivity(intentCap4);
                break;


        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (endBroadcaseReceiver.endGame()) {
//            Toast.makeText(MainActivity.this, "Quit game !", Toast.LENGTH_SHORT).show();
            try {
                finish();
            } catch (Exception e) {

            }
        }
        this.finish();
    }

    private void init() {

        imgCap1 = (ImageView) findViewById(R.id.imgCap1);
        imgCap1.setOnClickListener(this);
        imgCap2 = (ImageView) findViewById(R.id.imgCap2);
        imgCap2.setOnClickListener(this);
        imgCap3 = (ImageView) findViewById(R.id.imgCap3);
        imgCap3.setOnClickListener(this);
        imgCap4 = (ImageView) findViewById(R.id.imgCap4);
        imgCap4.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.sev_user.captainrocket.END_GAME");
        registerReceiver(endBroadcaseReceiver, intentFilter);

        backgroundSound.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(endBroadcaseReceiver);
        backgroundSound.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        backgroundSound.pause();
        super.onPause();

    }

    @Override
    protected void onStop() {
        backgroundSound.stop();
        super.onStop();
    }
}
