package hellboy.bka.tlvan.blowwifiprank.Process;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gelitenight.waveview.library.WaveView;

import hellboy.bka.tlvan.blowwifiprank.R;

/**
 * Created by tlvan on 9/15/2017.
 */

public class ActivityBlow extends AppCompatActivity {
    ImageView imgWifi, imgTransparent, imgBorder,imgGalaxy;
    Handler handler = new Handler();
    AudioMeter audioMeter;
    TextView tvWifiname, tvWifiStreng;
    WaveView mWaveView;
    WaveHelper mWaveHelper;
    Button btn_amtitude;
    Animation animFadein, animRotate;
    private boolean check = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvWifiname = (TextView) findViewById(R.id.tv_wifiname);
        tvWifiStreng = (TextView) findViewById(R.id.tv_wifiStreng);
        imgWifi = (ImageView) findViewById(R.id.image_wifi);
        imgBorder = (ImageView) findViewById(R.id.img_border);
        imgGalaxy = (ImageView) findViewById(R.id.img_galaxy);
        imgGalaxy.setAlpha(0.6f);
        imgTransparent = (ImageView) findViewById(R.id.img_wifiTrans);
        imgTransparent.setImageResource(R.drawable.wifi_3d_blow);
        imgTransparent.setAlpha(0.3f);
        mWaveView = (WaveView) findViewById(R.id.wave);
        mWaveView.setBorder(1, Color.parseColor("#44FFFF"));
        mWaveView.setWaveColor(Color.parseColor("#2E7D32"),
                Color.parseColor("#2E7D32"));
        mWaveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(mWaveView);
        mWaveHelper.start();

        btn_amtitude = (Button) findViewById(R.id.btn_amtitude);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        CheckWifiState();
        btn_amtitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWaveView.startAnimation(animFadein);
                imgTransparent.startAnimation(animFadein);
            }
        });

            audioMeter = new AudioMeter();
            audioMeter.startRecording();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(30000);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                CheckWifiState();
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {

                                Log.e("Amplitude= ", String.valueOf(audioMeter.getAmplitude()));
                                if (audioMeter.getAmplitude() > 0 && audioMeter.getAmplitude() < 60) {

                                    mWaveView.setWaterLevelRatio(mWaveView.getWaterLevelRatio() + 0.03f);
                                    tvWifiStreng.setText("Your Streng Wifi: " + String.valueOf(displayStrengWifi()) +
                                            "\n Your Wifi is Stronger! Keep On Trying!");
                                } else if (mWaveView.getWaterLevelRatio() >= 0.3f) {
                                    mWaveView.setWaterLevelRatio(mWaveView.getWaterLevelRatio() - 0.01f);
                                    tvWifiStreng.setText("Your Streng Wifi: " + String.valueOf(displayStrengWifi()) +
                                            "\n Your Wifi is weakening! Keep On Trying!");

                                }

                                Log.e("stopping ", String.valueOf(mWaveView.getWaterLevelRatio()));
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        Log.e("onPause ", "onPause");
        audioMeter.stopRecording();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioMeter.stopRecording();
        Log.e("Onresume ", "Onresume");
        audioMeter = new AudioMeter();
        audioMeter.startRecording();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void CheckWifiState() {
        //wifi state
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        tvWifiStreng.setText("Your Streng Wifi: " + String.valueOf(level));
        String wifname = wifiInfo.getSSID();
        tvWifiname.setText("You are connecting to the wifi: " + wifname);
        //end wifi state
        switch (level) {
            case 0:
                mWaveView.setWaterLevelRatio(0f);
                break;
            case 1:
                mWaveView.setWaterLevelRatio(0.24f);
                break;
            case 2:
                mWaveView.setWaterLevelRatio(0.4f);
                break;
            case 3:
                mWaveView.setWaterLevelRatio(0.6f);
                break;
            default:
                mWaveView.setWaterLevelRatio(0.75f);
                break;
        }
    }

    private int displayStrengWifi() {
        int index = 0;
        float compareNumber = mWaveView.getWaterLevelRatio();
        if (compareNumber >= 0.64f) {
            index = 4;
        } else if (compareNumber < 0.64f && compareNumber >= 0.46f) {
            index = 3;
        } else if (compareNumber < 0.46f && compareNumber >= 0.28f) {
            index = 2;
        } else if (compareNumber < 0.28f && compareNumber >= 0.12f) {
            index = 1;
        }
        return index;
    }
}
