package hellboy.bka.tlvan.rocketwarrior.Ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import hellboy.bka.tlvan.rocketwarrior.Activities.GameActivity;
import hellboy.bka.tlvan.rocketwarrior.R;

public class EndGame extends Activity {
    SharedPreferences sharedPreferences;
    private int highScore;
    private TextView tvHighScore;
    private TextView tvYourScore;
    private ImageView btnReplay, btnHome;
    private int idImage;
    private AnimationDrawable animStupidBird,animPikalong,animPsyDuck,animHulk;
    ImageView hightScoreAva, yourScoreAva;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        //adv
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        //adv
        btnReplay = (ImageView) findViewById(R.id.btnReplay);
        btnHome = (ImageView) findViewById(R.id.btnHome);
        tvYourScore = (TextView) findViewById(R.id.tvYourScore);
        tvHighScore = (TextView) findViewById(R.id.tvHighScore);
        hightScoreAva = (ImageView) findViewById(R.id.highScoreAva);
        yourScoreAva = (ImageView) findViewById(R.id.yourScoreAva);
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        sharedPreferences = getSharedPreferences("HighScore", MODE_PRIVATE);
        highScore = sharedPreferences.getInt("high", 0);
        idImage   = sharedPreferences.getInt("avatar", 0);
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

        if (score >= highScore) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("high", score);
            editor.putInt("highAva", idImage);
            editor.commit();
            switch (idImage)
            {
                case 1:
//                    hightScoreAva.setImageResource(R.drawable.pikalong_right);
                    hightScoreAva.setImageDrawable(animPikalong);
                    animPikalong.setOneShot(false);
                    animPikalong.start();
                    break;
                case 2:
//                    hightScoreAva.setImageResource(R.drawable.goku);
                    hightScoreAva.setImageDrawable(animStupidBird);
                    animStupidBird.setOneShot(false);
                    animStupidBird.start();
                    break;
                case 3:
//                    hightScoreAva.setImageResource(R.drawable.captain3_front);
                    hightScoreAva.setImageDrawable(animPsyDuck);
                    animPsyDuck.setOneShot(false);
                    animPsyDuck.start();
                    break;
                case 4:
//                    hightScoreAva.setImageResource(R.drawable.captain4_front);
                    hightScoreAva.setImageDrawable(animHulk);
                    animHulk.setOneShot(false);
                    animHulk.start();
                    break;
                default:
                    break;

            }
        }
        else{
            switch (sharedPreferences.getInt("highAva", 0))
            {
                case 1:
//                    hightScoreAva.setImageResource(R.drawable.pikalong_right);
                    hightScoreAva.setImageDrawable(animPikalong);
                    animPikalong.setOneShot(false);
                    animPikalong.start();
                    break;
                case 2:
//                    hightScoreAva.setImageResource(R.drawable.goku);
                    hightScoreAva.setImageDrawable(animStupidBird);
                    animStupidBird.setOneShot(false);
                    animStupidBird.start();
                    break;
                case 3:
//                    hightScoreAva.setImageResource(R.drawable.captain3_front);
                    hightScoreAva.setImageDrawable(animPsyDuck);
                    animPsyDuck.setOneShot(false);
                    animPsyDuck.start();
                    break;
                case 4:
//                    hightScoreAva.setImageResource(R.drawable.captain4_front);
                    hightScoreAva.setImageDrawable(animHulk);
                    animHulk.setOneShot(false);
                    animHulk.start();
                    break;
                default:
                    hightScoreAva.setImageResource(R.drawable.captain3_front);
                    break;

            }
        }


        switch (idImage)
        {
            case 1:
                yourScoreAva.setImageDrawable(animPikalong);
                animPikalong.setOneShot(false);
                animPikalong.start();
                break;
            case 2:
                yourScoreAva.setImageDrawable(animStupidBird);
                animStupidBird.setOneShot(false);
                animStupidBird.start();
                break;
            case 3:
//                yourScoreAva.setImageResource(R.drawable.captain3_front);
                yourScoreAva.setImageDrawable(animPsyDuck);
                animPsyDuck.setOneShot(false);
                animPsyDuck.start();
                break;
            case 4:
//                yourScoreAva.setImageResource(R.drawable.captain4_front);
                yourScoreAva.setImageDrawable(animHulk);
                animHulk.setOneShot(false);
                animHulk.start();
                break;
            default:
                break;

        }

        tvYourScore.setText("Your Score " + String.valueOf(score));
        tvHighScore.setText("High Score " + sharedPreferences.getInt("high", score));

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndGame.this, GameActivity.class);
                intent.putExtra("cap", idImage);
                startActivity(intent);
                EndGame.this.finish();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndGame.this, MainActivity.class);
                startActivity(intent);
                EndGame.this.finish();
            }
        });
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

//        Luc nhan back press dang ky
        Intent intentEnd = new Intent("com.example.sev_user.captainrocket.END_GAME");
        sendBroadcast(intentEnd);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
