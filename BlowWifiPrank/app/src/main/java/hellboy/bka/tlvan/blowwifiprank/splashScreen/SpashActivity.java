package hellboy.bka.tlvan.blowwifiprank.splashScreen;

/**
 * Created by tlvan on 9/15/2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.gelitenight.waveview.library.WaveView;

import hellboy.bka.tlvan.blowwifiprank.Process.MainActivity;
import hellboy.bka.tlvan.blowwifiprank.Process.WaveHelper;
import hellboy.bka.tlvan.blowwifiprank.R;


public class SpashActivity extends AppCompatActivity {
    WaveView mWaveViewSplash;
    WaveHelper mWaveHelperSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mWaveViewSplash = (WaveView) findViewById(R.id.wave1);
        mWaveViewSplash.setBorder(1, Color.parseColor("#0c8cb1"));
        mWaveViewSplash.setWaveColor(Color.parseColor("#A1887F"),
                Color.parseColor("#E65100"));
        mWaveViewSplash.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelperSplash = new WaveHelper(mWaveViewSplash,0.93f);
        mWaveHelperSplash.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 6000);
    }
}
