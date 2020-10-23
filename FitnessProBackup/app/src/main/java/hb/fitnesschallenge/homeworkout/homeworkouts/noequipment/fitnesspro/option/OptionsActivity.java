package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.option;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.MainActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;

public class OptionsActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsService.getInstance().setGender(this, 1);
        setContentView((int) R.layout.activity_launch);
        Log.e("hellboy","Oncreate Launch");
        ButterKnife.bind((Activity) this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(OptionsActivity.this,MainActivity.class);
                OptionsActivity.this.startActivity(mainIntent);
                OptionsActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }


}
