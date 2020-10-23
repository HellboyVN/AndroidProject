package hellboy.bka.tlvan.rocketwarrior.Ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import hellboy.bka.tlvan.rocketwarrior.Activities.GameActivity;
import hellboy.bka.tlvan.rocketwarrior.R;

public class MainActivity extends Activity implements View.OnClickListener {
    private AdView mAdView;
    private EndBroadcaseReceiver endBroadcaseReceiver;
    public MediaPlayer backgroundSound;
    private AnimationDrawable animStupidBird,animPikalong,animPsyDuck,animHulk;
    private ImageView imgCap1;
    private ImageView imgCap2;
    private ImageView imgCap3;
    private ImageView imgCap4;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //adv
        MobileAds.initialize(this, "ca-app-pub-6893701289996917~6925320179");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        //adv
        imgCap1 = (ImageView)findViewById(R.id.imgCap1) ;
        imgCap2 = (ImageView)findViewById(R.id.imgCap2) ;
        imgCap3 = (ImageView)findViewById(R.id.imgCap3) ;
        imgCap4 = (ImageView)findViewById(R.id.imgCap4) ;
        sharedPreferences = getSharedPreferences("HighScore", MODE_PRIVATE);
        endBroadcaseReceiver = new EndBroadcaseReceiver();
        animStupidBird = new AnimationDrawable();
        animStupidBird.addFrame(
                getResources().getDrawable(R.drawable.flappy1),
                100);
        animStupidBird.addFrame(
                getResources().getDrawable(R.drawable.flappy2),
                100);
        animStupidBird.addFrame(
                getResources().getDrawable(R.drawable.flappy3),
                100);
        animStupidBird.addFrame(
                getResources().getDrawable(R.drawable.flappy1),
                100);

        animPikalong = new AnimationDrawable();
        animPikalong.addFrame(
                getResources().getDrawable(R.drawable.pikalong1),
                100);
        animPikalong.addFrame(
                getResources().getDrawable(R.drawable.pikalong2),
                100);
        animPikalong.addFrame(
                getResources().getDrawable(R.drawable.pikalong3),
                100);
        animPikalong.addFrame(
                getResources().getDrawable(R.drawable.pikalong4),
                100);
        animPikalong.addFrame(
                getResources().getDrawable(R.drawable.pikalong5),
                100);

        animPsyDuck = new AnimationDrawable();
        animPsyDuck.addFrame(
                getResources().getDrawable(R.drawable.captain3_back),
                100);
        animPsyDuck.addFrame(
                getResources().getDrawable(R.drawable.captain3_front),
                100);

        animHulk = new AnimationDrawable();
        animHulk.addFrame(
                getResources().getDrawable(R.drawable.captain4_back),
                100);
        animHulk.addFrame(
                getResources().getDrawable(R.drawable.captain4_front),
                100);

        imgCap2.setImageDrawable(animStupidBird);
        animStupidBird.setOneShot(false);
        animStupidBird.start();

        imgCap1.setImageDrawable(animPikalong);
        animPikalong.setOneShot(false);
        animPikalong.start();

        imgCap3.setImageDrawable(animPsyDuck);
        animPsyDuck.setOneShot(false);
        animPsyDuck.start();

        imgCap4.setImageDrawable(animHulk);
        animHulk.setOneShot(false);
        animHulk.start();

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
        intentFilter.addAction("hellboy.bka.tlvan.rocketwarrior.END_GAME");
        registerReceiver(endBroadcaseReceiver, intentFilter);

        backgroundSound.start();
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(endBroadcaseReceiver);
        backgroundSound.release();
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        backgroundSound.pause();
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();

    }

    @Override
    protected void onStop() {
        backgroundSound.stop();
        super.onStop();
    }
}
