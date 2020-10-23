package com.example.sev_user.captainrocket.Ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sev_user.captainrocket.Activities.GameActivity;
import com.example.sev_user.captainrocket.R;

public class EndGame extends Activity {
    SharedPreferences sharedPreferences;
    private int highScore;
    private TextView tvHighScore;
    private TextView tvYourScore;
    private ImageView btnReplay;
    private  int idImage;
    ImageView hightScoreAva, yourScoreAva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        btnReplay = (ImageView) findViewById(R.id.btnReplay);
        tvYourScore = (TextView) findViewById(R.id.tvYourScore);
        tvHighScore = (TextView) findViewById(R.id.tvHighScore);
        hightScoreAva = (ImageView) findViewById(R.id.highScoreAva);
        yourScoreAva = (ImageView) findViewById(R.id.yourScoreAva);
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);

        sharedPreferences = getSharedPreferences("HighScore", MODE_PRIVATE);
        highScore = sharedPreferences.getInt("high", 0);
        idImage   = sharedPreferences.getInt("avatar", 0);
        if (score >= highScore) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("high", score);
            editor.putInt("highAva", idImage);
            editor.commit();
            switch (idImage)
            {
                case 1:
                    hightScoreAva.setImageResource(R.drawable.captain1_front);
                    break;
                case 2:
                    hightScoreAva.setImageResource(R.drawable.captain2_front);
                    break;
                case 3:
                    hightScoreAva.setImageResource(R.drawable.captain3_front);
                    break;
                case 4:
                    hightScoreAva.setImageResource(R.drawable.captain4_front);
                    break;
                default:
                    break;

            }
        }
        else{
            switch (sharedPreferences.getInt("highAva", 0))
            {
                case 1:
                    hightScoreAva.setImageResource(R.drawable.captain1_front);
                    break;
                case 2:
                    hightScoreAva.setImageResource(R.drawable.captain2_front);
                    break;
                case 3:
                    hightScoreAva.setImageResource(R.drawable.captain3_front);
                    break;
                case 4:
                    hightScoreAva.setImageResource(R.drawable.captain4_front);
                    break;
                default:
                    hightScoreAva.setImageResource(R.drawable.captain3_front);
                    break;

            }
        }


        switch (idImage)
        {
            case 1:
                yourScoreAva.setImageResource(R.drawable.captain1_front);
                break;
            case 2:
                yourScoreAva.setImageResource(R.drawable.captain2_front);
                break;
            case 3:
                yourScoreAva.setImageResource(R.drawable.captain3_front);
                break;
            case 4:
                yourScoreAva.setImageResource(R.drawable.captain4_front);
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
