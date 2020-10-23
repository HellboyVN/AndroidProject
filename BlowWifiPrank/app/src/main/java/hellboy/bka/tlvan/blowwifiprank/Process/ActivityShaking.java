package hellboy.bka.tlvan.blowwifiprank.Process;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gelitenight.waveview.library.WaveView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import hellboy.bka.tlvan.blowwifiprank.R;

public class ActivityShaking extends AppCompatActivity {
    ImageView imgWifi, imgTransparent, imgBorder,imgGalaxy;
    Handler handler = new Handler();
    AudioMeter audioMeter;
    TextView tvWifiname, tvWifiStreng,tvHeader;
    WaveView mWaveView;
    WaveHelper mWaveHelper;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 1;
    Button btn_amtitude;
    Animation animFadein, animRotate;
    private ShakeListener mShaker;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //adv
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_shake));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        tvWifiname = (TextView) findViewById(R.id.tv_wifiname);
        tvWifiStreng = (TextView) findViewById(R.id.tv_wifiStreng);

        tvHeader = (TextView) findViewById(R.id.textViewHeader);
        tvHeader.setText("Shake Your Device to enhance Wifi!");

        imgWifi = (ImageView) findViewById(R.id.image_wifi);
        imgGalaxy = (ImageView) findViewById(R.id.img_galaxy);
        imgGalaxy.setAlpha(0.6f);
        imgBorder = (ImageView) findViewById(R.id.img_border);
        imgTransparent = (ImageView) findViewById(R.id.img_wifiTrans);
        imgTransparent.setAlpha(0.3f);
        mWaveView = (WaveView) findViewById(R.id.wave);
        mWaveView.setBorder(1, Color.parseColor("#44FFFF"));
        mWaveView.setWaveColor(Color.parseColor("#2962FF"),
                Color.parseColor("#2962FF"));
        mWaveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(mWaveView);
        mWaveHelper.start();

        btn_amtitude = (Button) findViewById(R.id.btn_amtitude);
        btn_amtitude.setText("Rotate");
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        CheckWifiState();
        btn_amtitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                mWaveView.setWaterLevelRatio(0.6f);
                imgTransparent.startAnimation(animRotate);
                mWaveView.startAnimation(animRotate);
                imgBorder.startAnimation(animRotate);
//                // Get running processes
//                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//                List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
//
//                for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
//
//                    // Get UID of the selected process
//
//                    int uid =  runningApp.uid;
//
//                    // Get traffic data
//                    long received = TrafficStats.getMobileRxBytes();
//                    long send   = TrafficStats.getTotalRxBytes();
//                    Log.v("" + uid , "Send :" + send + ", Received :" + received);
//                }

            }
        });
        ((SeekBar) findViewById(R.id.seekBar))
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        mBorderWidth = i;
                        // mWaveView.setBorder(mBorderWidth, mBorderColor);
                        mWaveView.setWaterLevelRatio((float) i / 100);
                        Log.e("value", String.valueOf((float) i / 100));
                        tvWifiStreng.setText("Your Streng Wifi: "+String.valueOf(displayStrengWifi()));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(10000);
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



        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(100);
                //Toast.makeText(getApplicationContext(), "Shaking", Toast.LENGTH_SHORT).show();
                mWaveView.setWaterLevelRatio(mWaveView.getWaterLevelRatio() + 0.05f);
                tvWifiStreng.setText("Your Streng Wifi: "+String.valueOf(displayStrengWifi()));
            }
        });

    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.e(this.getClass().getName(), "back button pressed");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    private void CheckWifiState(){
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
